#include <Windows.h>
#include <Vfw.h>
#include <stdio.h>
#include <jni.h>
#include "Mpeglx.h"



int x = 4;
static char xy[] = "Majs";

class s {
public : s() {
			 //this->d();
		 }
	void d() {
					 MCIWndCreate(NULL,NULL,NULL,NULL);
		 printf("MAHESH");	
	}
};

JNIEXPORT void JNICALL Java_Mpeglx_loadMpegLayer3File
(JNIEnv *, jobject){
	x=0;
	printf("xxxxMMMMMMMMMMMMMMMM");
	printf(xy);
	new s();


}



