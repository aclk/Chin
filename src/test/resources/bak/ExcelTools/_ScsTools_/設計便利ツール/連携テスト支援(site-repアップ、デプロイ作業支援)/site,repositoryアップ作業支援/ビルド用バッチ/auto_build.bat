@ECHO OFF
REM ------------------------------------------------------------------------------
REM �w��f�B���N�g�����̃v���_�N�g��
REM �ꊇ��A�e�X�g���܂�
REM 
REM ���O��
REM   maven2��bin�t�H���_�Ƀp�X���ʂ��Ă��邱�ƁB
REM   SPIRTS�psite/���|�W�g��UP�R�}���h"rel.bat"����������Ă��邱�ƁB
REM 
REM �����p���@��
REM   �P�D���̃o�b�`�Ɠ��t�H���_�Ƀr���h�������v���W�F�N�g���`�F�b�N�A�E�g
REM   �Q�D���t�H���_��"target_product.txt"��ҏW�A�r���h�������v���W�F�N�g�����񋓂���B
REM   �R�D�o�b�`���s
REM   �S�D���ʂ�"build_logs"�̒��ɓ����Ă��郍�O�����Ɋm�F�B
REM ------------------------------------------------------------------------------

REM �Ώۃv���_�N�g�����̃o�b�`�Ɠ���t�H���_��"target_product.txt"�ɗ񋓂��Ă��������B

SET ROOT_DIR=%CD%

FOR /f "tokens=*" %%j IN (target_product.txt) DO (
	IF EXIST "%ROOT_DIR%\%%j\pom.xml" (
		echo build_start:%%j
		CD "%%j"
		rel.bat > "%ROOT_DIR%\build_logs\%%j.log"
		CD "%ROOT_DIR%"
	)
)
PAUSE
