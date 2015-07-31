package test.format;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Test {
    /****************************/
    /* 書式付き出力 */
    /* coded by Y.Suganuma */
    /****************************/
    public static void main(String args[]) {
         int a = 123, b = 12345678;
         double x = 123.4567, y = 3.14;
         String str1, str2, str3, str4, str5, str6;

         str1 = Format.Edit(x, 0, 4, 15, " ");
         str2 = Format.Edit(y, 0, 4, 15, "*");
         System.out.println(str1 + str2);
         str3 = Format.Edit(x, 1, 4, 15, " ");
         str4 = Format.Edit(y, 1, 4, 15, "*");
         System.out.println(str3 + str4);
         str5 = Format.Edit(a, 15, " ");
         str6 = Format.Edit(b, 15, "*");
         System.out.println(str5 + str6);

        // System.err.println("[" + new DecimalFormat("0000000000").format(115)
        // + "]");

        // ⇒33:11:11
        // System.err.printf("%3$d:%1$d:%d%n", 11, 22, 33);
        // ⇒11 22 22 11 11 33 33 44
        // System.err.printf("%d %d %<d %1$d %<s %d %<d %d%n", 11, 22, 33, 44);

        // Y 年（4桁） printf("%tY%n", new Date()); 2008
        // m 月（2桁。0埋め） printf("%tm%n", new Date()); 05
        // d 日（2桁。0埋め） printf("%td%n", new Date()); 20
        // H 時（2桁。0埋め） printf("%tH%n", new Date()); 02
        // M 分（2桁。0埋め） printf("%tM%n", new Date()); 34
        // S 秒（2桁。0埋め） printf("%tS%n", new Date()); 40
        // L ミリ秒（3桁。0埋め） printf("%tL%n", new Date()); 953
        // D 日付（%tm/%td/%tyと同義） printf("%tD%n", new Date()); 05/20/08
        // F 日付（%tY-%tm-%tdと同義） printf("%tF%n", new Date()); 2008-05-20
        // T 時刻（%tH:%tM:%tSと同義） printf("%tT%n", new Date()); 02:34:40
        // 日付書式を複数指定する場合、引数もその個数分必要。 Date d = new Date();
        // printf("%tY/%tm/%td%n", d, d, d); 2008/05/20
        // ひとつの日時に複数の日時要素を使いたい場合、引数番号を使用するのが楽かも。 Date d = new Date();
        // printf("%1$tY/%1$tm/%1$td%n", d); 2008/05/20

//        append(new File("c:\\tmp\\test.txt"), "value");
    }

    public static void append(File file, String content) {
        try {
            PrintStream ps = new PrintStream(new BufferedOutputStream(
                    new FileOutputStream(file, true)));
            ps.println(content);
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println(file);
        }
    }

    private void toConsole(Object o) {
        System.err.println("[" + o + "]");
    }

    /**********************/
    /* クラスFormatの定義 */
    /**********************/
    static class Format {

        /***********************************************/
        /* 整数を指定した長さの文字列に変換 */
        /* data : 整数データ */
        /* n : 文字列の長さ */
        /* f : 左側を埋める文字（「.」を除く） */
        /* return : 文字列 */
        /* 長すぎるときは「*」で埋める */
        /***********************************************/
        static String Edit(int data, int n, String f) {
            int i1, len;
            String str;

            str = String.valueOf(data);
            len = str.length();

            if (len != n) {
                // 長さが短い
                if (len < n) {
                    for (i1 = 0; i1 < n - len; i1++)
                        str = f + str;
                }
                // 長さが長すぎる
                else {
                    char c[] = new char[n];
                    for (i1 = 0; i1 < n; i1++)
                        c[i1] = '*';
                    str = String.copyValueOf(c);
                }
            }

            return str;
        }

        /***********************************************/
        /* 実数を指定した長さの文字列に変換 */
        /* （オーバーフローのチェックは行っていない） */
        /* data : 実数データ */
        /* sw : 表現方法 */
        /* =0 : ±xxxx.xx */
        /* =1 : ±x.xxxxxE±xxx */
        /* p : 小数点以下の桁数 */
        /* n : 文字列の長さ */
        /* f : 左側を埋める文字（「.」を除く） */
        /* return : 文字列 */
        /* 長すぎるときは「*」で埋める */
        /***********************************************/
        static String Edit(double data, int sw, int p, int n, String f) {
            double x;
            int i1, k, k1, len;
            String str, s1, s2, s3;

            str = String.valueOf(data); // 元のデータの文字列表現
            /*
             * 固定小数点表示
             */
            if (sw == 0) {
                // 入力データが固定小数点表示の場合
                if (str.indexOf('E') < 0 && str.indexOf('e') < 0) {
                    // 小数点以上と以下を取り出す
                    k = str.indexOf('.');
                    s1 = str.substring(0, k);
                    s2 = str.substring(k + 1);
                    // 全体の文字列の作成
                    str = Append(data, p, n, s1, s2, f);
                }
                // 入力データが浮動小数点表示の場合
                else {
                    // 仮数部と指数部
                    k = str.indexOf('E');
                    if (k < 0)
                        k = str.indexOf('e');
                    s1 = str.substring(0, k);
                    s2 = str.substring(k + 1);
                    // 指数部の値
                    k1 = Integer.parseInt(s2);
                    // 仮数部の小数点以上と以下
                    k = s1.indexOf('.');
                    s2 = s1.substring(k + 1);
                    s1 = s1.substring(0, k);
                    // 小数点以上と以下の調整
                    if (k1 != 0) {
                        if (k1 > 0) {
                            len = s2.length();
                            if (k1 == len) {
                                s1 += s2;
                                s2 = "0";
                            } else {
                                if (k1 < len) {
                                    s1 += s2.substring(0, k1 - 1);
                                    s2 = s2.substring(k1);
                                } else {
                                    s1 += s2;
                                    for (i1 = 0; i1 < k1 - len; i1++)
                                        s1 += "0";
                                    s2 = "0";
                                }
                            }
                        } else {
                            len = s1.length();
                            k1 = -k1;
                            if (k1 == len) {
                                s2 = s1 + s2;
                                s1 = "0";
                            } else {
                                if (k1 < len) {
                                    s2 = s1.substring(len - k1) + s2;
                                    s1 = s1.substring(0, len - k1);
                                } else {
                                    s2 = s1 + s2;
                                    for (i1 = 0; i1 < k1 - len; i1++)
                                        s2 = "0" + s2;
                                    s1 = "0";
                                }
                            }
                        }
                    }
                    // 全体の文字列の作成
                    str = Append(data, p, n, s1, s2, f);
                }
            }
            /*
             * 浮動小数点表示
             */
            else {
                // 入力データが固定小数点表示の場合
                if (str.indexOf('E') < 0 && str.indexOf('e') < 0) {
                    // 指数部と仮数部を決める
                    k1 = 0;
                    x = Math.abs(data);
                    if (x >= 10.0) {
                        while (x >= 10.0) {
                            x /= 10.0;
                            k1++;
                        }
                    } else {
                        if (x < 1.0) {
                            while (x < 1.0) {
                                x *= 10.0;
                                k1--;
                            }
                        }
                    }
                    // 指数部の調整
                    if (k1 >= 0) {
                        s3 = String.valueOf(k1);
                        len = s3.length();
                        if (len < 3) {
                            for (i1 = 0; i1 < 3 - len; i1++)
                                s3 = "0" + s3;
                        }
                        s3 = "+" + s3;
                    } else {
                        s3 = String.valueOf(-k1);
                        len = s3.length();
                        if (len < 3) {
                            for (i1 = 0; i1 < 3 - len; i1++)
                                s3 = "0" + s3;
                        }
                        s3 = "-" + s3;
                    }
                    // 仮数部の小数点以上と以下
                    str = String.valueOf(x);
                    k = str.indexOf('.');
                    s1 = str.substring(0, k);
                    s2 = str.substring(k + 1);
                    // 仮数部の調整
                    str = Append(x, p, n - 5, s1, s2, f);
                    // 桁数オーバー
                    k = str.indexOf('.');
                    if (k < 0) {
                        for (i1 = 0; i1 < 5; i1++)
                            str += "*";
                    }
                    // 全体の文字列
                    else
                        str = str + "E" + s3;
                }
                // 入力データが浮動小数点表示の場合
                else {
                    // 仮数部と指数部
                    k = str.indexOf('E');
                    if (k < 0)
                        k = str.indexOf('e');
                    s1 = str.substring(0, k);
                    s2 = str.substring(k + 1);
                    x = Double.parseDouble(s1);
                    // 指数部の調整
                    k1 = Integer.parseInt(s2);
                    if (k1 >= 0) {
                        s3 = String.valueOf(k1);
                        len = s3.length();
                        if (len < 3) {
                            for (i1 = 0; i1 < 3 - len; i1++)
                                s3 = "0" + s3;
                        }
                        s3 = "+" + s3;
                    } else {
                        s3 = String.valueOf(-k1);
                        len = s3.length();
                        if (len < 3) {
                            for (i1 = 0; i1 < 3 - len; i1++)
                                s3 = "0" + s3;
                        }
                        s3 = "-" + s3;
                    }
                    // 仮数部の小数点以上と以下
                    k = s1.indexOf('.');
                    s2 = s1.substring(k + 1);
                    s1 = s1.substring(0, k);
                    // 仮数部の調整
                    str = Append(x, p, n - 5, s1, s2, f);
                    // 桁数オーバー
                    k = str.indexOf('.');
                    if (k < 0) {
                        for (i1 = 0; i1 < 5; i1++)
                            str += "*";
                    }
                    // 全体の文字列
                    else
                        str = str + "E" + s3;
                }
            }

            return str;
        }

        /********************************************************/
        /* 小数点以上，及び，以下の文字列から全体の文字列を作成 */
        /* data : 実数データ */
        /* p : 小数点以下の桁数 */
        /* n : 文字列の長さ */
        /* s1 : 小数点以上の文字列 */
        /* s2 : 小数点以下の文字列 */
        /* f : 左側を埋める文字（「.」を除く） */
        /* return : 文字列 */
        /********************************************************/
        static String Append(double data, int p, int n, String s1, String s2,
                String f) {
            long L;
            int i1, k, len;
            String str;

            // 小数点以下の桁数の調整
            if (p == 0) {
                L = (long) data;
                if ((data - (double) L) >= 0.5) {
                    L = Long.parseLong(s1) + 1;
                    s1 = String.valueOf(L);
                }
            }

            else {

                len = s2.length();

                if (len != p) {
                    if (len < p) {
                        for (i1 = 0; i1 < p - len; i1++)
                            s2 += "0";
                    } else {
                        L = Long.parseLong(s2.substring(0, p));
                        k = Integer.parseInt(s2.substring(p, p + 1));
                        if (k >= 5)
                            L++;
                        s2 = String.valueOf(L);
                    }
                }
            }
            // 全体の長さの調整
            if (p == 0)
                str = s1;
            else
                str = s1 + "." + s2;

            len = str.length();

            if (len != n) {
                if (len < n) {
                    for (i1 = 0; i1 < n - len; i1++)
                        str = f + str;
                } else {
                    char c[] = new char[n];
                    for (i1 = 0; i1 < n; i1++)
                        c[i1] = '*';
                    str = String.copyValueOf(c);
                }
            }

            return str;
        }
    }
}
