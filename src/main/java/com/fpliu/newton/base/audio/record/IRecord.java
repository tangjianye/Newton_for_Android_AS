package com.fpliu.newton.base.audio.record;

import java.io.File;
import java.io.IOException;

/**
 * 录音接口
 * 
 * @author 792793182@qq.com 2015-05-22
 *
 */
public interface IRecord {

	void init(int audioSource, File desFile) throws IOException;
	
	void start();
	
	void pause();
	
	void resume();
	
	void stop();
	
	boolean isRecording();
}
