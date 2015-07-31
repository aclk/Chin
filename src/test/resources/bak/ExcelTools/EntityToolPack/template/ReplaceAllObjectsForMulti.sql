spool ReplaceAllObjects.log
-- オリジナル
-- * 全MV削除
@all/DropMaterializedView.sql
-- * 全View削除
@hand/dropAllView.sql
@all/CreateMaterializedView.sql
@all/CreateIndex.sql
@all/CommentOn.sql

-- ハンド
-- * View化のマテビューを削除
@hand/DropPartOfMaterializedViews.sql
-- * View化(マテビューで作成済みのものはエラー)
@hand/mkAllView.sql

-- バージョン
@hand/UpdateVersion.sql

@hand/AddVersionTable.sql
commit;
spool off
exit
