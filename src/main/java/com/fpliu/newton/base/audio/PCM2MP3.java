package com.fpliu.newton.base.audio;

/**
 * PCM数据转换为MP3工具类
 * 
 * @author 792793182@qq.com 2015-06-02
 *
 */
public final class PCM2MP3 {

	static {
		System.loadLibrary("pcm2mp3");
	}
	
	private PCM2MP3() { }
	
	/**
	 * 初始化
	 * @param pcmSampleRate   采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	 * @param pcmSampleBit    采样大小，Android支持16bit和8bit
	 * @param pcmChannels     通道数量
	 * @param quality         取值范围[0-9]，0=很好很慢 9=很差很快
	 * @return                初始化成功后者失败
	 */
	public native boolean init(int pcmSampleRate, int pcmSampleBit, int pcmChannels, int quality);
	
	/**
	 * 编码
	 * @param buffer  要编码的数据
	 * @param len     buffer中截取的长度
	 * @return        编码后的数据
	 */
	public native byte[] encode(short[] buffer, int len);
	
	/**
	 * 释放资源
	 * @return  释放资源成功后者失败
	 */
	public native boolean destroy();
	
	/**
	 * 将PCM格式的文件转换成MP3格式的文件
	 * 
	 * @param pcmFilePath  PCM格式的文件路径
	 * @param mp3FilePath  MP3格式的文件路径
	 * @return             是否转换成功
	 */
	public static native boolean convert(String pcmFilePath, String mp3FilePath);
}
