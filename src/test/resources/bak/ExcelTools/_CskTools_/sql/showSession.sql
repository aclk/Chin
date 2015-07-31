select osuser, machine, terminal, program, schemaname, logon_time, sid, serial# from v$session
where schemaname = 'A1M4MEM075'
