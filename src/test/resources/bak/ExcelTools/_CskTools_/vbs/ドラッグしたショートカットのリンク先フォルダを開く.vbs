'�V���[�g�J�b�g��̃t�H���_���J���X�N���v�g
'���̃t�@�C���ɑ΂��A�V���[�g�J�b�g�t�@�C�����h���b�O���邱�Ƃɂ��Ώۃt�H���_���J���܂��B
'SendTo�ɂ��̃X�N���v�g�t�@�C����u���ƕ֗��Ȃ͂��ł��B

Option Explicit

Dim oParam
Dim WSH, LnkFile, LnkFileName
Set oParam = WScript.Arguments
Set WSH = CreateObject("WScript.Shell")

If oParam.length > 0 Then
    ' 1�ڂ̈����̂ݑΏ�
    LnkFileName = oParam(0)
    If InStr(LnkFileName, ".lnk") > 0 Then
        Set LnkFile = WSH.CreateShortcut(LnkFileName)
        WSH.exec("explorer /select, " & Chr(34) & LnkFile.TargetPath & Chr(34))
    Else
        Msgbox "�V���[�g�J�b�g���Ⴀ����܂���"
    End If
Else
    Msgbox "�V���[�g�J�b�g���ǂ�������ĉ�����"
End If
Set oParam = Nothing
Set LnkFile = Nothing
Set WSH = Nothing
