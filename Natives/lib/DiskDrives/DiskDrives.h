/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class DiskDrives */

#ifndef _Included_DiskDrives
#define _Included_DiskDrives
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     DiskDrives
 * Method:    getDriveType
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_DiskDrives_getDriveType
  (JNIEnv *, jobject, jstring);

/*
 * Class:     DiskDrives
 * Method:    getDriveVolumeLabel
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_DiskDrives_getDriveVolumeLabel
  (JNIEnv *, jobject, jstring);

/*
 * Class:     DiskDrives
 * Method:    getFileSystem
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_DiskDrives_getFileSystem
  (JNIEnv *, jobject, jstring);

/*
 * Class:     DiskDrives
 * Method:    getDiskFreeSpace
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_DiskDrives_getDiskFreeSpace
  (JNIEnv *, jobject, jstring);

/*
 * Class:     DiskDrives
 * Method:    getDiskTotalSpace
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_DiskDrives_getDiskTotalSpace
  (JNIEnv *, jobject, jstring);

/*
 * Class:     DiskDrives
 * Method:    getDiskTotalUserSpace
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_DiskDrives_getDiskTotalUserSpace
  (JNIEnv *, jobject, jstring);

/*
 * Class:     DiskDrives
 * Method:    setDriveVolumeLabel
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_DiskDrives_setDriveVolumeLabel
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     DiskDrives
 * Method:    getValidationCode
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_DiskDrives_getValidationCode
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
