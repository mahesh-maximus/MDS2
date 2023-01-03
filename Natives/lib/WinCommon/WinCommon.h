/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class WinCommon */

#ifndef _Included_WinCommon
#define _Included_WinCommon
#ifdef __cplusplus
extern "C" {
#endif
#undef WinCommon_SW_HIDE
#define WinCommon_SW_HIDE 0L
#undef WinCommon_SW_MAXIMIZE
#define WinCommon_SW_MAXIMIZE 1L
#undef WinCommon_SW_MINIMIZE
#define WinCommon_SW_MINIMIZE 2L
#undef WinCommon_SW_RESTORE
#define WinCommon_SW_RESTORE 3L
#undef WinCommon_SW_SHOW
#define WinCommon_SW_SHOW 4L
#undef WinCommon_SW_SHOWDEFAULT
#define WinCommon_SW_SHOWDEFAULT 5L
#undef WinCommon_SW_SHOWMAXIMIZED
#define WinCommon_SW_SHOWMAXIMIZED 6L
#undef WinCommon_SW_SHOWMINIMIZED
#define WinCommon_SW_SHOWMINIMIZED 7L
#undef WinCommon_SW_SHOWMINNOACTIVE
#define WinCommon_SW_SHOWMINNOACTIVE 8L
#undef WinCommon_SW_SHOWNA
#define WinCommon_SW_SHOWNA 9L
#undef WinCommon_SW_SHOWNOACTIVATE
#define WinCommon_SW_SHOWNOACTIVATE 10L
#undef WinCommon_SW_SHOWNORMAL
#define WinCommon_SW_SHOWNORMAL 11L
/*
 * Class:     WinCommon
 * Method:    createProcess
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_WinCommon_createProcess
  (JNIEnv *, jobject, jstring);

/*
 * Class:     WinCommon
 * Method:    terminateProcess
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_WinCommon_terminateProcess
  (JNIEnv *, jobject, jlong);

/*
 * Class:     WinCommon
 * Method:    executeExe
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_WinCommon_executeExe
  (JNIEnv *, jobject, jstring, jstring, jint);

/*
 * Class:     WinCommon
 * Method:    createMutex
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_WinCommon_createMutex
  (JNIEnv *, jobject, jstring);

/*
 * Class:     WinCommon
 * Method:    getValidationCode
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_WinCommon_getValidationCode
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
