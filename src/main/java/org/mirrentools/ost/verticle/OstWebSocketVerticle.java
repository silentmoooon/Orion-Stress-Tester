package org.mirrentools.ost.verticle;

import org.mirrentools.ost.MainVerticle;
import org.mirrentools.ost.common.Constant;
import org.mirrentools.ost.common.EventBusAddress;
import org.mirrentools.ost.common.LocalDataBoolean;
import org.mirrentools.ost.common.LocalDataCounter;
import org.mirrentools.ost.common.LocalDataHttpClient;
import org.mirrentools.ost.common.LocalDataRequestOptions;
import org.mirrentools.ost.common.LocalDataServerWebSocket;
import org.mirrentools.ost.common.ResultFormat;
import org.mirrentools.ost.enums.OstCommand;
import org.mirrentools.ost.enums.OstSslCertType;
import org.mirrentools.ost.handler.OstWebSocketRequestHandler;
import org.mirrentools.ost.model.OstRequestOptions;
import org.mirrentools.ost.model.OstResponseInfo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PfxOptions;

/**
 * 处理WebSocket请求的Verticle,创建时需要传入请求的optionsId(String):请求的id
 * 
 * @author <a href="http://mirrentools.org">Mirren</a>
 *
 */
public class OstWebSocketVerticle extends AbstractVerticle {
	/** 日志 */
	private final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		try {
			// 注册WebSocket测试处理器
			vertx.eventBus().consumer(EventBusAddress.WEB_SOCKET_TEST_HANDLER, this::webSocketTestHandler);
			String optionsId = config().getString("optionsId");
			OstRequestOptions options = LocalDataRequestOptions.get(optionsId);
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
					int indeTime = ((Number) (options.getCount() * (options.getInterval() / 1000))).intValue();
					hOptions.setIdleTimeout(indeTime);
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
						for (int i = 1; i <= count; i++) {
							if (socket.isClosed()) {
								break;
							}
							JsonObject message = new JsonObject();
							message.put("id", optionsId);
							message.put("count", i);
							message.put("index", 1);
							message.put("init", !options.isKeepAlive());
							vertx.eventBus().send(EventBusAddress.WEB_SOCKET_TEST_HANDLER, message);
							OstResponseInfo proEnd = new OstResponseInfo();
							proEnd.setCount(i);
							writeMsg(proEnd, OstCommand.TEST_SUBMIT_PROGRESS, socket);
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
			LOG.error("执行初始化WebSocket测试Verticle-->失败:", e);
			startPromise.fail(e);
		}
	}

	/**
	 * WebSocket的测试处理器
	 * 
	 * @param msg
	 *          接收参数JsonObject{id(String):请求id,count(int):第几批请求,index(int):第几次请求,init(boolean):是否创建客户端}
	 */
	private void webSocketTestHandler(Message<JsonObject> msg) {
		String id = msg.body().getString("id");
		int count = msg.body().getInteger("count");
		if (LOG.isDebugEnabled()) {
			LOG.debug("Thread[" + Thread.currentThread().getId() + "] [" + count + "]处理器:" + deploymentID());
		}
		boolean init = msg.body().getBoolean("init");
		ServerWebSocket socket = LocalDataServerWebSocket.get(id);
		if (socket == null || socket.isClosed()) {
			return;
		}
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
			if (options.getTimeout() != null) {
				hOptions.setConnectTimeout(options.getTimeout());
			}
			httpClient = vertx.createHttpClient(hOptions);
		} else {
			httpClient = LocalDataHttpClient.get(id);
		}

		OstWebSocketRequestHandler.requestAbs(httpClient, options, res -> {
			OstResponseInfo info = new OstResponseInfo();
			info.setCount(count);
			if (res.succeeded()) {
				WebSocket webSocket = res.result();
				Buffer buffer = Buffer.buffer();
				if (options.isPrintResInfo()) {
					webSocket.handler(buffer::appendBuffer);
				}
				vertx.executeBlocking(exec -> {
					try {
						long initTime = System.currentTimeMillis();
						for (int i = 0; i < options.getAverage(); i++) {
							int when = (i + 1);
							boolean ended = (when == options.getAverage());
							long startTime = System.currentTimeMillis() - initTime;
							long oriTime = options.getInterval() * i;
							long execTime = startTime > oriTime ? 1 : (oriTime - startTime);
							vertx.setTimer(execTime < 1 ? 1 : execTime, tid -> {
								if (LOG.isDebugEnabled()) {
									LOG.debug("WebSocket正在发送信息-->第[" + count + "-" + when + "]次!");
								}
								if (webSocket.isClosed()) {
									exec.tryComplete();
									return;
								}
								webSocket.writeTextMessage(options.getBody() == null ? "" : options.getBody().toString(), send -> {
									if (ended) {
										if (!options.isKeepAlive()) {
											webSocket.close();
										}
										exec.tryComplete();
									}
								});
							});
						}
					} catch (Exception e) {
						System.out.println(e);
						exec.fail(e);
					}
				}, end -> {
					if (socket==null||socket.isClosed()) {
						return;
					}
					if (end.succeeded()) {
						LocalDataCounter.incrementAndGet(Constant.REQUEST_SUCCEEDED_PREFIX + id);
						info.setState(1);
						info.setCode(webSocket.closeStatusCode() == null ? 0 : webSocket.closeStatusCode());
						if (options.isPrintResInfo()) {
							info.setBody(buffer.toString());
							writeMsg(info, OstCommand.TEST_LOG_RESPONSE, socket);
						}
						if (!webSocket.isClosed()) {
							webSocket.endHandler(ed -> {
								if (init) {
									httpClient.close();
								}
							});
						}
					} else {
						if (socket==null||socket.isClosed()) {
							return;
						}
						LocalDataCounter.incrementAndGet(Constant.REQUEST_FAILED_PREFIX + id);
						info.setBody(res.cause() == null ? "" : res.cause().getMessage());
						if (init) {
							httpClient.close();
						}
						info.setState(0);
						writeMsg(info, OstCommand.TEST_LOG_RESPONSE, socket);
					}
				});
			} else {
				if (socket==null||socket.isClosed()) {
					return;
				}
				LocalDataCounter.incrementAndGet(Constant.REQUEST_FAILED_PREFIX + id);
				info.setBody(res.cause().getMessage());
				if (init) {
					httpClient.close();
				}
				info.setState(0);
				writeMsg(info, OstCommand.TEST_LOG_RESPONSE, socket);
			}
		});
	}

	/**
	 * 响应信息到前端
	 * 
	 * @param info
	 *          响应信息
	 * @param command
	 *          信息类型
	 * @param socket
	 */
	private void writeMsg(OstResponseInfo info, OstCommand command, ServerWebSocket socket) {
		if (socket == null || socket.isClosed()) {
			return;
		}
		String result = ResultFormat.success(command, info.toJson());
		socket.writeTextMessage(result);
	}

}
