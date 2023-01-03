/*
 * Title:        MDS
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */


#include <jni.h>          
#include <stdio.h>
#include "WinUI.h"
#include <conio.h>
#include <process.h>

extern "C"



JNIEXPORT void JNICALL Java_WinUI_setActiveWindow(JNIEnv *env,jobject, jstring jcaption, jstring jcName){
   const char* msg=env->GetStringUTFChars(jMsg,0);
   printf(msg);
   env->ReleaseStringUTFChars(jMsg, msg);
} 