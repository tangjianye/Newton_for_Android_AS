package com.fpliu.newton.framework.util;

/**
 * 字节操作工具类
 * 
 * @author 792793182@qq.com 2015-05-24
 *
 */
public final class ByteUtil {

	private ByteUtil() { }

	/**
	 * 将字节数组转换成长整数
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 长整数
	 */
	public static long toLong(byte[] bytes) {
		long temp = 0;
		long res = 0;
		for (int i = 0; i < 8; i++) {
			res <<= 8;
			temp = bytes[i] & 0xff;
			res |= temp;
		}
		return res;
	}
	
	/**
	 * 将字节数组转换成整数
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 整数
	 */
	public static int toInt(byte[] bytes) {
		int result = bytes[0] & 0xFF;
		result |= ((bytes[1] << 8) & 0xFF00);
		result |= ((bytes[2] << 16) & 0xFF0000);
		result |= ((bytes[3] << 24) & 0xFF000000);
		return result;
	}

	/**
	 * 将整数转成字节
	 * @param number  整数
	 * @return        小端模式的字节存储
	 */
	public static byte[] toByte(int number) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (number & 0xff);
		bytes[1] = (byte) ((number >> 8) & 0xff);
		bytes[2] = (byte) ((number >> 16) & 0xff);
		bytes[3] = (byte) ((number >> 24) & 0xff);
		return bytes;
	}
	
	/**
	 * 将字节数组转换成短整数
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 短整数
	 */
	public static short toShort(byte[] bytes) {
		short result = (short) (bytes[0] & 0xFF);
		result |= ((bytes[1] << 8) & 0xFF00);
		return result;
	}

	/**
	 * 将短整数转成字节
	 * @param number  短整数
	 * @return        小端模式的字节存储
	 */
	public static byte[] toByte(short number) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (number & 0xff);
		bytes[1] = (byte) ((number >> 8) & 0xff);
		return bytes;
	}
}