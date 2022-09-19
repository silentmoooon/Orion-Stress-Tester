package org.mirrentools.ost.model;

import io.vertx.core.json.Json;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求配置
 * 
 * @author <a href="http://mirrentools.org">Mirren</a>
 *
 */
public class OstRequestOptions {
	/** 当前请求配置的id通常对应客户端WebSocket的写id */
	private String id;
	/** 请求的类型 */
	private String type;
	/** 主机地址,仅TCP有效 */
	private String host;
	/** 端口号,仅TCP有效 */
	private int port;
	/** 服务器的名称,仅TCP有效 */
	private String serverName;
	/** WebSocket的版本 */
	private String webSocketVersion;
	/** WenSocket的请求子协议 */
	private List<String> subProtocols;
	/** 请求的url */
	private String url;
	/** http请求的method类型 {@link io.vertx.core.http.HttpMethod} */
	private String method;
	/** 是否使用SSL */
	private boolean ssl;
	/** 证书的类型 */
	private String cert;
	/** 证书的key */
	private String certKey;
	/** 证书的value */
	private String certValue;
	/** 请求的header数据 */
	private Map<String,String> headers=new HashMap<>();
	private LinkedHashMap<String,String> parameters=new LinkedHashMap<>();
	/** 请求的body */
	private String body;
	/** 请求的总次数 */
	private int count;
	/** 同时并发数 */
	private int average;
	/** 请求的间隔 */
	private long interval;
	/** 是否输出URL服务器返回的数据 */
	private boolean printResInfo;
	/** 是否保持连接 */
	private boolean keepAlive;
	/** 最大建立连接数 */
	private int poolSize;
	/** 请求超时时间(ms) */
	private Integer timeout;
	/**
	 * 吞吐量 每秒请求数上限
	 */
	private Integer throughput;

	/**
	 * 获取当前请求配置的id通常对应客户端WebSocket的写id
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置当前请求配置的id通常对应客户端WebSocket的写id
	 * 
	 * @param id
	 * @return
	 */
	public OstRequestOptions setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * 获取请求类型
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置请求类型
	 * 
	 * @param type
	 * @return
	 */
	public OstRequestOptions setType(String type) {
		this.type = type;
		return this;
	}

	/**
	 * 获取主机地址,仅TCP类型时有效
	 * 
	 * @return
	 */
	public String getHost() {
		return host;
	}

	/**
	 * 设置主机地址,仅TCP类型时有效
	 * 
	 * @param host
	 * @return
	 */
	public OstRequestOptions setHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * 获取主机端口,仅TCP类型时有效
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 设置主机端口,仅TCP类型时有效
	 * 
	 * @param port
	 * @return
	 */
	public OstRequestOptions setPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * 获取主机名称,仅TCP类型时有效
	 * 
	 * @return
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * 设置主机名称,仅TCP类型时有效
	 * 
	 * @param serverName
	 * @return
	 */
	public OstRequestOptions setServerName(String serverName) {
		this.serverName = serverName;
		return this;
	}

	/**
	 * 获取WebSocket的版本号
	 * 
	 * @return
	 */
	public String getWebSocketVersion() {
		return webSocketVersion;
	}

	/**
	 * 设置WebSocket的版本号
	 * 
	 * @param webSocketVersion
	 * @return
	 */
	public OstRequestOptions setWebSocketVersion(String webSocketVersion) {
		this.webSocketVersion = webSocketVersion;
		return this;
	}

	/**
	 * 获取WebSocket的子协议
	 * 
	 * @return
	 */
	public List<String> getSubProtocols() {
		return subProtocols;
	}

	/**
	 * 设置WebSocket的子协议
	 * 
	 * @param subProtocols
	 * @return
	 */
	public OstRequestOptions setSubProtocols(List<String> subProtocols) {
		this.subProtocols = subProtocols;
		return this;
	}

	/**
	 * 获取URL仅http跟WebSocket类型时有效
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置URL仅http跟WebSocket类型时有效
	 * 
	 * @param url
	 * @return
	 */
	public OstRequestOptions setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * 获取HTTP的请求方法
	 * 
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 设置HTTP的请求方法
	 * 
	 * @param method
	 * @return
	 */
	public OstRequestOptions setMethod(String method) {
		this.method = method;
		return this;
	}

	/**
	 * 是否为SSL
	 * 
	 * @return
	 */
	public boolean isSsl() {
		return ssl;
	}

	/**
	 * 设置是否为SSL
	 * 
	 * @param ssl
	 * @return
	 */
	public OstRequestOptions setSsl(boolean ssl) {
		this.ssl = ssl;
		return this;
	}

	/**
	 * 获取证书类型
	 * 
	 * @return
	 */
	public String getCert() {
		return cert;
	}

	/**
	 * 设置证书类型
	 * 
	 * @param cert
	 * @return
	 */
	public OstRequestOptions setCert(String cert) {
		this.cert = cert;
		return this;
	}

	/**
	 * 获取证书的key
	 * 
	 * @return
	 */
	public String getCertKey() {
		return certKey;
	}

	/**
	 * 设置证书的key
	 * 
	 * @param certKey
	 * @return
	 */
	public OstRequestOptions setCertKey(String certKey) {
		this.certKey = certKey;
		return this;
	}

	/**
	 * 获取证书的值
	 * 
	 * @return
	 */
	public String getCertValue() {
		return certValue;
	}

	/**
	 * 设置证书的值
	 * 
	 * @param certValue
	 * @return
	 */
	public OstRequestOptions setCertValue(String certValue) {
		this.certValue = certValue;
		return this;
	}

	/**
	 * 获取Header
	 * 
	 * @return
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * 设置Header
	 * 
	 * @param headers
	 * @return
	 */
	public OstRequestOptions setHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	/**
	 * 获取Body
	 * 
	 * @return
	 */
	public String getBody() {
		return body;
	}


	/**
	 * 设置body
	 * 
	 * @param body
	 * @return
	 */
	public OstRequestOptions setBody(String body) {
		this.body = body;
		return this;
	}

	/**
	 * 获取请求的总次数
	 * 
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 设置请求的总次数
	 * 
	 * @param count
	 * @return
	 */
	public OstRequestOptions setCount(int count) {
		this.count = count;
		return this;
	}

	/**
	 * 获取同时并发数
	 * 
	 * @return
	 */
	public int getAverage() {
		return average;
	}

	/**
	 * 设置每次请求的数量
	 * 
	 * @param average
	 * @return
	 */
	public OstRequestOptions setAverage(int average) {
		this.average = average;
		return this;
	}


	/**
	 * 获取每次请求间隔
	 * 
	 * @return
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * 设置每次请求间隔
	 * 
	 * @param interval
	 * @return
	 */
	public OstRequestOptions setInterval(long interval) {
		this.interval = interval;
		return this;
	}

	/**
	 * 获取是否返回响应信息
	 * 
	 * @return
	 */
	public boolean isPrintResInfo() {
		return printResInfo;
	}

	/**
	 * 设置是否返回响应信息
	 * 
	 * @param printResInfo
	 * @return
	 */
	public OstRequestOptions setPrintResInfo(boolean printResInfo) {
		this.printResInfo = printResInfo;
		return this;
	}

	/**
	 * 获取是否保持连接
	 * 
	 * @return
	 */
	public boolean isKeepAlive() {
		return keepAlive;
	}

	/**
	 * 设置是否保持连接
	 * 
	 * @param keepAlive
	 * @return
	 */
	public OstRequestOptions setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	/**
	 * 获取最大建立连接数
	 * 
	 * @return
	 */
	public int getPoolSize() {
		return poolSize;
	}

	/**
	 * 设置最大建立连接数
	 * 
	 * @param poolSize
	 * @return
	 */
	public OstRequestOptions setPoolSize(int poolSize) {
		this.poolSize = poolSize;
		return this;
	}

	/**
	 * 获取请求超时时间(ms)
	 * 
	 * @return
	 */
	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * 设置请求超时时间(ms)
	 * 
	 * @param timeout
	 * @return
	 */
	public OstRequestOptions setTimeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}

	public Integer getThroughput() {
		return throughput;
	}



	public OstRequestOptions setThroughput(Integer throughput) {
		this.throughput = throughput;
		return this;
	}

	public LinkedHashMap<String, String> getParameters() {
		return parameters;
	}

	public OstRequestOptions setParameters(LinkedHashMap<String, String> parameters) {
		this.parameters = parameters;
		return this;
	}

	@Override
	public String toString() {
		return "OstRequestOptions [id=" + id + ", type=" + type + ", host=" + host + ", port=" + port + ", serverName=" + serverName + ", webSocketVersion=" + webSocketVersion + ", subProtocols="
				+ subProtocols + ", url=" + url + ", method=" + method + ", ssl=" + ssl + ", cert=" + cert + ", certKey=" + certKey + ", certValue=" + certValue + ", headers=" + headers + ", body=" + body
				+ ", count=" + count + ", average=" + average + ", interval=" + interval + ", printResInfo=" + printResInfo + ", keepAlive=" + keepAlive + ", poolSize=" + poolSize + ", timeout=" + timeout
				+ "]";
	}

	public static void main(String[] args) {

		OstRequestOptions options = new OstRequestOptions();
		options.setType("HTTP");
		options.setMethod("GET");
		options.setUrl("http://127.0.0.1:8170");
		LinkedHashMap<String, String> paramters = new LinkedHashMap<>();
		paramters.put("orderNo", "0084${date.yyyyMMddHHmmssSSS}${random.num(11)}");
		options.setParameters(paramters);
		options.setBody("{\"reqDate\":\"${date.yyyyMMdd}\",\"orderNo\":\"${orderNo}\"}");
		//http是否keepAlive 以及保留的连接池数据
		options.setKeepAlive(true);
		options.setPoolSize(50);
		//是否打印响应信息(暂时不会打印)
		options.setPrintResInfo(false);
		//请求总数量
		options.setCount(1000000);
		//同时发送数量,N个请求积累到一起发送,如果不设置默认为1,不设置也没关系
		options.setAverage(0);
		// 吞吐量
		options.setThroughput(5000);
		//是否为https
		options.setSsl(false);
		//https证书 一般为默认
		options.setCert("DEFAULT");
		System.out.println(Json.encode(options));
	}

}
