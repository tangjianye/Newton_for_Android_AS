package com.fpliu.newton.framework.qr.encoding;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public final class EncodingHandler {

	private static final int WHITE = 0xffffffff;

	private static final int BLACK = 0xff000000;

	private EncodingHandler() {
	}

	/**
	 * 生成二维码
	 * 
	 * @param source
	 *            二维码中的文本信息
	 * @param widthAndHeight
	 *            二维码的高度和宽度，宽度和高度相等，单位px
	 * @return 位图
	 * @throws WriterException
	 */
	public static Bitmap generateQRCode(String source, int widthAndHeight) throws WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix matrix = new MultiFormatWriter().encode(source,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 生成带小图标的二维码
	 * 
	 * @param icon
	 *            二维码中的小图标
	 * @param source
	 *            二维码中的文本信息
	 * @param widthAndHeight
	 *            二维码的高度和宽度，宽度和高度相等，单位px
	 * @return 位图
	 * @throws WriterException
	 */
	private static final int IMAGE_HALFWIDTH = 100;
	public static Bitmap generateQRCode(Bitmap icon, String source, int widthAndHeight) throws WriterException {
		icon = zoomBitmap(icon, IMAGE_HALFWIDTH);
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		
		BitMatrix matrix = new MultiFormatWriter().encode(source,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
						&& y > halfH - IMAGE_HALFWIDTH
						&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = icon.getPixel(x - halfW
							+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = BLACK;
					} else {
						pixels[y * width + x] = WHITE;
					}
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}

	/**
	 * 缩放图片
	 * 
	 * @param icon
	 * @param widthAndHeight
	 * @return
	 */
	private static Bitmap zoomBitmap(Bitmap icon, int widthAndHeight) {
		Matrix matrix = new Matrix();
		float sx = (float) 2 * widthAndHeight / icon.getWidth();
		float sy = (float) 2 * widthAndHeight / icon.getHeight();
		matrix.setScale(sx, sy);
		return Bitmap.createBitmap(icon, 0, 0, icon.getWidth(),
				icon.getHeight(), matrix, false);
	}
}
