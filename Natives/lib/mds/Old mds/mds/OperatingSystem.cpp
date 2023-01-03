#include "mds.h"



//extern "C"

void ThrowLastException(JNIEnv*);
LRESULT CALLBACK GetMsgProc(int code, WPARAM wParam, LPARAM lParam);

UINT WindowsKeyToJavaKey(UINT windowsKey);
jint GetJavaModifiers();

HANDLE hStdOut;
HHOOK hhookMsgHook;


JavaVM *javaVM = NULL;
jobject parser;


typedef struct {
    UINT javaKey;
    UINT windowsKey;
} KeyMapEntry;

KeyMapEntry keyMapTable[] = {
	// Modifier keys
	{SystemKeyEvent_VK_CAPS_LOCK, VK_CAPITAL},
	{SystemKeyEvent_VK_SHIFT, VK_SHIFT},
	{SystemKeyEvent_VK_CONTROL, VK_CONTROL},
	{SystemKeyEvent_VK_ALT, VK_MENU},
	{SystemKeyEvent_VK_NUM_LOCK, VK_NUMLOCK},

    // Miscellaneous Windows keys
	{SystemKeyEvent_VK_WINDOWS,          VK_LWIN},
	{SystemKeyEvent_VK_WINDOWS,          VK_RWIN},
	{SystemKeyEvent_VK_CONTEXT_MENU,     VK_APPS},

    // Alphabet
    {SystemKeyEvent_VK_A,                'A'},
    {SystemKeyEvent_VK_B,                'B'},
    {SystemKeyEvent_VK_C,                'C'},
    {SystemKeyEvent_VK_D,                'D'},
    {SystemKeyEvent_VK_E,                'E'},
    {SystemKeyEvent_VK_F,                'F'},
    {SystemKeyEvent_VK_G,                'G'},
    {SystemKeyEvent_VK_H,                'H'},
    {SystemKeyEvent_VK_I,                'I'},
    {SystemKeyEvent_VK_J,                'J'},
    {SystemKeyEvent_VK_K,                'K'},
    {SystemKeyEvent_VK_L,                'L'},
    {SystemKeyEvent_VK_M,                'M'},
    {SystemKeyEvent_VK_N,                'N'},
    {SystemKeyEvent_VK_O,                'O'},
    {SystemKeyEvent_VK_P,                'P'},
    {SystemKeyEvent_VK_Q,                'Q'},
    {SystemKeyEvent_VK_R,                'R'},
    {SystemKeyEvent_VK_S,                'S'},
    {SystemKeyEvent_VK_T,                'T'},
    {SystemKeyEvent_VK_U,                'U'},
    {SystemKeyEvent_VK_V,                'V'},
    {SystemKeyEvent_VK_W,                'W'},
    {SystemKeyEvent_VK_X,                'X'},
    {SystemKeyEvent_VK_Y,                'Y'},
    {SystemKeyEvent_VK_Z,                'Z'},

    // Standard numeric row
    {SystemKeyEvent_VK_0,                '0'},
    {SystemKeyEvent_VK_1,                '1'},
    {SystemKeyEvent_VK_2,                '2'},
    {SystemKeyEvent_VK_3,                '3'},
    {SystemKeyEvent_VK_4,                '4'},
    {SystemKeyEvent_VK_5,                '5'},
    {SystemKeyEvent_VK_6,                '6'},
    {SystemKeyEvent_VK_7,                '7'},
    {SystemKeyEvent_VK_8,                '8'},
    {SystemKeyEvent_VK_9,                '9'},

    // Misc key from main block
    {SystemKeyEvent_VK_ENTER,            VK_RETURN},
    {SystemKeyEvent_VK_SPACE,            VK_SPACE},
    {SystemKeyEvent_VK_BACK_SPACE,       VK_BACK},
    {SystemKeyEvent_VK_TAB,              VK_TAB},
    {SystemKeyEvent_VK_ESCAPE,           VK_ESCAPE},

    // NumPad with NumLock off & extended block (rectangular)
    {SystemKeyEvent_VK_INSERT,           VK_INSERT},
    {SystemKeyEvent_VK_DELETE,           VK_DELETE},
    {SystemKeyEvent_VK_HOME,             VK_HOME},
    {SystemKeyEvent_VK_END,              VK_END},
    {SystemKeyEvent_VK_PAGE_UP,          VK_PRIOR},
    {SystemKeyEvent_VK_PAGE_DOWN,        VK_NEXT},
    {SystemKeyEvent_VK_CLEAR,            VK_CLEAR}, // NumPad 5

    // NumPad with NumLock off & extended arrows block (triangular)
    {SystemKeyEvent_VK_LEFT,             VK_LEFT},
    {SystemKeyEvent_VK_RIGHT,            VK_RIGHT},
    {SystemKeyEvent_VK_UP,               VK_UP},
    {SystemKeyEvent_VK_DOWN,             VK_DOWN},

    // NumPad with NumLock on: numbers
    {SystemKeyEvent_VK_NUMPAD0,          VK_NUMPAD0},
    {SystemKeyEvent_VK_NUMPAD1,          VK_NUMPAD1},
    {SystemKeyEvent_VK_NUMPAD2,          VK_NUMPAD2},
    {SystemKeyEvent_VK_NUMPAD3,          VK_NUMPAD3},
    {SystemKeyEvent_VK_NUMPAD4,          VK_NUMPAD4},
    {SystemKeyEvent_VK_NUMPAD5,          VK_NUMPAD5},
    {SystemKeyEvent_VK_NUMPAD6,          VK_NUMPAD6},
    {SystemKeyEvent_VK_NUMPAD7,          VK_NUMPAD7},
    {SystemKeyEvent_VK_NUMPAD8,          VK_NUMPAD8},
    {SystemKeyEvent_VK_NUMPAD9,          VK_NUMPAD9},

    // NumPad with NumLock on 
    {SystemKeyEvent_VK_MULTIPLY,         VK_MULTIPLY},
    {SystemKeyEvent_VK_ADD,              VK_ADD},
    {SystemKeyEvent_VK_SEPARATOR,        VK_SEPARATOR},
    {SystemKeyEvent_VK_SUBTRACT,         VK_SUBTRACT},
    {SystemKeyEvent_VK_DECIMAL,          VK_DECIMAL},
    {SystemKeyEvent_VK_DIVIDE,           VK_DIVIDE},

    // Functional keys
    {SystemKeyEvent_VK_F1,               VK_F1},
    {SystemKeyEvent_VK_F2,               VK_F2},
    {SystemKeyEvent_VK_F3,               VK_F3},
    {SystemKeyEvent_VK_F4,               VK_F4},
    {SystemKeyEvent_VK_F5,               VK_F5},
    {SystemKeyEvent_VK_F6,               VK_F6},
    {SystemKeyEvent_VK_F7,               VK_F7},
    {SystemKeyEvent_VK_F8,               VK_F8},
    {SystemKeyEvent_VK_F9,               VK_F9},
    {SystemKeyEvent_VK_F10,              VK_F10},
    {SystemKeyEvent_VK_F11,              VK_F11},
    {SystemKeyEvent_VK_F12,              VK_F12},
    {SystemKeyEvent_VK_F13,              VK_F13},
    {SystemKeyEvent_VK_F14,              VK_F14},
    {SystemKeyEvent_VK_F15,              VK_F15},
    {SystemKeyEvent_VK_F16,              VK_F16},
    {SystemKeyEvent_VK_F17,              VK_F17},
    {SystemKeyEvent_VK_F18,              VK_F18},
    {SystemKeyEvent_VK_F19,              VK_F19},
    {SystemKeyEvent_VK_F20,              VK_F20},
    {SystemKeyEvent_VK_F21,              VK_F21},
    {SystemKeyEvent_VK_F22,              VK_F22},
    {SystemKeyEvent_VK_F23,              VK_F23},
    {SystemKeyEvent_VK_F24,              VK_F24},

    {SystemKeyEvent_VK_PRINTSCREEN,      VK_SNAPSHOT},
    {SystemKeyEvent_VK_SCROLL_LOCK,      VK_SCROLL},
    {SystemKeyEvent_VK_PAUSE,            VK_PAUSE},
    {SystemKeyEvent_VK_CANCEL,           VK_CANCEL},
    {SystemKeyEvent_VK_HELP,             VK_HELP},

    // Japanese
    {SystemKeyEvent_VK_CONVERT,          VK_CONVERT},
    {SystemKeyEvent_VK_NONCONVERT,       VK_NONCONVERT},
    {SystemKeyEvent_VK_INPUT_METHOD_ON_OFF, VK_KANJI},
    //{SystemKeyEvent_VK_ALPHANUMERIC,     VK_DBE_ALPHANUMERIC},
    //{SystemKeyEvent_VK_KATAKANA,         VK_DBE_KATAKANA},
    //{SystemKeyEvent_VK_HIRAGANA,         VK_DBE_HIRAGANA},
    //{SystemKeyEvent_VK_FULL_WIDTH,       VK_DBE_DBCSCHAR},
    //{SystemKeyEvent_VK_HALF_WIDTH,       VK_DBE_SBCSCHAR},
    //{SystemKeyEvent_VK_ROMAN_CHARACTERS, VK_DBE_ROMAN},


	{SystemKeyEvent_VK_UNDEFINED,        0}

};



void ThrowLastException(JNIEnv* env) {
    LPVOID lpMsgBuf;
    FormatMessage(FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
                  NULL, GetLastError(), MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR) &lpMsgBuf, 0,
                  NULL );
    //env->ThrowNew(env->FindClass("java/lang/RuntimeException"), (LPCTSTR)lpMsgBuf);
    env->ThrowNew(env->FindClass("NativeLibrayException"), (LPCTSTR)lpMsgBuf);
	//JNIEnv *env = (JNIEnv *)JNU_GetEnv(jvm, JNI_VERSION_1_2);
}


JNIEXPORT void JNICALL Java_OperatingSystem_createMutex(JNIEnv *env, jobject,  jstring jName) {
    const char* name=env->GetStringUTFChars(jName,0);
    LPSTR mName = (LPSTR)name;
    CreateMutex(NULL, NULL, mName);
    if(GetLastError() == ERROR_ALREADY_EXISTS) {
        env->ThrowNew(env->FindClass("MutexAlreadyExistsException"), name);
	} else if (GetLastError() != ERROR_SUCCESS ){
		ThrowLastException(env);
	}
}


JNIEXPORT jint JNICALL Java_OperatingSystem_getDriveType (JNIEnv* env, jobject, jstring jDrive) {

    const char* msg=env->GetStringUTFChars(jDrive,0);

    long storeDriveType=0;

    switch (GetDriveType(msg)) {
        case DRIVE_UNKNOWN:
            storeDriveType=10;
            break;
        case DRIVE_NO_ROOT_DIR:
            storeDriveType=20;
            break;
        case DRIVE_REMOVABLE:
            storeDriveType=30;
            break;
        case DRIVE_FIXED:
            storeDriveType=40;
            break;
        case DRIVE_REMOTE:
            storeDriveType=50;
            break;
        case DRIVE_CDROM:
            storeDriveType=60;
            break;
        case DRIVE_RAMDISK:
            storeDriveType=70;
            break;
        default:
            storeDriveType=80;
            break;
    }

    env->ReleaseStringUTFChars(jDrive, msg);

    return storeDriveType;

}


JNIEXPORT jstring JNICALL Java_OperatingSystem_getVolumeName (JNIEnv* env, jobject, jstring jDrive) {

    const char* msg=env->GetStringUTFChars(jDrive,0);

    jstring jstrDriveVolumeLabel;

    char  szVolName[MAX_PATH];
    char  szFileSysName[80];
    DWORD dwSerialNumber;
    DWORD dwMaxComponentLen;
    DWORD dwFileSysFlags;

    if(GetVolumeInformation(msg, szVolName, MAX_PATH, &dwSerialNumber, &dwMaxComponentLen, &dwFileSysFlags, szFileSysName, 80) == FALSE) {
        jstrDriveVolumeLabel = env->NewStringUTF("");
    } else {
        jstrDriveVolumeLabel = env->NewStringUTF(szVolName);
    }

    env->ReleaseStringUTFChars(jDrive, msg);

    return jstrDriveVolumeLabel;

}


JNIEXPORT jstring JNICALL Java_OperatingSystem_getFileSystemName (JNIEnv* env, jobject, jstring jDrive) {

    const char* msg=env->GetStringUTFChars(jDrive,0);

    jstring jstrDriveFileSystem;

    char  szVolName[MAX_PATH];
    char  szFileSysName[80];
    DWORD dwSerialNumber;
    DWORD dwMaxComponentLen;
    DWORD dwFileSysFlags;

    if(GetVolumeInformation(msg, szVolName, MAX_PATH, &dwSerialNumber, &dwMaxComponentLen, &dwFileSysFlags, szFileSysName, 80) == FALSE) {
        jstrDriveFileSystem = env->NewStringUTF("");
    } else {
        jstrDriveFileSystem = env->NewStringUTF(szFileSysName);
    }

    env->ReleaseStringUTFChars(jDrive, msg);

    return jstrDriveFileSystem;

}


JNIEXPORT void JNICALL Java_OperatingSystem_setDriveVolumeLabel(JNIEnv* env, jobject, jstring jDrive, jstring jLabel) {
    const char* drive=env->GetStringUTFChars(jDrive,0);
    const char* label=env->GetStringUTFChars(jLabel,0);
    if(SetVolumeLabel(drive, label) == FALSE) {
        ThrowLastException(env);
    }
}


JNIEXPORT jboolean JNICALL Java_OperatingSystem_setHighPriorityClass(JNIEnv *, jobject){
	printf("Java_MDS_setHighPriorityClass");
	return SetPriorityClass(GetCurrentProcess(), HIGH_PRIORITY_CLASS);
}


JNIEXPORT jlong JNICALL Java_OperatingSystem_createProcess__Ljava_lang_String_2(JNIEnv *env,jobject,  jstring jName){
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


JNIEXPORT jlong JNICALL Java_OperatingSystem_createProcess__Ljava_lang_String_2Ljava_lang_String_2ZLjava_lang_String_2(JNIEnv *env,jobject,  jstring japplicationName, jstring jcommandLine, jboolean inheritHandles, jstring jcurrentDirectory){
    const char* appName=env->GetStringUTFChars(japplicationName,0);
	const char* cmdLine=env->GetStringUTFChars(jcommandLine,0);
	const char* curDir=env->GetStringUTFChars(jcurrentDirectory,0);
    STARTUPINFO si;
    PROCESS_INFORMATION pi;
    LPSTR applicationName = (LPSTR)appName;
	LPSTR commandLine = (LPSTR)cmdLine;
	LPSTR currentDirectory = (LPSTR)curDir;
    ZeroMemory( &si, sizeof(si) );
    si.cb = sizeof(si);
    ZeroMemory( &pi, sizeof(pi) );


    if(!CreateProcess( applicationName, commandLine, NULL, NULL, inheritHandles, 0, NULL, currentDirectory, &si, &pi)) {
        //MessageBox (GetFocus(), "CreateProcess failed.", "###", MB_OK|MB_ICONHAND);
    }

    return (unsigned long)pi.hProcess;

}


JNIEXPORT jboolean JNICALL Java_OperatingSystem_terminateProcess(JNIEnv *env, jobject,  jlong jId){

    bool t = false;

    if(TerminateProcess((PVOID)((ULONG)jId), 0)) {
        t = true;
    }

    return t;

}


JNIEXPORT jboolean JNICALL Java_OperatingSystem_allocateConsole(JNIEnv *env, jobject){

    bool t = false;
	t = AllocConsole();
	hStdOut = GetStdHandle(STD_OUTPUT_HANDLE);
    return t;

}


JNIEXPORT void JNICALL Java_OperatingSystem_showWindow(JNIEnv *env,jobject, jstring jClazz, jstring jCaption){
    const char* clazz=env->GetStringUTFChars(jClazz,0);
    const char* caption=env->GetStringUTFChars(jCaption,0);

    HWND wnd = FindWindow(clazz, caption);
    ShowWindow(wnd, SW_RESTORE);
    env->ReleaseStringUTFChars(jClazz, clazz);
    env->ReleaseStringUTFChars(jCaption, caption);
}


JNIEXPORT void JNICALL Java_OperatingSystem_setWindowsHookForBaseWindow (JNIEnv *env, jobject obj) {
	


	parser = env->NewGlobalRef(obj);
	env->GetJavaVM(&javaVM);

//=======================================================
	DWORD tid;
	HWND hwndBaseWindow = FindWindow("SunAwtFrame", "MDS");
	if(hwndBaseWindow == NULL)
		ThrowLastException(env);

	tid = GetWindowThreadProcessId(hwndBaseWindow, NULL);
	if(tid == NULL)
		ThrowLastException(env);

	hhookMsgHook = SetWindowsHookEx(WH_GETMESSAGE, GetMsgProc, NULL, tid);
	if(hhookMsgHook == NULL)
		ThrowLastException(env);


	PrintW("SetWindowsHookEx\n");
	
}

JNIEXPORT void JNICALL Java_OperatingSystem_unHookWindowsHookForBaseWindow (JNIEnv *, jobject) {
	if(hhookMsgHook != NULL)
		UnhookWindowsHookEx(hhookMsgHook);
}

void FireEvent(LPSTR mName, LPSTR mParaSig) {
	JNIEnv *java;
	if(javaVM->AttachCurrentThread((void **)&java, NULL) >= 0)
	{
		jclass cls = (java)->GetObjectClass(parser);
		jmethodID mid = (java)->GetMethodID(cls, mName, mParaSig);
		if(mid != NULL) {
			(java)->CallVoidMethod(parser, mid, 1);
		} else {
			PrintW("fireWindowMessageEvent NULL\n");	
		}
	}
}

void WmMouseDown(int x, int y, int button) {
	JNIEnv *java;
	if(javaVM->AttachCurrentThread((void **)&java, NULL) >= 0)
	{
		jclass cls = (java)->GetObjectClass(parser);
		jmethodID mid = (java)->GetMethodID(cls, "fireSystemMousePressedEvent", "(IIII)V");
		if(mid != NULL) {
			(java)->CallVoidMethod(parser, mid, x, y, button, GetJavaModifiers());
		} 
	}	
}

void WmMouseUp(int x, int y, int button) {
	JNIEnv *java;
	if(javaVM->AttachCurrentThread((void **)&java, NULL) >= 0)
	{
		jclass cls = (java)->GetObjectClass(parser);
		jmethodID mid = (java)->GetMethodID(cls, "fireSystemMouseReleasedEvent", "(IIII)V");
		if(mid != NULL) {
			(java)->CallVoidMethod(parser, mid, x, y, button, GetJavaModifiers());
		} 
	}	
}

void WmMouseWheel(int x, int y, int wheelRotation) {
	JNIEnv *java;
	if(javaVM->AttachCurrentThread((void **)&java, NULL) >= 0)
	{
		jclass cls = (java)->GetObjectClass(parser);
		jmethodID mid = (java)->GetMethodID(cls, "fireSystemMouseWheelMovedEvent", "(IIII)V");
		if(mid != NULL) {
			(java)->CallVoidMethod(parser, mid, x, y, wheelRotation, GetJavaModifiers());
		} 
	}	
}

void WmKeyDown(UINT keyCode) {
	JNIEnv *java;
	if(javaVM->AttachCurrentThread((void **)&java, NULL) >= 0)
	{
		jclass cls = (java)->GetObjectClass(parser);
		jmethodID mid = (java)->GetMethodID(cls, "fireSystemKeyPressedEvent", "(II)V");
		if(mid != NULL) {
			(java)->CallVoidMethod(parser, mid, WindowsKeyToJavaKey(keyCode), GetJavaModifiers());	
		} 
	}	
}

void WmKeyUp(UINT keyCode) {
	JNIEnv *java;
	if(javaVM->AttachCurrentThread((void **)&java, NULL) >= 0)
	{
		jclass cls = (java)->GetObjectClass(parser);
		jmethodID mid = (java)->GetMethodID(cls, "fireSystemKeyReleasedEvent", "(II)V");
		if(mid != NULL) {
			(java)->CallVoidMethod(parser, mid, WindowsKeyToJavaKey(keyCode), GetJavaModifiers());	
		} 
	}	
}

UINT WindowsKeyToJavaKey(UINT windowsKey) {
	for (int i = 0; keyMapTable[i].windowsKey != 0; i++) {
        if (keyMapTable[i].windowsKey == windowsKey) {
            return keyMapTable[i].javaKey;
        }
    }
	return SystemKeyEvent_VK_UNDEFINED;
}


jint GetJavaModifiers()
{
    jint modifiers = 0;

    if (HIBYTE(::GetKeyState(VK_CONTROL)) != 0) {
        modifiers |= SystemKeyEvent_CTRL_DOWN_MASK;
    }
    if (HIBYTE(::GetKeyState(VK_SHIFT)) != 0) {
        modifiers |= SystemKeyEvent_SHIFT_DOWN_MASK;
    }
    if (HIBYTE(::GetKeyState(VK_MENU)) != 0) {
        modifiers |= SystemKeyEvent_ALT_DOWN_MASK;
    } 
    if (HIBYTE(::GetKeyState(VK_MBUTTON)) != 0) {
        modifiers |= SystemKeyEvent_BUTTON2_DOWN_MASK;
    }
    if (HIBYTE(::GetKeyState(VK_RBUTTON)) != 0) {
        modifiers |= SystemKeyEvent_BUTTON3_DOWN_MASK;
    }
    if (HIBYTE(::GetKeyState(VK_LBUTTON)) != 0) {
        modifiers |= SystemKeyEvent_BUTTON1_DOWN_MASK;
    }
    return modifiers;
}



LRESULT CALLBACK GetMsgProc(int code, WPARAM wParam, LPARAM lParam) {
	
	if (code < 0) // do not process message 
        return CallNextHookEx(hhookMsgHook, code, wParam, lParam); 

	if (code == HC_ACTION) {
		
		MSG* pMsg = (MSG*)lParam;

		DWORD curPos;
		POINT myPos;

		curPos = GetMessagePos();
		myPos.x = GET_X_LPARAM(curPos);
		myPos.y = GET_Y_LPARAM(curPos);

		switch(pMsg->message) {
			case WM_KEYDOWN:
				WmKeyDown(static_cast<UINT>(pMsg->wParam));
				//PrintW("WM_KEYDOWN\n");
				break;
			case WM_KEYUP:
				WmKeyUp(static_cast<UINT>(pMsg->wParam));
				//PrintW("WM_KEYUP\n");
				break;
			case WM_LBUTTONDOWN:
				WmMouseDown(myPos.x, myPos.y, SystemMouseEvent_BUTTON1); 
				//PrintW("WM_LBUTTONDOWN\n");
				break;
			case WM_LBUTTONUP:
				WmMouseUp(myPos.x, myPos.y, SystemMouseEvent_BUTTON1); 
				//PrintW("WM_LBUTTONDOWN\n");
				break;

			case WM_RBUTTONDOWN:
				WmMouseDown(myPos.x, myPos.y, SystemMouseEvent_BUTTON3); 
				//PrintW("WM_RBUTTONDOWN\n");
				break;
			case WM_RBUTTONUP:
				WmMouseUp(myPos.x, myPos.y, SystemMouseEvent_BUTTON3); 
				//PrintW("WM_RBUTTONUP\n");
				break;

			case WM_MOUSEWHEEL:
				WmMouseWheel(GET_X_LPARAM(pMsg->lParam), GET_Y_LPARAM(pMsg->lParam), (int)pMsg->wParam);
				break;


		}
	}

	return CallNextHookEx(hhookMsgHook, code, wParam, lParam);
}
