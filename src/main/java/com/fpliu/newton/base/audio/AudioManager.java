package com.fpliu.newton.base.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.util.ByteUtil;
import com.fpliu.newton.framework.util.IOUtil;

/**
 * 音频处理
 * 
 * @author 792793182@qq.com 2015-05-22
 *
 */
public final class AudioManager {

	private static final String TAG = AudioManager.class.getSimpleName();

	private static final class InstanceHolder {
		private static AudioManager instance = new AudioManager();
	}

	public static AudioManager getInstance() {
		return InstanceHolder.instance;
	}

	/**
	 * 16位的WAV文件头，插入这些信息就可以得到可以播放的文件。
	 * 
	 * @param totalAudioLen  音频长度
	 * @param totalDataLen   总数据长度
	 * @param sampleRate     采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	 * @param sampleBit      采样大小，Android支持16bit和8bit
	 * @param channels       通道数
	 * @return
	 */
	public byte[] getWAVHeader(int totalAudioLen, int totalDataLen,
			int sampleRate, int sampleBit, int channels) {
		
		long byteRate = getByteRate(sampleRate, sampleBit, channels);
		
		byte[] header = new byte[44];
		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (sampleRate & 0xff);
		header[25] = (byte) ((sampleRate >> 8) & 0xff);
		header[26] = (byte) ((sampleRate >> 16) & 0xff);
		header[27] = (byte) ((sampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (16 / 8);// (2 * 16 / 8); // block align
		header[33] = 0;
		header[34] = (byte) sampleBit; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

		return header;
	}

	/**
	 * 将PCM文件转换成WAV文件
	 * @param pcmFile      PCM文件
	 * @param wavFile      WAV文件
	 * @param sampleRate   采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	 * @param sampleBit    采样大小，Android支持16bit和8bit
	 * @param channels     通道数量
	 */
	public void convertPCM2WAV(File pcmFile, File wavFile, int sampleRate, int sampleBit, int channels) {
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			in = new FileInputStream(pcmFile);
			out = new FileOutputStream(wavFile);

			int totalAudioLen = (int) pcmFile.length();
			int totalDataLen = totalAudioLen + 44;
			
			// 写入头
			out.write(getWAVHeader(totalAudioLen, totalDataLen, sampleRate, sampleBit, channels));
			
			int readCount = 0;
			byte[] buffer = new byte[1024];
			// 写入PCM数据
			while ((readCount = in.read(buffer)) != -1) {
				out.write(buffer, 0, readCount);
			}
		} catch (Exception e) {
			DebugLog.e(TAG, "convertPCM2WAV()", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "convertPCM2WAV()", e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "convertPCM2WAV()", e);
				}
			}
		}
	}
	
	/**
	 * 获取数据速率，但是为KB/S
	 * @param sampleRate   采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	 * @param sampleBit    采样大小，Android支持16bit和8bit
	 * @param channels     通道数量
	 */
	public int getByteRate(int sampleRate, int sampleBit, int channels) {
		return sampleRate * sampleBit * channels / 8;
	}
	
	/**
	 * 获取WAV文件的播放时长，单位为S
	 * @param totalAudioLen   音频数据长度，单位是KB
	 * @param byteRate        速率，单位是KB/S，可以通过getByteRate方法获取
	 */
	public int getWavDuration(int totalAudioLen, int byteRate) {
		return totalAudioLen / byteRate;
	}
	
	/**
	 * 获取WAV文件的播放时长，单位为S
	 * @param wavFile   WAV文件
	 */
	public int getWavDuration(File wavFile) {
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(wavFile, "r");
			byte[] head = IOUtil.read(randomAccessFile, 0, 44);

			byte[] sampleRateBytes = { head[24], head[25], head[26], head[27] };
			byte[] sampleBitBytes = { head[34], 0, 0, 0 };
			byte[] channelsBytes = { head[22], 0, 0, 0 };
			byte[] totalAudioLenBytes = { head[40], head[41], head[42], head[43] };
			
			int sampleRate = ByteUtil.toInt(sampleRateBytes);
			int sampleBit = ByteUtil.toInt(sampleBitBytes);
			int channels = ByteUtil.toInt(channelsBytes);
			int byteRate = getByteRate(sampleRate, sampleBit, channels);
			int totalAudioLen = ByteUtil.toInt(totalAudioLenBytes);
			
			DebugLog.d(TAG, "sampleRate = " + sampleRate);
			DebugLog.d(TAG, "sampleBit = " + sampleBit);
			DebugLog.d(TAG, "channels = " + channels);
			DebugLog.d(TAG, "byteRate = " + byteRate);
			DebugLog.d(TAG, "totalAudioLen = " + totalAudioLen);
			
			return getWavDuration(totalAudioLen, byteRate);
		} catch (Exception e) {
			DebugLog.e(TAG, "getWavDuration()", e);
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "getWavDuration()", e);
				}
			}
		}
		return 0;
	}

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");
	
	/**
	 * 获取格式化的播放时长，格式为hh:mm:ss
	 * @param wavFile   WAV文件
	 * @return          格式化的播放时长
	 */
	public StringBuilder getWavDurationStr(File wavFile) {
		return getWavDurationStr(getWavDuration(wavFile));
	}
	
	public StringBuilder getWavDurationStr(int duration) {
		int hour = duration / (60 * 60);
		int minites = (duration % (60 * 60)) / 60;
		int seconds = duration - hour * 60 * 60 - minites * 60;
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(DECIMAL_FORMAT.format(hour)).append(':');
		stringBuilder.append(DECIMAL_FORMAT.format(minites)).append(':');
		stringBuilder.append(DECIMAL_FORMAT.format(seconds));
		return stringBuilder;
	}
}
