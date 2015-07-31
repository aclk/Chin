Option Explicit

WScript.Echo "1.Entityの中にあるMVをテスト用(REFRESH COMPLETE ON COMMIT)のDDLを実行するように変更"
WScript.Echo "2.Triggerスクリプトを実行しないようにEntityのReplaceAllObjects.sqlを補正"
WScript.Echo "3.部品表関連MVスクリプトを実行しないように ReplaceAllObjects.sqlを補正"

Dim TRGFILE
Dim STRBEFORE
STRBEFORE = "hand/bec/"
Dim STRAFTER
STRAFTER = "hand/bectest/"

'第１引数：書き換え対象Entityスクリプトファイル
If WScript.Arguments.Count = 1 Then
	TRGFILE = WScript.Arguments.Item(0)
    WScript.Echo "第１引数：書き換え対象Entityスクリプトファイル=" & TRGFILE
Else
    WScript.Echo "引数を設定してください。" & vbCrLf & _
    			"第１引数：書き換え対象Entityスクリプトファイル=" & TRGFILE
    WScript.Quit
End If


'★1.Entityの中にあるMVをテスト用(REFRESH COMPLETE ON COMMIT)のDDLを実行するように変更
'読み取り専用でファイルを開く
Dim objFSO      ' FileSystemObject
Dim objFile     ' ファイル読み込み用
Dim objWriteFile' ファイル書き込み用
Dim fileBefore	'読み込んだデータ
Dim fileAfter	'書き込むデータ

Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
If Err.Number = 0 Then
	'ファイルを読み取り専用で読み込む
    Set objFile = objFSO.OpenTextFile(TRGFILE, 1)
    If Err.Number = 0 Then
        fileBefore = objFile.ReadAll
        objFile.Close
    Else
        WScript.Echo "ファイルオープンエラー: " & Err.Description
        WScript.Quit
    End If
    
	'文字列を置換する(hand/bec/→hand/bectest/)
    fileAfter = Replace(fileBefore, STRBEFORE, STRAFTER)
    
    
    'ファイルを書き込み用として開く
    Set objWriteFile = objFSO.CreateTextFile(TRGFILE, True)
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

Dim strTriggerLine	'Triggerを含む読み込んだ行dデータ

'★2.Triggerを実行しないように ReplaceAllObjects.sql を書き換え
'★3.部品表関連MVスクリプトを実行しないように ReplaceAllObjects.sql を書き換え
If Err.Number = 0 Then
	'1行ずつ読み込み、Triggerの行を""に置換する
    Set objFile = objFSO.OpenTextFile(TRGFILE, 1)
    If Err.Number = 0 Then
	    Do While objFile.AtEndOfStream <> True
	        strTriggerLine = objFile.ReadLine
	        If InStr(strTriggerLine, "Trigger") > 0 Then
				'文字列を置換する("*Trigger*" → "/* Trigger削除 */")
			    fileAfter = Replace(fileAfter, strTriggerLine, "/* Trigger削除 */")
	        End If
	        If InStr(strTriggerLine, "@hand/CreateMaterializedViewLog.sql") > 0 _
	        	Or InStr(strTriggerLine, "@hand/CreateMaterializedView.sql") > 0 Then
				'文字列を置換する("@hand/CreateMaterializedView.sql" → "/* Trigger削除 */")
			    fileAfter = Replace(fileAfter, strTriggerLine, "/* 部品表関連MVスクリプト削除 */")
	        End If
	    Loop
	    objFile.Close
    Else
        WScript.Echo "ファイルオープンエラー: " & Err.Description
        WScript.Quit
    End If

    'ファイルを書き込み用として開く
    Set objWriteFile = objFSO.CreateTextFile(TRGFILE, True)
	If Err.Number = 0 Then
		'ファイルを保存して閉じる
	    objWriteFile.Write(fileAfter)
		objWriteFile.Close
	Else
	    WScript.Echo "書き込みファイルオープンエラー: " & Err.Description
	End If
End If

Set objFile = Nothing
Set objWriteFile = Nothing
Set objFSO = Nothing

