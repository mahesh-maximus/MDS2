#include "mds.h"


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

void PrintW(LPSTR msg) {
	DWORD cWritten;
	WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);	
}