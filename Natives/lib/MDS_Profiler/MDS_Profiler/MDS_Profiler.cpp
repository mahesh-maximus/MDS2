#include <stdio.h>
#include <string.h>
#include <jni.h>
#include <jvmti.h>

//Global static data 
static jvmtiEnv     *jvmti;



static void JNICALL
class_Load(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jthread thread,
			jclass klass)
{}

void JNICALL
Exception_throw(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jthread thread,
            jmethodID method,
            jlocation location,
            jobject exception,
            jmethodID catch_method,
            jlocation catch_location)
{
	fprintf(stdout, "eeeeeeeeeeeeee\n");
}



static void JNICALL
Exception_Catch(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jthread thread,
            jmethodID method,
            jlocation location,
            jobject exception)
{
	fprintf(stdout, "ExceptionCatch\n");
}



void JNICALL
Garbage_Collection_Start(jvmtiEnv *jvmti_env) 
{
	fprintf(stdout, "GarbageCollectionStart\n");
}



void JNICALL
Thread_Start(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jthread thread)
{

	////fprintf(stdout, "ThreadStart\n");

	jclass class_p = envr->FindClass("MDS_Profiler");
	jmethodID mid = envr->GetStaticMethodID(class_p, "fireThreadStart","(Ljava/lang/Thread;)V");
	jclass st = envr->FindClass("java/lang/Class");
	envr->CallStaticVoidMethod(class_p,mid, thread);

}



void JNICALL
Thread_End(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jthread thread)
{
	//jvmti_env->ForceGarbageCollection();
	fprintf(stdout, "ThreadEnd\n");
}



/* Agent_OnLoad() is called first, we prepare for a VM_INIT event here. */
JNIEXPORT jint JNICALL
Agent_OnLoad(JavaVM *vm, char *options, void *reserved)
{
    jint                rc;
    jvmtiError          err;
    jvmtiCapabilities   capabilities;
    jvmtiEventCallbacks callbacks;

	fprintf(stdout, "Agent_OnLoad \n");
    
    /* Get JVMTI environment */
    rc = vm->GetEnv( (void **)&jvmti, JVMTI_VERSION);
    if (rc != JNI_OK) {
	fprintf(stderr, "ERROR: Unable to create jvmtiEnv, GetEnv failed, error=%d\n", rc);
	return -1;
    }

	capabilities.can_get_source_file_name  = 1;

	memset(&callbacks, 0, sizeof(callbacks));

	callbacks.ClassLoad = &class_Load;
	callbacks.Exception = &Exception_throw;
	callbacks.ExceptionCatch = &Exception_Catch;
	callbacks.GarbageCollectionStart = &Garbage_Collection_Start;
	callbacks.ThreadStart = &Thread_Start;
	callbacks.ThreadEnd = &Thread_End;

	jvmti->SetEventCallbacks(&callbacks, sizeof(callbacks));

	jvmti->SetEventNotificationMode(JVMTI_ENABLE, 
        JVMTI_EVENT_CLASS_LOAD, NULL);
	jvmti->SetEventNotificationMode(JVMTI_ENABLE, 
        JVMTI_EVENT_EXCEPTION, NULL);
	jvmti->SetEventNotificationMode(JVMTI_ENABLE, 
        JVMTI_EVENT_EXCEPTION_CATCH, NULL);
	jvmti->SetEventNotificationMode(JVMTI_ENABLE, 
        JVMTI_EVENT_GARBAGE_COLLECTION_START, NULL);
	jvmti->SetEventNotificationMode(JVMTI_ENABLE, 
        JVMTI_EVENT_THREAD_START, NULL);
	jvmti->SetEventNotificationMode(JVMTI_ENABLE, 
        JVMTI_EVENT_THREAD_END, NULL);


    return 0;
}