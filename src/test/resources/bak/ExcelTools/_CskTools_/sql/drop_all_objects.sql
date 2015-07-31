declare
  sql1 varchar2(2000);
begin
  -- TableçÌèú
  for cu_table in (SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME NOT IN (SELECT MVIEW_NAME FROM USER_MVIEWS)) loop
    sql1 := 'DROP TABLE ' || cu_table.TABLE_NAME ||' CASCADE CONSTRAINTS PURGE';
    EXECUTE IMMEDIATE sql1;
  end loop;
  -- ViewçÌèú
  for cu_view in (SELECT VIEW_NAME FROM USER_VIEWS) loop
    sql1 := 'DROP VIEW ' || cu_view.VIEW_NAME;
    EXECUTE IMMEDIATE sql1;
  end loop;
  -- Materialized ViewçÌèú
  for cu_mview in (SELECT MVIEW_NAME FROM USER_MVIEWS) loop
    sql1 := 'DROP MATERIALIZED VIEW ' || cu_mview.MVIEW_NAME;
    EXECUTE IMMEDIATE sql1;
  end loop;
  -- TriggerçÌèú
  for cu_trigger in (SELECT TRIGGER_NAME FROM USER_TRIGGERS) loop
    sql1 := 'DROP TRIGGER ' || cu_trigger.TRIGGER_NAME;
    EXECUTE IMMEDIATE sql1;
  end loop;
  -- SequenceçÌèú
  for cu_seq in (SELECT SEQUENCE_NAME FROM USER_SEQUENCES) loop
    sql1 := 'DROP SEQUENCE ' || cu_seq.SEQUENCE_NAME;
    EXECUTE IMMEDIATE sql1;
  end loop;
end;
/
