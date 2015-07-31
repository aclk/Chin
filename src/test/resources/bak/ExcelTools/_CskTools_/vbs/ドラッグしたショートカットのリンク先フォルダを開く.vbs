'ショートカット先のフォルダを開くスクリプト
'このファイルに対し、ショートカットファイルをドラッグすることにより対象フォルダを開きます。
'SendToにこのスクリプトファイルを置くと便利なはずです。

Option Explicit

Dim oParam
Dim WSH, LnkFile, LnkFileName
Set oParam = WScript.Arguments
Set WSH = CreateObject("WScript.Shell")

If oParam.length > 0 Then
    ' 1つ目の引数のみ対象
    LnkFileName = oParam(0)
    If InStr(LnkFileName, ".lnk") > 0 Then
        Set LnkFile = WSH.CreateShortcut(LnkFileName)
        WSH.exec("explorer /select, " & Chr(34) & LnkFile.TargetPath & Chr(34))
    Else
        Msgbox "ショートカットじゃあありません"
    End If
Else
    Msgbox "ショートカットをどらっぐして下さい"
End If
Set oParam = Nothing
Set LnkFile = Nothing
Set WSH = Nothing
