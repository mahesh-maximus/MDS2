VERSION 5.00
Begin VB.Form Form1 
   Caption         =   "Form1"
   ClientHeight    =   1710
   ClientLeft      =   60
   ClientTop       =   345
   ClientWidth     =   5070
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   ScaleHeight     =   1710
   ScaleWidth      =   5070
   StartUpPosition =   3  'Windows Default
   Begin VB.CommandButton Command1 
      Caption         =   "Terminate"
      Height          =   375
      Left            =   2520
      TabIndex        =   1
      Top             =   720
      Width           =   2295
   End
   Begin VB.CommandButton cmdCreate 
      Caption         =   "Create Process"
      Height          =   375
      Left            =   240
      TabIndex        =   0
      Top             =   720
      Width           =   1695
   End
End
Attribute VB_Name = "Form1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

      Private Type PROCESS_INFORMATION
         hProcess As Long
         hThread As Long
         dwProcessId As Long
         dwThreadId As Long
      End Type

      Private Type STARTUPINFO
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

      Private Declare Function CreateProcess Lib "kernel32" _
         Alias "CreateProcessA" _
         (ByVal lpApplicationName As String, _
         ByVal lpCommandLine As String, _
         lpProcessAttributes As Any, _
         lpThreadAttributes As Any, _
         ByVal bInheritHandles As Long, _
         ByVal dwCreationFlags As Long, _
         lpEnvironment As Any, _
         ByVal lpCurrentDriectory As String, _
         lpStartupInfo As STARTUPINFO, _
         lpProcessInformation As PROCESS_INFORMATION) As Long

      Private Declare Function OpenProcess Lib "kernel32.dll" _
         (ByVal dwAccess As Long, _
         ByVal fInherit As Integer, _
         ByVal hObject As Long) As Long

      Private Declare Function TerminateProcess Lib "kernel32" _
         (ByVal hProcess As Long, _
         ByVal uExitCode As Long) As Long

      Private Declare Function CloseHandle Lib "kernel32" _
         (ByVal hObject As Long) As Long

      Const SYNCHRONIZE = 1048576
      Const NORMAL_PRIORITY_CLASS = &H20&
         Private pInfo As PROCESS_INFORMATION
         Private sInfo As STARTUPINFO
Private Sub cmdCreate_Click()
         'Dim pInfo As PROCESS_INFORMATION
         'Dim sInfo As STARTUPINFO
         Dim sNull As String
         Dim lSuccess As Long
         'Dim lRetValue As Long

         sInfo.cb = Len(sInfo)
         lSuccess = CreateProcess(sNull, _
                                 "FexMnt.exe", _
                                 ByVal 0&, _
                                 ByVal 0&, _
                                 1&, _
                                 NORMAL_PRIORITY_CLASS, _
                                 ByVal 0&, _
                                 sNull, _
                                 sInfo, _
                                 pInfo)

         MsgBox pInfo.hProcess

         'lRetValue = TerminateProcess(pInfo.hProcess, 0&)
         'lRetValue = CloseHandle(pInfo.hThread)
         'lRetValue = CloseHandle(pInfo.hProcess)

         'MsgBox "Calculator has terminated!"
End Sub

Private Sub Command1_Click()
         Dim lRetValue As Long
         lRetValue = TerminateProcess(pInfo.hProcess, 0&)
         lRetValue = CloseHandle(pInfo.hThread)
         lRetValue = CloseHandle(pInfo.hProcess)

         MsgBox "Calculator has terminated!"
         
         End
End Sub
