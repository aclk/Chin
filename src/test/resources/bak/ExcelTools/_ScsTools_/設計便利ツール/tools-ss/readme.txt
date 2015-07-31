【tools-ss】
ver1.0 2008/5/20 SCS榊原
ver1.1 2008/6/13 SCS榊原	MessageCopyを追加

今までに作成されたツール類の寄せ集めです。
■できること
1.CriteriaEvidenceCreator			Criteriaのエビとりに使用。
2.CriteriaEvidenceCreatorAdaptor	設定ファイルの指定が不要になります。
3.TrialImpl							BMCなどの試用ができます。テストデータ作成時などに利用。
4.LineCounter						指定したフォルダ以下のプロジェクトにあるCriteriaの行数を数える。
5.MessageCopy						build.jclを使わずに、RMSのメッセージをコピーする

■導入方法
Eclipseパッケージエクスプローラで
右クリック > インポート > 「一般」 > 「既存プロダクトをワークスペースへ」
「アーカイブファイルの選択」

以下ファイルのユーザ名/パスワード/(スキーマ名) を自分のものに編集
/src/main/resources/springconf/applicationContext-datasource-local.xml
/src/main/resources/jdbc.properties
/src/main/resources/input.txt 1行目にスキーマ名

■解説
1.CriteriaEvidenceCreator(CEC)
→Javadocにある解説を読んでください。
  各プロジェクトへコピーする必要はありません。
  実行方法
  ソースファイル右クリック＞ 実行＞Javaアプリケーション

2.CriteriaEvidenceCreatorAdaptor
CECを便利に利用するためのアダプタです。
-Main版→CriteriaEvidenceCreatorの解説と同等です。
 input.txtファイルだけ、場所固定になります(src/main/resources/input.txt)
 ソースファイル右クリック＞ 実行＞Javaアプリケーション

-API版
 普通の人はあまり利用しないです・・・
 後述のTraialImpl内で利用しています。

3.TrialImpl
build.jcl を起動することで、
execute()メソッドを呼び出します。
内容は各自好きなように編集してください。以下のことが可能です。
・DACの各メソッド実行
・Criteria実行
・DB書き込み(トランザクション対応)
・BMC/PSC呼び出し
・これらの処理のログ出力
・事前処理でのデータ投入(CriteriaEvidenceCreator API版利用)


4.LineCounter
→Javadocにある解説を読んでください。
 普通の人はあまり利用しないです。

5.MessageCopy
実行>Javaアプリケーション
プログラム引数に"diff"または"all"を指定して実行する。
(allで10分ほどかかります)

■TIPS
TrialImplで
BMCのinputVO・outputVOが自動的にログ出力される設定

log4j.xml で以下の記述をコメント外す

  <category name="murata.co.inspection.BMCLoggingAspect">
      <priority value="DEBUG" />
  </category>

■注意
・依存先ライブラリはpom.xmlによるので、注意が必要です(デフォルトではmurata-ss-saの最新)
・このプロジェクトは諸事情によりsvn登録できません。
  バグフィックスなどは、サーバー上のzipを更新していく予定です。