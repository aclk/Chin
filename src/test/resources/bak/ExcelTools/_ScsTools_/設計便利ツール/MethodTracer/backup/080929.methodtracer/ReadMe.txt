2008年1月30日 17:27:18 K.Miura

MethodTracer(プロダクトテストFW改造キット) 使用方法

■はじめに

これは、BSPのを使用するプロダクト・BSPのプロダクトテスト時に、BSPの呼出時の引数、戻り値を
テストケースごとに記録させるために作られた手法である。
また、通常プロダクト(BTP,BXP)のEditor・プロダクトテストFWにも使用することができる。

※まだ確定していない、2008年5月12日現在の暫定機構である。

以下に導入・使用方法を解説する。

(サンプルが必要な場合、オンラインなら「SOO0090」が導入済み。）

■導入方法

1,Eclipseのプロジェクト内、src/test/java に MethodTracer.java を追加してください。
MethodTracer.javaのソースの最上部、自身パッケージは、自身の環境にあわせ変更してください。

2,src/test/resource/springconf/ に、methodTracer.xml をコピーしてください。
methodTracer.xml を開き、以下を編集してください。
・MethodTracerのパッケージ名を、1で変更したパッケージ名へと変更してください。
・id="BspAutoProxy"の"beanNames"プロパティに、入出力したいBSPやBMC、PSCなどの
取得したいSpring上のDI定義のIDを記述してください。(*Bspなど、ワイルドカードも使えます)
・id="methodTracer"の"eviOutputPath"プロパティに、ファイルを出力したいディレクトリを指定してください。

3,自身プロジェクトの、src/test/resource/springconf/testBeanRefContext.xml を編集してください。

大抵のプロダクトでは、testBeanRefContext.xmlの末尾に、

	<!-- アプリケーション定義ファイル -->
	<value>murata/ss/ss/bsp/springconf/[プロダクトＩＤ]-bean.xml</value>
	<value>murata/ss/ss/bsp/springconf/[プロダクトＩＤ]-criteria.xml</value>

のような記述があると思いますが、その上に

	<!-- ※テスト用。ログのインターセプターの仕込み。 -->
	<value>springconf/methodTracer.xml</value>

を追加してください。


4,src/test/java のテストクラス(～Test.java or ～TestRunner.java)に、

    /**
     *テストケース毎の初期処理
     */
    @Before
    public void setUpForMethodTracer() throws Exception {
        // インターセプター用のファイル吐きクラスを初期化。
        try {
	        MethodTracer tracer = (MethodTracer)context.getBean("methodTracer");
	        tracer.initOutMessage("BSPの引数・戻り値の記録開始。");
        } catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {}
    }
	
を追加してください。

上記、①で変更した出力場所に、引数・戻り値VOのtoString()を記録した、
  
  bspAndBmcParams1.log bspAndBmcParams2.log bspAndBmcParams3.log …
  
というファイルが保存されていきます。


■MqTracerについて

MqTracerは、MethodTracerの特化版であり、MessagingPSC.send()メソッドを監視し、
その引数の構成から、JUnitテストクラス用の確認ソースを出力するものです。


■MqTracerの導入方法

上記、MethodTracerが導入されていることが前提です。
methodTracer.xml のクラス指定
    <bean id="methodTracer" class="murata.ss.ss.bsp.MethodTracer">
から
    <bean id="methodTracer" class="murata.ss.ss.bsp.MqTracer">
へと変更してください。
また、BspAutoProxyタグの<property name="beanNames">リスト内に、
	        <value>*essagingPSC</value>
の記述があることを確認してください。
導入後実行すると、MethodTracer時の指定出力フォルダに、
新たに、"mqTestSrc(番号).txt"というテキストファイルが出力されます。

■作業上の注意

通常のMethodTracerによる出力ファイルで値の内容を確認、
問題なければテストクラスの個々ケースへ貼り付けてください。

・注意
既知の問題として、複数回の呼出が行われる場合でも、テストソースでは必ず
	
	// 件数確認
	assertEquals(1, messages.size());
	
と出力されてしまいます。複数件のMQ呼出がある場合は、
貼り付けた後、手作業で変更してください。



■改定履歴

2008/07/23 K.Miura 
	直にClassだけのものに対しインターセプトする際に不具合が出るため、
	methodTracer.xml のproxyTargetClassのtrue指定を削除。