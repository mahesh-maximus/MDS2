#include <windows.h>
#include <winioctl.h>
#include <tchar.h>
#include <stdio.h>
#include "mds.h"

// Prototypes
BOOL EjectVolume(TCHAR cDriveLetter);

HANDLE OpenVolume(TCHAR cDriveLetter);
BOOL LockVolume(HANDLE hVolume);
BOOL DismountVolume(HANDLE hVolume);
BOOL PreventRemovalOfVolume(HANDLE hVolume, BOOL fPrevent);
BOOL AutoEjectVolume(HANDLE hVolume);
BOOL CloseVolume(HANDLE hVolume);
