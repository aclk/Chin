'------------------------------------------------------------------------
'ファイル名をTR→TPに変換する。
' regressionフォルダにある指定したTRファイルを同階層のinputフォルダにコピー、ファイル名をTPに変換する
' 同ファイルがある場合は、上書きされます
' SendToにこのスクリプトファイルを置くと便利なはずです。(cf.)C:\Documents and Settings\A1M4MEM294.NAGAOKASV1-DM.000\SendTo)
'------------------------------------------------------------------------
Option Explicit

Dim oParam
Dim WSH, LnkFile, LnkFileName
Set oParam = WScript.Arguments
Set WSH = CreateObject("WScript.Shell")

If oParam.length > 0 Then
    ' 全てのファイル・フォルダパスを取得
    Dim arg, fromStr
    Dim toFileName,toFilePath               'コピー元(TR)のファイルパス、ファイル名
    Dim fromPath ,fromFileName,fromFilePath 'コピー先(TP)のファイルパス、ファイル名
    Dim searchRegression                    'regressionフォルダの有無確認
    Dim FSO

	'--------------------------------------
	' コピー元のフォルダ名が"regression"か確認
	'--------------------------------------
	searchRegression = InStr(oParam(0),"\regression\")
	If searchRegression = 0 Then
		Msgbox " regressionフォルダにTRを格納して下さい "
	Else	
	'--------------------------------------
	' TPファイル作成（inputフォルダも作成）
	'--------------------------------------	
	    For Each arg In oParam
			Set FSO = CreateObject("Scripting.FileSystemObject")

	    	'コピー元(フルパス)
	    	fromStr = arg
	    	'コピー元(ファイル名)    	
	    	fromFileName = FSO.GetFileName(fromStr)    	
	    	'コピー元(パス)
	    	fromFilePath = Replace(fromStr, fromFileName, "")    	
	    	'コピー先（パス）
	    	toFilePath = Replace(fromFilePath, "regression", "input")
	    	'コピー先(ファイル名)    	
	    	toFileName = "TP" & Right(fromFileName, Len(fromFileName) - 2)

			'inputフォルダ作成
			IF FSO.folderexists(toFilePath) = false Then FSO.CreateFolder(toFilePath)

	    	'ファイルコピー
			FSO.CopyFile fromStr,toFilePath&toFileName
	        
	    Next
	    Msgbox "  inputフォルダにTPを作成しました  " & "(" & oParam.length & "個作成)"
	End If
Else
    Msgbox " ファイルを指定して下さい "
End If
Set oParam = Nothing
Set LnkFile = Nothing
Set WSH = Nothing