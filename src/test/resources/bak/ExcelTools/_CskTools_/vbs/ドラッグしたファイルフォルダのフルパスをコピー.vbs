'ドラッグしたファイルフォルダのフルパスコピースクリプト
'このファイルに対し、ファイルフォルダをドラッグすることによりフルパスをクリップボードにコピーします。
'SendToにこのスクリプトファイルを置くと便利なはずです。

Option Explicit

Dim oParam
Dim WSH, LnkFile, LnkFileName, IE
Set oParam = WScript.Arguments
Set WSH = CreateObject("WScript.Shell")

If oParam.length > 0 Then
    ' 全てのファイル・フォルダパスを取得
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
    Msgbox "ファイル・フォルダをどらっぐして下さい"
End If
Set oParam = Nothing
Set LnkFile = Nothing
Set WSH = Nothing
Set IE = Nothing
