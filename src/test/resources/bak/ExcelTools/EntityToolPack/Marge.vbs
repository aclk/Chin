Option Explicit

Dim objFunction     ' �֐��̃|�C���^

Dim EXCELTOOL_FILEPATH
Dim OLDENTITYPATH
Dim NEWENTITYPATH
Dim MARGEFILENAME
Dim VERSION
Dim ENTITYTYPE

'��P�����F�uEntity�X�N���v�g�����c�[��.xls�v�̃p�X
'��Q�����F��Entity�p�X(I9�ɑΉ�)
'��R�����F�VEntity�p�X(I10�ɑΉ�)
'��S�����F�o�̓t�@�C����(I12�ɑΉ�)
'��T�����F�o�[�W����(I14�ɑΉ�)
'��U�����F���(I16�ɑΉ�)
If WScript.Arguments.Count = 6 Then
	EXCELTOOL_FILEPATH = WScript.Arguments.Item(0)
    OLDENTITYPATH = WScript.Arguments.Item(1)
    NEWENTITYPATH = WScript.Arguments.Item(2)
    MARGEFILENAME = WScript.Arguments.Item(3)
    VERSION = WScript.Arguments.Item(4)
    ENTITYTYPE = WScript.Arguments.Item(5)
    WScript.Echo "��P�����F�uEntity�X�N���v�g�����c�[��.xls�v�̃p�X=" & EXCELTOOL_FILEPATH & vbCrLf & _
    			"��Q�����F��Entity�p�X(I9�ɑΉ�)=" & OLDENTITYPATH & vbCrLf & _
    			"��R�����F�VEntity�p�X(I10�ɑΉ�)=" & NEWENTITYPATH & vbCrLf & _
    			"��S�����F�o�̓t�@�C����(I12�ɑΉ�)=" & MARGEFILENAME & vbCrLf & _
    			"��T�����F�o�[�W����(I14�ɑΉ�)=" & VERSION & vbCrLf & _
    			"��U�����F���(I16�ɑΉ�)=" & ENTITYTYPE
    Set objFunction = GetRef("ExecuteExcelMacro")
    If Err.Number = 0 Then
        Call objFunction()
    Else
        WScript.Echo "ExecuteExcelMacro �֐����擾�ł��܂���ł����B"
    End If
    WScript.Echo EXCELTOOL_FILEPATH & "�̃}�N�������s�������܂����B"
    Set objFunction = Nothing
Else
    WScript.Echo "������ݒ肵�Ă��������B" & vbCrLf & _
    			"��P�����F�uEntity�X�N���v�g�����c�[��.xls�v�̃p�X" & vbCrLf & _
    			"��Q�����F��Entity�p�X(I9�ɑΉ�)" & vbCrLf & _
    			"��R�����F�VEntity�p�X(I10�ɑΉ�)" & vbCrLf & _
    			"��S�����F�o�̓t�@�C����(I12�ɑΉ�)" & vbCrLf & _
    			"��T�����F�o�[�W����(I14�ɑΉ�)" & vbCrLf & _
    			"��U�����F���(I16�ɑΉ�)"
End If


Sub Test()
    WScript.Echo "Test �֐����Ăяo����܂����B"
End Sub

'��Excel���N�����A�o�[�W�����쐬�}�N�������s����
Sub ExecuteExcelMacro()

	Dim xlApp
	Dim xlBook
	Dim xlSheet
	Dim strExcelBook
	Dim strProcName

	'�ϐ���`
    strExcelBook = EXCELTOOL_FILEPATH
    strProcName  = "ExecuteDiffForBatch"
'    strProcName  = "Testkawa"
	        
	Set xlApp = CreateObject("Excel.Application")
    xlApp.Visible = False
	Set xlBook = xlApp.Workbooks.Open(strExcelBook)
    If Err.Number = 0 Then
    	Set xlSheet = xlBook.Worksheets("���j���[")
	    If Err.Number = 0 Then
	    	'�ϐ��̒l���Z�b�g
	    	xlSheet.Cells(9, 9).Value = OLDENTITYPATH
	    	xlSheet.Cells(10, 9).Value = NEWENTITYPATH
	    	xlSheet.Cells(12, 9).Value = MARGEFILENAME
	    	xlSheet.Cells(14, 9).Value = VERSION
	    	xlSheet.Cells(16, 9).Value = ENTITYTYPE

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
