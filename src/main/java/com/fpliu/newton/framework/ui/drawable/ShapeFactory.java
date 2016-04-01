package com.fpliu.newton.framework.ui.drawable;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import com.fpliu.newton.MyApp;

/**
 * 形状
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class ShapeFactory {

	private ShapeFactory() { }
	
	public static Drawable getRoundRectShapeDrawable(int color) {
		// 背景设置为圆角矩形
		float r = 10;
		float[] outerR = new float[] { r, r, r, r, r, r, r, r };
		RoundRectShape rr = new RoundRectShape(outerR, null, null);
		ShapeDrawable drawable = new ShapeDrawable(rr);
		drawable.getPaint().setColor(color);
		return drawable;
	}
	
	/**
	 * 图片id转换为圆角
	 * @param drawbleId
	 * @param pixels
	 * @return
	 */
	public static Bitmap idToRoundCorner(int drawbleId, int pixels){
		Bitmap bitmap = BitmapFactory.decodeResource (MyApp.getApp().getResources(),drawbleId);
		Bitmap bitmap2 =ShapeFactory.toRoundCorner(bitmap, pixels);
		return bitmap2;
	}
	
	/**
	 * 图片转换圆角
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) { 
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888); 
		Canvas canvas = new Canvas(output); 
		final int color = 0xff424242; 
		final Paint paint = new Paint(); 
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
		final RectF rectF = new RectF(rect); 
		final float roundPx = pixels; 
		paint.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		canvas.drawBitmap(bitmap, rect, rect, paint); 
		return output; 
	} 
}
