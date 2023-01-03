
#define APPNAME "OS Event Listener"

#include <windows.h>
#include <stdio.h>
#include <iostream>
using namespace std;



// Global Variables:

HINSTANCE hInst;      // current instance
char szAppName[100];  // Name of the app
char szTitle[100];    // The title bar text


BOOL InitApplication(HINSTANCE);
BOOL InitInstance(HINSTANCE, int);
LRESULT CALLBACK WndProc(HWND, UINT, WPARAM, LPARAM);


int APIENTRY WinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPSTR     lpCmdLine,
                     int       nCmdShow)
{
   MSG msg;
   HACCEL hAccelTable;

   
    //if(lstrcmp(lpCmdLine, TEXT("911")) != 0) {
    //    exit(0);
    //}



   // Initialize global strings
   lstrcpy (szAppName, APPNAME);

   if (!InitApplication(hInstance)) {
       return (FALSE);
   }

   if (!InitInstance(hInstance, nCmdShow)) {
      return (FALSE);
   }

   hAccelTable = LoadAccelerators (hInstance, szAppName);

   // Main message loop:
   while (GetMessage(&msg, NULL, 0, 0)) {
      if (!TranslateAccelerator (msg.hwnd, hAccelTable, &msg)) {
         TranslateMessage(&msg);
         DispatchMessage(&msg);
      }
   }

   return (int)msg.wParam;

   lpCmdLine; // This will prevent 'unused formal parameter' warnings
}



BOOL InitApplication(HINSTANCE hInstance)
{
    WNDCLASS  wc;

    // Fill in window class structure with parameters that describe
	// the main window.
	wc.style         = CS_HREDRAW | CS_VREDRAW;
	wc.lpfnWndProc   = (WNDPROC)WndProc;
	wc.cbClsExtra    = 0;
	wc.cbWndExtra    = 0;
	wc.hInstance     = hInstance;
	wc.hIcon         = NULL;
	wc.hCursor       = LoadCursor(NULL, IDC_ARROW);
	wc.hbrBackground = (HBRUSH)(COLOR_WINDOW+1);
	wc.lpszMenuName  = NULL;
	wc.lpszClassName = szAppName;

	return RegisterClass(&wc);

}


BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   HWND hWnd;

   hInst = hInstance; // Store instance handle in our global variable

   hWnd = CreateWindow(szAppName, 
	                   "OS Event Listener", 
					   WS_OVERLAPPEDWINDOW,
                       CW_USEDEFAULT,
					   0,
					   CW_USEDEFAULT,
					   0,
                       NULL, NULL, NULL, NULL);

   if (!hWnd) {
      return (FALSE);
   }

   AllocConsole();

   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   return (TRUE);
}


LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
   int wmId, wmEvent;

   switch (message) {

      case WM_COMMAND:
         wmId    = LOWORD(wParam); // Remember, these are...
         wmEvent = HIWORD(wParam); // ...different for Win32!
         break;
      case WM_DISPLAYCHANGE: 
		break;
	  case WM_DEVICECHANGE:
		break;
	  case WM_DEVMODECHANGE: 
		break;
      case WM_DESTROY:
         PostQuitMessage(0);
         break;
      default:
         return (DefWindowProc(hWnd, message, wParam, lParam));
   }

   return (0);

}


 





