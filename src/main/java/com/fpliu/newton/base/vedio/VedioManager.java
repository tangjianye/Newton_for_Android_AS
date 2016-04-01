package com.fpliu.newton.base.vedio;

/**
 * 视频处理
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class VedioManager {

	private static final class InstanceHolder {
		private static VedioManager instance = new VedioManager();
	}
	
	public static VedioManager getInstance() {
		return InstanceHolder.instance;
	}
}
