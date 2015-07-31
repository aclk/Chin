@ECHO OFF
REM ------------------------------------------------------------------------------
REM 指定ディレクトリ下のプロダクトを
REM 一括回帰テストします
REM 
REM ※前提※
REM   maven2のbinフォルダにパスが通っていること。
REM   SPIRTS用site/リポジトリUPコマンド"rel.bat"が導入されていること。
REM 
REM ※利用方法※
REM   １．このバッチと同フォルダにビルドしたいプロジェクトをチェックアウト
REM   ２．同フォルダの"target_product.txt"を編集、ビルドしたいプロジェクトだけ列挙する。
REM   ３．バッチ実行
REM   ４．結果を"build_logs"の中に入っているログを元に確認。
REM ------------------------------------------------------------------------------

REM 対象プロダクトをこのバッチと同一フォルダの"target_product.txt"に列挙してください。

SET ROOT_DIR=%CD%

FOR /f "tokens=*" %%j IN (target_product.txt) DO (
	IF EXIST "%ROOT_DIR%\%%j\pom.xml" (
		echo build_start:%%j
		CD "%%j"
		rel.bat > "%ROOT_DIR%\build_logs\%%j.log"
		CD "%ROOT_DIR%"
	)
)
PAUSE
