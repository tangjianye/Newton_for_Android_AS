package com.fpliu.newton.base.audio.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import android.media.AudioFormat;
import android.media.AudioTrack;

import com.fpliu.newton.base.DebugLog;

/**
 * 使用AudioTrack播放音频
 * 
 * @author 792793182@qq.com 2015-05-22
 *
 */
public final class PlayWithAudioTrack implements IPlayer {

	private static final String TAG = PlayWithAudioTrack.class.getSimpleName();
	
	/** 要播放的数据队列 */
	private ConcurrentLinkedQueue<byte[]> pcmAudioQueue = new ConcurrentLinkedQueue<byte[]>();
	
	/** 要播放的WAV文件 */
	private File audioFile;
	
	/** 播放器 */
	private AudioTrack audioTrack;
	
	/** 是否正在播放的标志 */
	private AtomicBoolean isPlaying = new AtomicBoolean(false);
	
	/** 播放线程 */
	private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	@Override
	public void init(int streamType) {
		int sampleRateInHz = 16000;
		int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
		int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
		int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
		int mode = AudioTrack.MODE_STREAM;
		
		audioTrack = new AudioTrack(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);
	}
	
	@Override
	public void setData(File audioFile) {
		this.audioFile = audioFile;
	}
	
	@Override
	public void addData(byte[] pcmAudio) {
		if (pcmAudio != null && pcmAudio.length > 0) {
			pcmAudioQueue.offer(pcmAudio);
		}
	}

	@Override
	public void start() {
		isPlaying.set(true);
		
		if (audioTrack != null) {
			audioTrack.play();
		}
		
		if (audioFile != null) {
			playFile();
		} else {
			playByte(pcmAudioQueue);
		}
	}

	@Override
	public void pause() {
		isPlaying.set(false);
		
		if (audioTrack != null) {
			audioTrack.pause();
		}
	}

	@Override
	public void resume() {
		isPlaying.set(true);
		
		if (audioTrack != null) {
			audioTrack.play();
		}
	}

	@Override
	public void stop() {
		isPlaying.set(false);
		
		if (audioTrack != null) {
			audioTrack.stop();
			audioTrack.release();
		}
	}

	@Override
	public boolean isPlaying() {
		return isPlaying.get();
	}
	
	private void playFile() {
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				FileInputStream fileInputStream = null;
				byte[] buffer = new byte[1024];
				try {
					fileInputStream = new FileInputStream(audioFile);
					while (isPlaying.get()) {
						int readSize = fileInputStream.read(buffer);
						audioTrack.write(buffer, 0, readSize);
					}
				} catch (Exception e) {
					DebugLog.d(TAG, "playFile()", e);
				} finally {
					if (fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
							DebugLog.d(TAG, "playFile()", e);
						}
					}
				}
			}
		});
	}
	
	private void playByte(final ConcurrentLinkedQueue<byte[]> audioDataQueue) {
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					while (isPlaying.get()) {
						byte[] data = audioDataQueue.poll();
						if (data != null && data.length > 0) {
							audioTrack.write(data, 0, data.length);
						}
					}
				} catch (Exception e) {
					DebugLog.d(TAG, "playByte()", e);
				}
			}
		});
	}
}