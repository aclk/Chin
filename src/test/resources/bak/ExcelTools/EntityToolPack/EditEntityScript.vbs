Option Explicit

WScript.Echo "1.Entity�̒��ɂ���MV���e�X�g�p(REFRESH COMPLETE ON COMMIT)��DDL�����s����悤�ɕύX"
WScript.Echo "2.Trigger�X�N���v�g�����s���Ȃ��悤��Entity��ReplaceAllObjects.sql��␳"
WScript.Echo "3.���i�\�֘AMV�X�N���v�g�����s���Ȃ��悤�� ReplaceAllObjects.sql��␳"

Dim TRGFILE
Dim STRBEFORE
STRBEFORE = "hand/bec/"
Dim STRAFTER
STRAFTER = "hand/bectest/"

'��P�����F���������Ώ�Entity�X�N���v�g�t�@�C��
If WScript.Arguments.Count = 1 Then
	TRGFILE = WScript.Arguments.Item(0)
    WScript.Echo "��P�����F���������Ώ�Entity�X�N���v�g�t�@�C��=" & TRGFILE
Else
    WScript.Echo "������ݒ肵�Ă��������B" & vbCrLf & _
    			"��P�����F���������Ώ�Entity�X�N���v�g�t�@�C��=" & TRGFILE
    WScript.Quit
End If


'��1.Entity�̒��ɂ���MV���e�X�g�p(REFRESH COMPLETE ON COMMIT)��DDL�����s����悤�ɕύX
'�ǂݎ���p�Ńt�@�C�����J��
Dim objFSO      ' FileSystemObject
Dim objFile     ' �t�@�C���ǂݍ��ݗp
Dim objWriteFile' �t�@�C���������ݗp
Dim fileBefore	'�ǂݍ��񂾃f�[�^
Dim fileAfter	'�������ރf�[�^

Set objFSO = WScript.CreateObject("Scripting.FileSystemObject")
If Err.Number = 0 Then
	'�t�@�C����ǂݎ���p�œǂݍ���
    Set objFile = objFSO.OpenTextFile(TRGFILE, 1)
    If Err.Number = 0 Then
        fileBefore = objFile.ReadAll
        objFile.Close
    Else
        WScript.Echo "�t�@�C���I�[�v���G���[: " & Err.Description
        WScript.Quit
    End If
    
	'�������u������(hand/bec/��hand/bectest/)
    fileAfter = Replace(fileBefore, STRBEFORE, STRAFTER)
    
    
    '�t�@�C�����������ݗp�Ƃ��ĊJ��
    Set objWriteFile = objFSO.CreateTextFile(TRGFILE, True)
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

Dim strTriggerLine	'Trigger���܂ޓǂݍ��񂾍sd�f�[�^

'��2.Trigger�����s���Ȃ��悤�� ReplaceAllObjects.sql ����������
'��3.���i�\�֘AMV�X�N���v�g�����s���Ȃ��悤�� ReplaceAllObjects.sql ����������
If Err.Number = 0 Then
	'1�s���ǂݍ��݁ATrigger�̍s��""�ɒu������
    Set objFile = objFSO.OpenTextFile(TRGFILE, 1)
    If Err.Number = 0 Then
	    Do While objFile.AtEndOfStream <> True
	        strTriggerLine = objFile.ReadLine
	        If InStr(strTriggerLine, "Trigger") > 0 Then
				'�������u������("*Trigger*" �� "/* Trigger�폜 */")
			    fileAfter = Replace(fileAfter, strTriggerLine, "/* Trigger�폜 */")
	        End If
	        If InStr(strTriggerLine, "@hand/CreateMaterializedViewLog.sql") > 0 _
	        	Or InStr(strTriggerLine, "@hand/CreateMaterializedView.sql") > 0 Then
				'�������u������("@hand/CreateMaterializedView.sql" �� "/* Trigger�폜 */")
			    fileAfter = Replace(fileAfter, strTriggerLine, "/* ���i�\�֘AMV�X�N���v�g�폜 */")
	        End If
	    Loop
	    objFile.Close
    Else
        WScript.Echo "�t�@�C���I�[�v���G���[: " & Err.Description
        WScript.Quit
    End If

    '�t�@�C�����������ݗp�Ƃ��ĊJ��
    Set objWriteFile = objFSO.CreateTextFile(TRGFILE, True)
	If Err.Number = 0 Then
		'�t�@�C����ۑ����ĕ���
	    objWriteFile.Write(fileAfter)
		objWriteFile.Close
	Else
	    WScript.Echo "�������݃t�@�C���I�[�v���G���[: " & Err.Description
	End If
End If

Set objFile = Nothing
Set objWriteFile = Nothing
Set objFSO = Nothing

