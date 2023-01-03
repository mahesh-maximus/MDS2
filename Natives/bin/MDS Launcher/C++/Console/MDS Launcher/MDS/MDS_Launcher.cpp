
#include<stdio.h>
#include <windows.h>
#include <conio.h>
#include <string>
#include <iostream>
using namespace std;

#define BACKGROUND_WHITE  (WORD) 0x00f0
#define BACKGROUND_CYAN   (WORD) 0x0030
#define FOREGROUND_YELLOW (WORD) 0x0006
#define FOREGROUND_CYAN   (WORD) 0x0003
#define FOREGROUND_WHITE  (WORD) 0x0007

bool runningMDS = true;


DWORD WINAPI ThreadFunc( LPVOID lpParam ){

	 string symbol[] = {"\\", "|", "|", "/", "-", "|", "-"};
	 int x = 0;

	 while(runningMDS) {
		Sleep(100);
		cout << symbol[x] << "\r";
		x++;
		if(x == 7) x = 0;
	 }

    return 0;
}


void main(void) {

	TCHAR tempMdsPath[MAX_PATH];
	string mdsPath;
	string jdkPath;
	string jdkToolsPath;
	string mdsLibPath;
	string mdsBinPath;
	string commandLine;
	string QUOTE;
	string DEBUG_MODE = "-debug";
	LPTSTR tempCommandLine;
	LPTSTR tempJdkPath;
	LPTSTR currentDirectory;
	LPSTR msg;

	DWORD cWritten;

	HANDLE hThreadFlash;
	DWORD dwThreadId, dwThrdParam = 1;

	HANDLE hStdOut;
    HANDLE hStdin;

	STARTUPINFO si;
    PROCESS_INFORMATION pi;

	QUOTE +=('"');

	BOOL success;

    ZeroMemory( &si, sizeof(si) );
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi) );

    CreateMutex(NULL, NULL, "MDS_2.0_342444");

    if(GetLastError() == ERROR_ALREADY_EXISTS) {
		exit(0);
    }

    hStdOut = GetStdHandle(STD_OUTPUT_HANDLE);
    hStdin = GetStdHandle(STD_INPUT_HANDLE);

    msg = "#\n#";
    SetConsoleTextAttribute(hStdOut, FOREGROUND_WHITE | FOREGROUND_INTENSITY);
    WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);
    msg = " MDS";
    SetConsoleTextAttribute(hStdOut, FOREGROUND_YELLOW | FOREGROUND_INTENSITY);
    WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);
    SetConsoleTextAttribute(hStdOut, FOREGROUND_WHITE | FOREGROUND_INTENSITY);
    msg = " Mahesh Desktop System [Developer's Edition]\n";
    WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);
    msg = "#\n#";
    WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);

	msg = "Loading Java Runtime ... \n";
    SetConsoleTextAttribute(hStdOut, FOREGROUND_RED | FOREGROUND_INTENSITY);
    WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);

	//msg = "#\n#";
    SetConsoleTextAttribute(hStdOut, FOREGROUND_WHITE);
    //WriteConsole(hStdOut, msg, lstrlen(msg), &cWritten, NULL);


	if(GetCurrentDirectory(sizeof(tempMdsPath)/sizeof(TCHAR), tempMdsPath)) {

	    hThreadFlash = CreateThread(NULL, 0, ThreadFunc, &dwThrdParam, 0, &dwThreadId);
        //SetThreadPriority(hThreadFlash, THREAD_PRIORITY_LOWEST);

		mdsPath = (string)tempMdsPath;


		////jdkPath = mdsPath + "\\java\\jdk1.5.0_06\\BIN\\java.exe";

		//////cout << jdkPath << endl;

		////jdkToolsPath = mdsPath + "\\java\\jdk1.5.0_06\\LIB\\Tools.jar;";

		//cout << jdkToolsPath << endl;

		jdkPath = "D:\\Program Files\\Java\\jre1.6.0\\bin\\java.exe";

		jdkToolsPath = "D:\\Program Files\\Java\jre1.6.0\\lib\\Tools.jar;";



		mdsLibPath = mdsPath + "\\lib;";

		mdsBinPath = mdsPath + "\\bin;";

		commandLine = " -cp " + QUOTE + mdsPath + ";" + jdkToolsPath + mdsLibPath + mdsBinPath + QUOTE + " -splash:splash.gif MDS_Main " + DEBUG_MODE;

		//cout << commandLine;

		tempCommandLine = (LPTSTR)commandLine.c_str(); 

		tempJdkPath = (LPTSTR)jdkPath.c_str(); 

		currentDirectory = (LPTSTR) mdsPath.c_str();



		success = CreateProcess(tempJdkPath, 
						tempCommandLine, 
						NULL, 
						NULL, 
						TRUE, 
						HIGH_PRIORITY_CLASS, 
						NULL, 
						currentDirectory, 
						&si, 
						&pi);

		
		if(!success) {
			LPVOID lpMsgBuf;
			FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
						NULL, GetLastError(), MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR) &lpMsgBuf, 0,
						NULL );

			MessageBox(NULL, (LPCTSTR) lpMsgBuf, "MDS Launcher", MB_ICONERROR);
		}



		WaitForSingleObject(pi.hProcess, INFINITE);

		runningMDS = false;



	}








	//getchar();


}