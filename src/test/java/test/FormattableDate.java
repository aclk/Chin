package test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Formattable;
import java.util.Formatter;

@SuppressWarnings("serial")
public class FormattableDate extends Date implements Formattable {

    public void formatTo(Formatter formatter, int flags, int width,
            int precision) {
        // format_test1(formatter); // 実験結果→2015/4/2 17:59:52.43
        format_test2(formatter); // 実験結果→2015/04/02 18:00:08.561
        // format_test3(formatter); // 実験結果→2015/04/02 18:00:24.577
    }

    /** appendを使って出力する例 */
    void format_test1(Formatter formatter) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH) + 1;
        int dd = cal.get(Calendar.DATE);
        int hh = cal.get(Calendar.HOUR_OF_DAY);
        int mn = cal.get(Calendar.MINUTE);
        int ss = cal.get(Calendar.SECOND);
        int ms = cal.get(Calendar.MILLISECOND);

        Appendable a = formatter.out();
        try {
            a.append(Integer.toString(yy));
            a.append('/');
            a.append(Integer.toString(mm));
            a.append('/');
            a.append(Integer.toString(dd));
            a.append(' ');
            a.append(Integer.toString(hh));
            a.append(':');
            a.append(Integer.toString(mn));
            a.append(':');
            a.append(Integer.toString(ss));
            a.append('.');
            a.append(Integer.toString(ms));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** formatと%dを使う方式 */
    void format_test2(Formatter formatter) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH) + 1;
        int dd = cal.get(Calendar.DATE);
        int hh = cal.get(Calendar.HOUR_OF_DAY);
        int mn = cal.get(Calendar.MINUTE);
        int ss = cal.get(Calendar.SECOND);
        int ms = cal.get(Calendar.MILLISECOND);
        formatter.format("%04d/%02d/%02d %02d:%02d:%02d.%03d", yy, mm, dd, hh,
                mn, ss, ms);
    }

    /** formatと1$を使う方式 */
    void format_test3(Formatter formatter) {
        formatter.format("%1$tY/%1$tm/%1$td %1$tH:%1$tM:%1$tS.%1$tL", this);
        // formatter.format("%tY/%<tm/%<td %<tH:%<tM:%<tS.%<tL", this);
    }

    public static void main(String[] args) {
        Date date = new FormattableDate();

        System.out.printf("実験結果→%s%n", date);
        System.out.printf("実験結果→%tY/%<tm/%<td %<tH:%<tM:%<tS.%<tL%n",
                Calendar.getInstance());
        System.out.printf("実験結果→%1$tY/%1$tm/%1$td %1$tH:%1$tM:%1$tS.%1$tL%n",
                Calendar.getInstance());
    }
}
