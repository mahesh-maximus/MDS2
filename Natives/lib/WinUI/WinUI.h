/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class WinUI */

#ifndef _Included_WinUI
#define _Included_WinUI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     WinUI
 * Method:    findWindow
 * Signature: (Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_WinUI_findWindow
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     WinUI
 * Method:    showWindow
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_WinUI_showWindow
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     WinUI
 * Method:    sendMessage
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_WinUI_sendMessage
  (JNIEnv *, jobject, jstring, jstring, jstring);

/*
 * Class:     WinUI
 * Method:    getValidationCode
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_WinUI_getValidationCode
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
