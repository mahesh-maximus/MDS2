Attribute VB_Name = "modMain"
Public Declare Function FindWindow Lib "user32" Alias "FindWindowA" (ByVal lpClassName As String, ByVal lpWindowName As String) As Long
Public Declare Function DestroyWindow Lib "user32" (ByVal hwnd As Long) As Long
Public Declare Function SetWindowPos Lib "user32" (ByVal hwnd As Long, _
    ByVal hWndInsertAfter As Long, ByVal X As Long, ByVal Y As Long, _
    ByVal cx As Long, ByVal cy As Long, ByVal wFlags As Long) As Long
    
Private Const HWND_TOPMOST = -1
Private Const SWP_NOACTIVATE = &H10
Private Const SWP_SHOWWINDOW = &H40
Private Const SWP_HIDEWINDOW = &H80
Private Const SWP_NOZORDER = &H4
Private Const SWP_NOMOVE = &H2
Private Const SWP_NOREPOSITION = &H200
Private Const SWP_NOSIZE = &H1

Sub Main()
    Dim hwnd As Long
    hwnd = FindWindow("ThunderRT6FormDC", "MDS1.0_Launcher_Splash_4942452")
    Call SetWindowPos(hwnd, 0, 0, 0, 0, 0, SWP_HIDEWINDOW)
    'DestroyWindow hwnd
    End
End Sub
