■改定履歴
1.0		2010/03/21	川床	新規作成
1.1		2010/04/01	福島	使用上の注意及びツールの作成経緯を追記


■事前準備
　※本ツールは「summaryChecker」「coverageTracer」を内部で実行します

・「siteChecker.bat」の環境設定を確認。必要に応じて修正

■使い方
 ＜siteChecker＞
　・実行対象のプロジェクトの直下に「siteChecker.bat」をおいてファイルをダブルクリックして実行
　　※もしくはPathを通しておけばいちいちプロジェクトの直下におく必要がないので便利
　　
　＜remoteSiteChecker＞
　・こちらはsiteとしてupされているProjectSiteより取得するバッチ
　
　　eg) remoteSiteChecker sjo0010
　　


■使用上の注意点

このツールは、Eclipse上から使用することは想定されていない。
よって、使用する際は、エクスプローラからworkspaceを開いた後、
そこから実行して下さい

■出力サンプル
--------[ｺｺｶﾗ]-------------
■Test Summary 
ProductID	Tests	Errors	Failures	Skipped	Success Rate	Time
murata-soo5150	40	0	0	0	100%	156.266
■Coverage Check 
Soo5150_01EditorImpl
Line:373 || 0        } finally {
Line:379 || 0        }
Line:502 || 0        } finally {
Line:507 || 0        }
Line:563 || 0        } finally {
Line:568 || 0        }
Line:3240 || 0        } finally {
Line:3242 || 0        }
Line:3284 || 0        } finally {
Line:3289 || 0        }
Line:3326 || 0        } finally {
Line:3331 || 0        }
Line:3368 || 0        } finally {
Line:3373 || 0        }
Line:3408 || 0        } finally {
Line:3413 || 0        }
Line:3553 || 0                    } finally {
Line:3558 || 0                    }
Line:3664 || 0        } finally {
Line:3669 || 0        }
--------[ｺｺﾏﾃﾞ]-------------

～～～～～「siteChecker」の作成経緯～～～～～～

■何ができるの？
実行対象プロダクトのプロダクトテスト結果＆Coverage状況を
テキストファイルで記述してくれる。
site情報をテキストで残してくれるので非常に便利です。
しかも出力形式が『[プロダクトID]-[YYYYMMDD]_[time].log』という形なので、
記録として完全に残ります。


■なぜこれが必要か？
問題・変更対応時にテストが全部OKなのか？Coverageが100%になのか？
ということを記録しておける。
※できていない場合でも残課題として形に残せる。
