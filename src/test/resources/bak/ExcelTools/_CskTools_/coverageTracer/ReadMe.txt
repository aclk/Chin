■coverageTracer

　□なにをするツール？
　　- ProjectSiteのCoverageReportからカバレッジ不通過の箇所のみ抽出して出力するツールツール
　
　□なにが便利？
　　- ソースが長い場合にカバレッジ不通過箇所のみチェック可能
　　- テキストに出力できるので、改修前後でのカバレッジ変化をdiffチェックすることが可能
　
　□使い方
　　１．ProjectをEclipseへインポートする
　　　ファイル　＞　インポート　＞　既存プロジェクトをワークスペースへ
　　　
　　　ルートディレクトリにcoverageTracerが配置されているディレクトリを指定して
　　　coverageTracerをインポートし、ビルドする
　　　
　　２．対象プロダクトの設定
　　　target.txtに対象プロダクトのURLを列挙する
　　
　　３．実行
　　　main/CoverageTracer を右クリックし、　実行　＞　Javaアプリケーション　で起動


　□出力サンプル
　
LimitRecordAndItemBMCImpl
Line:414 || 0        } finally {
Line:418 || 0        }
Line:441 || 0        } finally {
Line:445 || 0        }
Line:471 || 0        } finally {
Line:475 || 0        }
