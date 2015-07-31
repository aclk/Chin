Option Explicit

Dim objFunction     ' �֐��̃|�C���^

Dim EXCELTOOL_FILEPATH
Dim DIRECTORYPATH
Dim DDLVERSION
Dim ENTITYTYPE

'��P�����F�uEntity�X�N���v�g�����c�[��.xls�v�̃p�X
'��Q�����FEntity�p�X(I30�ɑΉ�)
'��R�����F�o�[�W����(I32�ɑΉ�)
'��S�����F���(I34�ɑΉ�)
If WScript.Arguments.Count = 4 Then
	EXCELTOOL_FILEPATH = WScript.Arguments.Item(0)
    DIRECTORYPATH = WScript.Arguments.Item(1)
    DDLVERSION = WScript.Arguments.Item(2)
    ENTITYTYPE = WScript.Arguments.Item(3)
    WScript.Echo "��P�����F�uEntity�X�N���v�g�����c�[��.xls�v�̃p�X=" & EXCELTOOL_FILEPATH & vbCrLf & _
    			"��Q�����FEntity�p�X(I30�ɑΉ�)=" & DIRECTORYPATH & vbCrLf & _
    			"��R�����F�o�[�W����(I32�ɑΉ�)=" & DDLVERSION & vbCrLf & _
    			"��S�����F���(I34�ɑΉ�)=" & ENTITYTYPE
    Set objFunction = GetRef("ExecuteExcelMacro")
    If Err.Number = 0 Then
        Call objFunction()
        WScript.Echo EXCELTOOL_FILEPATH & "�̃}�N�������s�������܂����B"
    Else
        WScript.Echo "ExecuteExcelMacro �֐����擾�ł��܂���ł����B"
    End If
    Set objFunction = Nothing
Else
    WScript.Echo "������ݒ肵�Ă��������B" & vbCrLf & _
    			"��P�����F�uEntity�X�N���v�g�����c�[��.xls�v�̃p�X" & vbCrLf & _
    			"��Q�����FEntity�p�X(I30�ɑΉ�)" & vbCrLf & _
    			"��R�����F�o�[�W����(I32�ɑΉ�)" & vbCrLf & _
    			"��S�����F���(I34�ɑΉ�)"
End If



'��Excel���N�����A�o�[�W�����쐬�}�N�������s����
Sub ExecuteExcelMacro()

	Dim xlApp
	Dim xlBook
	Dim xlSheet
	Dim strExcelBook
	Dim strProcName

	'�ϐ���`
    strExcelBook = EXCELTOOL_FILEPATH
    strProcName  = "ExecuteCreateScriptForBatch"
'    strProcName  = "Testkawa"
	        
	Set xlApp = CreateObject("Excel.Application")
    xlApp.Visible = False
	Set xlBook = xlApp.Workbooks.Open(strExcelBook)
    If Err.Number = 0 Then
    	Set xlSheet = xlBook.Worksheets("���j���[")
	    If Err.Number = 0 Then
	    	'�ϐ��̒l���Z�b�g
	    	xlSheet.Cells(30, 9).Value = DIRECTORYPATH
	    	xlSheet.Cells(32, 9).Value = DDLVERSION
	    	xlSheet.Cells(34, 9).Value = ENTITYTYPE

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
