'�h���b�O�����t�@�C���t�H���_�̃t���p�X�R�s�[�X�N���v�g
'���̃t�@�C���ɑ΂��A�t�@�C���t�H���_���h���b�O���邱�Ƃɂ��t���p�X���N���b�v�{�[�h�ɃR�s�[���܂��B
'SendTo�ɂ��̃X�N���v�g�t�@�C����u���ƕ֗��Ȃ͂��ł��B

Option Explicit

Dim oParam
Dim WSH, LnkFile, LnkFileName, IE
Set oParam = WScript.Arguments
Set WSH = CreateObject("WScript.Shell")

If oParam.length > 0 Then
    ' �S�Ẵt�@�C���E�t�H���_�p�X���擾
    Dim arg, copyStr
    For Each arg In oParam
        copyStr = copyStr & arg  & vbCrLf
    Next
    Set IE = CreateObject("InternetExplorer.Application")
    IE.Navigate "about:blank"
    Do Until IE.ReadyState=4
     WScript.Sleep 1
    Loop
    IE.Document.ParentWindow.ClipboardData.SetData "Text", copyStr
    IE.Quit
Else
    Msgbox "�t�@�C���E�t�H���_���ǂ�������ĉ�����"
End If
Set oParam = Nothing
Set LnkFile = Nothing
Set WSH = Nothing
Set IE = Nothing
