package com.fpliu.newton.base.image;

/**
 * 图像处理
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class ImageManager {

	private static final class InstanceHolder {
		private static ImageManager instance = new ImageManager();
	}
	
	public static ImageManager getInstance() {
		return InstanceHolder.instance;
	}
}
