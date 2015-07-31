declare
  sql1 varchar2(2000);
begin
  --ONLINEÇ∆ARCHIVEÇÃTableçÌèú
  for cu_table in (SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME LIKE '%ARCHIVE' OR  TABLE_NAME LIKE '%ONLINE') loop
    sql1 := 'DROP TABLE ' || cu_table.TABLE_NAME ||' CASCADE CONSTRAINTS PURGE';
    EXECUTE IMMEDIATE sql1;
  end loop;
  
  -- ARCHIVEópÇÃTriggerçÌèú
  for cu_trigger in (SELECT TRIGGER_NAME FROM USER_TRIGGERS WHERE TRIGGER_NAME LIKE '%ARCHIVE') loop
    sql1 := 'DROP TRIGGER ' || cu_trigger.TRIGGER_NAME;
    EXECUTE IMMEDIATE sql1;
  end loop;
end;
/
