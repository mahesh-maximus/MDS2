/*
 * Title:        MDS
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

#include "WinProcess.h"
#include <jni.h>
#include <windows.h>
#include <shellapi.h>
#include <tlhelp32.h>
#include <stdio.h>

extern "C"



JNIEXPORT jlong JNICALL Java_WinProcess_createProcess__Ljava_lang_String_2(JNIEnv *env,jobject,  jstring jName){
    const char* name=env->GetStringUTFChars(jName,0);
    STARTUPINFO si;
    PROCESS_INFORMATION pi;
    LPSTR commandLine = (LPSTR)name;
    ZeroMemory( &si, sizeof(si) );
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi) );


    if(!CreateProcess( NULL, commandLine, NULL, NULL, FALSE, 0, NULL, NULL, &si, &pi)) {
        //MessageBox (GetFocus(), "CreateProcess failed.", "###", MB_OK|MB_ICONHAND);
    }

    return (unsigned long)pi.hProcess;

}



JNIEXPORT jlong JNICALL Java_WinProcess_createProcess__Ljava_lang_String_2Ljava_lang_String_2ZLjava_lang_String_2(JNIEnv *env,jobject,  jstring japplicationName, jstring jcommandLine, jboolean inheritHandles, jstring jcurrentDirectory){
    const char* appName=env->GetStringUTFChars(japplicationName,0);
	const char* cmdLine=env->GetStringUTFChars(jcommandLine,0);
	const char* curDir=env->GetStringUTFChars(jcurrentDirectory,0);
    STARTUPINFO si;
    PROCESS_INFORMATION pi;
    LPSTR applicationName = (LPSTR)appName;
	LPSTR commandLine = (LPSTR)cmdLine;
	LPSTR currentDirectory = (LPSTR)curDir;
    ZeroMemory( &si, sizeof(si) );
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi) );


    if(!CreateProcess( applicationName, commandLine, NULL, NULL, inheritHandles, 0, NULL, currentDirectory, &si, &pi)) {
        //MessageBox (GetFocus(), "CreateProcess failed.", "###", MB_OK|MB_ICONHAND);
    }

    return (unsigned long)pi.hProcess;

}



//JNIEXPORT jboolean JNICALL Java_WinProcess_terminateProcess__J(JNIEnv *env, jobject,  jlong jId){
//
//    bool t = false;
//
//    if(TerminateProcess((PVOID)((ULONG)jId), 0)) {
//        t = true;
//    }
//
//    return t;
//
//}



JNIEXPORT jboolean JNICALL Java_WinProcess_terminateProcess(JNIEnv *env, jobject,  jlong jId){

    bool t = false;

    if(TerminateProcess((PVOID)((ULONG)jId), 0)) {
        t = true;
    }

    return t;

}


JNIEXPORT void JNICALL Java_WinProcess_executeExe(JNIEnv *env, jobject,  jstring jPath, jstring jParameters, jint showCmd) {
    const char* path=env->GetStringUTFChars(jPath,0);
    const char* parameters=env->GetStringUTFChars(jParameters,0);
    INT nShowCmd = NULL;

    switch(showCmd) {
        case 0:
            nShowCmd = SW_HIDE;
            break;
        case 1:
            nShowCmd = SW_MAXIMIZE;
            break;
        case 2:
            nShowCmd = SW_MINIMIZE;
            break;
        case 3:
            nShowCmd = SW_RESTORE;
            break;
        case 4:
            nShowCmd = SW_SHOW;
            break;
        case 5:
            nShowCmd = SW_SHOWDEFAULT;
            break;
        case 6:
            nShowCmd = SW_SHOWMAXIMIZED;
            break;
        case 7:
            nShowCmd = SW_SHOWMINIMIZED;
            break;
        case 8:
            nShowCmd = SW_SHOWMINNOACTIVE;
            break;
        case 9:
            nShowCmd = SW_SHOWNA;
            break;
        case 10:
            nShowCmd = SW_SHOWNOACTIVATE;
            break;
        case 11:
            nShowCmd = SW_SHOWNORMAL;
            break;
        default:
            nShowCmd = NULL;
    }

    ShellExecute(NULL, NULL, path , parameters, NULL, nShowCmd);

}




JNIEXPORT jlong JNICALL Java_WinProcess_getValidationCode(JNIEnv*, jobject) {
    return 409932;
}




