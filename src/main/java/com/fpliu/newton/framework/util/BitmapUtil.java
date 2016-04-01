package com.fpliu.newton.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Build;

import com.fpliu.newton.base.DebugLog;

/**
 * 
 * 图片处理工具类
 * 
 * @author 792793182@qq.com 2014-9-28 
 *
 */
public final class BitmapUtil {

	private static final String TAG = BitmapUtil.class.getSimpleName();
	
	private BitmapUtil() { }
	
	/**
	 * Exif（Exchangeable Image File，可交换图像文件）是一种图像文件格式，它的数据存储与JPEG格式是完全相同的。
	 * 实际上，Exif格式就是在JPEG格式头部插入了数码照片的信息，包括拍摄时的光圈、快门、白平衡、ISO、焦距、日期时间等
	 * 各种和拍摄条件以及相机品牌、型号、色彩编码、拍摄时录制的声音以及全球定位系统（GPS）、缩略图等。
	 * 简单地说，Exif=JPEG+拍摄参数。因此，你可以利用任何可以查看JPEG文件的看图软件浏览Exif格式的照片，
	 * 但并不是所有的图形程序都能处理Exif信息。
	 * @param imageFilePath 图片路径
	 * @return
	 * @throws IOException
	 */
	public static ExifInterface getExifInterface(String imageFilePath) throws IOException {
		return new ExifInterface(imageFilePath);
	}

	/**
	 * 获取图片的宽度和高度
	 * @param imageFilePath 图片的路径
	 * @return  (width, height)，分别为宽度和高度
	 */
	public static Point getWidthAndHeight(String imageFilePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 不去真的解析图片，只获取图片头部信息
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(imageFilePath, options);
		
		int width = options.outWidth;
		int height = options.outHeight;
		
		return new Point(width, height);
	}

	public static int getBitmapSize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
			return bitmap.getAllocationByteCount();
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API 12
			return bitmap.getByteCount();
		}
		
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
	
	/**
	 * 获取图片的宽度和高度
	 * @param is 图片的文件流
	 * @return   (width, height)，分别为宽度和高度
	 */
	public static Point getWidthAndHeight(InputStream is) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 不去真的解析图片，只获取图片头部信息
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(is, null, options);
		
		int width = options.outWidth;
		int height = options.outHeight;

		return new Point(width, height);
	}
	
	
	
	/**
	 * 图片缩放
	 * @param bitmap    目标图片
	 * @param desWidth  目标宽度
	 * @param desHeight 目标高度
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int desWidth, int desHeight) {
		return zoomBitmap(bitmap, (float) desWidth / bitmap.getWidth(), 
				                  (float) desHeight / bitmap.getHeight());
	}
	
	/**
	 * 图片缩放
	 * @param bitmap    目标图片
	 * @param desWidth  目标宽度
	 * @param desHeight 目标高度
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, float sx, float sy) {
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				                                          bitmap.getHeight(), matrix, true);
	}
	
	public static byte[] compress(Bitmap bitmap, Bitmap.CompressFormat format) {
		return compress(bitmap, 75, format);
	}
	
	public static byte[] compress(Bitmap bitmap, int quality, Bitmap.CompressFormat format) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(format, quality, baos);
		return baos.toByteArray();
	}
	
	public static boolean saveBitmapToFile(Bitmap bitmap, String filePath, Bitmap.CompressFormat format) {
		return saveBitmapToFile(bitmap, filePath, 75, format);
	}
	
	/**
	 * 保存Bitmap到文件
	 * @param bitmap    位图
	 * @param fileName  文件名，路径自己选择
	 * @return          完整的路径
	 */
	public static boolean saveBitmapToFile(Bitmap bitmap, String filePath, int quality, Bitmap.CompressFormat format) {
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(compress(bitmap, quality, format));
			return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "saveBitmapToFile()", e);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "saveBitmapToFile()", e);
				}
			}
		}
	}
	
	public static Bitmap toBitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
}
