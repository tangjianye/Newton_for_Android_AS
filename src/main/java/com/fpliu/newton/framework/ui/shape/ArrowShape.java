package com.fpliu.newton.framework.ui.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

/**
 * 箭头图形
 * 
 * @author 792793182@qq.com 2014-10-13
 *
 */
public class ArrowShape extends Shape {

	private Path path;
	
	//整个宽度与箭身宽度的比例
	private float widthRatio;
	
	//箭身和箭头的高度比例
	private float heightRatio;
	
	private float degree;
	
	public ArrowShape(float widthRatio, float heightRatio) {
		this(widthRatio, heightRatio, 0);
	}
	
	public ArrowShape(float widthRatio, float heightRatio, float degree) {
		path = new Path();
		this.widthRatio = widthRatio;
		this.heightRatio = heightRatio;
		this.degree = degree;
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		path.reset();
		
		float width = getWidth();
		float height = getHeight();
		
		float h2 = height / (heightRatio + 1);
		
		//先画三角形
		path.moveTo(width / 2, 0);
		path.lineTo(0, h2);
		path.lineTo(width, h2);
		path.lineTo(width / 2, 0);
		
		//再画矩形
		float w2 = width / widthRatio / 2;
		path.moveTo(width / 2 - w2, h2);
		path.lineTo(width / 2 - w2, height);
		path.lineTo(width / 2 + w2, height);
		path.lineTo(width / 2 + w2, h2);
	
		if (degree != 0) {
			canvas.rotate(degree, width / 2, height /2);
		}
		
		canvas.drawPath(path, paint);
	}

}
