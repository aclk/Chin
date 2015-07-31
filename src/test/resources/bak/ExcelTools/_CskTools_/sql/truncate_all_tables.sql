set timi on
set serveroutput on
declare
CURSOR CUR1 IS SELECT /*+ RULE */ TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME NOT IN ('CR9020', 'DB_VERSION', 'RESOURCE_VERSION'); -- CR9020: message resource table
begin
FOR CUR_RECORD IN CUR1 LOOP
	BEGIN
		EXECUTE IMMEDIATE 'TRUNCATE TABLE "'||CUR_RECORD.TABLE_NAME||'"';
	EXCEPTION
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Truncate Error[' || CUR_RECORD.TABLE_NAME || '][' || sqlcode || '][' || sqlerrm || ']');
	END;
END LOOP;
end;
/
set timi off
