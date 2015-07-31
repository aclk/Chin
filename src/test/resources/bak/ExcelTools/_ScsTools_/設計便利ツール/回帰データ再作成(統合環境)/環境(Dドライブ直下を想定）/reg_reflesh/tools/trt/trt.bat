cd /D "%~dp0"
rem java -jar "%~dp0lib\digger-0.0.1.jar" ss_scs.trt.TestReportTrailer %1 %2 > "trt-result.txt"
java -jar "%~dp0lib\digger-0.0.1.jar" %1 %2 > "trt-result.txt"