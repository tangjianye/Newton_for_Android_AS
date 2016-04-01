#include<stdlib.h>
#include"newton.h"

char *algorithm(int array[], int length) {
	/** 在堆中分配了一段内存，注意用完后释放堆内存 */
	char* result = (char*)calloc(length + 1, sizeof(char));
	int i;
	for(i = 0; i < length; i++) {
		result[i] = array[i] - i + 1;
	}
	return result;
}

JNIEXPORT jstring JNICALL func(JNIEnv * env, jobject obj, int array[], int length) {
	jstring result = NULL;
	char* str = algorithm(array, length);
	if (NULL != str) {
		result = env->NewStringUTF(str);

		/** 用完后释放内存 */
		free(str);
	}
	return result;
}

JNIEXPORT jstring JNICALL Java_com_fpliu_newton_MyApp_a
  (JNIEnv * env, jobject obj) {
    int a[16] = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
    return func(env, obj, a, 16);
}

JNIEXPORT jstring JNICALL Java_com_fpliu_newton_MyApp_b
  (JNIEnv * env, jobject obj) {
	int a[16] = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
	return func(env, obj, a, 16);
}

JNIEXPORT jstring JNICALL Java_com_fpliu_newton_MyApp_c
  (JNIEnv * env, jobject obj) {
	int a[16] = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
	return func(env, obj, a, 16);
}

JNIEXPORT jstring JNICALL Java_com_fpliu_newton_MyApp_d
  (JNIEnv * env, jobject obj) {
	int a[16] = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
	return func(env, obj, a, 16);
}

JNIEXPORT jstring JNICALL Java_com_fpliu_newton_MyApp_e
  (JNIEnv * env, jobject obj) {
	int a[16] = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
	return func(env, obj, a, 16);
}

JNIEXPORT jstring JNICALL Java_com_fpliu_newton_MyApp_f
  (JNIEnv * env, jobject obj) {
	int a[16] = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
	return func(env, obj, a, 16);
}

JNIEXPORT jstring JNICALL Java_com_fpliu_newton_MyApp_g
  (JNIEnv * env, jobject obj) {
	int a[16] = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
	return func(env, obj, a, 16);
}

JNIEXPORT jstring JNICALL Java_com_fpliu_newton_MyApp_h
  (JNIEnv * env, jobject obj) {
	int a[16] = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
	return func(env, obj, a, 16);
}
