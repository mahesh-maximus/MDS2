/*
 * Title:        MDS
 * Description:  New Desktop Environment for Windows
 * Author:       Mahesh Dharmasena
 * Version:      1.0
 */
 

#include "WinCommon.h"
#include <jni.h>
#include <windows.h>
#include <shellapi.h>

extern "C"



JNIEXPORT jlong JNICALL Java_WinCommon_createProcess(JNIEnv *env,jobject,  jstring jName){
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



JNIEXPORT jboolean JNICALL Java_WinCommon_terminateProcess(JNIEnv *env, jobject,  jlong jId){

    bool t = false;

    if(TerminateProcess((PVOID)((ULONG)jId), 0)) {
        t = true;
    }

    return t;

}



JNIEXPORT void JNICALL Java_WinCommon_executeExe(JNIEnv *env, jobject,  jstring jPath, jstring jParameters, jint showCmd) {
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



JNIEXPORT void JNICALL Java_WinCommon_createMutex(JNIEnv *env, jobject,  jstring jName) {
    const char* name=env->GetStringUTFChars(jName,0);
    LPSTR mName = (LPSTR)name;
    CreateMutex(NULL, NULL, mName);
    if(GetLastError() == ERROR_ALREADY_EXISTS) {
        env->ThrowNew(env->FindClass("MutexAlreadyExistsException"), name);
    }
}







JNIEXPORT jlong JNICALL Java_WinCommon_getValidationCode(JNIEnv*, jobject) {
    return 901321;
}




