package com.fpliu.newton.base.audio.player;

import java.io.File;

/**
 * 播放音频接口
 * 
 * @author 792793182@qq.com 2015-05-22
 *
 */
public interface IPlayer {

	/**
	 * 初始化
	 * @param streamType  参看AudioManager.STREAM_XX
	 */
	void init(int streamType);
	
	/**
	 * 设置数据源
	 * @param audioFile 只能是WAV文件
	 */
	void setData(File audioFile);
	
	/**
	 * 设置数据源
	 * @param pcmAudio 只能是PCM音频数据
	 */
	void addData(byte[] pcmAudio);
	
	/**
	 * 开始播放
	 */
	void start();
	
	/**
	 * 暂停播放
	 */
	void pause();
	
	/**
	 * 恢复播放
	 */
	void resume();
	
	/**
	 * 停止播放
	 */
	void stop();
	
	/**
	 * 是否正在播放
	 */
	boolean isPlaying();
}
