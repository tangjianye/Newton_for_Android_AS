package com.fpliu.newton.base.audio.record;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import android.media.AudioFormat;
import android.media.AudioRecord;

import com.fpliu.newton.base.DebugLog;

/**
 * 使用AudioRecord进行录音
 * 
 * @author 792793182@qq.com 2015-05-22
 *
 */
public final class RecordWithAudioRecord implements IRecord {

	private static final String TAG = RecordWithAudioRecord.class.getSimpleName();
	
	private AudioRecord audioRecord;
	
	private int bufferSizeInBytes;
	
	private AtomicBoolean isRecording;
	
	private DataOutputStream dos;
	
	private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	
	/**
	 * 
	 * @param audioSource @see MediaRecorder.AudioSource 音频来源
	 */
	@Override
	public void init(int audioSource, File desFile) throws IOException {
		File dir = desFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		isRecording = new AtomicBoolean(false);
		
		int sampleRateInHz = 16000;
		int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
		bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
		audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
		
		dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(desFile)));
	}

	@Override
	public void start() {
		DebugLog.d(TAG, "start()");
		
		isRecording.set(true);
		
		audioRecord.startRecording();
		
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				byte[] audioData = new byte[bufferSizeInBytes];
				try {
					while (isRecording()) {
						int readsize = audioRecord.read(audioData, 0, bufferSizeInBytes);
						if (AudioRecord.ERROR_INVALID_OPERATION != readsize && dos != null) {
							dos.write(audioData, 0, readsize);
						}
					}
				} catch (Exception e) {
					DebugLog.e(TAG, "start()", e);
				} finally {
					if (dos != null) {
						try {
							dos.close();
						} catch (IOException e) {
							DebugLog.e(TAG, "start()", e);
						}
					}
				}
			}
		});
	}
	
	@Override
	public void pause() {
		DebugLog.d(TAG, "pause()");
		
		audioRecord.stop();
		isRecording.set(false);
	}
	
	@Override
	public void resume() {
		DebugLog.d(TAG, "resume()");
		
		start();
	}
	
	@Override
	public void stop() {
		DebugLog.d(TAG, "stop()");
		
		try {
			audioRecord.stop();
		} catch (Exception e) {
			DebugLog.e(TAG, "stop()", e);
		}
		
		audioRecord.release();
		isRecording.set(false);
	}
	
	@Override
	public boolean isRecording() {
		return isRecording.get();
	}
}
