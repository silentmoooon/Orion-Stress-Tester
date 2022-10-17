package org.mirrentools.ost;

import com.google.common.util.concurrent.RateLimiter;
import io.netty.util.internal.StringUtil;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.*;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.mirrentools.ost.common.*;
import org.mirrentools.ost.enums.OstCommand;
import org.mirrentools.ost.enums.OstRequestType;
import org.mirrentools.ost.enums.OstSslCertType;
import org.mirrentools.ost.handler.OstTcpRequestHandler;
import org.mirrentools.ost.handler.OstWebSocketRequestHandler;
import org.mirrentools.ost.model.OstRequestOptions;
import org.mirrentools.ost.task.TaskBean;
import org.mirrentools.ost.verticle.OstHttpVerticle;
import org.mirrentools.ost.verticle.OstTcpVerticle;
import org.mirrentools.ost.verticle.OstWebSocketVerticle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <pre>
 * 嗨!我通过下面这4行代码,运行了很长很长的时间后,它打印的内容进化成现在的这个项目
 * Hi! I ran the following four lines of code for a long time, and the printed content evolved into the current project
 * while (true) {
 *   System.out.print(new Random().nextInt(2));
 *   Thread.sleep(1000);
 * }
 *   可能我上面的内容很疯狂,但是你可能听信过更疯狂的,比如:我们现在生活的世界是大爆炸而来的;
 * Maybe what I am talking above is crazy, but you may have heard much more crazy things, for example: The world we live in now is from the big bang;
 * 因为我们不关心或不容易证实,所以一些假说与谎言传多了就被当真了;
 * Because we don't care about it or it's not easy to prove it, some hypotheses and lies are taken seriously when they are spread too much;
 * 计算机需要被制造,程序也需要被编写或生成才有;程序能做什么,能获取到计算机的什么信息都已经在编写的时候设定好了;
 * A computer needs to be made, and a program needs to be written or generated. What a program can do and what information it can get has been set up at the time of writing;
 * 计算机的世界好比我们现在生活的世界,程序好比现在看到这段注释的你我;
 * The world of computer is like the world we live in now, and the program is like you and me who see this comment now;
 * 这个世界有一位造物主,虽然眼不能见但是籍着祂所造的一切,只要我们不压着我们的良心我们都能感受到;
 * There is a creator in this world, who can't see but can feel everything created by HIM as long as we don't press our conscience;
 *   我们编写程序需要有文档或注释帮助我们了解程序相关的,同样如果要了解这个世界的一切我们只能通过她的说明书,就是圣经
 * We need to have documents or notes to help us understand the program, and if we want to understand everything in the world, we can only through her instructions, that is, the BIBLE.
 * 程序入口
 * Main
 * </pre>
 *
 * @author <a href="https://mirrentools.org/">Mirren</a>
 * @date 2019-02-14
 */
public class MainVerticle extends AbstractVerticle {
    /**
     * 日志
     */
    private final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
    /**
     * GC overhead limit exceeded
     */
    private boolean gcOverheadLimit = false;
    /**
     * 实例的数量
     */
    private int instances;

    public static ConcurrentLinkedQueue<TaskBean> TASK_QUEUE = new ConcurrentLinkedQueue<>();

    public static RateLimiter rateLimiter = null;

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * IDE中启动的Main
     *
     * @param args
     */
    public static void main(String[] args) {
        MainLauncher.start();
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        instances = config().getInteger("instances", JvmMetricsUtil.availableProcessors());
        int mode = config().getInteger("mode", 0);
        if (mode == 0) {
            startWithCmd(startPromise);
        } else {
            startWithWeb(startPromise);
        }


    }

    private void startWithWeb(Promise<Void> startPromise) {

        Integer port = config().getInteger("httpPort", 7090);
        Router router = Router.router(vertx);
        router.route().handler(StaticHandler.create("webroot").setDefaultContentEncoding("UTF-8"));
        vertx.createHttpServer().requestHandler(router).webSocketHandler(socket -> {
            SocketAddress address = socket.remoteAddress();
            if (LOG.isDebugEnabled()) {
                LOG.debug(address.host() + ":" + address.port() + socket.path() + " -->连接控制台成功!");
            }
            if (socket.query() != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("\"query: \" + socket.query()");
                }
            }
            if (gcOverheadLimit) {
                String failed = ResultFormat.failed(OstCommand.GC_OVERHEAD_LIMIT, "The task exceeds the processing capacity of the console. please set the startup parameter to increase the JVM memory", "");
                socket.writeTextMessage(failed);
                socket.end();
                return;
            }
            // 关闭或结束时清理请求
            Promise<String> clearRequest = Promise.promise();
            clearRequest.future().onSuccess(id -> {
                System.out.println("close:" + id);
                LocalDataServerWebSocket.remove(id);
                LocalDataRequestOptions.remove(id);
                LocalDataCounter.remove(id);
                LocalDataBoolean.remove(id);
                Vertx remove = LocalDataVertx.remove(id);
                if (remove != null) {
                    Set<String> deploymentIDs = remove.deploymentIDs();
                    for (String did : deploymentIDs) {
                        remove.undeploy(did);
                    }
                    remove.close(close -> {
                        if (close.failed()) {
                            LOG.error("关闭WebSocket->关闭测试服务->" + id + "-->异常", close.cause());
                        } else {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("关闭WebSocket->关闭测试服务->" + id + "-->成功");
                            }
                        }
                    });
                }
            });
            // 处理用户的信息
            socket.handler(buf -> {
                try {
                    JsonObject body = new JsonObject(buf);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("收到用户请求:" + body);
                    }
                    Integer code = body.getInteger(Constant.CODE);
                    if (code.equals(OstCommand.CANCEL.value())) {
                        clearRequest.tryComplete(socket.textHandlerID());
                        socket.end();
                    } else if (code.equals(OstCommand.SUBMIT_TEST.value())) {
                        JsonObject data = body.getJsonObject(Constant.DATA);
                        OstRequestOptions ostRequestOptions = Json.decodeValue(data.toString(), OstRequestOptions.class);
                        checkAndLoadRequestOptions(ostRequestOptions, res -> {
                            if (res.succeeded()) {
                                // 检查与装载数据成功,提交测试任务
                                OstRequestOptions options = res.result();
                                String id = socket.textHandlerID();
                                System.out.println("deploy:" + id);
                                options.setId(id);
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("加载并检查请求参数-->成功:" + options);
                                }
								/*vertx.setPeriodic(1000, tid -> {
									if (socket.isClosed()) {
										vertx.cancelTimer(tid);
										return;
									}
									JsonObject result = new JsonObject();
									result.put("processors", JvmMetricsUtil.availableProcessors());
									result.put("totalMemory", JvmMetricsUtil.totalMemory());
									result.put("maxMemory", JvmMetricsUtil.maxMemory());
									result.put("freeMemory", JvmMetricsUtil.freeMemory());
									LOG.info("执行发送信息给客户端-->当前服务器性能:" + result);
									String metrics = ResultFormat.success(OstCommand.JVM_METRIC, result);
									socket.writeTextMessage(metrics);
								});*/
                                submitTest(options, socket);
                                // 设置Socket关闭事件
                                socket.endHandler(end -> {
                                    System.out.println("clearRequest:" + socket.textHandlerID());
                                    clearRequest.tryComplete(socket.textHandlerID());
                                });
                            } else {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("加载并检查请求参数-->失败:", res.cause());
                                }
                                socket.writeTextMessage(ResultFormat.failed(OstCommand.MISSING_PARAMETER, res.cause().getMessage(), buf.toString()));
                            }
                        });
                    } else {
                        socket.writeTextMessage(ResultFormat.failed(OstCommand.MISSING_PARAMETER, "请求失败,无效的操作指令!", buf.toString()));
                    }
                } catch (Exception e) {
                    LOG.error("解析用户请求-->失败:" + buf);
                    socket.writeTextMessage(ResultFormat.failed(OstCommand.MISSING_PARAMETER, "请求失败,存在无效的数据!", buf.toString()));
                }
            });
        }).listen(port, res -> {
            if (res.succeeded()) {
                System.out.println("Orion-Stress-Tester running http://127.0.0.1:" + port);
                try {
                    CommandUtil.browse(new URI("http://127.0.0.1:" + port));
                } catch (Exception e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("执行打开默认浏览器失败:", e);
                    }
                }
                startPromise.complete();
            } else {
                LOG.error("Orion-Stress-Tester start failed. If the port is occupied, you can modify the httpport of data/config.json");
                startPromise.fail(res.cause());
            }
        });
    }

    private void startWithCmd(Promise<Void> startPromise) {
        byte[] bs;
        try {
            String root = System.getProperty("user.dir");
            Path path = Paths.get(root, "task", "task.json");
            bs = Files.readAllBytes(path);
            OstRequestOptions ostRequestOptions = Json.decodeValue(new String(bs, StandardCharsets.UTF_8), OstRequestOptions.class);
            String id = String.valueOf(System.currentTimeMillis());
            ostRequestOptions.setId(id);
            checkAndLoadRequestOptions(ostRequestOptions, res -> {
                if (res.succeeded()) {
                    // 检查与装载数据成功,提交测试任务
                    OstRequestOptions options = res.result();

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("加载并检查请求参数-->成功:" + options);
                    }
					/*vertx.setPeriodic(1000, tid -> {

						JsonObject result = new JsonObject();
						result.put("processors", JvmMetricsUtil.availableProcessors());
						result.put("totalMemory", JvmMetricsUtil.totalMemory());
						result.put("maxMemory", JvmMetricsUtil.maxMemory());
						result.put("freeMemory", JvmMetricsUtil.freeMemory());
						LOG.info("执行发送信息给客户端-->当前服务器性能:" + result);
						String metrics = ResultFormat.success(OstCommand.JVM_METRIC, result);
						System.out.println(metrics);
					});*/
                    submitTest(options);

                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("加载并检查请求参数-->失败:", res.cause());
                    }
                    System.err.println("加载并检查请求参数-->失败:");
                    System.exit(0);

                }
            });

            startPromise.complete();
        } catch (IOException e) {
            System.err.println("任务配置文件读取失败");
            System.exit(0);
        }
    }

    /**
     * 提交测试请求
     *
     * @param options
     */
    private void submitTest(OstRequestOptions options, ServerWebSocket socket) {
        checkRequest(options, res -> {
            if (res.succeeded()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("检查请求是否可用-->结果:成功!");
                }
                String result = ResultFormat.success(OstCommand.BEFORE_REQUEST_TEST, 1);
                socket.writeTextMessage(result);
                // 请求的id
                String optionsId = socket.textHandlerID();
                // 共享WebSocket
                LocalDataServerWebSocket.put(optionsId, socket);
                // 共享请求配置
                LocalDataRequestOptions.put(optionsId, options);
                // 开启测试
                startTest(options, socket);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("检查请求是否可用-->结果:失败:", res.cause());
                }
                if (socket.isClosed()) {
                    return;
                }
                String result = ResultFormat.failed(OstCommand.BEFORE_REQUEST_TEST, res.cause().getMessage(), 0);
                socket.writeTextMessage(result);
                socket.end();
            }
        });
    }

    private void submitTest(OstRequestOptions options) {
        checkRequest(options, res -> {
            if (res.succeeded()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("检查请求是否可用-->结果:成功!");
                }
                String result = ResultFormat.success(OstCommand.BEFORE_REQUEST_TEST, 1);
                // 请求的id
                String optionsId = options.getId();

                // 共享请求配置
                LocalDataRequestOptions.put(optionsId, options);
                // 开启测试
                startTest(options);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("检查请求是否可用-->结果:失败:", res.cause());
                }
                System.exit(0);
            }
        });
    }

    /**
     * 启动测试服务
     *
     * @param options 请求的配置
     * @param socket  Socket
     */
    private void startTest(OstRequestOptions options, ServerWebSocket socket) {

        // 测试服务的名称
        String testName;
        // 测试镜像的名称
        String snapshotName;
        // 要启动的服务名称
        String verticleName;
        // 请求的总数量
        long requestTotal;
        if (OstRequestType.valueOf(options.getType()) == OstRequestType.TCP) {
            testName = "TCP";
            snapshotName = "vertx.net";
            verticleName = OstTcpVerticle.class.getName();
            requestTotal = options.getCount();
        } else if (OstRequestType.valueOf(options.getType()) == OstRequestType.WebSocket) {
            testName = "WebSocket";
            snapshotName = "vertx.http";
            verticleName = OstWebSocketVerticle.class.getName();
            requestTotal = options.getCount();
        } else if (OstRequestType.valueOf(options.getType()) == OstRequestType.HTTP) {
            testName = "HTTP";
            snapshotName = "vertx.http";
            verticleName = OstHttpVerticle.class.getName();
            requestTotal = (options.getCount());
        } else if (OstRequestType.valueOf(options.getType()) == OstRequestType.JDBC) {
            testName = "JDBC";
            snapshotName = "vertx.JDBC";
            verticleName = "OstJdbcVerticle.class.getName()";
            requestTotal = (options.getCount());
        } else {
            LOG.error("错误的类型!");
            if (socket != null && !socket.isClosed()) {
                socket.writeTextMessage("错误的类型");
                socket.close();
            }
            return;
        }


        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckInterval(1000 * 60 * 60);
        Vertx newVertx = Vertx.vertx(vertxOptions);
        newVertx.exceptionHandler(err -> {
            if ("GC overhead limit exceeded".equals(err.getMessage())) {
                Set<String> iDs = newVertx.deploymentIDs();
                for (String did : iDs) {
                    newVertx.undeploy(did);
                }
                gcOverheadLimit = true;
                String msg = ResultFormat.failed(OstCommand.ERROR, "The task exceeds the processing capacity of the console. please set the startup parameter to increase the JVM memory", err.getMessage());
                LOG.info("执行控制台超过GC开销已停止工作,请重启软件并调大JVM内存!");
                if (socket != null && !socket.isClosed()) {
                    socket.writeTextMessage(msg);
                    socket.close();
                }
            }
        });
        LocalDataVertx.put(options.getId(), newVertx);
        JsonObject config = new JsonObject().put("optionsId", options.getId());
        DeploymentOptions deployments = new DeploymentOptions();
        deployments.setInstances(instances);
        deployments.setConfig(config);
        if (LOG.isDebugEnabled()) {
            LOG.debug("正在启动" + testName + "测试服务:" + deployments.toJson());
        }

        Promise<String> promise = Promise.promise();
        promise.future().onSuccess(h -> {
            //MetricsService service = MetricsService.create(newVertx);
            vertx.setPeriodic(5000, tid -> {
                if (socket.isClosed()) {
                    vertx.cancelTimer(tid);
                    return;
                }
               /* JsonObject snapshot = service.getMetricsSnapshot(snapshotName);
                long succeeded = LocalDataCounter.getCount(Constant.REQUEST_SUCCEEDED_PREFIX + options.getId());
                long failed = LocalDataCounter.getCount(Constant.REQUEST_FAILED_PREFIX + options.getId());
                long endSum = (succeeded + failed);
                snapshot.put("succeeded", succeeded);
                snapshot.put("failed", failed);
                socket.writeTextMessage(ResultFormat.success(OstCommand.TEST_RESPONSE, snapshot), writed -> {
                    if (endSum >= requestTotal) {
                        JsonObject metrics = service.getMetricsSnapshot(snapshotName);
                        metrics.put("succeeded", succeeded);
                        metrics.put("failed", failed);
                        String msg = ResultFormat.success(OstCommand.TEST_RESPONSE, metrics);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("执行" + testName + "测试->完成-->结果:" + metrics);
                        }
                        socket.writeTextMessage(msg, mwend -> {
                            String end = ResultFormat.success(OstCommand.TEST_COMPLETE, 1);
                            socket.writeTextMessage(end, ended -> {
                                socket.end();
                            });
                        });
                    } else {
                        LOG.info("执行发送信息给客户端-->已请求数量: " + endSum + " / " + requestTotal);
                    }
                });*/
            });
        });

        newVertx.deployVerticle(verticleName, deployments, res -> {
            if (res.succeeded()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("启动" + testName + "测试服务-->成功!");
                }
                promise.complete();
            } else {
                LOG.error("启动" + testName + "测试服务-->失败:", res.cause());
                String result = ResultFormat.failed(OstCommand.ERROR, res.cause().getMessage(), 0);
                socket.writeTextMessage(result);
                socket.end();
            }
        });
    }

    private void startTest(OstRequestOptions options) {

        // 测试服务的名称
        String testName;
        // 测试镜像的名称
        String snapshotName;
        // 要启动的服务名称
        String verticleName;
        // 请求的总数量
        long requestTotal;
        if (OstRequestType.valueOf(options.getType()) == OstRequestType.TCP) {
            testName = "TCP";
            snapshotName = "vertx.net";
            verticleName = OstTcpVerticle.class.getName();
            requestTotal = options.getCount();
        } else if (OstRequestType.valueOf(options.getType()) == OstRequestType.WebSocket) {
            testName = "WebSocket";
            snapshotName = "vertx.http";
            verticleName = OstWebSocketVerticle.class.getName();
            requestTotal = options.getCount();
        } else if (OstRequestType.valueOf(options.getType()) == OstRequestType.HTTP) {
            testName = "HTTP";
            snapshotName = "vertx.http";
            verticleName = OstHttpVerticle.class.getName();
            requestTotal = (options.getCount());
        } else if (OstRequestType.valueOf(options.getType()) == OstRequestType.JDBC) {
            testName = "JDBC";
            snapshotName = "vertx.JDBC";
            verticleName = "OstJdbcVerticle.class.getName()";
            requestTotal = (options.getCount());
        } else {
            System.out.println("错误的类型!");

            return;
        }

       /* MicrometerMetricsOptions metricsOptions = new MicrometerMetricsOptions().setEnabled(true);
        metricsOptions.setMicrometerRegistry(new SimpleMeterRegistry());
        VertxOptions vertxOptions = new VertxOptions().setMetricsOptions(metricsOptions);

        vertxOptions.setBlockedThreadCheckInterval(1000 * 60 * 60);*/
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckInterval(1000 * 60 * 60);
        //vertxOptions.setWorkerPoolSize(32);
        Vertx newVertx = Vertx.vertx(vertxOptions);
        newVertx.exceptionHandler(err -> {
            if ("GC overhead limit exceeded".equals(err.getMessage())) {
                Set<String> iDs = newVertx.deploymentIDs();
                for (String did : iDs) {
                    newVertx.undeploy(did);
                }
                gcOverheadLimit = true;
                String msg = ResultFormat.failed(OstCommand.ERROR, "The task exceeds the processing capacity of the console. please set the startup parameter to increase the JVM memory", err.getMessage());
                LOG.info("执行控制台超过GC开销已停止工作,请重启软件并调大JVM内存!");
                System.err.println("执行控制台超过GC开销已停止工作");
                System.exit(0);
            }
        });
        LocalDataVertx.put(options.getId(), newVertx);
        JsonObject config = new JsonObject().put("optionsId", options.getId());
        DeploymentOptions deployments = new DeploymentOptions();
        deployments.setInstances(instances);
        deployments.setConfig(config);
        if (options.getCount() > instances * 10) {
            int eachLoopCount = options.getCount() / (instances);
            for (int i = 0; i < instances; i++) {
                TaskBean taskBean = new TaskBean();
                taskBean.setStartIndex(eachLoopCount * i + 1);
                taskBean.setEndIndex(eachLoopCount * (i + 1));
                if (i == instances - 1 && options.getCount() % (instances) > 0) {
                    taskBean.setEndIndex(taskBean.getEndIndex() + options.getCount() % (instances));
                }
                TASK_QUEUE.offer(taskBean);
            }
        } else {
            TaskBean taskBean = new TaskBean();
            taskBean.setStartIndex(1);
            taskBean.setEndIndex(options.getCount());
            TASK_QUEUE.offer(taskBean);
        }
        if (options.getThroughput() != null && options.getThroughput() > 0) {
            rateLimiter = RateLimiter.create(options.getThroughput());
        }
        if (options.isKeepAlive()) {
            HttpClientOptions hOptions = new HttpClientOptions();
            if (options.isSsl() && options.getCert() != null && OstSslCertType.valueOf(options.getCert()) != OstSslCertType.DEFAULT) {
                if (OstSslCertType.PFX == OstSslCertType.valueOf(options.getCert())) {
                    PfxOptions certOptions = new PfxOptions();
                    certOptions.setPassword(options.getCertKey());
                    certOptions.setValue(Buffer.buffer(options.getCertValue()));
                    hOptions.setPfxKeyCertOptions(certOptions);
                } else if (OstSslCertType.JKS == OstSslCertType.valueOf(options.getCert())) {
                    JksOptions certOptions = new JksOptions();
                    certOptions.setPassword(options.getCertKey());
                    certOptions.setValue(Buffer.buffer(options.getCertValue()));
                    hOptions.setKeyStoreOptions(certOptions);
                } else {
                    PemKeyCertOptions certOptions = new PemKeyCertOptions();
                    certOptions.setKeyValue(Buffer.buffer(options.getCertKey()));
                    certOptions.setCertValue(Buffer.buffer(options.getCertValue()));
                    hOptions.setPemKeyCertOptions(certOptions);
                }
            }
            hOptions.setMaxPoolSize(options.getPoolSize());

            if (options.getTimeout() != null) {
                hOptions.setConnectTimeout(options.getTimeout());
            }
            hOptions.setKeepAlive(options.isKeepAlive());
            hOptions.setSsl(options.isSsl());
            URL url = null;
            try {
                url = new URL(options.getUrl());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            int port = url.getPort();
            if (port == -1) {
                if (options.isSsl()) {
                    port = 443;
                } else {
                    port = 80;
                }
            }
            options.setRequestUri(url.getFile());
            options.setHost(url.getHost());
            options.setPort(port);
            hOptions.setDefaultPort(port);
            hOptions.setDefaultHost(url.getHost());
            HttpClient httpClient = vertx.createHttpClient(hOptions);
            // 共享http客户端
            LocalDataHttpClient.put(options.getId(), httpClient);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("正在启动" + testName + "测试服务:" + deployments.toJson());
        }
        Promise<String> promise = Promise.promise();

        promise.future().onSuccess(h -> {
            //MetricsService service = MetricsService.create(newVertx);
            vertx.setPeriodic(5000, tid -> {
                String optionId = options.getId();
                Long newestEndTime = LocalDataCounter.getEndTime(optionId);
                if (newestEndTime == null) {
                    return;
                }
                long lastTime = LocalDataCounter.getCount(Constant.REQUEST_LAST_TIME_PREFIX + optionId);
                if (newestEndTime == lastTime) {
                    return;
                }

                LocalDataCounter.setCount(Constant.REQUEST_LAST_TIME_PREFIX + optionId, newestEndTime);
                long succeeded = LocalDataCounter.getCount(Constant.REQUEST_SUCCEEDED_PREFIX + optionId);
                long failed = LocalDataCounter.getCount(Constant.REQUEST_FAILED_PREFIX + optionId);
                long endSum = (succeeded + failed);
                long lastCount = LocalDataCounter.getCount(Constant.REQUEST_LAST_COUNT_PREFIX + optionId);


                long startTime = LocalDataCounter.getStartTime(optionId);
                long totalDelay = LocalDataCounter.getTotalDelay(optionId);
                long minDelay = LocalDataCounter.getMinDelay(optionId);
                long maxDelay = LocalDataCounter.getMaxDelay(optionId);

                LocalDataCounter.setCount(Constant.REQUEST_LAST_COUNT_PREFIX + optionId, endSum);


                Instant instant = Instant.ofEpochMilli(newestEndTime);
                String msg = "\n";
                msg += dateTimeFormatter.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                msg = msg + "\t 距上次(开始)时间:" + (newestEndTime - lastTime) / 1000 + "s\n";
              /*  System.out.println("now:"+now);
                System.out.println("start:"+startTime);
                System.out.println("last:"+lastTime);*/
                msg += "本次tps: " + String.format("%.2f", ((double) endSum - (double) lastCount) / ((newestEndTime - lastTime)) * 1000) + "/s\t总TPS:" + String.format("%.2f", ((double) endSum) / ((newestEndTime - startTime)) * 1000) + "/s\n";
                msg += "本次执行: " + (endSum - lastCount) + "\t 累计执行: " + endSum + "\t 累计成功: " + succeeded + "\t 累计失败: " + failed + " \n";
                msg += "累计耗时: " + totalDelay + "ms\t 平均耗时: " + String.format("%.4f", (double) totalDelay / (double) endSum) + "ms\t 最大耗时: " + maxDelay + "ms\t 最小耗时: " + minDelay + "ms";

               /* if (LOG.isDebugEnabled()) {
                    LOG.debug("执行" + testName + "测试->完成-->结果:" + metrics);
                }*/
                System.out.println(msg);
                if (endSum >= requestTotal) {
                    System.out.println("测试完成");
                    this.getVertx().close();
                    System.exit(0);

                }
            });
        });

        newVertx.deployVerticle(verticleName, deployments).onSuccess(event -> {
            if (LOG.isDebugEnabled()) {
                LOG.debug("启动" + testName + "测试服务-->成功!");
            }
            LocalDataCounter.setStartTime(options.getId(), System.currentTimeMillis());
            promise.complete();
        }).onFailure(event -> {
            LOG.error("启动" + testName + "测试服务-->失败:", event.getCause());
            promise.fail("启动" + testName + "测试服务-->失败:");
        });

    }


    /**
     * 检查请求是否有效
     *
     * @param options 请求的配置
     * @param handler 失败返回错误提示信息
     */
    private void checkRequest(OstRequestOptions options, Handler<AsyncResult<Void>> handler) {
        try {
            OstRequestType type = OstRequestType.valueOf(options.getType());
            HttpClientOptions hOptions = new HttpClientOptions();
            if (options.getCert() != null && OstSslCertType.valueOf(options.getCert()) != OstSslCertType.DEFAULT) {
                if (OstSslCertType.PFX == OstSslCertType.valueOf(options.getCert())) {
                    PfxOptions certOptions = new PfxOptions();
                    certOptions.setPassword(options.getCertKey());
                    certOptions.setValue(Buffer.buffer(options.getCertValue()));
                    hOptions.setPfxKeyCertOptions(certOptions);
                } else if (OstSslCertType.JKS == OstSslCertType.valueOf(options.getCert())) {
                    JksOptions certOptions = new JksOptions();
                    certOptions.setPassword(options.getCertKey());
                    certOptions.setValue(Buffer.buffer(options.getCertValue()));
                    hOptions.setKeyStoreOptions(certOptions);
                } else {
                    PemKeyCertOptions certOptions = new PemKeyCertOptions();
                    certOptions.setKeyValue(Buffer.buffer(options.getCertKey()));
                    certOptions.setCertValue(Buffer.buffer(options.getCertValue()));
                    hOptions.setPemKeyCertOptions(certOptions);
                }
            }

            if (type == OstRequestType.HTTP) {
                HttpClient httpClient = vertx.createHttpClient(hOptions);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("进行测试前请求正在检查HTTP后端服务是否可用...");
                }
                handler.handle(Future.succeededFuture());
                /*OstHttpRequestHandler.requestAbs(httpClient, options, res -> {
                    if (res.succeeded()) {
                        HttpClientResponse result = res.result();
                        int code = result.statusCode();
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("进行测试前请求检查->HTTP-->结果:" + code);
                        }
                        if (code >= 200 && code < 300) {
                            handler.handle(Future.succeededFuture());
                        } else {
                            handler.handle(Future.failedFuture("进行测试前请求检查失败:返回了无效的状态码: " + code));
                        }
                    } else {
                        String msg = res.cause().getMessage();
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("进行测试前请求检查->HTTP-->失败:", res);
                        }
                        handler.handle(Future.failedFuture("进行测试前请求检查失败:" + msg));
                    }
                });*/

            } else if (type == OstRequestType.WebSocket) {
                HttpClient httpClient = vertx.createHttpClient(hOptions);
                OstWebSocketRequestHandler.requestAbs(httpClient, options, res -> {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("进行测试前请求检查->WebSocket-->成功!");
                    }
                    handler.handle(Future.succeededFuture());
                }, err -> {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("进行测试前请求检查->WebSocket-->失败:", err);
                    }
                    String msg = err.getMessage();
                    handler.handle(Future.failedFuture("进行测试前请求检查失败:" + msg));
                });
            } else if (type == OstRequestType.TCP) {
                NetClientOptions cOption = new NetClientOptions();
                if (options.getCert() != null) {
                    if (OstSslCertType.valueOf(options.getCert()) != OstSslCertType.DEFAULT) {
                        if (OstSslCertType.PFX == OstSslCertType.valueOf(options.getCert())) {
                            PfxOptions certOptions = new PfxOptions();
                            certOptions.setPassword(options.getCertKey());
                            certOptions.setValue(Buffer.buffer(options.getCertValue()));
                            cOption.setPfxKeyCertOptions(certOptions);
                        } else if (OstSslCertType.JKS == OstSslCertType.valueOf(options.getCert())) {
                            JksOptions certOptions = new JksOptions();
                            certOptions.setPassword(options.getCertKey());
                            certOptions.setValue(Buffer.buffer(options.getCertValue()));
                            cOption.setKeyStoreOptions(certOptions);
                        } else {
                            PemKeyCertOptions certOptions = new PemKeyCertOptions();
                            certOptions.setKeyValue(Buffer.buffer(options.getCertKey()));
                            certOptions.setCertValue(Buffer.buffer(options.getCertValue()));
                            cOption.setPemKeyCertOptions(certOptions);
                        }
                    }
                    cOption.setSsl(true);
                }
                NetClient netClient = vertx.createNetClient(cOption);
                OstTcpRequestHandler.request(netClient, options, res -> {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("进行测试前请求检查->TCP-->成功!");
                    }
                    handler.handle(Future.succeededFuture());
                }, err -> {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("进行测试前请求检查->TCP-->失败:", err);
                    }
                    String msg = err.getMessage();
                    handler.handle(Future.failedFuture("进行测试前请求检查失败:" + msg));
                });
            } else {
                handler.handle(Future.failedFuture("无效的请求类型!"));
            }
        } catch (Exception e) {
            LOG.error("进行测试前请求检查-->失败:", e);
            handler.handle(Future.failedFuture(e.getMessage()));
        }

    }

    /**
     * 参数检查并加载请求信息<br>
     * type(String): 请求的类型:HTTP,WebSocket,TCP<br>
     * url(String):请求的url<br>
     * method(String): http请求的类型 {@link io.vertx.core.http.HttpMethod}<br>
     * isSSL(boolean): 是否使用SSL<br>
     * cert(String):证书的类型:DEFAULT,PEM,PFX,JKS <br>
     * certKey(String):证书的key <br>
     * certValue(String):证书的value <br>
     * headers(JsonArray(JsonObject){key,value}) 请求的header数据<br>
     * body(String):请求的body <br>
     * count(Long):请求的总次数 <br>
     * average(Long):每次请求多数次<br>
     * interval(Long):请求的间隔 <br>
     * keepAlive(boolean):是否保持连接 <br>
     * virtualUsers(Long):请求客户端数量 <br>
     *
     * @param result
     * @return
     */
    private void checkAndLoadRequestOptions(OstRequestOptions result, Handler<AsyncResult<OstRequestOptions>> handler) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行参数检查-->数据:" + Json.encode(result));
            }

            String url = result.getUrl();
            if (OstRequestType.valueOf(result.getType()) == OstRequestType.HTTP) {
                try {
                    new URL(url);
                } catch (MalformedURLException e) {
                    handler.handle(Future.failedFuture("无效的URL:" + url));
                    return;
                }
                try {
                    HttpMethod.valueOf(result.getMethod());
                } catch (Exception e) {
                    handler.handle(Future.failedFuture("无效的method:" + result.getMethod()));
                    return;
                }
            } else if (OstRequestType.valueOf(result.getType()) == OstRequestType.WebSocket) {
                if (!url.startsWith("ws") && !url.startsWith("wss")) {
                    handler.handle(Future.failedFuture("无效的URL:" + url));
                    return;
                }

            }
            result.setUrl(url);
            Boolean ssl = result.isSsl();
            if (ssl) {
                result.setSsl(ssl);
                OstSslCertType sslType = OstSslCertType.valueOf(result.getCert());
                if (sslType != OstSslCertType.DEFAULT) {

                    if (StringUtil.isNullOrEmpty(result.getCertKey()) || StringUtil.isNullOrEmpty(result.getCertValue())) {
                        handler.handle(Future.failedFuture("如果不使用默认SSL证书,证书的key与value不能为空"));
                        return;
                    }
                }
            }
           /* if (!result.getParameters().isEmpty()) {
                UserParameter.resolveConstant(result.getParameters());
            }*/
           /* if (body.getJsonArray("headers") != null) {
                MultiMap header = MultiMap.caseInsensitiveMultiMap();
                JsonArray th = body.getJsonArray("headers");
                for (int i = 0; i < th.size(); i++) {
                    JsonObject h = th.getJsonObject(i);
                    if (h.getString("key") != null && h.getString("value") != null) {
                        header.add(h.getString("key"), h.getString("value"));
                    }
                }
                if (header.size() > 0) {
                    result.setHeaders(header);
                }
            }*/

            int count = result.getCount();
            if (count < 1) {
                handler.handle(Future.failedFuture("无效的请求的总次数"));
                return;
            }

            int average = result.getAverage();
            if (average < 1) {
                result.setAverage(1);
            }

            handler.handle(Future.succeededFuture(result));
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行参数检查并加载请求信息-->失败:", e);
            }
            handler.handle(Future.failedFuture("缺少参数或存在无效的参数"));
        }
    }


}
