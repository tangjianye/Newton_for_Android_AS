package com.fpliu.newton.base.audio.record;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.media.MediaRecorder;

/**
 * 使用MediaRecord进行录音
 * 
 * @author 792793182@qq.com 2015-05-22
 *
 */
public class RecordWithMediaRecorder implements IRecord {

	private MediaRecorder mediaRecorder;
	
	private AtomicBoolean isRecording = new AtomicBoolean(false);
	
	@Override
	public synchronized void init(int audioSource, File desFile) throws IOException {
		isRecording.set(false);
		
		File dir = desFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		mediaRecorder = new MediaRecorder();
	    mediaRecorder.setAudioSource(audioSource);
	    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	    mediaRecorder.setOutputFile(desFile.getAbsolutePath());
	    mediaRecorder.prepare();
	}
	
	@Override
	public synchronized void start() {
	    mediaRecorder.start();
	    isRecording.set(true);
	}
	
	@Override
	public synchronized void stop() {
		if (mediaRecorder != null) {
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}
		
		isRecording.set(false);
	}

	@Override
	public synchronized void pause() {
		if (mediaRecorder != null) {
			mediaRecorder.stop();
		}
		
		isRecording.set(false);
	}

	@Override
	public synchronized void resume() {
		mediaRecorder.start();
		isRecording.set(true);
	}
	
	@Override
	public boolean isRecording() {
		return isRecording.get();
	}
	
}
