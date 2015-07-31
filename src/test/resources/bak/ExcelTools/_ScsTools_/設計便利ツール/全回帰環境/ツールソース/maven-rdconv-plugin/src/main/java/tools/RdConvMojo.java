package tools;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;

import murata.co.producttest.managers.ManualProductTestManager;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.context.ApplicationContext;

/**
 * SPRITS用テストエビデンス→回帰テストデータ作成用プラグイン
 * @goal convert
 * @phase process-sources
 */
public class RdConvMojo extends AbstractMojo {

    /**
     * Location of the String.
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private String outputDirectory;

    /**
     * Location of the String.
     * @parameter expression="${project.build.testOutputDirectory}"
     * @required
     */
    private String testOutputDirectory;

    /**
     * 実行
     * {@inheritDoc}
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute() throws MojoExecutionException {

        try {

            // パラメータをテスト出力
            paramDebug();

            // クラスパスを足してみる
            addClassPath((URLClassLoader)SingletonBeanFactoryLocator.class
                .getClassLoader());

            // コンテキストの生成
            ApplicationContext context =
                (ApplicationContext)SingletonBeanFactoryLocator.getInstance(
                    "springconf/testBeanRefContext.xml").useBeanFactory(
                    "context").getFactory();

            // マニュアルテストマネージャーを生成する
            ManualProductTestManager manager =
                (ManualProductTestManager)context.getBean("manualTestManager");

            // 事後エビデンス出力処理を行う
            manager.convert("", "");

        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }

    }

    /**
     * 対象プロジェクトの"/target/classes" , "/target/test-classes"を動的にクラスパスに追加する。
     * @param classLoader 対象となるクラスローダ
     * @throws IOException
     */
    private void addClassPath(URLClassLoader classLoader) throws IOException {

        // クラスパス動的書換ユティリティを作成
        ClassPathModifier cpm = new ClassPathModifier(classLoader);

        // 自身環境のパス追加(targetの参照可能なClassフォルダ)
        if (outputDirectory != null) {

            super.getLog().debug((CharSequence)"classpath1:" + outputDirectory);
            cpm.addFile(outputDirectory);

            super.getLog().debug((CharSequence)"classpath2:" + testOutputDirectory);
            cpm.addFile(testOutputDirectory);

        }

    }

    /**
     * 主要パラメータのデバッグ出力
     * <br>
     * 挙動確認用にシステム変数など確認したいものをMojoの設定のlogオブジェクトで出力。
     */
    public void paramDebug() {
        super.getLog()
            .debug((CharSequence)"outputDirectory:" + outputDirectory);
        super.getLog()
        .debug((CharSequence)"testOutputDirectory:" + testOutputDirectory);
        super.getLog().debug(
            (CharSequence)"userDir:" + System.getProperty("user.dir"));
        super.getLog().debug(
            (CharSequence)"java.class.path"
                + System.getProperty("java.class.path"));
    }

    /**
     * 対象フォルダ(コンソール実行 or 任意で指定したい場合用)
     * @param outputDirectory 対象となるフォルダ({project}\target)
     */
    public void setDirectory(String directory) {
        String target = directory + File.separator + "target";
        this.outputDirectory = target + File.separator + "classes";
        this.testOutputDirectory = target + File.separator + "test-classes";;
    }

    /**
     * コンソール実行時の処理
     * @param args コンソール引数
     * @throws MojoExecutionException 全てのエラー
     */
    public static void main(String[] args) throws MojoExecutionException {

        RdConvMojo self = new RdConvMojo();
        if (args.length > 0) {
            self.setDirectory(args[0]);
        }
        self.execute();
    }

}
