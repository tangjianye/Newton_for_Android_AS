package com.fpliu.newton.framework.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.fpliu.newton.base.DebugLog;

/**
 * 对称加密和解密
 * 
 * @author 792793182@qq.com 2014-09-28
 *
 */
public final class SymmetricalEncryption {

	private static final String TAG = SymmetricalEncryption.class.getSimpleName();

	private SymmetricalEncryption() { }

	/**
	 * 加密
	 * @param algorithms 算法
	 * @param input      要加密的数据
	 * @param key        密钥
	 * @return           加密后的数据
	 */
	public static byte[] encrypt(Algorithm algorithms, byte[] input, byte[] key) {
		return crypt(Cipher.ENCRYPT_MODE, algorithms, input, key);
	}

	/**
	 * 解密
	 * @param algorithms 算法
	 * @param input      要解密的数据
	 * @param key        密钥
	 * @return           解密后的数据
	 */
	public static byte[] decrypt(Algorithm algorithms, byte[] input, byte[] key) {
		return crypt(Cipher.DECRYPT_MODE, algorithms, input, key);
	}

	private static byte[] crypt(int opmode, Algorithm algorithms, byte[] input, byte[] key) {
		if (key == null || key.length == 0) {
			return null;
		}
		
		String algorithmsStr = "";
		
		switch (algorithms) {
		case DES:
			algorithmsStr = "DES/ECB/PKCS7Padding";
			break;
		case DES3:
			algorithmsStr = "DESede/ECB/PKCS5Padding";	
			break;
		case AES:
			algorithmsStr = "AES/ECB/PKCS7Padding";
			break;
		default:
			break;
		}
		
		try {
			SecretKey deskey = new SecretKeySpec(key, algorithmsStr);
			Cipher cipher = Cipher.getInstance(algorithmsStr);
			cipher.init(opmode, deskey);
			return cipher.doFinal(input);
		} catch (Exception e) {
			DebugLog.e(TAG, "crypt()", e);
		}

		return null;
	}
	
	public static enum Algorithm {
		DES, //传入的key必须是8byte=64bit
		DES3,//传入的key必须是128bit=16byte、192bit=24byte
		AES  //传入的key必须是128bit=16byte、192bit=24byte、256bit=32byte
	}
}
