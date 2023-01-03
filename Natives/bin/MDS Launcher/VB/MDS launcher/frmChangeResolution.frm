VERSION 5.00
Begin VB.Form frmChangeResolution 
   BorderStyle     =   1  'Fixed Single
   Caption         =   "MDS Launcher"
   ClientHeight    =   1890
   ClientLeft      =   45
   ClientTop       =   330
   ClientWidth     =   7350
   Icon            =   "frmChangeResolution.frx":0000
   LinkTopic       =   "Form1"
   MaxButton       =   0   'False
   MinButton       =   0   'False
   ScaleHeight     =   1890
   ScaleWidth      =   7350
   StartUpPosition =   2  'CenterScreen
   Begin VB.CommandButton cmdChange 
      Caption         =   "Change"
      Default         =   -1  'True
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   375
      Left            =   2280
      TabIndex        =   4
      Top             =   1200
      Width           =   1215
   End
   Begin VB.CommandButton cmdCancel 
      Cancel          =   -1  'True
      Caption         =   "Cancel"
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   375
      Left            =   3720
      TabIndex        =   3
      Top             =   1200
      Width           =   1215
   End
   Begin VB.PictureBox Picture1 
      BorderStyle     =   0  'None
      Height          =   615
      Left            =   240
      Picture         =   "frmChangeResolution.frx":0442
      ScaleHeight     =   615
      ScaleWidth      =   615
      TabIndex        =   1
      Top             =   240
      Width           =   615
   End
   Begin VB.Label Label2 
      Caption         =   "Your display resolution should be changed to 800 by 600."
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   1080
      TabIndex        =   2
      Top             =   600
      Width           =   6135
   End
   Begin VB.Label Label1 
      Caption         =   "MDS does not support your current display resolution."
      BeginProperty Font 
         Name            =   "MS Sans Serif"
         Size            =   9.75
         Charset         =   0
         Weight          =   700
         Underline       =   0   'False
         Italic          =   0   'False
         Strikethrough   =   0   'False
      EndProperty
      Height          =   255
      Left            =   1080
      TabIndex        =   0
      Top             =   240
      Width           =   5655
   End
End
Attribute VB_Name = "frmChangeResolution"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Option Explicit

Private Sub cmdCancel_Click()
    End
End Sub

Private Sub cmdChange_Click()
    Dim dblreturn As Long
    dblreturn = Shell("rundll32.exe shell32.dll,Control_RunDLL desk.cpl,,0", 5)
    End
End Sub

Private Sub Form_Unload(Cancel As Integer)
    End
End Sub

