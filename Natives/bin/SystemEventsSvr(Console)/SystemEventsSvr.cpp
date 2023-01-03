
#define APPNAME "Generic"
#include <iostream>                 // for i/o functions
using namespace std;

// Windows Header Files:
#include <windows.h>
//#include <stdio.h>

//#include <conio.h>

char szAppName[100];  // Name of the app
char szTitle[100];    // The title bar text

BOOL InitApplication();
BOOL InitInstance();
LRESULT CALLBACK WndProc(HWND, UINT, WPARAM, LPARAM);

void main() {
	
	   MSG msg;
   HACCEL hAccelTable;

   //cr();

   // Initialize global strings
   lstrcpy (szAppName, APPNAME);


      // Perform instance initialization:
      if (!InitApplication()) {
         printf("ERROR ");
      }

   // Perform application initialization:
   if (!InitInstance()) {
      printf("ERROR ");
   }

   //hAccelTable = LoadAccelerators (hInstance, szAppName);

   // Main message loop:
   while (GetMessage(&msg, NULL, 0, 0)) {
      if (!TranslateAccelerator (msg.hwnd, NULL, &msg)) {
         TranslateMessage(&msg);
         DispatchMessage(&msg);
      }
   }
}

BOOL InitApplication()
{
    WNDCLASS  wc;
    HWND      hwnd;

    // Win32 will always set hPrevInstance to NULL, so lets check
    // things a little closer. This is because we only want a single
    // version of this app to run at a time
    hwnd = FindWindow (szAppName, szTitle);
    if (hwnd) {
        // We found another version of ourself. Lets defer to it:
        if (IsIconic(hwnd)) {
            ShowWindow(hwnd, SW_RESTORE);
        }
        SetForegroundWindow (hwnd);

        // If this app actually had any functionality, we would
        // also want to communicate any action that our 'twin'
        // should now perform based on how the user tried to
        // execute us.
        return FALSE;
        }

        // Fill in window class structure with parameters that describe
        // the main window.
        wc.style         = CS_HREDRAW | CS_VREDRAW;
        wc.lpfnWndProc   = (WNDPROC)WndProc;
        wc.cbClsExtra    = 0;
        wc.cbWndExtra    = 0;
        wc.hInstance     = NULL;
        wc.hIcon         = NULL;
        wc.hCursor       = LoadCursor(NULL, IDC_ARROW);
        wc.hbrBackground = (HBRUSH)(COLOR_WINDOW+1);
        wc.lpszMenuName  = NULL;
        wc.lpszClassName = szAppName;

        return RegisterClass(&wc);

}

BOOL InitInstance()
{
   HWND hWnd;

   //hInst = hInstance; // Store instance handle in our global variable

   hWnd = CreateWindow(szAppName, NULL, WS_OVERLAPPEDWINDOW,
      CW_USEDEFAULT, 0, CW_USEDEFAULT, 0,
      NULL, NULL, NULL, NULL);

   if (!hWnd) {
      return (FALSE);
   }

   ShowWindow(hWnd, SW_SHOW);
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
      case WM_DISPLAYCHANGE: // Only comes through on plug'n'play systems
      break;
      case WM_PAINT:
         break;
      case WM_DESTROY:
         PostQuitMessage(0);
         break;
      case WM_LBUTTONDOWN:
		  //printf("MMMMMMMMMMMMMMMMMMMMMMMMMMM\n");
		  //cout<<"MAHESH";
		  cout << "MMMMMMMMMMMMM\n" << flush;
         break;
	  case WM_KEYDOWN:
		  //printf("MMMMMMMMMMMMMMMMMMMMMMMMMMM\n");
		 // cout<<"MAHESH";
		  cout << "MMMMMMMMMMM\n" << flush;
		  break;
	  case WM_DEVICECHANGE:
		  cout << "Mahesh WE\n" << flush;
		  break;
		 
      default:
         return (DefWindowProc(hWnd, message, wParam, lParam));
   }
   return (0);
}