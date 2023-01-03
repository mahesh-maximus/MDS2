
#include <stdio.h>
#include <jni.h>
#include "SystemInfo.h"

#define _WIN32_DCOM
#include <iostream>
using namespace std;
#include <comdef.h>
#include <Wbemidl.h>

# pragma comment(lib, "wbemuuid.lib")

bool registered = false;

IWbemServices *pSvc = NULL;

int GetValues(IWbemServices *);


void registerWMI() {
	printf("register >>>\n");

	    HRESULT hres;
    // Step 1: --------------------------------------------------
    // Initialize COM. ------------------------------------------
	int nproc=1;
    hres =  CoInitializeEx(0, COINIT_MULTITHREADED); 
    if (FAILED(hres))
    {
        cout << "Failed to initialize COM library. Error code = 0x" 
            << hex << hres << endl;
        //return 1;                  // Program has failed.
    }

	//cout << "Please enter the # of processors : ";

	//cin>>nproc;
	ULONG *ulinitVal = new ULONG[(nproc+1)*2];//+1 for the total
	int i=0;
	ULONG *ulVal = new ULONG[(nproc+1)*2];//+1 for the total
    // Step 2: --------------------------------------------------
    // Set general COM security levels --------------------------
    // Note: If you are using Windows 2000, you need to specify -
    // the default authentication credentials for a user by using
    // a SOLE_AUTHENTICATION_LIST structure in the pAuthList ----
    // parameter of CoInitializeSecurity ------------------------

    hres =  CoInitializeSecurity(
        NULL, 
        -1,                          // COM authentication
        NULL,                        // Authentication services
        NULL,                        // Reserved
        RPC_C_AUTHN_LEVEL_DEFAULT,   // Default authentication 
        RPC_C_IMP_LEVEL_IMPERSONATE, // Default Impersonation  
        NULL,                        // Authentication info
        EOAC_NONE,                   // Additional capabilities 
        NULL                         // Reserved
        );

                      
    if (FAILED(hres))
    {
        cout << "Failed to initialize security. Error code = 0x" 
            << hex << hres << endl;
        CoUninitialize();
        //return 1;                    // Program has failed.
    }
    
    // Step 3: ---------------------------------------------------
    // Obtain the initial locator to WMI -------------------------

    IWbemLocator *pLoc = NULL;

    hres = CoCreateInstance(
        CLSID_WbemLocator,             
        0, 
        CLSCTX_INPROC_SERVER, 
        IID_IWbemLocator, (LPVOID *) &pLoc);
 
    if (FAILED(hres))
    {
        cout << "Failed to create IWbemLocator object."
            << " Err code = 0x"
            << hex << hres << endl;
        CoUninitialize();
        //return 1;                 // Program has failed.
    }

    // Step 4: -----------------------------------------------------
    // Connect to WMI through the IWbemLocator::ConnectServer method

    //IWbemServices *pSvc = NULL;
	
    // Connect to the root\cimv2 namespace with
    // the current user and obtain pointer pSvc
    // to make IWbemServices calls.
    hres = pLoc->ConnectServer(
         _bstr_t(L"ROOT\\CIMV2"), // Object path of WMI namespace
         NULL,                    // User name. NULL = current user
         NULL,                    // User password. NULL = current
         0,                       // Locale. NULL indicates current
         NULL,                    // Security flags.
         0,                       // Authority (e.g. Kerberos)
         0,                       // Context object 
         &pSvc                    // pointer to IWbemServices proxy
         );
    
    if (FAILED(hres))
    {
        cout << "Could not connect. Error code = 0x" 
             << hex << hres << endl;
        pLoc->Release();     
        CoUninitialize();
        //return 1;                // Program has failed.
    }

    cout << "Connected to ROOT\\CIMV2 WMI namespace" << endl;


    // Step 5: --------------------------------------------------
    // Set security levels on the proxy -------------------------

    //hres = CoSetProxyBlanket(
    //   pSvc,                        // Indicates the proxy to set
    //   RPC_C_AUTHN_WINNT,           // RPC_C_AUTHN_xxx
    //   RPC_C_AUTHZ_NONE,            // RPC_C_AUTHZ_xxx
    //   NULL,                        // Server principal name 
    //   RPC_C_AUTHN_LEVEL_CALL,      // RPC_C_AUTHN_LEVEL_xxx 
    //   RPC_C_IMP_LEVEL_IMPERSONATE, // RPC_C_IMP_LEVEL_xxx
    //   NULL,                        // client identity
    //   EOAC_NONE                    // proxy capabilities 
    //);

    if (FAILED(hres))
    {
        cout << "Could not set proxy blanket. Error code = 0x" 
            << hex << hres << endl;
        pSvc->Release();
        pLoc->Release();     
        CoUninitialize();
        //return 1;               // Program has failed.
    }
    
	    GetValues(pSvc);
		//Sleep(1000);

	registered = true;
}

int GetValues(IWbemServices *pSvc)
{
	//int nError = 0;
	// // Step 6: --------------------------------------------------
 //   // Use the IWbemServices pointer to make requests of WMI ----

 //   // For example, get the name of the operating system
    IEnumWbemClassObject* pEnumerator = NULL;
 //   HRESULT hres = pSvc->ExecQuery(
 //       bstr_t("WQL"), 
 //       bstr_t("SELECT * FROM Win32_PerfRawData_PerfOS_Processor"),
 //       WBEM_FLAG_FORWARD_ONLY | WBEM_FLAG_RETURN_IMMEDIATELY, 
 //       NULL,
 //       &pEnumerator);
 //   
 //   if (FAILED(hres))
 //   {
 //       cout << "Query for operating system name failed."
 //           << " Error code = 0x" 
 //           << hex << hres << endl;

 //       return 1;               // Fxn has failed.
 //   }

 //   // Step 7: -------------------------------------------------
 //   // Get the data from the query in step 6 -------------------
 //
 //   IWbemClassObject *pclsObj;
 //   ULONG uReturn = 0;
	//int nCtr = 0;
 // 
 //   while (nCtr<n)
 //   {
 //       HRESULT hr = pEnumerator->Next(WBEM_INFINITE, 1, 
 //           &pclsObj, &uReturn);

 //       if(0 == uReturn)
 //       {
 //           break;
 //       }

 //       VARIANT vtProp;
 //       VariantInit(&vtProp);

	//	hr = pclsObj->Get(L"PercentProcessorTime", 0, &vtProp, 0, 0);
	//	ulVal[nCtr] = _wtol(vtProp.bstrVal);
 //       VariantClear(&vtProp);
	//	hr = pclsObj->Get(L"TimeStamp_Sys100NS", 0, &vtProp, 0, 0);
	//	ulVal[nCtr+1] = _wtol(vtProp.bstrVal);
	//	VariantClear(&vtProp);
	//	nCtr+=2;
 //   }
	//pclsObj->Release();
	//pEnumerator->Release();
	//return nError;

		HRESULT hRes;
	BSTR strQuery = (L"Select * from win32_Processor");
	BSTR strQL = (L"WQL");
	hRes = pSvc->ExecQuery(strQL, strQuery,WBEM_FLAG_RETURN_IMMEDIATELY,NULL,&pEnumerator);
	


	if(hRes != S_OK)
	{
		printf("Could not execute Query");
		return 0;
	}

	ULONG uCount = 1, uReturned;
	IWbemClassObject * pClassObject = NULL;


	hRes = pEnumerator->Reset();

	if(hRes != S_OK)
	{
		printf("Could not Enumerate");
		return 0;
	}

	hRes = pEnumerator->Next(WBEM_INFINITE,uCount, &pClassObject, &uReturned);
	if(hRes != S_OK)
	{
		printf("Could not Enumerate");
		return 0;
	}

	VARIANT v;
	BSTR strClassProp = SysAllocString(L"LoadPercentage");
	hRes = pClassObject->Get(strClassProp, 0, &v, 0, 0);
	if(hRes != S_OK)
	{
		printf("Could not Get Value");
		return 0;
	}

	SysFreeString(strClassProp);
	
	_bstr_t bstrPath = &v;  //Just to convert BSTR to ANSI
	char* strPath=(char*)bstrPath;


	if (SUCCEEDED(hRes)){
		printf(strPath);
	    printf("\n");
	}
	else
		printf("Error in getting object");

	VariantClear( &v );
	//pIWbemLocator->Release();
	pEnumerator->Release();
	
//	pWbemServices->Release();
	//pEnumObject->Release();
	//pClassObject->Release();
	pClassObject->Release();
	CoUninitialize();
}

JNIEXPORT void JNICALL Java_SystemInfo_ProcessorLoadPercentage (JNIEnv *, jobject){
	if(!registered) registerWMI(); 
	GetValues(pSvc);
	//printf("Mahesh");
}