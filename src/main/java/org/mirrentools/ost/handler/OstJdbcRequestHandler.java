package org.mirrentools.ost.handler;

import io.netty.util.internal.StringUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import org.mirrentools.ost.model.OstRequestOptions;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP请求处理器
 *
 * @author <a href="http://mirrentools.org">Mirren</a>
 */
public interface OstJdbcRequestHandler {
    /**
     * 执行HTTP测试请求
     *
     * @param httpClient
     * @param options
     * @param handler
     */
    @SuppressWarnings("deprecation")
    static void requestAbs(HttpClient httpClient, OstRequestOptions options, Handler<AsyncResult<HttpClientResponse>> handler) {
        try {
            String url = options.getUrl();

            final Map<String, String> userParameter = new HashMap<>();
            LinkedHashMap<String, String> originParameters = options.getParameters();
            if (!originParameters.isEmpty()) {
                UserParameter.resolve(originParameters, userParameter);
            }
            if (url.contains("${") && url.contains("}") && url.indexOf("{$") < url.indexOf("}")) {
                url = UserParameter.resolveExpression(url, userParameter);
            }
            httpClient.request(HttpMethod.valueOf(options.getMethod()), url).onSuccess(request -> {
                if (options.getTimeout() != null) {
                    request.setTimeout(options.getTimeout());
                }
                if (options.getHeaders() != null) {
                    request.headers().addAll(options.getHeaders());
                }
                Future<HttpClientResponse> send;
                String body = options.getBody();
                //System.out.println("body:("+body+")");
                if (!StringUtil.isNullOrEmpty(body)) {
                    if (body.contains("${") && body.contains("}") && body.indexOf("{$") < body.indexOf("}")) {
                        body = UserParameter.resolveExpression(body, userParameter);
                    /*System.out.println("body after:("+body+")");
                    System.out.println("\n");*/
                    }
                    send = request.send(Buffer.buffer(body));
                } else {
                    send = request.send();
                }
                send.onSuccess(res -> handler.handle(Future.succeededFuture(res))).onFailure(err -> handler.handle(Future.failedFuture(err)));
            });
        } catch (Exception e) {
            handler.handle(Future.failedFuture(e));
        }
    }


    //
    // /**
    // *
    // * @param httpClient
    // * Http客户端
    // * @param options
    // * 请求配置信息
    // * @param succeededHandler
    // * 成功处理器
    // * @param exceptionHandler
    // * 失败处理器
    // */
    // static void requestAbs(HttpClient httpClient, OstRequestOptions options,
    // Handler<HttpClientResponse> succeededHandler, Handler<Throwable>
    // exceptionHandler) {
    // try {
    // requestAbs(httpClient, options, null, succeededHandler, exceptionHandler,
    // null);
    // } catch (Exception e) {
    // if (exceptionHandler != null) {
    // exceptionHandler.handle(e);
    // }
    // }
    // }
    //
    // /**
    // *
    // * @param httpClient
    // * Http客户端
    // * @param options
    // * 请求配置信息
    // * @param connectionHandler
    // * 连接建立的处理器
    // * @param succeededHandler
    // * 成功处理器
    // * @param exceptionHandler
    // * 失败处理器
    // */
    // static void requestAbs(HttpClient httpClient, OstRequestOptions options,
    // Handler<HttpConnection> connectionHandler, Handler<HttpClientResponse>
    // succeededHandler, Handler<Throwable> exceptionHandler,
    // Handler<AsyncResult<OstRequestStatistics>> statisticsHandler) {
    // try {
    // requestAbs(httpClient, options.getMethod(), options.getUrl(),
    // options.getHeaders(), options.getBody(), connectionHandler,
    // succeededHandler, exceptionHandler, statisticsHandler);
    // } catch (Exception e) {
    // if (exceptionHandler != null) {
    // exceptionHandler.handle(e);
    // }
    // }
    // }
    //
    // /**
    // * 执行HTTP请求
    // *
    // * @param httpClient
    // * Http客户端
    // * @param method
    // * 请求方法
    // * @param url
    // * 请求路径
    // * @param headers
    // * 请求的headers
    // * @param body
    // * 请求的body
    // * @param succeededHandler
    // * 成功的处理器
    // * @param exceptionHandler
    // * 失败的处理器
    // */
    // static void requestAbs(HttpClient httpClient, HttpMethod method, String
    // url, MultiMap headers, Buffer body, Handler<HttpClientResponse>
    // succeededHandler, Handler<Throwable> exceptionHandler) {
    // requestAbs(httpClient, method, url, headers, body, null, succeededHandler,
    // exceptionHandler, null);
    // }
    //
    // /**
    // * 执行HTTP请求
    // *
    // * @param httpClient
    // * Http客户端
    // * @param method
    // * 请求方法
    // * @param url
    // * 请求路径
    // * @param headers
    // * 请求的headers
    // * @param body
    // * 请求的body
    // * @param connectionHandler
    // * 连接建立的处理器
    // * @param succeededHandler
    // * 成功的处理器
    // * @param exceptionHandler
    // * 失败的处理器
    // */
    // @SuppressWarnings("deprecation")
    // static void requestAbs(HttpClient httpClient, HttpMethod method, String
    // url, MultiMap headers, Buffer body, Handler<HttpConnection>
    // connectionHandler, Handler<HttpClientResponse> succeededHandler,
    // Handler<Throwable> exceptionHandler,
    // Handler<AsyncResult<OstRequestStatistics>> statisticsHandler) {
    // try {
    // OstRequestStatistics statistics = new OstRequestStatistics();
    // statistics.setStartTime(System.nanoTime());
    // HttpClientRequest request = httpClient.requestAbs(method, url);
    // if (headers != null) {
    // request.headers().addAll(headers);
    // }
    // httpClient.connectionHandler(conn -> {
    // statistics.setConnTime(System.nanoTime());
    // if (connectionHandler != null) {
    // connectionHandler.handle(conn);
    // }
    // });
    // if (exceptionHandler != null) {
    // request.exceptionHandler(exceptionHandler);
    // }
    // request.handler(res -> {
    // statistics.setEndTime(System.nanoTime());
    // String rlen = res.getHeader(HttpHeaders.CONTENT_LENGTH);
    // if (rlen != null) {
    // try {
    // statistics.setResponseLen(new Long(rlen));
    // } catch (NumberFormatException e) {
    // }
    // }
    //
    // if (succeededHandler != null) {
    // int statusCode = res.statusCode();
    // if (statusCode == 301 || statusCode == 302 || statusCode == 303 ||
    // statusCode == 307 || statusCode == 308) {
    // Future<HttpClientRequest> apply = httpClient.redirectHandler().apply(res);
    // if (apply.succeeded()) {
    // HttpClientRequest result = apply.result();
    // if (exceptionHandler != null) {
    // result.exceptionHandler(exceptionHandler);
    // }
    // result.handler(succeededHandler);
    // result.end();
    // } else {
    // if (exceptionHandler != null) {
    // exceptionHandler.handle(apply.cause());
    // }
    // }
    // } else {
    // succeededHandler.handle(res);
    // }
    // }
    // });
    // if (body != null) {
    // statistics.setRequestLen(body.length());
    // request.end(body);
    // } else {
    // request.end();
    // }
    // if (statisticsHandler != null) {
    // statisticsHandler.handle(Future.succeededFuture(statistics));
    // }
    // } catch (Exception e) {
    // if (exceptionHandler != null) {
    // exceptionHandler.handle(e);
    // }
    // if (statisticsHandler != null) {
    // statisticsHandler.handle(Future.failedFuture(e));
    // }
    // }
    // }
}
