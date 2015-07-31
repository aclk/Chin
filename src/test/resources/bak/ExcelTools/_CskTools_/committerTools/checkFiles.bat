@echo off
rem check filesバッチ

rem 引数チェック
if "%1" == "" goto usage

rem ファイルチェック実行
:exectest
if "%1" == "" goto end
if exist %1\nul (

cd %1

echo ☆★☆★☆★☆★ [%1] start ☆★☆★☆★☆★
more src\site\apt\index.apt
pause
more pom.xml
echo ☆★☆★☆★☆★ [%1] end  ☆★☆★☆★☆★
pause

cd ..
) else echo [%time%] "%1" プロジェクトが存在しません.
shift /1
goto exectest

rem USAGE
:usage

echo usage: checkFiles [productId] [productId] ...
echo.

goto end

rem 終了
:end

