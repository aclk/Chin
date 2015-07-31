@echo off
setlocal
rem maven close忘れチェック用クリーンアップバッチ
rem Ver1.0 CSK D.Saruhashi

if exist src\test\java\CloseCheckAspect.java (
	del src\test\java\CloseCheckAspect.java
)
if exist pom.xml.bak (
	copy pom.xml.bak pom.xml > nul
	del pom.xml.bak
)
if exist src\test\resources\springconf\applicationContext-product-test.xml.bak (
	copy src\test\resources\springconf\applicationContext-product-test.xml.bak src\test\resources\springconf\applicationContext-product-test.xml > nul
	del src\test\resources\springconf\applicationContext-product-test.xml.bak
)
if exist src\test\resources\log4j.xml.bak (
	copy src\test\resources\log4j.xml.bak src\test\resources\log4j.xml > nul
	del src\test\resources\log4j.xml.bak
)
if exist src\main\resources\log4j.xml.bak (
	copy src\main\resources\log4j.xml.bak src\main\resources\log4j.xml > nul
	del src\main\resources\log4j.xml.bak
)

endlocal
