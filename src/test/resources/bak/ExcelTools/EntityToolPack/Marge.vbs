Option Explicit

Dim objFunction     ' 関数のポインタ

Dim EXCELTOOL_FILEPATH
Dim OLDENTITYPATH
Dim NEWENTITYPATH
Dim MARGEFILENAME
Dim VERSION
Dim ENTITYTYPE

'第１引数：「Entityスクリプト生成ツール.xls」のパス
'第２引数：旧Entityパス(I9に対応)
'第３引数：新Entityパス(I10に対応)
'第４引数：出力ファイル名(I12に対応)
'第５引数：バージョン(I14に対応)
'第６引数：種別(I16に対応)
If WScript.Arguments.Count = 6 Then
	EXCELTOOL_FILEPATH = WScript.Arguments.Item(0)
    OLDENTITYPATH = WScript.Arguments.Item(1)
    NEWENTITYPATH = WScript.Arguments.Item(2)
    MARGEFILENAME = WScript.Arguments.Item(3)
    VERSION = WScript.Arguments.Item(4)
    ENTITYTYPE = WScript.Arguments.Item(5)
    WScript.Echo "第１引数：「Entityスクリプト生成ツール.xls」のパス=" & EXCELTOOL_FILEPATH & vbCrLf & _
    			"第２引数：旧Entityパス(I9に対応)=" & OLDENTITYPATH & vbCrLf & _
    			"第３引数：新Entityパス(I10に対応)=" & NEWENTITYPATH & vbCrLf & _
    			"第４引数：出力ファイル名(I12に対応)=" & MARGEFILENAME & vbCrLf & _
    			"第５引数：バージョン(I14に対応)=" & VERSION & vbCrLf & _
    			"第６引数：種別(I16に対応)=" & ENTITYTYPE
    Set objFunction = GetRef("ExecuteExcelMacro")
    If Err.Number = 0 Then
        Call objFunction()
    Else
        WScript.Echo "ExecuteExcelMacro 関数を取得できませんでした。"
    End If
    WScript.Echo EXCELTOOL_FILEPATH & "のマクロを実行完了しました。"
    Set objFunction = Nothing
Else
    WScript.Echo "引数を設定してください。" & vbCrLf & _
    			"第１引数：「Entityスクリプト生成ツール.xls」のパス" & vbCrLf & _
    			"第２引数：旧Entityパス(I9に対応)" & vbCrLf & _
    			"第３引数：新Entityパス(I10に対応)" & vbCrLf & _
    			"第４引数：出力ファイル名(I12に対応)" & vbCrLf & _
    			"第５引数：バージョン(I14に対応)" & vbCrLf & _
    			"第６引数：種別(I16に対応)"
End If


Sub Test()
    WScript.Echo "Test 関数が呼び出されました。"
End Sub

'■Excelを起動し、バージョン作成マクロを実行する
Sub ExecuteExcelMacro()

	Dim xlApp
	Dim xlBook
	Dim xlSheet
	Dim strExcelBook
	Dim strProcName

	'変数定義
    strExcelBook = EXCELTOOL_FILEPATH
    strProcName  = "ExecuteDiffForBatch"
'    strProcName  = "Testkawa"
	        
	Set xlApp = CreateObject("Excel.Application")
    xlApp.Visible = False
	Set xlBook = xlApp.Workbooks.Open(strExcelBook)
    If Err.Number = 0 Then
    	Set xlSheet = xlBook.Worksheets("メニュー")
	    If Err.Number = 0 Then
	    	'変数の値をセット
	    	xlSheet.Cells(9, 9).Value = OLDENTITYPATH
	    	xlSheet.Cells(10, 9).Value = NEWENTITYPATH
	    	xlSheet.Cells(12, 9).Value = MARGEFILENAME
	    	xlSheet.Cells(14, 9).Value = VERSION
	    	xlSheet.Cells(16, 9).Value = ENTITYTYPE

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
