
#include "mds.h"


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
const char * gDirName; 
JavaVM *javaVM1 = NULL;
jobject javaListener;


void CallBackJava(bool dir) {

	JNIEnv *java;

	if(javaVM1->AttachCurrentThread((void **)&java, NULL) >= 0)
	{
		jclass cls = (java)->GetObjectClass(javaListener);
		jmethodID mid = (java)->GetMethodID(cls, "fireFileSystemEvent", "(Z)V");
		if(mid != NULL) 
			(java)->CallVoidMethod(javaListener, mid, dir);
		else 
			PrintW("fireFileSystemEvent DIR NULL\n");
		javaVM1->DetachCurrentThread();
	}
}


DWORD WINAPI ThreadFunc( LPVOID lpParam ){


	int blackIndex = -1;

	////PrintW("registerFileSystemListener\n");

	for(int x = 0; x < 10; x++) {
		if(ListenersTable[x].threadId <= 0){
			blackIndex = x;
			break;
		}
	}


	if(blackIndex == -1) return 0;

	ListenersTable[blackIndex].threadId =  GetCurrentThreadId();
	ListenersTable[blackIndex].id = gid;	
	
	
	DWORD dwWaitStatus;
	const char * DirName = gDirName;

	ListenersTable[blackIndex].ffHandles[0] = FindFirstChangeNotification( 
		DirName,                       
		FALSE,                         
		FILE_NOTIFY_CHANGE_FILE_NAME); 

	ListenersTable[blackIndex].ffHandles[1] = FindFirstChangeNotification( 
		DirName,                      
		FALSE,                          
		FILE_NOTIFY_CHANGE_DIR_NAME);  

	 
	if (ListenersTable[blackIndex].ffHandles[0] != INVALID_HANDLE_VALUE && ListenersTable[blackIndex].ffHandles[1] != INVALID_HANDLE_VALUE)
	{
		////PrintW("I'm monitoring any file, directory deletion/creation in \n");
	}

	while (TRUE) 
	{ 

		dwWaitStatus = WaitForMultipleObjects(2, ListenersTable[blackIndex].ffHandles, FALSE, INFINITE); 

		switch (dwWaitStatus) 
		{ 
			case 0: // WAIT_OBJECT_0
				if (FindNextChangeNotification(ListenersTable[blackIndex].ffHandles[0]) == FALSE) 
					ExitProcess(GetLastError());
				else {
					////PrintW("File created/deleted\n");
					CallBackJava(false);
				}
				break;
			case 1: // WAIT_OBJECT_0 + 1
				if (FindNextChangeNotification(ListenersTable[blackIndex].ffHandles[1]) == FALSE) 
					ExitProcess(GetLastError());
				else {
					////PrintW("Directory created/deleted\n");
					CallBackJava(true);
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

			ListenersTable[x].threadId = -1;
			break;

		}
	}
}


JNIEXPORT void JNICALL Java_FileSystemEventManager_registerFileSystemListener
					(JNIEnv *env, jobject obj, jstring dir, jobject callBack, jint id) {


	javaListener = env->NewGlobalRef(callBack);
	env->GetJavaVM(&javaVM1);


	int blackIndex = -1;

	gDirName = env->GetStringUTFChars(dir,0);
	gid = id;


	CreateThread(NULL, 0, ThreadFunc, &dwThrdParam, 0, &dwThreadId);
	SetThreadPriority(hThreadFlash, THREAD_PRIORITY_LOWEST);


}


