�����藚��
1.0     2009/08/5   �쏰    �V�K�쐬
1.1     2009/08/7   �쏰    Entity�̒��ɂ���MV���e�X�g�p(ON COMMIT)��DDL��K�p����悤�ɕύX���鏈����ǉ�
1.2     2009/08/20  �쏰    �s�v�ȃt�@�C��(DBA�p�t�@�C���H)���폜���鏈����ǉ�
1.3     2009/10/12  �쏰    MV�̍X�V������ON DEMAND��ON COMMIT�֕ϊ����鏈����ǉ�
1.4     2009/12/18  �쏰    SVN����擾���̃��W�b�N���C��
                                �V�p�X����擾�B�擾�ł��Ȃ��ꍇ�͋��p�X����擾�B
                                �i���Fhttp://m6f1b013/svn/prod/tags/ �� �V�Fhttp://m6f1b013/svn/prod/spirits/co/tags/�j
                            �yMulti��View���z�̏������o�[�W�����ύX�����̏ꍇ�����s����悤�ɏC��
1.5     2010/03/12  �쏰    1.SDOLI�X�N���v�g��Ώۂɒǉ�
                            2.SJO3600���\���PMV��SMV1370(EUC)��Ώۂɒǉ�
                            3.Trigger�X�N���v�g�����s���Ȃ��悤��Entity��ReplaceAllObjects.sql��␳
                            4.���i�\�֘AMV�X�N���v�g�����s���Ȃ��悤�� ReplaceAllObjects.sql��␳
                            5.���k�t�@�C���`����7z �`���ɌŒ�
                            6.Constants�t�@�C���𐮔�(VersionConstants,PCConstants,ParameterConstants)

�����O����
�E�uEntity�X�N���v�g�����c�[��.xls�v�̍ŐV�ł��擾
�E�uMV��View�ϊ��}�N��.xls�v�̍ŐV�ł��擾

�ESVN���C���X�g�[��

��EntityTookPack�̎��s�菇
�EVersionConstants.bat �̓��e���C��
�EPCConstants.bat �̓��e���C��

�EAll.bat �����s

    VersionConstants.bat �Őݒ肵���o�[�W�������A
    PCConstants.bat �Őݒ肵�� DDL_DIR �̏ꏊ�֍쐬���܂��B
    �u��۰����ް�ײ��ް�ޮ݁v�t�H���_���쐬����A���̉���DDL���쐬����܂��B
    �����`�F�b�N���AOK�ł���΁u��۰����ް�ײ��ް�ޮ݁v�t�H���_���ہXK�h���C�u�ɃR�s�[���Ċ����B
    
�����s���Ԃ̖ڈ�
Core�ȊO�̐V�E�������擾����20����

�����O�t�@�C���ɂ���
�ulogs\ExecuteToolPack.log�v�ɖ���o�͂��Ă��܂��B
�����Ƃ��Ďc�������ꍇ�́A�uAll.bat�v�̈ȉ��̉ӏ���؂�ւ��Ă��������B

------------------------------------------
REM SET LOGFILE=logs\ExecuteToolPack%LOGTIME%.log
SET LOGFILE=logs\ExecuteToolPack.log
------------------------------------------
