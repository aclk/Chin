spool ReplaceAllObjects.log
-- �I���W�i��
-- * �SMV�폜
@all/DropMaterializedView.sql
-- * �SView�폜
@hand/dropAllView.sql
@all/CreateMaterializedView.sql
@all/CreateIndex.sql
@all/CommentOn.sql

-- �n���h
-- * View���̃}�e�r���[���폜
@hand/DropPartOfMaterializedViews.sql
-- * View��(�}�e�r���[�ō쐬�ς݂̂��̂̓G���[)
@hand/mkAllView.sql

-- �o�[�W����
@hand/UpdateVersion.sql

@hand/AddVersionTable.sql
commit;
spool off
exit
