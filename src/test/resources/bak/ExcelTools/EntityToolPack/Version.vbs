Option Explicit

Dim objFunction     ' 関数のポインタ

Dim EXCELTOOL_FILEPATH
Dim DIRECTORYPATH
Dim DDLVERSION
Dim ENTITYTYPE

'第１引数：「Entityスクリプト生成ツール.xls」のパス
'第２引数：Entityパス(I30に対応)
'第３引数：バージョン(I32に対応)
'第４引数：種別(I34に対応)
If WScript.Arguments.Count = 4 Then
	EXCELTOOL_FILEPATH = WScript.Arguments.Item(0)
    DIRECTORYPATH = WScript.Arguments.Item(1)
    DDLVERSION = WScript.Arguments.Item(2)
    ENTITYTYPE = WScript.Arguments.Item(3)
    WScript.Echo "第１引数：「Entityスクリプト生成ツール.xls」のパス=" & EXCELTOOL_FILEPATH & vbCrLf & _
    			"第２引数：Entityパス(I30に対応)=" & DIRECTORYPATH & vbCrLf & _
    			"第３引数：バージョン(I32に対応)=" & DDLVERSION & vbCrLf & _
    			"第４引数：種別(I34に対応)=" & ENTITYTYPE
    Set objFunction = GetRef("ExecuteExcelMacro")
    If Err.Number = 0 Then
        Call objFunction()
        WScript.Echo EXCELTOOL_FILEPATH & "のマクロを実行完了しました。"
    Else
        WScript.Echo "ExecuteExcelMacro 関数を取得できませんでした。"
    End If
    Set objFunction = Nothing
Else
    WScript.Echo "引数を設定してください。" & vbCrLf & _
    			"第１引数：「Entityスクリプト生成ツール.xls」のパス" & vbCrLf & _
    			"第２引数：Entityパス(I30に対応)" & vbCrLf & _
    			"第３引数：バージョン(I32に対応)" & vbCrLf & _
    			"第４引数：種別(I34に対応)"
End If



'■Excelを起動し、バージョン作成マクロを実行する
Sub ExecuteExcelMacro()

	Dim xlApp
	Dim xlBook
	Dim xlSheet
	Dim strExcelBook
	Dim strProcName

	'変数定義
    strExcelBook = EXCELTOOL_FILEPATH
    strProcName  = "ExecuteCreateScriptForBatch"
'    strProcName  = "Testkawa"
	        
	Set xlApp = CreateObject("Excel.Application")
    xlApp.Visible = False
	Set xlBook = xlApp.Workbooks.Open(strExcelBook)
    If Err.Number = 0 Then
    	Set xlSheet = xlBook.Worksheets("メニュー")
	    If Err.Number = 0 Then
	    	'変数の値をセット
	    	xlSheet.Cells(30, 9).Value = DIRECTORYPATH
	    	xlSheet.Cells(32, 9).Value = DDLVERSION
	    	xlSheet.Cells(34, 9).Value = ENTITYTYPE

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
