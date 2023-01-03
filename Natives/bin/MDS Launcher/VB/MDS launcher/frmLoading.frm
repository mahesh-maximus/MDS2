VERSION 5.00
Begin VB.Form frmLoading 
   BackColor       =   &H00000040&
   BorderStyle     =   0  'None
   Caption         =   "MDS1.0_Launcher_Splash_4942452"
   ClientHeight    =   4500
   ClientLeft      =   0
   ClientTop       =   0
   ClientWidth     =   6000
   ControlBox      =   0   'False
   Icon            =   "frmLoading.frx":0000
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MinButton       =   0   'False
   MousePointer    =   11  'Hourglass
   Picture         =   "frmLoading.frx":030A
   ScaleHeight     =   4500
   ScaleWidth      =   6000
   ShowInTaskbar   =   0   'False
   StartUpPosition =   2  'CenterScreen
   Begin VB.Timer Timer1 
      Interval        =   300
      Left            =   2520
      Top             =   1440
   End
End
Attribute VB_Name = "frmLoading"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

Private Sub Form_Initialize()
    If FindWindow("ThunderRT6FormDC", "MDS1.0_Launcher_Splash_4942452") Then
        End
    End If
End Sub

Private Sub Form_Load()
    
    Const DEBUG_MODE = "-debug"
    Dim lR As Long
    Dim mdsPath As String
    Dim jdkPath As String
    Dim jdkToolsPath As String
    Dim mdsLibPath As String
    Dim mdsBinPath As String
    Dim commandline As String
    
    Dim pInfo As PROCESS_INFORMATION
    Dim sInfo As STARTUPINFO
    Dim lSuccess As Long
    Dim sNull As String
         
    
    mdsPath = App.Path
    'jdkPath = mdsPath & "\java.exe "
    
    jdkPath = mdsPath & "\java\jdk1.5.0_06\BIN\java.exe "
    
    jdkToolsPath = mdsPath & "\java\jdk1.5.0_06\LIB\Tools.jar;"
    mdsLibPath = mdsPath & "\lib;"
    mdsBinPath = mdsPath & "\bin;"
    
    If Command = DEBUG_MODE Then
        commandline = " -cp " & QUOTE & mdsPath & ";" & jdkToolsPath & mdsLibPath & mdsBinPath & QUOTE & " MDS_Main " & DEBUG_MODE
    Else
        commandline = " -cp " & QUOTE & mdsPath & ";" & jdkToolsPath & mdsLibPath & mdsBinPath & QUOTE & " MDS_Main "
    End If
    
    MsgBox commandline
    
    MsgBox mdsPath
    
    sInfo.cb = Len(sInfo)
    
    Me.Show
    Me.Refresh
    
    lR = SetTopMostWindow(Me.hwnd, True)
                                 
    lSuccess = CreateProcess(jdkPath, _
                             commandline, _
                             ByVal 0&, _
                             ByVal 0&, _
                             1&, _
                             HIGH_PRIORITY_CLASS, _
                             ByVal 0&, _
                             mdsPath, _
                             sInfo, _
                             pInfo)
                             
     If lSuccess <= 0 Then
        Unload Me
        MsgBox "Unable to launch java Runtime environment", vbCritical, "MDS Launcher"
        End
     End If
                                
     
End Sub

Private Sub Timer1_Timer()
    If Not Me.Visible Then
        End
    End If
    
    Dim hwnd As Long
    If FindWindow("BaseWindow", "MDS 1.0 [Developer's Edition]") Then
        Call SetWindowPos(Me.hwnd, 0, 0, 0, 0, 0, SWP_HIDEWINDOW)
    End If
    
End Sub
