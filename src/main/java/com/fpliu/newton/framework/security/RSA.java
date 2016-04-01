package com.fpliu.newton.framework.security;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.fpliu.newton.base.DebugLog;


/**
 * RSA非对称加密和解密
 * 
 * @author 792793182@qq.com 2014-09-28
 *
 */
public final class RSA {

	private static final String TAG = RSA.class.getSimpleName();
	
	private RSA() { }
	
	/**
	 * 加载KeyStore文件
	 * @param keyStoreFile       密钥库文件
	 * @param keyStorePassword   KeyStore文件的密码
	 */
	public static KeyStore loadKeySotre(File keyStoreFile, char[] keyStorePassword) {
		InputStream is = null;
		try {
			is = new FileInputStream(keyStoreFile);
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(is, keyStorePassword);
			return keyStore;
		} catch (Exception e) {
			DebugLog.e(TAG, "loadKeySotre()", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "loadKeySotre()", e);
				}
			}
		}
		return null;
	}
	
	/**
	 * 从KeyStore文件中获取证书
	 * @param keyStoreFile       密钥库文件
	 * @param keyStorePassword   密钥库文件的密码
	 * @param alias              证书条目的别名
	 */
	public static Certificate getCertificatey(File keyStoreFile, char[] keyStorePassword, String alias) {
		KeyStore keyStore = loadKeySotre(keyStoreFile, keyStorePassword);
		if (keyStore != null) {
			try {
				return keyStore.getCertificate(alias);
			} catch (Exception e) {
				DebugLog.e(TAG, "getCertificatey()", e);
			}
		}
		return null;
	}
	
	/**
	 * 从证书中获取公钥
	 * @param certificate  证书
	 */
	public static PublicKey getPublicKey(Certificate certificate) {
		return certificate.getPublicKey();
	}

	/**
	 * 从X.509证书文件（*.cer）中获取公钥
	 * @param certificateFile  证书文件
	 */
	public static PublicKey getPublicKey(File certificateFile) {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			FileInputStream fileInputStream = new FileInputStream(certificateFile);
			Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
			return certificate.getPublicKey();
		} catch (Exception e) {
			DebugLog.e(TAG, "getPublicKey()", e);
			return null;
		}
	}
	
	/**
	 * 从KeyStore文件中获取指定条目的私钥
	 * @param keyStoreFile       密钥库文件
	 * @param keyStorePassword   密钥库文件的密码
	 * @param alias              证书条目的别名
	 * @param aliasPassword      证书条目的密码
	 */
	public static PrivateKey getPrivateKey(File keyStoreFile, char[] keyStorePassword, String alias, char[] aliasPassword) {
		KeyStore keyStore = loadKeySotre(keyStoreFile, keyStorePassword);
		try {
			return (PrivateKey) keyStore.getKey(alias, aliasPassword);
		} catch (Exception e) {
			DebugLog.e(TAG, "getPrivateKey()", e);
			return null;
		}
	}

	/**
	 * 获取公钥的长度
	 * @param publicKey 公钥
	 * @return          公钥的长度
	 */
	public static int getLength(PublicKey publicKey) {
		try {
			// 获取算法
			String algorithm = publicKey.getAlgorithm();
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			BigInteger modulus = null;

			// 如果是RSA加密
			if ("RSA".equals(algorithm)) {
				RSAPublicKeySpec keySpec = (RSAPublicKeySpec) keyFactory
						.getKeySpec(publicKey, RSAPublicKeySpec.class);
				modulus = keySpec.getModulus();
			}
			// 如果是DSA加密
			else if ("DSA".equals(algorithm)) {
				DSAPublicKeySpec keySpec = (DSAPublicKeySpec) keyFactory
						.getKeySpec(publicKey, DSAPublicKeySpec.class);
				modulus = keySpec.getP();
			}
			// 转换为二进制，获取公钥长度
			return modulus.toString(2).length();
		} catch (Exception e) {
			DebugLog.e(TAG, "getLength()", e);
			return 0;
		}
	}
	

	/**
	 * 获取私钥的长度
	 * @param privateKey 私钥
	 * @return           私钥的长度
	 */
	public static int getLength(PrivateKey privateKey) {
		try {
			// 获取算法
			String algorithm = privateKey.getAlgorithm();
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
			BigInteger modulus = null;

			// 如果是RSA加密
			if ("RSA".equals(algorithm)) {
				RSAPrivateKeySpec keySpec = (RSAPrivateKeySpec) keyFactory
						.getKeySpec(privateKey, RSAPrivateKeySpec.class);
				modulus = keySpec.getModulus();
			}
			// 如果是DSA加密
			else if ("DSA".equals(algorithm)) {
				DSAPrivateKeySpec keySpec = (DSAPrivateKeySpec) keyFactory
						.getKeySpec(privateKey, DSAPrivateKeySpec.class);
				modulus = keySpec.getP();
			}
			// 转换为二进制，获取公钥长度
			return modulus.toString(2).length();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 用公钥进行加密
	 * @param src       明文
	 * @param publicKey 公钥
	 * @return          加密后的数据
	 */
	public static byte[] encrypt(byte[] src, PublicKey publicKey) {
		if (src == null|| publicKey == null) {
			return null;
		}

		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			int totalLength = src.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (totalLength - offSet > 0) {
				if (totalLength - offSet > 117) {
					cache = cipher.doFinal(src, offSet, 117);
				} else {
					cache = cipher.doFinal(src, offSet, totalLength - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * 117;
			}

			return out.toByteArray();
		} catch (Exception e) {
			DebugLog.e(TAG, "decrypt()", e);
			return null;
		}
	}

	/**
	 * 用私钥进行解密
	 * @param src        密文
	 * @param privateKey 私钥
	 * @return           解密后的数据
	 */
	public static byte[] decrypt(byte[] src, PrivateKey privateKey) {
		if (src == null|| privateKey == null) {
			return null;
		}

		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			int totalLength = src.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (totalLength - offSet > 0) {
				if (totalLength - offSet > 128) {
					cache = cipher.doFinal(src, offSet, 128);
				} else {
					cache = cipher.doFinal(src, offSet, totalLength - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * 128;
			}

			return out.toByteArray();
		} catch (Exception e) {
			DebugLog.e(TAG, "decrypt()", e);
			return null;
		}
	}

	/**
	 * 用证书文件中的公钥加密文件
	 * @param srcFile           要加密的文件
	 * @param desFile           加密后的文件
	 * @param certificateFile   证书文件
	 * @return                  是否成功
	 */
	public static boolean encrypt(File srcFile, File desFile, File certificateFile) {
		if (srcFile == null || desFile == null || certificateFile == null) {
			return false;
		}
		
		PublicKey publicKey = getPublicKey(certificateFile);
		if (publicKey == null) {
			return false;
		}
		
		//Key的长度单位是bit
		int keyLength = getLength(publicKey);
		//能够加密的最大字节数
		int bufferLength = keyLength / 8 - 11;
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(desFile);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] cache = null;
			byte[] buffer = new byte[bufferLength];
			while (true) {
				int readCount = fis.read(buffer);
				DebugLog.d(TAG, "readCount = " + readCount);
				if (readCount == -1) {
					break;
				} else {
					cache = cipher.doFinal(buffer, 0, readCount);
					fos.write(cache);
				}
			}
			return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "encrypt()", e);
			return false;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "encrypt()", e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "encrypt()", e);
				}
			}
		}
	}
	
	/**
	 * 用KeyStore文件中的私钥解密文件
	 * @param srcFile            要解密的文件
	 * @param desFile            解密后的文件
	 * @param keyStoreFile       密钥库文件
	 * @param keyStorePassword   密钥库文件的密码
	 * @param alias              证书条目的别名
	 * @param aliasPassword      证书条目的密码
	 * @return                   是否成功
	 */
	public static boolean decrypt(File srcFile, File desFile, File keyStoreFile, char[] keyStorePassword, String alias, char[] aliasPassword) {
		if (srcFile == null || desFile == null || keyStoreFile == null) {
			return false;
		}
		
		PrivateKey privateKey = getPrivateKey(keyStoreFile, keyStorePassword, alias, aliasPassword);
		if (privateKey == null) {
			return false;
		}
		
		//Key的长度单位是bit
		int keyLength = getLength(privateKey);
		//能够加密的最大字节数
		int bufferLength = keyLength / 8;
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(desFile);
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			byte[] cache = null;
			byte[] buffer = new byte[bufferLength];
			while (true) {
				int readCount = fis.read(buffer);
				DebugLog.d(TAG, "readCount = " + readCount);
				if (readCount == -1) {
					break;
				} else {
					cache = cipher.doFinal(buffer, 0, readCount);
					fos.write(cache);
				}
			}
			return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "encrypt()", e);
			return false;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "encrypt()", e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "encrypt()", e);
				}
			}
		}
	}
	
	/**
	 * 使用私钥对数据进行签名
	 * @param data        要签名的数据
	 * @param privateKey  私钥
	 * @return            签名后，并Base64编码的数据
	 */
	public static String sign(byte[] data, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initSign(privateKey);
			signature.update(data);
			return Base64.encode(signature.sign());
		} catch (Exception e) {
			DebugLog.e(TAG, "sign()", e);
			return "";
		}
	}

	/**
	 * 使用公钥对数据的签名进行验证
	 * @param data      签名的数据
	 * @param publicKey 公钥
	 * @param sign      签名，Base64编码
	 * @return          是否验证通过
	 */
	public static boolean verify(byte[] data, PublicKey publicKey, String sign) {
		try {
			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initVerify(publicKey);
			signature.update(data);
			return signature.verify(Base64.decode(sign.getBytes()));
		} catch (Exception e) {
			DebugLog.e(TAG, "verify()", e);
			return false;
		}
	}
	
	/**
	 * Android特有的
	 * @param context
	 * @return
	 */
	public static android.content.pm.Signature getAPKSignature(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			android.content.pm.Signature[] signatures = packageInfo.signatures;
			return signatures[0];
		} catch (Exception e) {
			DebugLog.e(TAG, "verify()", e);
			return null;
		}
	}
}
