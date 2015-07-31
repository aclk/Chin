cd "%~dp0"
java -jar "%~dp0regrep.jar" %1 "%~dp0excludeList.txt" > "%~dp0result.txt"