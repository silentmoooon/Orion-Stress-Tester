package org.mirrentools.ost.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 本地计数器数据存储
 * 
 * @author <a href="http://szmirren.com">Mirren</a>
 *
 */
public class LocalDataCounter {
	/** 计数器 */
	private static Map<String, AtomicLong> COUNTER_MAP = new ConcurrentHashMap<>();
	private static Map<String, Long> Start_TIME_MAP = new ConcurrentHashMap<>();

	/**
	 * 数量+1
	 * 
	 * @param key
	 *          计数器id
	 * @return 添加后的数量
	 */
	public static long incrementAndGet(String key) {
		AtomicLong count = COUNTER_MAP.computeIfAbsent(key, n -> new AtomicLong(0L));
		long result = count.incrementAndGet();
		return result;
	}

	public static void setCount(String key,long count) {
		AtomicLong counter = COUNTER_MAP.computeIfAbsent(key, n -> new AtomicLong(0L));
		counter.set(count);
	}
	public static void setStartTime(String optionsId,long time){
		  Start_TIME_MAP.put(Constant.REQUEST_START_TIME_PREFIX+optionsId,time);
		COUNTER_MAP.put(Constant.REQUEST_LAST_TIME_PREFIX + optionsId, new AtomicLong(time));
	}
	public static long getStartTime(String optionsId){
		return Start_TIME_MAP.get(Constant.REQUEST_START_TIME_PREFIX+optionsId);
	}

	public static void setEndTime(String optionsId,long time){
		Start_TIME_MAP.put(Constant.REQUEST_END_TIME_PREFIX+optionsId,time);
	}
	public static Long getEndTime(String optionsId){
		return Start_TIME_MAP.get(Constant.REQUEST_END_TIME_PREFIX+optionsId);
	}

	/**
	 * 数量-1
	 * 
	 * @param key
	 *          计数器id
	 * @return 减少后的数量
	 */
	public static long decrementAndGet(String key) {
		AtomicLong count = COUNTER_MAP.computeIfAbsent(key, n -> new AtomicLong(0L));
		long result = count.decrementAndGet();
		return result;
	}

	/**
	 * 获取数量
	 * 
	 * @param key
	 *          计数器id
	 * @return 当前数量
	 */
	public static long getCount(String key) {
		if (key == null || COUNTER_MAP.get(key) == null) {
			return 0L;
		}
		AtomicLong count = COUNTER_MAP.computeIfAbsent(key, n -> new AtomicLong(0));
		return count.get();
	}

	/**
	 * 获取计数器
	 * 
	 * @param key
	 *          计数器id
	 * @return 计数器,如果计数器为空或者key为空返回一个结果为0的计数器
	 */
	public static AtomicLong getCounter(String key) {
		if (key == null || COUNTER_MAP.get(key) == null) {
			return null;
		}
		return COUNTER_MAP.computeIfAbsent(key, n -> new AtomicLong(0));
	}

	/**
	 * 新建或替换计数器,初始值=0
	 * 
	 * @param key
	 *          计数器id,不能为空
	 * @return 返回创建后的计数器值
	 * @throws NullPointerException
	 *           如果key为空
	 */
	public static AtomicLong newCounter(String key) {
		AtomicLong result = new AtomicLong(0L);
		COUNTER_MAP.put(key, result);
		return result;
	}

	/**
	 * 新建或替换计数器
	 * 
	 * @param key
	 *          计数器的id
	 * @param value
	 *          初始值
	 * @return 返回创建后的计数器值
	 * @throws NullPointerException
	 *           如果key为空
	 */
	public static AtomicLong newCounter(String key, long value) {
		AtomicLong result = new AtomicLong(value);
		COUNTER_MAP.put(key, result);
		return result;
	}

	/**
	 * 删除一个计数器
	 * 
	 * @param key
	 * @return
	 */
	public static AtomicLong remove(String key) {
		if (key == null) {
			return null;
		}
		return COUNTER_MAP.remove(key);
	}
}
