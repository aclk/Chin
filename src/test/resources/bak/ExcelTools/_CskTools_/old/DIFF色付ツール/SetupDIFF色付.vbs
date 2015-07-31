'################################################################################
'#
'# Function  :  指定したEXCELアドインを登録する
'#
'# 2006/10/01 Y.Imura     Initial release
'#
'################################################################################

Const sTitle = "EXCELアドイン登録処理"

Set objShell = WScript.CreateObject("WScript.Shell")
'Set objNetwork = WScript.CreateObject("WScript.Network")
'Set objEnv = objShell.Environment("PROCESS")

call main()


Function main()

	Dim FS, XL, Args, wBook, xlAddIn
	Dim wOdlName, wBase, wExt, wAddInFlg, wYN
	Dim wVNo, I
'	Dim wAddInPath, wAddInBase
	Dim wAddInName

	On Error Resume Next
	Set XL = GetObject(, "Excel.application")
	If Err.Number = 0 Then
		call objShell.Popup( "Excelをすべて終了させてから実行してください", , sTitle, vbInformation )
		Exit Function
	End If
	On Error GoTo 0

	Set Args = wscript.Arguments
	Dim FnameAddIn

	If Args.Count <> 1 Then
		call objShell.Popup( "アドイン登録する xlaファイルを指定してください", , sTitle, vbInformation )
		Exit Function
	End If

	wAddInName = Args(0)

	wExt = Right(LCase(wAddInName), 4)
	If wExt <> ".xla" Then
		call objShell.Popup( "登録しようとしているファイルが xlaファイルではありません", , sTitle, vbInformation )
		Exit Function
	End If

	Set FS = CreateObject( "scripting.filesystemobject" )
	Set XL = CreateObject( "Excel.application" )
	wVNo = Split( XL.Version, "." )(0)
	wBase = FS.GetBaseName( wAddInName )
	Set wBook = XL.Workbooks.Add

'	wAddInPath = objEnv.Item("APPDATA") & "\Microsoft\AddIns"
'	wAddInBase = wBase & ".xla"
'	wAddInName = wAddInPath & "\" & wAddInBase

	wYN = objShell.Popup( _
				"アドイン" & vbCrLf & _
				wAddInName & vbCrLf & _
				"を登録し、使用可能にします。" &  vbCrLf & _
				vbCrLf & _
				"よろしいですか？"_
				, , sTitle, vbYesNo+vbQuestion)
	If wYN = vbNo Then
		wBook.Close False
		XL.Quit
		Set FS = Nothing
		Set XL = Nothing
		Exit Function
	End If

	On Error Resume Next
	Set xlAddIn = XL.AddIns( wBase )
	If Err.Number = 0 Then
		wOldName = xlAddIn.FullName
		If wOldName <> wAddInName Then
			wYN = objShell.Popup( _
						"同名のアドインが別の場所に存在します" & vbCrLf & _
						xlAddIn.FullName & vbCrLf & _
						"を新しいアドインで置き換えますか？"_
						, , sTitle, vbYesNo+vbQuestion)
			If wYN = vbNo Then
				wBook.Close False
				XL.Quit
				Set FS = Nothing
				Set XL = Nothing
				call objShell.Popup( "アドインの登録を中止しました", , sTitle, vbInformation )
				Exit Function
			End If
			If xlAddIn.Installed Then 
				xlAddIn.Installed = False
			End If
			wBook.Close False
			XL.Quit
			DeleteEntry wOldName
			Set XL = CreateObject( "Excel.application" )
			Set wBook = XL.Workbooks.Add
		End If
	End If
	On Error GoTo 0

'	call objShell.Run( "cmd /c copy """ & wAddInName & """ """ & wAddInName & ".bak""", 0, True )
'	call objShell.Run( "cmd /c copy """ & FnameAddIn & """ """ & wAddInName & """", 0, True )

	Set xlAddIn = XL.AddIns.Add( wAddInName, True )
	xlAddIn.Installed = True
	call objShell.Popup( "アドインの登録が完了しました", , sTitle, vbInformation )
	wBook.Close False
	XL.Quit
	Set FS = Nothing
	Set XL = Nothing

End Function

' レジストリから登録削除
Sub DeleteEntry( ByVal pAddin )
	Dim wText, wTemp
	wText = "REGEDIT4" & vbCrLf & vbCrLf _
				& "[HKEY_CURRENT_USER\Software\Microsoft\Office\" _
				& FormatNumber(wVNo, 1) & "\Excel\Add-in Manager]" _
				& vbCrLf & """" & Replace(pAddin, "\", "\\") & """=-" & vbCrLf
	With FS
		wTemp = .BuildPath(.GetSpecialFolder(2).Path, .GetTempName)
		With .CreateTextFile(wTemp)
			.Write wText
			.Close
		End With
		call objShell.Run( "regedit /s " & wTemp, 0, True )
		.DeleteFile wTemp
	End With
End Sub

