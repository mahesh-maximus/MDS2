#include <windows.h>
//#include <windowsx.h>
//#include "windows.h"
#include <windowsx.h>
#include <zmouse.h>
#include <stdio.h>
#include <jni.h>
#include "OperatingSystem.h"
#include "SystemKeyEvent.h"
#include "SystemMouseEvent.h"
#include "FileSystemEventManager.h"

extern HANDLE hStdOut;
//extern HHOOK hhookMsgHook;
extern HINSTANCE hns;
//void PrintW(LPSTR);
void SetConsoleForegroundColor(int);
void PrintW(LPCTSTR);
void PrintW(LPCTSTR, int);
void ThrowLastException(JNIEnv*);
void ThrowLastException(JNIEnv*, LPCTSTR);

