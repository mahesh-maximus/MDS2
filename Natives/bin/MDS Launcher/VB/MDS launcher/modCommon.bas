Attribute VB_Name = "modCommon"
Option Explicit

Global Const QUOTE = """"
      
'Public Const SWP_NOMOVE = 2
'Public Const SWP_NOSIZE = 1

'Public Const HWND_TOPMOST = -1
Public Const HWND_NOTOPMOST = -2

Public Const HWND_TOPMOST = -1
Public Const SWP_NOACTIVATE = &H10
Public Const SWP_SHOWWINDOW = &H40
Public Const SWP_HIDEWINDOW = &H80
Public Const SWP_NOZORDER = &H4
Public Const SWP_NOMOVE = &H2
Public Const SWP_NOREPOSITION = &H200
Public Const SWP_NOSIZE = &H1

Public Const FLAGS = SWP_NOMOVE Or SWP_NOSIZE

Public Const NORMAL_PRIORITY_CLASS = &H20&
Public Const HIGH_PRIORITY_CLASS = &H80


Public Const ERROR_ALREADY_EXISTS = 183&
 


Public Type PROCESS_INFORMATION
         hProcess As Long
         hThread As Long
         dwProcessId As Long
         dwThreadId As Long
End Type
      
Public Type STARTUPINFO
         cb As Long
         lpReserved As String
         lpDesktop As String
         lpTitle As String
         dwX As Long
         dwY As Long
         dwXSize As Long
         dwYSize As Long
         dwXCountChars As Long
         dwYCountChars As Long
         dwFillAttribute As Long
         dwFlags As Long
         wShowWindow As Integer
         cbReserved2 As Integer
         lpReserved2 As Long
         hStdInput As Long
         hStdOutput As Long
         hStdError As Long
End Type

Public Type SECURITY_ATTRIBUTES
        nLength As Long
        lpSecurityDescriptor As Long
        bInheritHandle As Long
End Type

            

Public Declare Function CreateProcess Lib "kernel32" Alias "CreateProcessA" (ByVal lpApplicationName As String, _
                                                                             ByVal lpCommandLine As String, _
                                                                             lpProcessAttributes As Any, _
                                                                             lpThreadAttributes As Any, _
                                                                             ByVal bInheritHandles As Long, _
                                                                             ByVal dwCreationFlags As Long, _
                                                                             lpEnvironment As Any, _
                                                                             ByVal lpCurrentDriectory As String, _
                                                                             lpStartupInfo As STARTUPINFO, _
                                                                             lpProcessInformation As PROCESS_INFORMATION) As Long

Public Declare Function SetWindowPos Lib "user32" (ByVal hwnd As Long, ByVal hWndInsertAfter As Long, ByVal x As Long, ByVal y As Long, ByVal cx As Long, ByVal cy As Long, ByVal wFlags As Long) As Long
Public Declare Function FindWindow Lib "user32" Alias "FindWindowA" (ByVal lpClassName As String, ByVal lpWindowName As String) As Long
Public Declare Function DestroyWindow Lib "user32" (ByVal hwnd As Long) As Long
Public Declare Function CreateMutex Lib "kernel32" Alias "CreateMutexA" (lpMutexAttributes As SECURITY_ATTRIBUTES, ByVal bInitialOwner As Long, ByVal lpName As String) As Long
Public Declare Function GetLastError Lib "kernel32" () As Long

             
            

Public Function SetTopMostWindow(hwnd As Long, Topmost As Boolean) As Long

    If Topmost = True Then 'Make the window topmost
        SetTopMostWindow = SetWindowPos(hwnd, HWND_TOPMOST, 0, 0, 0, 0, FLAGS)
    Else
        SetTopMostWindow = SetWindowPos(hwnd, HWND_NOTOPMOST, 0, 0, 0, 0, FLAGS)
        SetTopMostWindow = False
    End If
End Function

