
#include<stdio.h>
#include <windows.h>
#include <shlobj.h>
#include <conio.h>
#include <string>
#include <TCHAR.H>
#include <iostream>
using namespace std;

void StartMDS();
BOOL FileExists(LPCTSTR);
void Update_jvmLauncherPath();

TCHAR TCHAR_jvmLauncherPath[MAX_PATH];


int APIENTRY WinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPSTR     lpCmdLine,
                     int       nCmdShow)
{
	StartMDS();
}

void StartMDS() {

	TCHAR TCHAR_tempMdsPath[MAX_PATH];
	string string_mdsPath;
	string string_jvmLauncherPath;
	string string_jdkToolsPath;
	string string_mdsLibPath;
	string string_mdsBinPath;
	string string_commandLine;
	string string_QUOTE;
	string string_DEBUG_MODE = "-debug";
	LPTSTR LPTSTR_commandLine;
	LPTSTR LPTSTR_jvmLauncherPath;
	LPTSTR LPTSTR_currentDirectory;
	BOOL success;

	string mainClassPath;
	
	STARTUPINFO si;
    PROCESS_INFORMATION pi;

	string_QUOTE +=('"');

    ZeroMemory( &si, sizeof(si) );
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi) );

  //  CreateMutex(NULL, NULL, "MDS_2.0_342444");

  //  if(GetLastError() == ERROR_ALREADY_EXISTS) {
		//exit(0);
  //  }

	if(GetCurrentDirectory(sizeof(TCHAR_tempMdsPath)/sizeof(TCHAR), TCHAR_tempMdsPath)) {

		string_mdsPath = (string)TCHAR_tempMdsPath;

		//string_jvmLauncherPath = "D:\\Program Files\\Java\\jdk1.6.0_03\\binx\\javaw.exe";
		string_jvmLauncherPath = string_mdsPath.substr(0, 2) + "\\jdk1.6.0_03\\bin\\javaw.exe";

		string_mdsLibPath = string_mdsPath + "\\lib;";

		string_mdsLibPath = string_mdsPath + "\\lib\\mdsLib.jar;";

		string_commandLine = " -cp " + string_QUOTE + string_mdsLibPath +  string_QUOTE + " -splash:splash.gif MDS_Main " + string_DEBUG_MODE;

		LPTSTR_commandLine = (LPTSTR)string_commandLine.c_str(); 
		
		

		if(lstrlen(_TEXT(TCHAR_jvmLauncherPath)) > 0) {
			LPTSTR_jvmLauncherPath = lstrcat(_TEXT(TCHAR_jvmLauncherPath), "\\javaw.exe");;
		} else {
			LPTSTR_jvmLauncherPath = (LPTSTR)string_jvmLauncherPath.c_str(); 
		}


		LPTSTR_currentDirectory = (LPTSTR) string_mdsPath.c_str();


		mainClassPath = string_mdsPath + "\\MDS_Main.class";
		if(FileExists((LPTSTR)mainClassPath.c_str())) {

			success = CreateProcess(LPTSTR_jvmLauncherPath, 
									LPTSTR_commandLine, 
									NULL, 
									NULL, 
									TRUE, 
									NORMAL_PRIORITY_CLASS, 
									NULL, 
									LPTSTR_currentDirectory, 
									&si, 
									&pi);

			LPVOID lpMsgBuf;
			FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
						NULL, GetLastError(), MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR) &lpMsgBuf, 0,
						NULL );

			if(!success) {
				MessageBox(NULL, (LPCTSTR) "Unable to start Java runtime.", "MDS Launcher", MB_ICONERROR);
				
				Update_jvmLauncherPath();
				StartMDS();
			}

		} else {
			MessageBox(NULL, (LPCTSTR) "Unable to start MDS \n\n 'MDS_Main.class' not found.", "MDS Launcher", MB_ICONERROR);
		}


	}
}


void Update_jvmLauncherPath() {
	BROWSEINFO bi;
		ZeroMemory(&bi, sizeof(bi));

	TCHAR szDisplayName[MAX_PATH];
	//szDisplayName[0] = '\0';
	
	bi.hwndOwner = NULL;
	bi.pidlRoot = NULL;
	bi.pszDisplayName = szDisplayName;
	bi.lpszTitle = TEXT("Locate Java [JDK] 'bin' Derectory\n Ex: 'C:\\Program Files\\Java\\jdk1.6.0_03\\bin'.");
	bi.ulFlags = BIF_EDITBOX | BIF_VALIDATE ;

	bi.lParam = NULL;
	bi.iImage = 0;

	LPITEMIDLIST pidl = SHBrowseForFolder(&bi);

	if(pidl == NULL) exit(0);

	SHGetPathFromIDList(pidl, TCHAR_jvmLauncherPath);

}


BOOL FileExists(LPCTSTR path) {
	WIN32_FIND_DATA FindFileData;
	HANDLE hFind;
	BOOL result = FALSE;

	hFind = FindFirstFile(path, &FindFileData);
	if(hFind != INVALID_HANDLE_VALUE) {
		result = TRUE;	
		FindClose(hFind);
	}
	return result;
}
