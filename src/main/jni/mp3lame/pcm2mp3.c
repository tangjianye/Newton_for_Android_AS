#include "lame-3.98.4_libmp3lame/lame.h"
#include "pcm2mp3.h"

#define BUFFER_SIZE 4096

lame_t lame;

JNIEXPORT jboolean JNICALL Java_com.fpliu.newton_base_audio_PCM2MP3_init(
		JNIEnv * env, jobject obj, jint pcmSampleRate, jint pcmSampleBit,
		jint pcmChannels, jint quality) {
	lame = lame_init();

	//设置通道数量
	lame_set_num_channels(lame, pcmChannels);

	//设置采样率
	lame_set_in_samplerate(lame, pcmSampleRate);

	//计算速率
	int byteRate = pcmSampleRate * pcmSampleBit * pcmChannels / 8;

	//设置速率
	lame_set_brate(lame, byteRate);

	lame_set_mode(lame, 1);

	//设置MP3质量
	lame_set_quality(lame, quality);

	lame_init_params(lame);

	return JNI_TRUE;
}

JNIEXPORT jbyteArray JNICALL Java_com.fpliu.newton_base_audio_PCM2MP3_encode(
		JNIEnv * env, jobject obj, jshortArray buffer, jint length) {

	int nb_write = 0;

	char output[BUFFER_SIZE];

	// 转换为本地数组
	jshort *input = (*env)->GetShortArrayElements(env, buffer, NULL);

	// 压缩mp3
	nb_write = lame_encode_buffer(lame, input, input, length, output,
			BUFFER_SIZE);

	// 局部引用，创建一个byte数组
	jbyteArray result = (*env)->NewByteArray(env, nb_write);

	// 给byte数组设置值
	(*env)->SetByteArrayRegion(env, result, 0, nb_write, (jbyte *) output);

	// 释放本地数组(避免内存泄露)
	(*env)->ReleaseShortArrayElements(env, buffer, input, 0);

	return result;
}

JNIEXPORT jboolean JNICALL Java_com.fpliu.newton_base_audio_PCM2MP3_destroy(
		JNIEnv * env, jobject obj) {
	lame_close(lame);
	return JNI_TRUE;
}

char* jstringTostring(JNIEnv* env, jstring jstr) {
	char* rtn = NULL;
	jclass clsstring = (*env)->FindClass(env, "java/lang/String");
	jstring strencode = (*env)->NewStringUTF(env, "utf-8");
	jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
	jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid, strencode);
	jsize alen = (*env)->GetArrayLength(env, barr);
	jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	(*env)->ReleaseByteArrayElements(env, barr, ba, 0);
	return rtn;
}

JNIEXPORT jboolean JNICALL Java_com.fpliu.newton_base_audio_PCM2MP3_convert
  (JNIEnv * env, jclass clazz, jstring pcmFilePath, jstring mp3FilePath) {
	int read, write;

	FILE *pcm = fopen(jstringTostring(env, pcmFilePath), "rb");
	FILE *mp3 = fopen(jstringTostring(env, mp3FilePath), "wb");

	const int PCM_SIZE = 8192;
	const int MP3_SIZE = 8192;

	short int pcm_buffer[PCM_SIZE * 2];
	unsigned char mp3_buffer[MP3_SIZE];

	lame_t lame = lame_init();

	//设置通道数量
	lame_set_num_channels(lame, 1);

	//设置采样率
	lame_set_in_samplerate(lame, 8000);

	lame_set_out_samplerate(lame, 8000);

	//设置速率
	lame_set_brate(lame, 32);

	lame_set_mode(lame, 1);

	//设置MP3质量
	lame_set_quality(lame, 7);

	lame_set_VBR(lame, vbr_default);

	lame_init_params(lame);

	do {
		read = fread(pcm_buffer, 2 * sizeof(short int), PCM_SIZE, pcm);
		if (read == 0) {
			write = lame_encode_flush(lame, mp3_buffer, MP3_SIZE);
		}
		else {
			write = lame_encode_buffer_interleaved(lame, pcm_buffer, read,
								mp3_buffer, MP3_SIZE);
		}
		fwrite(mp3_buffer, write, 1, mp3);
	} while (read != 0);

	lame_close(lame);
	fclose(mp3);
	fclose(pcm);

	return JNI_TRUE;
}
