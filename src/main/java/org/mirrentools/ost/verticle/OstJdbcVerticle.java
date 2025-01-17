package org.mirrentools.ost.verticle;

import com.google.common.util.concurrent.RateLimiter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PfxOptions;
import org.mirrentools.ost.MainVerticle;
import org.mirrentools.ost.common.*;
import org.mirrentools.ost.enums.OstCommand;
import org.mirrentools.ost.enums.OstSslCertType;
import org.mirrentools.ost.handler.OstHttpRequestHandler;
import org.mirrentools.ost.model.OstRequestOptions;
import org.mirrentools.ost.model.OstResponseInfo;

/**
 * 处理HTTP请求的Verticle,创建时需要传入请求的optionsId(String):请求的id
 *
 * @author <a href="http://mirrentools.org">Mirren</a>
 */
public class OstJdbcVerticle extends AbstractVerticle {
    /**
     * 日志
     */
    private final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
    RateLimiter rateLimiter = null;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        try {
            // 注册HTTP测试处理器
            vertx.eventBus().consumer(EventBusAddress.HTTP_TEST_HANDLER, this::httpTestHandler);
            String optionsId = config().getString("optionsId");
            OstRequestOptions options = LocalDataRequestOptions.get(optionsId);
            if (options.getThroughput() != null && options.getThroughput() > 0) {
                rateLimiter = RateLimiter.create(options.getThroughput());
            }
            ServerWebSocket socket = LocalDataServerWebSocket.get(optionsId);
            Boolean created = LocalDataBoolean.putIfAbsent(optionsId, true);
            if (created == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("执行测试任务提交->" + deploymentID() + "-->进行发布任务!");
                }
                if (options.isKeepAlive()) {
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
                    hOptions.setMaxPoolSize(options.getPoolSize());

                    if (options.getTimeout() != null) {
                        hOptions.setConnectTimeout(options.getTimeout());
                    }
                    hOptions.setKeepAlive(options.isKeepAlive());
                    HttpClient httpClient = vertx.createHttpClient(hOptions);
                    // 共享http客户端
                    LocalDataHttpClient.put(optionsId, httpClient);
                }
                vertx.executeBlocking(push -> {
                    try { // 发布测试任务
                        int count = options.getCount();
                        int average = options.getAverage();

                        for (int i = 1; i <= count; i++) {

                            int size = i;
                            vertx.executeBlocking(exec -> {
                                if (rateLimiter != null) {
                                    rateLimiter.acquire(average);
                                }
                                if (average == 1) {
                                    JsonObject message = new JsonObject();
                                    message.put("id", optionsId);
                                    message.put("count", size);
                                    message.put("index", 1);
                                    message.put("init", !options.isKeepAlive());
                                    vertx.eventBus().send(EventBusAddress.HTTP_TEST_HANDLER, message);
                                }else {
                                    for (int j = 1; j <= average; j++) {
                                        JsonObject message = new JsonObject();
                                        message.put("id", optionsId);
                                        message.put("count", size);
                                        message.put("index", j);
                                        message.put("init", !options.isKeepAlive());
                                        vertx.eventBus().send(EventBusAddress.HTTP_TEST_HANDLER, message);
                                    }
                                }
                                OstResponseInfo proEnd = new OstResponseInfo();
                                proEnd.setCount(size);
                                writeMsg(proEnd, OstCommand.TEST_SUBMIT_PROGRESS, socket);
                                exec.complete();
                            },false, end -> {
                            });
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("执行测试任务提交-->成功!");
                        }
                        push.complete();
                    } catch (Exception e) {
                        push.fail(e);
                        LOG.error("执行测试任务提交-->失败:", e);
                    }
                }, startPromise);
            } else {
                startPromise.complete();
            }
        } catch (Exception e) {
            LOG.error("执行初始化HTTP测试Verticle-->失败:", e);
            startPromise.fail(e);
        }
    }

    /**
     * HTTP的测试处理器
     *
     * @param msg 接收参数JsonObject{id(String):请求id,count(int):第几批请求,index(int):第几次请求,init(boolean):是否创建客户端}
     */
    private void httpTestHandler(Message<JsonObject> msg) {
        String id = msg.body().getString("id");
        int count = msg.body().getInteger("count");
        int index = msg.body().getInteger("index");
        /*if (LOG.isDebugEnabled()) {
            LOG.debug("Thread[" + Thread.currentThread().getId() + "] [" + count + "-" + index + "]处理器:" + deploymentID());
        }*/
        boolean init = msg.body().getBoolean("init");
        ServerWebSocket socket = LocalDataServerWebSocket.get(id);
		/*if (socket == null || socket.isClosed()) {
			return;
		}*/
        OstRequestOptions options = LocalDataRequestOptions.get(id);

        final HttpClient httpClient;
        if (init) {
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
            hOptions.setMaxPoolSize(1);
            hOptions.setKeepAlive(false);
            httpClient = vertx.createHttpClient(hOptions);
        } else {
            httpClient = LocalDataHttpClient.get(id);
        }

        OstHttpRequestHandler.requestAbs(httpClient, options, res -> {

            OstResponseInfo info = new OstResponseInfo();
            info.setCount(count);
            info.setIndex(index);
            if (res.succeeded()) {
                LocalDataCounter.incrementAndGet(Constant.REQUEST_SUCCEEDED_PREFIX + id);
                info.setState(1);
                info.setCode(res.result().statusCode());
                if (options.isPrintResInfo()) {
                    res.result().bodyHandler(body -> {
                        info.setBody(body.toString());
                        writeMsg(info, OstCommand.TEST_LOG_RESPONSE, socket);
                    });
                }
            } else {

                LocalDataCounter.incrementAndGet(Constant.REQUEST_FAILED_PREFIX + id);
                info.setBody(res.cause().getMessage());
                info.setState(0);
                writeMsg(info, OstCommand.TEST_LOG_RESPONSE, socket);
            }
            if (count == LocalDataCounter.getCount(options.getId())) {
                LocalDataCounter.setEndTime(options.getId(), System.currentTimeMillis());
            }
        });
    }

    /**
     * 响应信息到前端
     *
     * @param info    响应信息
     * @param command 信息类型
     * @param socket
     */
    private void writeMsg(OstResponseInfo info, OstCommand command, ServerWebSocket socket) {
        String result = ResultFormat.success(command, info.toJson());
        if (socket == null || socket.isClosed()) {
            //System.out.println("msg:" + result);
            return;
        }
        socket.writeTextMessage(result);
    }

}
