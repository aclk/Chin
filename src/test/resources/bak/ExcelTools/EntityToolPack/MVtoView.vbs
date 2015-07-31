Option Explicit

Dim EXCELTOOL_FILEPATH
Dim TRGDIR
Dim VERSION
Dim SQLFILEPATH_FORMULTI
Dim SQLFILEPATH_FORMULTI_ORGALL

'��P�����F�uMV��View�ϊ��}�N��.xls�v�̃p�X
'��Q�����F�Ώۃt�H���_
'��R�����F�o�[�W����
'��S�����FMV�pSQL�X�N���v�g�t�@�C��
If WScript.Arguments.Count = 5 Then
	EXCELTOOL_FILEPATH = WScript.Arguments.Item(0)
	TRGDIR = WScript.Arguments.Item(1)
	VERSION = WScript.Arguments.Item(2) & "V"
	SQLFILEPATH_FORMULTI = WScript.Arguments.Item(3)
	SQLFILEPATH_FORMULTI_ORGALL = WScript.Arguments.Item(4)
    WScript.Echo "��P�����F�uMV��View�ϊ��}�N��.xls�v�̃p�X=" & EXCELTOOL_FILEPATH & vbCrLf & _
    			"��Q�����F�Ώۃt�H���_=" & TRGDIR & vbCrLf & _
    			"��R�����F�o�[�W����=" & VERSION & vbCrLf & _
    			"��S�����FMV�pSQL�X�N���v�g�t�@�C��=" & SQLFILEPATH_FORMULTI & vbCrLf & _
    			"��T�����FMV�쐬�pSQL�X�N���v�g�t�@�C��(ALL)=" & SQLFILEPATH_FORMULTI_ORGALL
Else
    WScript.Echo "������ݒ肵�Ă��������B" & vbCrLf & _
    			"��P�����F�uMV��View�ϊ��}�N��.xls�v�̃p�X" & vbCrLf & _
    			"��Q�����F�Ώۃt�H���_" & vbCrLf & _
    			"��R�����F�o�[�W����" & vbCrLf & _
    			"��S�����FMV�pSQL�X�N���v�g�t�@�C��" & vbCrLf & _
    			"��T�����FMV�쐬�pSQL�X�N���v�g�t�@�C��(ALL)"
    WScript.Quit
End If

'��Excel���N�����AMV��View�ϊ��}�N�������s����
Call ExecuteExcelMacro

'��@hand/DropPartOfMaterializedViews.sql���쐬
'CONVERT�֐����g�p���Ă���MV�̔z����擾
Dim MVList
MVList = GetNotWithConvertMVList()
'DropPartOfMaterializedViews.sql���쐬
Call CreateFileDropPartOfMaterializedViews(MVList)

'��ReplaceAllObjects.sql��ReplaceAllObjectsForMulti.sql�̓��e�ŏ���������
Dim objFSO          ' FileSystemObject
Dim objWshShell     ' WshShell �I�u�W�F�N�g

Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
Set objWshShell = WScript.CreateObject("WScript.Shell")
objFSO.CopyFile SQLFILEPATH_FORMULTI, TRGDIR & "\ReplaceAllObjects.sql", True
Set objFSO = Nothing

'��MV�̍X�V�������uON COMMIT�v�ɒu������
ReplaceDemandToCommit TRGDIR & "\" & SQLFILEPATH_FORMULTI_ORGALL

WScript.Echo EXCELTOOL_FILEPATH & "�̃}�N�������s�������܂����B"
'------
' END
'------

'------------------------------------------------------------------------------
' �����F�uCreateMaterializedView.sql�v����ACONVERT�֐����g�p���Ă���MV���擾
'------------------------------------------------------------------------------
Function GetNotWithConvertMVList()

	Dim objFSO      ' FileSystemObject
	Dim objFile     ' �t�@�C���ǂݍ��ݗp
	Dim strLine		' �ǂݍ��񂾂P�s
	
	Dim strNotConvertMV	' CONVERT�֐����g�p���Ă���MV���X�g
	Dim strMV		' MV���[�N�ϐ�
	Dim wordBuf
	
	Dim exitConvertMV	'CONVERT�֐����g�p���Ă���Ɣ��f�ł�����

	Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
	If Err.Number = 0 Then
	    Set objFile = objFSO.OpenTextFile(TRGDIR & "\all\CreateMaterializedView.sql")
	    If Err.Number = 0 Then
	        Do While objFile.AtEndOfStream <> True
	            strLine = objFile.ReadLine
	            If InStr(strLine, "CREATE") = 1 Then
	            	'MV�����擾
	            	wordBuf = Split(strLine, " ")
	            	strMV = wordBuf(3)
	            	exitConvertMV = False
	            End If
	            If Instr(strLine, "CONVERT") > 0 Then
	            	exitConvertMV = True
	            End If
	            If InStr(strLine, "FROM") = 1 Then
	            	If exitConvertMV = False Then
		            	'�J���}��؂��CONVERT�֐����g�p���Ă��Ȃ�MV�����擾
		            	strNotConvertMV = strNotConvertMV & "," & strMV
	            	End If
	            End If
	        Loop
	        objFile.Close
	    Else
	        WScript.Echo "�t�@�C���I�[�v���G���[: " & Err.Description
	    End If
	Else
	    WScript.Echo "�G���[: " & Err.Description
	End If

	Set objFile = Nothing
	Set objFSO = Nothing

	strNotConvertMV = Right(strNotConvertMV, Len(strNotConvertMV) - 1)
	'�J���}��؂�Ŕz�񂩂����I�u�W�F�N�g��Ԃ�
	GetNotWithConvertMVList = Split(strNotConvertMV, ",")

End Function
'------------------------------------------------------------------------------
' �����FDropPartOfMaterializedViews.sql���쐬
'------------------------------------------------------------------------------
Sub CreateFileDropPartOfMaterializedViews(arrMV)

	Dim objFSO      ' FileSystemObject
	Dim objFile     ' �t�@�C���������ݗp
	Dim lngLoop     ' ���[�v�J�E���^

	Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
	If Err.Number = 0 Then
	    Set objFile = objFSO.OpenTextFile(TRGDIR & "\hand\DropPartOfMaterializedViews.sql", 2, True)
	    If Err.Number = 0 Then

			For lngLoop = LBound(arrMV) To UBound(arrMV)
				objFile.WriteLine("DROP MATERIALIZED VIEW " & arrMV(lngLoop) & ";")
			Next
	        objFile.Close
	    Else
	        WScript.Echo "�t�@�C���I�[�v���G���[: " & Err.Description
	    End If
	Else
	    WScript.Echo "�G���[: " & Err.Description
	End If

	Set objFile = Nothing
	Set objFSO = Nothing

End Sub

'------------------------------------------------------------------------------
' �����FExcel���N�����AMV��View�ϊ��}�N�������s����
'------------------------------------------------------------------------------
Sub ExecuteExcelMacro()

	Dim xlApp
	Dim xlBook
	Dim xlSheet
	Dim strExcelBook
	Dim strProcName

	'�ϐ���`
    strExcelBook = EXCELTOOL_FILEPATH
'    strProcName  = "cmdExecConvert_Click"
    strProcName  = "ExecuteCreateScriptForBatch"
	        
	Set xlApp = CreateObject("Excel.Application")
    xlApp.Visible = False
	Set xlBook = xlApp.Workbooks.Open(strExcelBook)
    If Err.Number = 0 Then
    	Set xlSheet = xlBook.Worksheets("�ϊ��}�N��")
	    If Err.Number = 0 Then
	    	'�ϐ��̒l���Z�b�g
	    	xlSheet.Cells(3, 3).Value = TRGDIR & "\all\CreateMaterializedView.sql"
	    	xlSheet.Cells(4, 3).Value = TRGDIR & "\hand\"
	    	xlSheet.Cells(5, 3).Value = VERSION

	    	'�}�N�������s
		    xlApp.Run strProcName
		    xlBook.Saved = True
		    xlApp.Quit
	    End If
    
    Else
        WScript.Echo strExcelBook & "���J�����Ƃ��ł��܂���ł����B"
    End If

	Set xlSheet = Nothing
	Set xlBook = Nothing
	Set xlApp  = Nothing

End Sub

'------------------------------------------------------------------------------
' �����F�Ώ�SQL�X�N���v�g�t�@�C����MV�̍X�V������ ON DEMAND �� ON COMMIT �֒u������
'------------------------------------------------------------------------------
Sub ReplaceDemandToCommit(trgFilePath)

	'�ǂݎ���p�Ńt�@�C�����J��
	Dim objFSO      ' FileSystemObject
	Dim objFile     ' �t�@�C���ǂݍ��ݗp
	Dim objWriteFile' �t�@�C���������ݗp
	Dim fileBefore	'�ǂݍ��񂾃f�[�^
	Dim fileAfter	'�������ރf�[�^
	Dim STRBEFORE
	STRBEFORE = "ON DEMAND"
	Dim STRAFTER
	STRAFTER = "ON COMMIT"

	Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
	If Err.Number = 0 Then
		'�t�@�C����ǂݎ���p�œǂݍ���
	    Set objFile = objFSO.OpenTextFile(trgFilePath, 1)
	    If Err.Number = 0 Then
	        fileBefore = objFile.ReadAll
	        objFile.Close
	    Else
	        WScript.Echo "�t�@�C���I�[�v���G���[: " & Err.Description
	        WScript.Quit
	    End If
	    
		'�������u������(ON DEMAND��ON COMMIT)
	    fileAfter = Replace(fileBefore, STRBEFORE, STRAFTER)
	    
	    
	    '�t�@�C�����������ݗp�Ƃ��ĊJ��
	    Set objWriteFile = objFSO.CreateTextFile(trgFilePath, True)
		If Err.Number = 0 Then
			'�t�@�C����ۑ����ĕ���
		    objWriteFile.Write(fileAfter)
			objWriteFile.Close
		Else
		    WScript.Echo "�������݃t�@�C���I�[�v���G���[: " & Err.Description
		End If

	Else
	    WScript.Echo "�G���[: " & Err.Description
	    WScript.Quit
	End If

	Set objFile = Nothing
	Set objWriteFile = Nothing
	Set objFSO = Nothing


End Sub