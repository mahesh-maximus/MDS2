
#include "mds.h"
#include <windows.h>

typedef struct {
	int id;
	DWORD threadId;
    HANDLE ffHandles[2];
} Listener;

Listener ListenersTable[10];

static bool start = false;

DWORD dwThreadId, dwThrdParam = 1;
HANDLE hThreadFlash;

int gid;
JavaVM *javaVM1 = NULL;
jobject parser1;
JNIEnv *env1;
jobject cbl;

DWORD WINAPI ThreadFunc( LPVOID lpParam ){

	////while(true) {
	////	Sleep(1000);
	////	//PrintW("THREAD registerFileSystemListener\n");
	////	

	////	for(int x = 0; x < 10; x++) {
	////		if(ListenersTable[x].threadId > 0){
	////			SetLastError(0);
	////			HANDLE thd = OpenThread(THREAD_QUERY_INFORMATION, TRUE, ListenersTable[x].threadId);
	////			SetLastError(0);
	////			int tp = GetThreadPriority(thd);
	////			
	////			if(GetLastError() > 0) {
	////			//if(thd == NULL) {
	////				PrintW("THREAD NULL ERROR ERROR ERROR ERROR ERROR\n");	
	////				ListenersTable[x].threadId = -1;
	////				FindCloseChangeNotification(ListenersTable[x].ffHandles[0]);
	////				FindCloseChangeNotification(ListenersTable[x].ffHandles[1]);
	////			} else {
	////				//PrintW("THREAD CCCCCCCCCC VVVVVVVVV FOUND ERROR\n");

	////			}
	////			//if(GetLastError() > 0) {
	////			//	PrintW("THREAD NULL ERROR ERROR ERROR ERROR ERROR\n");	
	////			//	ListenersTable[x].threadId = -1;
	////			//	FindCloseChangeNotification(ListenersTable[x].ffHandles[0]);
	////			//	FindCloseChangeNotification(ListenersTable[x].ffHandles[1]);
	////			//}
	////		}

	////	}
	////}




	int blackIndex = -1;



	PrintW("registerFileSystemListener\n");

	//MessageBox(NULL, "", "", MB_OK);
	
	for(int x = 0; x < 10; x++) {
		if(ListenersTable[x].threadId <= 0){
			blackIndex = x;
			break;
		}
	}


	if(blackIndex == -1) return 0;

	//blackIndex = 0;

	ListenersTable[blackIndex].threadId =  GetCurrentThreadId();
	ListenersTable[blackIndex].id = gid;	
	
	

	DWORD dwWaitStatus;

	//HANDLE dwChangeHandles[2];

	char * DirName = "E:\\ds\\"; // file
	char * DirName1 = "E:\\ds\\";         // directory

	//const char* DirName = env->GetStringUTFChars(dir,0);  // file
	//const char* DirName1 = env->GetStringUTFChars(dir,0); // directory


	//ListenersTable[blackIndex].id = (LPSTR)DirName;


	// Watch the F:\myproject directory for file creation and deletion. 

	ListenersTable[blackIndex].ffHandles[0] = FindFirstChangeNotification( 
		DirName,                       
		FALSE,                         
		FILE_NOTIFY_CHANGE_FILE_NAME); 




	// Watch the F:\\ subtree for directory creation and deletion. 
	ListenersTable[blackIndex].ffHandles[1] = FindFirstChangeNotification( 
		DirName1,                      
		TRUE,                          
		FILE_NOTIFY_CHANGE_DIR_NAME);  

	 


	if (ListenersTable[blackIndex].ffHandles[0] != INVALID_HANDLE_VALUE && ListenersTable[blackIndex].ffHandles[1] != INVALID_HANDLE_VALUE)
	{
		PrintW("I'm monitoring any file, directory deletion/creation in \n");
	}

	bool b = true;

	while (TRUE) 
	{ 

		dwWaitStatus = WaitForMultipleObjects(2, ListenersTable[blackIndex].ffHandles, FALSE, INFINITE); 

		switch (dwWaitStatus) 
		{ 
			
			JNIEnv *java;

			case 0: // WAIT_OBJECT_0

				if (FindNextChangeNotification(ListenersTable[blackIndex].ffHandles[0]) == FALSE) 
					ExitProcess(GetLastError());
				else {
					PrintW("File created/deleted\n");
					//jclass cls = (env)->GetObjectClass(callBack);
					//jmethodID mid = (env)->GetMethodID(cls, "fireFileSystemEvent", "(Z)V");
					//if(mid != NULL) 
					//	(env)->CallVoidMethod(callBack, mid, false, 1);
					//else
					//	PrintW("fireFileSystemEvent FILE NULL\n");
					
				}
				break;

			case 1: // WAIT_OBJECT_0 + 1

				if (FindNextChangeNotification(ListenersTable[blackIndex].ffHandles[1]) == FALSE) 
					ExitProcess(GetLastError());
				else {
					PrintW("Directory created/deleted\n");
					if(javaVM1->AttachCurrentThread((void **)&java, NULL) >= 0)
	{
					jclass cls = (java)->GetObjectClass(parser1);
					jmethodID mid = (java)->GetMethodID(cls, "fireFileSystemEvent", "(Z)V");
					if(mid != NULL) 
						(java)->CallVoidMethod(parser1, mid, true, 1);
					else 
						PrintW("fireFileSystemEvent DIR NULL\n");
					javaVM1->DetachCurrentThread();
	}
	

		

				}

				break; 

			default:
				ExitProcess(GetLastError());

		}

	}







    return 0;
}

JNIEXPORT void JNICALL Java_FileSystemEventManager_unRegisterFileSystemListener
(JNIEnv *, jobject, jint id) {

	for(int x = 0; x < 10; x++) {
		if(ListenersTable[x].id == id){
			HANDLE h = OpenThread(THREAD_ALL_ACCESS, FALSE, ListenersTable[x].threadId);
			if(h == NULL) PrintW("HANDLE EMPTY>>>>>>>>>>>>>>>>>>\n");

			DWORD d;
			if(!GetExitCodeThread(h, &d)) PrintW("D EMPTY>>>>>>>>>>>>>>>>>>\n");

			SetLastError(0);
			TerminateThread(h, d);
			if(GetLastError() > 0) PrintW("GetLastError > 0 >>>>>>>>>>>>>>>>>>\n");


			//FindCloseChangeNotification(ListenersTable[x].ffHandles[0]);
			//FindCloseChangeNotification(ListenersTable[x].ffHandles[1]);
			ListenersTable[x].threadId = -1;
			break;

		}
	}
}


JNIEXPORT void JNICALL Java_FileSystemEventManager_registerFileSystemListener
					(JNIEnv *env, jobject obj, jstring dir, jobject callBack, jint id) {


	parser1 = env->NewGlobalRef(callBack);
	env->GetJavaVM(&javaVM1);


	int blackIndex = -1;

	gid = id;
env1 = env;
cbl = callBack;

	//if(!start) {
 //       hThreadFlash = CreateThread(NULL, 0, ThreadFunc, &dwThrdParam, 0, &dwThreadId);
 //       SetThreadPriority(hThreadFlash, THREAD_PRIORITY_LOWEST);
	//	start = true;
	//}

	CreateThread(NULL, 0, ThreadFunc, &dwThrdParam, 0, &dwThreadId);
	SetThreadPriority(hThreadFlash, THREAD_PRIORITY_LOWEST);






	//PrintW("registerFileSystemListener\n");

	//MessageBox(NULL, "", "", MB_OK);
	//
	//for(int x = 0; x < 10; x++) {
	//	if(ListenersTable[x].threadId <= 0){
	//		blackIndex = x;
	//		break;
	//	}
	//}


	//if(blackIndex == -1) return;

	////blackIndex = 0;

	//ListenersTable[blackIndex].threadId =  GetCurrentThreadId();
	//ListenersTable[blackIndex].id = id;	
	//
	//

	//DWORD dwWaitStatus;

	////HANDLE dwChangeHandles[2];

	//char * DirName = "E:\\ds\\"; // file
	//char * DirName1 = "E:\\ds\\";         // directory

	////const char* DirName = env->GetStringUTFChars(dir,0);  // file
	////const char* DirName1 = env->GetStringUTFChars(dir,0); // directory


	////ListenersTable[blackIndex].id = (LPSTR)DirName;


	//// Watch the F:\myproject directory for file creation and deletion. 

	//ListenersTable[blackIndex].ffHandles[0] = FindFirstChangeNotification( 
	//	DirName,                       
	//	FALSE,                         
	//	FILE_NOTIFY_CHANGE_FILE_NAME); 




	//// Watch the F:\\ subtree for directory creation and deletion. 
	//ListenersTable[blackIndex].ffHandles[1] = FindFirstChangeNotification( 
	//	DirName1,                      
	//	TRUE,                          
	//	FILE_NOTIFY_CHANGE_DIR_NAME);  

	// 


	//if (ListenersTable[blackIndex].ffHandles[0] != INVALID_HANDLE_VALUE && ListenersTable[blackIndex].ffHandles[1] != INVALID_HANDLE_VALUE)
	//{
	//	PrintW("I'm monitoring any file, directory deletion/creation in \n");
	//}

	//bool b = true;

	//while (TRUE) 
	//{ 

	//	dwWaitStatus = WaitForMultipleObjects(2, ListenersTable[blackIndex].ffHandles, FALSE, INFINITE); 

	//	switch (dwWaitStatus) 
	//	{ 

	//		case 0: // WAIT_OBJECT_0

	//			if (FindNextChangeNotification(ListenersTable[blackIndex].ffHandles[0]) == FALSE) 
	//				ExitProcess(GetLastError());
	//			else {
	//				PrintW("File created/deleted\n");
	//				jclass cls = (env)->GetObjectClass(callBack);
	//				jmethodID mid = (env)->GetMethodID(cls, "fireFileSystemEvent", "(Z)V");
	//				if(mid != NULL) 
	//					(env)->CallVoidMethod(callBack, mid, false, 1);
	//				else
	//					PrintW("fireFileSystemEvent FILE NULL\n");
	//				
	//			}
	//			break;

	//		case 1: // WAIT_OBJECT_0 + 1

	//			if (FindNextChangeNotification(ListenersTable[blackIndex].ffHandles[1]) == FALSE) 
	//				ExitProcess(GetLastError());
	//			else {
	//				PrintW("Directory created/deleted\n");
	//				jclass cls = (env)->GetObjectClass(callBack);
	//				jmethodID mid = (env)->GetMethodID(cls, "fireFileSystemEvent", "(Z)V");
	//				if(mid != NULL) 
	//					(env)->CallVoidMethod(callBack, mid, true, 1);
	//				else 
	//					PrintW("fireFileSystemEvent DIR NULL\n");

	//				//b = false;

	//			}

	//			break; 

	//		default:
	//			ExitProcess(GetLastError());

	//	}

	//}
     // May close the handles...

     //FindCloseChangeNotification(ListenersTable[blackIndex].ffHandles[0]);
     //FindCloseChangeNotification(ListenersTable[blackIndex].ffHandles[1]);







	

}


