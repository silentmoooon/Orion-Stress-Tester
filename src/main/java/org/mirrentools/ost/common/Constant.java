package org.mirrentools.ost.common;

/**
 * 常量
 * 
 * @author <a href="http://mirrentools.org">Mirren</a>
 *
 */
public interface Constant {
	/** 常量code */
	public final static String CODE = "code";
	/** 常量msg */
	public final static String MSG = "msg";
	/** 常量data */
	public final static String DATA = "data";
	/** 请求成功数量的前缀,通常跟请求id配合使用 */
	public final static String REQUEST_SUCCEEDED_PREFIX = "request://succeeded";
	/** 请求失败数量的前缀,通常跟请求id配合使用 */
	public final static String REQUEST_FAILED_PREFIX = "request://failed";
	public final static String REQUEST_LAST_COUNT_PREFIX = "request://lastCount";
	public final static String REQUEST_LAST_TIME_PREFIX = "request://lastTime";
	public final static String REQUEST_START_TIME_PREFIX = "request://startTime";
	public final static String REQUEST_END_TIME_PREFIX = "request://endTime";

}
