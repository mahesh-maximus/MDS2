VERSION 5.00
Begin VB.Form Form1 
   Caption         =   "Form1"
   ClientHeight    =   3195
   ClientLeft      =   60
   ClientTop       =   345
   ClientWidth     =   4680
   LinkTopic       =   "Form1"
   ScaleHeight     =   3195
   ScaleWidth      =   4680
   StartUpPosition =   3  'Windows Default
End
Attribute VB_Name = "Form1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit
Private Declare Function FindWindow Lib "user32" Alias "FindWindowA" (ByVal lpClassName As String, ByVal lpWindowName As String) As Long
Private Declare Function ShowWindow Lib "user32" (ByVal hwnd As Long, ByVal nCmdShow As Long) As Long
Private Declare Function SetActiveWindow Lib "user32" (ByVal hwnd As Long) As Long

Const SW_SHOWNORMAL = 1

Private Sub Form_Load()
Dim r As Long
Dim r2 As Long
r = FindWindow("w123", "Mahesh")
'MsgBox r
r2 = ShowWindow(r, SW_SHOWNORMAL)
'r2 = SetActiveWindow(r)

'MsgBox r2
End
End Sub
