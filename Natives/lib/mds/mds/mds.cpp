#include "mds.h"


#define BACKGROUND_WHITE  (WORD) 0x00f0
#define BACKGROUND_CYAN   (WORD) 0x0030
#define FOREGROUND_YELLOW (WORD) 0x0006
#define FOREGROUND_CYAN   (WORD) 0x0003
#define FOREGROUND_WHITE  (WORD) 0x0007

WORD consoleBackgroundColor;


HINSTANCE hns;

BOOL WINAPI DllMain(HANDLE hHandle, DWORD dwReason, LPVOID lpReserved)
{
	switch (dwReason)
	{
		case DLL_PROCESS_ATTACH:
			//printf("DLL_PROCESS_ATTACH\n");
			hns = (HINSTANCE)hHandle;
			//MessageBox(NULL, "DLL_PROCESS_ATTACH TestDll", TEXT("Error"), MB_OK); 
		case DLL_THREAD_ATTACH:
			//MessageBox(NULL, "DLL_THREAD_ATTACH TestDll", TEXT("Error"), MB_OK); 
		case DLL_THREAD_DETACH:
			//MessageBox(NULL, "DLL_THREAD_DETACH TestDll", TEXT("Error"), MB_OK); 
		case DLL_PROCESS_DETACH:
			//if(hhookMsgHook != NULL)
			//	UnhookWindowsHookEx(hhookMsgHook);
			//printf("DLL_PROCESS_DETACH\n");
			//MessageBox(NULL, "DLL_PROCESS_DETACH TestDll", TEXT("Error"), MB_OK); 
			break;
    }

	//hhookMsgFilterHook = SetWindowsHookEx(WH_KEYBOARD, KeyboardFunc, (HINSTANCE)hHandle, NULL);

	return TRUE;
}

//void PrintW(LPSTR msg) {
//	DWORD cWritten;
//	WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);	
//}

WORD Get_WORD_To_int_Color(int color) {

	WORD wcolor;

	switch(color) {
	case OperatingSystem_COLOR_WHITE:
		wcolor = FOREGROUND_WHITE;
		break;
	case OperatingSystem_COLOR_INTENSITY_YELLOW:
		wcolor = FOREGROUND_YELLOW | FOREGROUND_INTENSITY;
		break;
	case OperatingSystem_COLOR_INTENSITY_BLUE:
		wcolor = FOREGROUND_BLUE | FOREGROUND_INTENSITY;
		break;
	case OperatingSystem_COLOR_INTENSITY_GREEN:
		wcolor = FOREGROUND_GREEN | FOREGROUND_INTENSITY;
		break;
	case OperatingSystem_COLOR_INTENSITY_RED:
		wcolor = FOREGROUND_RED | FOREGROUND_INTENSITY;
		break;
	}

		return wcolor;

}


void SetConsoleForegroundColor(int color) {
	consoleBackgroundColor = Get_WORD_To_int_Color(color);	
}

void PrintW(LPCTSTR msg) {
	DWORD cWritten;
	SetConsoleTextAttribute(hStdOut, consoleBackgroundColor);
	WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);	
}

void PrintW(LPCTSTR msg, int color) {
	DWORD cWritten;
	////switch(color) {
	////	case 0:
	////		SetConsoleTextAttribute(hStdOut, FOREGROUND_WHITE);
	////		break;
	////	case 1:
	////		SetConsoleTextAttribute(hStdOut, FOREGROUND_YELLOW | FOREGROUND_INTENSITY);
	////		break;
	////	case 2:
	////		SetConsoleTextAttribute(hStdOut, FOREGROUND_BLUE | FOREGROUND_INTENSITY);
	////		break;
	////	case 3:
	////		SetConsoleTextAttribute(hStdOut, FOREGROUND_GREEN | FOREGROUND_INTENSITY);
	////		break;
	////	case 4:
	////		SetConsoleTextAttribute(hStdOut, FOREGROUND_RED | FOREGROUND_INTENSITY);
	////		break;
	////}
	SetConsoleTextAttribute(hStdOut, Get_WORD_To_int_Color(color));
	WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);	
}

void ThrowLastException(JNIEnv* env) {
    LPVOID lpMsgBuf;
    FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
                  NULL, GetLastError(), MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR) &lpMsgBuf, 0,
                  NULL );
    //env->ThrowNew(env->FindClass("java/lang/RuntimeException"), (LPCTSTR)lpMsgBuf);
    env->ThrowNew(env->FindClass("NativeLibrayException"), (LPCTSTR)lpMsgBuf);
	////MessageBox(NULL, (LPCTSTR)lpMsgBuf, "", MB_OK);
	//JNIEnv *env = (JNIEnv *)JNU_GetEnv(jvm, JNI_VERSION_1_2);
}

void ThrowLastException(JNIEnv* env, LPCTSTR message) {
    env->ThrowNew(env->FindClass("NativeLibrayException"), message);
	////MessageBox(NULL, message, "", MB_OK);
}
