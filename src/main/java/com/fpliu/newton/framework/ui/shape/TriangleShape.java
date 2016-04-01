package com.fpliu.newton.framework.ui.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

/**
 * 三角形图形
 * 
 * @author 792793182@qq.com 2014-10-13
 *
 */
public class TriangleShape extends Shape {

	private Path path;
	private float degrees;
	
	public TriangleShape(int degrees) {
		path = new Path();
		this.degrees = degrees;
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		//先重置到原始状态
		path.reset();
		
		float width = getWidth();
		float height = getHeight();
		
		path.moveTo(width / 2, 0);
		path.lineTo(0, height);
		path.lineTo(width, height);
		path.lineTo(width / 2, 0);
		
		canvas.drawPath(path, paint);
		
		canvas.rotate(degrees, width / 2, height/ 2);
	}
	
}
