package com.fpliu.newton.framework.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;

import android.content.Context;
import android.text.TextUtils;

import com.fpliu.newton.base.DebugLog;

/**
 * IO工具类
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class IOUtil {
	
	private static final String TAG = IOUtil.class.getSimpleName();
		
	private IOUtil() { }

	/* 将输入流转换成字节数组 */
	public static byte[] inputStream2bytes(InputStream is) throws IOException {
		if (is == null) {
			return null;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] buff = new byte[1024];
			int len = 0;
			while (-1 != (len = is.read(buff))) {
				baos.write(buff, 0, len);
			}
			baos.flush();
			return baos.toByteArray();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				DebugLog.e(TAG, "inputStream2bytes()", e);
			}
			
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "inputStream2bytes()", e);
				}
			}
		}
	}
	
	public static byte[] file2bytes(File file) throws FileNotFoundException, IOException {
		if (file == null) {
			return null;
		}
		
		return inputStream2bytes(new FileInputStream(file));
	}
	
	public static void readTextFileByLine(Reader reader, ReadLineCallback callback) throws IOException {
		if (reader == null) {
			return;
		}
		
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(reader);
			String lineStr = null;
			while ((lineStr = bReader.readLine()) != null) {
				if (callback != null) {
					callback.readLine(lineStr);
				}
			}
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "readTextFileByLine()", e);
				}
			}
		}
	}
	
	public static void readTextFileByLine(File file, ReadLineCallback callback) throws FileNotFoundException, IOException {
		if (file == null) {
			return;
		}
		
		readTextFileByLine(new FileReader(file), callback);
	}
	
	public static void readTextFileByLine(InputStream is, ReadLineCallback callback) throws IOException {
		if (is == null) {
			return;
		}
		
		readTextFileByLine(new InputStreamReader(is), callback);
	}
	
	public static String readTextFile(Reader reader) {
		if (reader == null) {
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(reader);
			String lineStr = null;
			while ((lineStr = bReader.readLine()) != null) {
				stringBuilder.append(lineStr).append('\n');
			}
		} catch(IOException e) {
			DebugLog.e(TAG, "readTextFile()", e);
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "writeFile()", e);
				}
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * 读取文本文件
	 * @param file 文本文件
	 * @return     文件内容
	 * @throws FileNotFoundException 
	 */
	public static String readTextFile(File file) {
		if (file == null) {
			return null;
		}
		
		try {
			return readTextFile(new FileReader(file));
		} catch (FileNotFoundException e) {
			DebugLog.e(TAG, "readTextFile()", e);
			return "";
		}
	}
	
	/**
	 * 读取文本文件
	 * @param is   文件流
	 * @return     文件内容
	 * @throws IOException 
	 */
	public static String readTextFile(InputStream is) {
		if (is == null) {
			return null;
		}
		
		return readTextFile(new InputStreamReader(is));
	}
	
	/**
	 * 读取Assets目录下的文本文件
	 * @param context　   上下文
	 * @param fileName   文件流
	 * @return     　　　　　　文件内容
	 * @throws IOException 
	 */
	public static String readAssetsText(Context context, String fileName) throws IOException {
		return readTextFile(context.getAssets().open(fileName));
	}
	
	/**
	 * 读取二进制文件
	 * @param file 二进制文件
	 * @return     文件内容
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static byte[] readByteFile(File file) throws FileNotFoundException, IOException {
		if (file == null) {
			return null;
		}
		
		return inputStream2bytes(new FileInputStream(file));
	}
	
	/**
	 * 写文本文件
	 * @param desFile 目的文件
	 * @param content 文件内容
	 * @throws IOException 
	 */
	public static boolean writeFile(File desFile, String content, boolean append) throws IOException {
		if (desFile == null) {
			return false;
		}
		
		BufferedWriter bWriter = null;
		try {
			bWriter = new BufferedWriter(new FileWriter(desFile, append));
			bWriter.write(content);
			
			return true;
		} finally {
			if (bWriter != null) {
				try {
					bWriter.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "writeFile()", e);
				}
			}
		}
	}
	
	/**
	 * 写二进制文件
	 * @param desFile 目的文件
	 * @param content 文件内容
	 * @throws IOException 
	 */
	public static boolean writeFile(File desFile, byte[] content, boolean append) throws IOException {
		if (desFile == null) {
			return false;
		}
		
		OutputStream os = null;
		try {
			os = new FileOutputStream(desFile, append);
			os.write(content);
			
			return true;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "writeFile()", e);
				}
			}
		}
	}
	
	/**
	 * 写文件
	 * @param is       输入流
	 * @param desFile  目标文件
	 * @throws IOException 
	 */
	public static void writeFile(InputStream is, File desFile) throws IOException {
		if (is == null || desFile == null) {
			return;
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(desFile);
			byte[] buff = new byte[1024];
			int len = 0;
			while (-1 != (len = is.read(buff))) {
				fos.write(buff, 0, len);
			}
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "writeFile()", e);
				}
			}
			
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "writeFile()", e);
				}
			}
		}
	}
	
	/**
	 * 获取文件的大小
	 * @param filePath    文件路径
	 * @return            文件大小，单位：Byte
	 */
	public static long getFileSize(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return 0L;
		}
		return getFileSize(new File(filePath));
	}
	
	/**
	 * 获取文件的大小
	 * @param file    文件
	 * @return        文件大小，单位：Byte
	 */
	public static long getFileSize(File file) {
		if (file == null) {
			return 0L;
		}
		
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				return fis.available();
			} catch (Exception e) {
				DebugLog.e(TAG, "getFileSizes()", e);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						DebugLog.e(TAG, "getFileSizes()", e);
					}
				}
			}
		}
		
		return 0L;
	}
	
	/**
	 * 复制文件
	 * @param inputStream  源文件流
	 * @param desFile      目的文件
	 */
	public static boolean copy(InputStream inputStream, File desFile) {
		if (inputStream == null || desFile == null) {
			return false;
		}
		
		FileOutputStream fileOutputStream = null;
		try {
			desFile.createNewFile();
			fileOutputStream = new FileOutputStream(desFile);
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = inputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, count);
			}
			return true;
		} catch (IOException e) {
			DebugLog.e(TAG, "copy()", e);
			return false;
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "copy()", e);
				}
			}
			
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "copy()", e);
				}
			}
		}
	}
	
	/**
	 * 复制文件
	 * @param srcFile  源文件
	 * @param desFile  目的文件
	 */
	public static boolean copy(File srcFile, File desFile) {
		try {
			return copy(new FileInputStream(srcFile), desFile);
		} catch (FileNotFoundException e) {
			DebugLog.e(TAG, "copy()", e);
			return false;
		}
	}
	
	/**
	 * 随机读取文件的一段数据
	 * @param randomAccessFile
	 * @param offset
	 * @param length
	 * @return
	 */
	public static byte[] read(RandomAccessFile randomAccessFile, int offset, int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			try {
				result[i] = randomAccessFile.readByte();
			} catch (IOException e) {
				DebugLog.e(TAG, "read()", e);
			}
		}
		return result;
	}
	
	/**
	 * 读取一行的回调
	 *
	 */
	public static interface ReadLineCallback {
		void readLine(String lineStr);
	}
}