package com.fpliu.newton.framework.ui.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

/**
 * 房子图形
 * 
 * @author 792793182@qq.com 2014-10-13
 *
 */
public class HomeShape extends Shape {

	private Path path;
	
	//箭身和箭头的高度比例
	private float ratio;
	
	public HomeShape(float ratio) {
		path = new Path();
		this.ratio = ratio;
	}
	
	@Override
	public void draw(Canvas canvas, Paint paint) {
		path.reset();
		
		float width = getWidth();
		float height = getHeight();
		
		float h2 = height / (ratio + 1);
		
		//先画三角形
		path.moveTo(width / 2, 0);
		path.lineTo(0, h2);
		path.lineTo(width, h2);
		path.lineTo(width / 2, 0);
		
		canvas.drawPath(path, paint);
		
		//再画矩形
		canvas.drawRect(0, h2, width, height, paint);
	}

}
