'------------------------------------------------------------------------
'�t�@�C������TR��TP�ɕϊ�����B
' regression�t�H���_�ɂ���w�肵��TR�t�@�C���𓯊K�w��input�t�H���_�ɃR�s�[�A�t�@�C������TP�ɕϊ�����
' ���t�@�C��������ꍇ�́A�㏑������܂�
' SendTo�ɂ��̃X�N���v�g�t�@�C����u���ƕ֗��Ȃ͂��ł��B(cf.)C:\Documents and Settings\A1M4MEM294.NAGAOKASV1-DM.000\SendTo)
'------------------------------------------------------------------------
Option Explicit

Dim oParam
Dim WSH, LnkFile, LnkFileName
Set oParam = WScript.Arguments
Set WSH = CreateObject("WScript.Shell")

If oParam.length > 0 Then
    ' �S�Ẵt�@�C���E�t�H���_�p�X���擾
    Dim arg, fromStr
    Dim toFileName,toFilePath               '�R�s�[��(TR)�̃t�@�C���p�X�A�t�@�C����
    Dim fromPath ,fromFileName,fromFilePath '�R�s�[��(TP)�̃t�@�C���p�X�A�t�@�C����
    Dim searchRegression                    'regression�t�H���_�̗L���m�F
    Dim FSO

	'--------------------------------------
	' �R�s�[���̃t�H���_����"regression"���m�F
	'--------------------------------------
	searchRegression = InStr(oParam(0),"\regression\")
	If searchRegression = 0 Then
		Msgbox " regression�t�H���_��TR���i�[���ĉ����� "
	Else	
	'--------------------------------------
	' TP�t�@�C���쐬�iinput�t�H���_���쐬�j
	'--------------------------------------	
	    For Each arg In oParam
			Set FSO = CreateObject("Scripting.FileSystemObject")

	    	'�R�s�[��(�t���p�X)
	    	fromStr = arg
	    	'�R�s�[��(�t�@�C����)    	
	    	fromFileName = FSO.GetFileName(fromStr)    	
	    	'�R�s�[��(�p�X)
	    	fromFilePath = Replace(fromStr, fromFileName, "")    	
	    	'�R�s�[��i�p�X�j
	    	toFilePath = Replace(fromFilePath, "regression", "input")
	    	'�R�s�[��(�t�@�C����)    	
	    	toFileName = "TP" & Right(fromFileName, Len(fromFileName) - 2)

			'input�t�H���_�쐬
			IF FSO.folderexists(toFilePath) = false Then FSO.CreateFolder(toFilePath)

	    	'�t�@�C���R�s�[
			FSO.CopyFile fromStr,toFilePath&toFileName
	        
	    Next
	    Msgbox "  input�t�H���_��TP���쐬���܂���  " & "(" & oParam.length & "�쐬)"
	End If
Else
    Msgbox " �t�@�C�����w�肵�ĉ����� "
End If
Set oParam = Nothing
Set LnkFile = Nothing
Set WSH = Nothing