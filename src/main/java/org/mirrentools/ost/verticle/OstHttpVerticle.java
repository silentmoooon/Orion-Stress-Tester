package org.mirrentools.ost.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
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
import org.mirrentools.ost.task.TaskBean;

/**
 * 处理HTTP请求的Verticle,创建时需要传入请求的optionsId(String):请求的id
 *
 * @author <a href="http://mirrentools.org">Mirren</a>
 */
public class OstHttpVerticle extends AbstractVerticle {
    /**
     * 日志
     */
    private final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        try {
            // 注册HTTP测试处理器
            //vertx.eventBus().consumer(EventBusAddress.HTTP_TEST_HANDLER, this::httpTestHandler);
            String optionsId = config().getString("optionsId");
            OstRequestOptions options = LocalDataRequestOptions.get(optionsId);

            ServerWebSocket socket = LocalDataServerWebSocket.get(optionsId);
            TaskBean taskBean = MainVerticle.TASK_QUEUE.poll();
            if (taskBean != null) {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("执行测试任务提交->" + deploymentID() + "-->进行发布任务!");
                }


                int count = taskBean.getEndIndex() - taskBean.getStartIndex() + 1;
                vertx.setTimer(1, event -> {

                    for (long i = taskBean.getStartIndex(); i <= taskBean.getEndIndex(); i++) {
                        JsonObject message = new JsonObject();
                        message.put("id", optionsId);
                        message.put("count", i);
                        message.put("index", 1);
                        message.put("init", !options.isKeepAlive());
                        if (MainVerticle.rateLimiter != null) {
                            vertx.executeBlocking(event1 -> {
                                MainVerticle.rateLimiter.acquire(1);
                                send(message);

                            }, false);
                        } else {
                            send(message);
                        }
                        //vertx.eventBus().send(EventBusAddress.HTTP_TEST_HANDLER, message);

                    }
                });

                startPromise.complete();
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

       /* if (MainVerticle.rateLimiter != null) {
            vertx.executeBlocking(event -> {
                MainVerticle.rateLimiter.acquire(1);

                send(msg);
            }, false);

        } else {
            send(msg);
        }*/

    }

    private void send(JsonObject msg) {
        String id = msg.getString("id");
        int count = msg.getInteger("count");
        int index = msg.getInteger("index");
        /*if (LOG.isDebugEnabled()) {
            LOG.debug("Thread[" + Thread.currentThread().getId() + "] [" + count + "-" + index + "]处理器:" + deploymentID());
        }*/
        boolean init = msg.getBoolean("init");
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
            hOptions.setDefaultHost(options.getHost());
            hOptions.setDefaultPort(options.getPort());
            httpClient = vertx.createHttpClient(hOptions);
        } else {
            httpClient = LocalDataHttpClient.get(id);
        }
        OstHttpRequestHandler.request(httpClient, options, res -> {

            if (options.isPrintResInfo()) {
                OstResponseInfo info = new OstResponseInfo();
                info.setCount(count);
                info.setIndex(index);
                if (res.succeeded()) {
                    info.setState(1);
                    info.setCode(res.result().statusCode());
                } else {
                    info.setBody(res.cause().getMessage());
                    info.setState(0);
                }
                res.result().bodyHandler(body -> {
                    info.setBody(body.toString());
                    writeMsg(info, OstCommand.TEST_LOG_RESPONSE, socket);
                });
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
        if (socket == null || socket.isClosed()) {
            System.out.println("msg:" + info.toString());
            return;
        }
        String result = ResultFormat.success(command, info.toJson());

        socket.writeTextMessage(result);
    }

}
