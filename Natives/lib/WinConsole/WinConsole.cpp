/*
 * Title:        MDS
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

#include "WinConsole.h"
#include <jni.h>
#include <windows.h>
#include <shellapi.h>
#include <tlhelp32.h>
#include <stdio.h>

extern "C"



JNIEXPORT jboolean JNICALL Java_WinConsole_allocateConsole(JNIEnv *env, jobject){

    bool t = false;
	t = AllocConsole();
    return t;

}




JNIEXPORT jlong JNICALL Java_WinConsole_getValidationCode(JNIEnv*, jobject) {
    return 409932;
}




