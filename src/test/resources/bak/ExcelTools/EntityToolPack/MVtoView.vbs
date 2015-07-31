Option Explicit

Dim EXCELTOOL_FILEPATH
Dim TRGDIR
Dim VERSION
Dim SQLFILEPATH_FORMULTI
Dim SQLFILEPATH_FORMULTI_ORGALL

'第１引数：「MV→View変換マクロ.xls」のパス
'第２引数：対象フォルダ
'第３引数：バージョン
'第４引数：MV用SQLスクリプトファイル
If WScript.Arguments.Count = 5 Then
	EXCELTOOL_FILEPATH = WScript.Arguments.Item(0)
	TRGDIR = WScript.Arguments.Item(1)
	VERSION = WScript.Arguments.Item(2) & "V"
	SQLFILEPATH_FORMULTI = WScript.Arguments.Item(3)
	SQLFILEPATH_FORMULTI_ORGALL = WScript.Arguments.Item(4)
    WScript.Echo "第１引数：「MV→View変換マクロ.xls」のパス=" & EXCELTOOL_FILEPATH & vbCrLf & _
    			"第２引数：対象フォルダ=" & TRGDIR & vbCrLf & _
    			"第３引数：バージョン=" & VERSION & vbCrLf & _
    			"第４引数：MV用SQLスクリプトファイル=" & SQLFILEPATH_FORMULTI & vbCrLf & _
    			"第５引数：MV作成用SQLスクリプトファイル(ALL)=" & SQLFILEPATH_FORMULTI_ORGALL
Else
    WScript.Echo "引数を設定してください。" & vbCrLf & _
    			"第１引数：「MV→View変換マクロ.xls」のパス" & vbCrLf & _
    			"第２引数：対象フォルダ" & vbCrLf & _
    			"第３引数：バージョン" & vbCrLf & _
    			"第４引数：MV用SQLスクリプトファイル" & vbCrLf & _
    			"第５引数：MV作成用SQLスクリプトファイル(ALL)"
    WScript.Quit
End If

'■Excelを起動し、MV→View変換マクロを実行する
Call ExecuteExcelMacro

'■@hand/DropPartOfMaterializedViews.sqlを作成
'CONVERT関数を使用しているMVの配列を取得
Dim MVList
MVList = GetNotWithConvertMVList()
'DropPartOfMaterializedViews.sqlを作成
Call CreateFileDropPartOfMaterializedViews(MVList)

'■ReplaceAllObjects.sqlをReplaceAllObjectsForMulti.sqlの内容で書き換える
Dim objFSO          ' FileSystemObject
Dim objWshShell     ' WshShell オブジェクト

Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
Set objWshShell = WScript.CreateObject("WScript.Shell")
objFSO.CopyFile SQLFILEPATH_FORMULTI, TRGDIR & "\ReplaceAllObjects.sql", True
Set objFSO = Nothing

'■MVの更新属性を「ON COMMIT」に置換する
ReplaceDemandToCommit TRGDIR & "\" & SQLFILEPATH_FORMULTI_ORGALL

WScript.Echo EXCELTOOL_FILEPATH & "のマクロを実行完了しました。"
'------
' END
'------

'------------------------------------------------------------------------------
' 処理：「CreateMaterializedView.sql」から、CONVERT関数を使用しているMVを取得
'------------------------------------------------------------------------------
Function GetNotWithConvertMVList()

	Dim objFSO      ' FileSystemObject
	Dim objFile     ' ファイル読み込み用
	Dim strLine		' 読み込んだ１行
	
	Dim strNotConvertMV	' CONVERT関数を使用しているMVリスト
	Dim strMV		' MVワーク変数
	Dim wordBuf
	
	Dim exitConvertMV	'CONVERT関数を使用していると判断できたか

	Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
	If Err.Number = 0 Then
	    Set objFile = objFSO.OpenTextFile(TRGDIR & "\all\CreateMaterializedView.sql")
	    If Err.Number = 0 Then
	        Do While objFile.AtEndOfStream <> True
	            strLine = objFile.ReadLine
	            If InStr(strLine, "CREATE") = 1 Then
	            	'MV名を取得
	            	wordBuf = Split(strLine, " ")
	            	strMV = wordBuf(3)
	            	exitConvertMV = False
	            End If
	            If Instr(strLine, "CONVERT") > 0 Then
	            	exitConvertMV = True
	            End If
	            If InStr(strLine, "FROM") = 1 Then
	            	If exitConvertMV = False Then
		            	'カンマ区切りでCONVERT関数を使用していないMV名を取得
		            	strNotConvertMV = strNotConvertMV & "," & strMV
	            	End If
	            End If
	        Loop
	        objFile.Close
	    Else
	        WScript.Echo "ファイルオープンエラー: " & Err.Description
	    End If
	Else
	    WScript.Echo "エラー: " & Err.Description
	End If

	Set objFile = Nothing
	Set objFSO = Nothing

	strNotConvertMV = Right(strNotConvertMV, Len(strNotConvertMV) - 1)
	'カンマ区切りで配列かしたオブジェクトを返す
	GetNotWithConvertMVList = Split(strNotConvertMV, ",")

End Function
'------------------------------------------------------------------------------
' 処理：DropPartOfMaterializedViews.sqlを作成
'------------------------------------------------------------------------------
Sub CreateFileDropPartOfMaterializedViews(arrMV)

	Dim objFSO      ' FileSystemObject
	Dim objFile     ' ファイル書き込み用
	Dim lngLoop     ' ループカウンタ

	Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
	If Err.Number = 0 Then
	    Set objFile = objFSO.OpenTextFile(TRGDIR & "\hand\DropPartOfMaterializedViews.sql", 2, True)
	    If Err.Number = 0 Then

			For lngLoop = LBound(arrMV) To UBound(arrMV)
				objFile.WriteLine("DROP MATERIALIZED VIEW " & arrMV(lngLoop) & ";")
			Next
	        objFile.Close
	    Else
	        WScript.Echo "ファイルオープンエラー: " & Err.Description
	    End If
	Else
	    WScript.Echo "エラー: " & Err.Description
	End If

	Set objFile = Nothing
	Set objFSO = Nothing

End Sub

'------------------------------------------------------------------------------
' 処理：Excelを起動し、MV→View変換マクロを実行する
'------------------------------------------------------------------------------
Sub ExecuteExcelMacro()

	Dim xlApp
	Dim xlBook
	Dim xlSheet
	Dim strExcelBook
	Dim strProcName

	'変数定義
    strExcelBook = EXCELTOOL_FILEPATH
'    strProcName  = "cmdExecConvert_Click"
    strProcName  = "ExecuteCreateScriptForBatch"
	        
	Set xlApp = CreateObject("Excel.Application")
    xlApp.Visible = False
	Set xlBook = xlApp.Workbooks.Open(strExcelBook)
    If Err.Number = 0 Then
    	Set xlSheet = xlBook.Worksheets("変換マクロ")
	    If Err.Number = 0 Then
	    	'変数の値をセット
	    	xlSheet.Cells(3, 3).Value = TRGDIR & "\all\CreateMaterializedView.sql"
	    	xlSheet.Cells(4, 3).Value = TRGDIR & "\hand\"
	    	xlSheet.Cells(5, 3).Value = VERSION

	    	'マクロを実行
		    xlApp.Run strProcName
		    xlBook.Saved = True
		    xlApp.Quit
	    End If
    
    Else
        WScript.Echo strExcelBook & "を開くことができませんでした。"
    End If

	Set xlSheet = Nothing
	Set xlBook = Nothing
	Set xlApp  = Nothing

End Sub

'------------------------------------------------------------------------------
' 処理：対象SQLスクリプトファイルのMVの更新属性を ON DEMAND → ON COMMIT へ置換する
'------------------------------------------------------------------------------
Sub ReplaceDemandToCommit(trgFilePath)

	'読み取り専用でファイルを開く
	Dim objFSO      ' FileSystemObject
	Dim objFile     ' ファイル読み込み用
	Dim objWriteFile' ファイル書き込み用
	Dim fileBefore	'読み込んだデータ
	Dim fileAfter	'書き込むデータ
	Dim STRBEFORE
	STRBEFORE = "ON DEMAND"
	Dim STRAFTER
	STRAFTER = "ON COMMIT"

	Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
	If Err.Number = 0 Then
		'ファイルを読み取り専用で読み込む
	    Set objFile = objFSO.OpenTextFile(trgFilePath, 1)
	    If Err.Number = 0 Then
	        fileBefore = objFile.ReadAll
	        objFile.Close
	    Else
	        WScript.Echo "ファイルオープンエラー: " & Err.Description
	        WScript.Quit
	    End If
	    
		'文字列を置換する(ON DEMAND→ON COMMIT)
	    fileAfter = Replace(fileBefore, STRBEFORE, STRAFTER)
	    
	    
	    'ファイルを書き込み用として開く
	    Set objWriteFile = objFSO.CreateTextFile(trgFilePath, True)
		If Err.Number = 0 Then
			'ファイルを保存して閉じる
		    objWriteFile.Write(fileAfter)
			objWriteFile.Close
		Else
		    WScript.Echo "書き込みファイルオープンエラー: " & Err.Description
		End If

	Else
	    WScript.Echo "エラー: " & Err.Description
	    WScript.Quit
	End If

	Set objFile = Nothing
	Set objWriteFile = Nothing
	Set objFSO = Nothing


End Sub