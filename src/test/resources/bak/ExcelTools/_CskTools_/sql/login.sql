set line 200
set serveroutput on
set feedback off

set heading off

SELECT 'Now Logon to ' || username || '@' || global_name
FROM user_users, global_name;

set term off
column username new_value username1
column instance new_value instance1

SELECT username username, LOWER(RTRIM(instance_name)) instance
FROM user_users, v$instance ;

set sqlprompt '&username1@&instance1> '
set term on
set heading on
