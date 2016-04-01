package com.fpliu.newton.base.setting;

/**
 * 设置
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public interface ISetting {
	
	/**
	 * 该键是否被设置过值
	 */
	boolean isSetted(String key);
	
	/**
	 * 保存boolean类型的值
	 * @param key   键
	 * @param value 值
	 */
	void setSetting(String key, boolean value);
	
	/**
	 * 保存int类型的值
	 * @param key   键
	 * @param value 值
	 */
	void setSetting(String key, int value);
	
	/**
	 * 保存float类型的值
	 * @param key   键
	 * @param value 值
	 */
	void setSetting(String key, float value);
	
	/**
	 * 保存value类型的值
	 * @param key   键
	 * @param value 值
	 */
	void setSetting(String key, long value);
	
	/**
	 * 保存String类型的值
	 * @param key   键
	 * @param value 值
	 */
	void setSetting(String key, String value);
	
	/**
     * 删除缓存中的key
     * @param key
     */
	void removeSetting(String key);

	/**
	 * 获取boolean类型的值
	 * @param key   键
	 */
	boolean getBoolean(String key);
	
	/**
	 * 获取boolean类型的值，如果不存在就返回给定的默认值，并将默认值保存起来
	 * @param key   键
	 */
	boolean getBoolean(String key, boolean defaultValue);
	
	/**
	 * 获取int类型的值
	 * @param key   键
	 */
	int getInt(String key);
	
	/**
	 * 获取int类型的值，如果不存在就返回给定的默认值，并将默认值保存起来
	 * @param key   键
	 */
	int getInt(String key, int defaultValue);
	
	/**
	 * 获取float类型的值
	 * @param key   键
	 */
	float getFloat(String key);
	
	/**
	 * 获取float类型的值，如果不存在就返回给定的默认值，并将默认值保存起来
	 * @param key   键
	 */
	float getFloat(String key, float defaultValue);
	
	/**
	 * 获取long类型的值
	 * @param key   键
	 */
	long getLong(String key);
	
	/**
	 * 获取long类型的值，如果不存在就返回给定的默认值，并将默认值保存起来
	 * @param key   键
	 */
	long getLong(String key, long defaultValue);
	
	/**
	 * 获取String类型的值
	 * @param key   键
	 */
	String getString(String key);
	
	/**
	 * 获取String类型的值，如果不存在就返回给定的默认值，并将默认值保存起来
	 * @param key   键
	 */
	String getString(String key, String defaultValue);
	
	/**
	 * 序列化对象
	 * @param fileName 文件名(完成路径)
	 * @param object   对象
	 */
	void saveObject(String filePath, Object object);
	
	/**
	 * 反序列化
	 * @param fileName 文件名
	 */
	Object readObject(String fileName);
	
	/**
	 * 清除序列化的数据
	 */
	void clearObject(String fileName);
}
