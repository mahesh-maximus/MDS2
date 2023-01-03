/*
 * Title:        MDS
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

#include <jni.h>
#include "MDS.h"
#include <windows.h>
#include <tlhelp32.h>
#include <stdio.h>

extern "C"



JNIEXPORT jboolean JNICALL Java_MDS_isPrevInstance(JNIEnv*, jobject) {
    bool r = false;
    CreateMutex(NULL, NULL, "MDS_1.0_91183290129038");

    if(GetLastError() == ERROR_ALREADY_EXISTS) {
        r = true;
    }

    return r;

}



JNIEXPORT jboolean JNICALL Java_MDS_setHighPriorityClass(JNIEnv *, jobject){
	printf("Java_MDS_setHighPriorityClass");
	return SetPriorityClass(GetCurrentProcess(), HIGH_PRIORITY_CLASS);
}



JNIEXPORT jlong JNICALL Java_MDS_getValidationCode(JNIEnv*, jobject) {
    return 194307;
}




