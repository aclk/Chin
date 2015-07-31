開発用Readme
2009/11/06  仁茂田　正明

このツールはEclipseを使って開発していません。
そのため、以下のようなフォルダ構成になっています。

DB設定書き換え
├─classes
└─sources

１．sourcesフォルダにはソースファイルがあり、この中のcompile.batを起動するとコンパイルされます。
コンパイルされたclassファイルはclassesフォルダへ出力されます。

２．classesフォルダにはコンパイル前には２つのファイルが格納されています。
MANIFEST.MF　←こいつが無いとJarファイルをダブルクリックしても何も起こらない。
makejar.bat　←Jarファイル作成用バッチ

手順１でコンパイルした後でmakejar.batを起動すると上位フォルダにJarファイルが作成されます。

○その他
・Version
DBSetting.javaのL328ぐらいにソースで直書きしています。
なんか弄ったらreadme.txtに履歴を書くのと、Versionを上げてください。

