package demo;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.junit.Test;

public class TestBean {
	public class Hoge {
		int a;
		String b;
		Date c;

		public Hoge() {
		}

		/**
		 * @return a
		 */
		public int getA() {
			return a;
		}

		/**
		 * @param a
		 *            セットする a
		 */
		public void setA(int a) {
			this.a = a;
		}

		/**
		 * @return b
		 */
		public String getB() {
			return b;
		}

		/**
		 * @param b
		 *            セットする b
		 */
		public void setB(String b) {
			this.b = b;
		}

		/**
		 * @return c
		 */
		public Date getC() {
			return c;
		}

		/**
		 * @param c
		 *            セットする c
		 */
		public void setC(Date c) {
			this.c = c;
		}

		@Override
		public String toString() {
			return super.toString() + " => a=\'" + a + "\' ,b=\'" + b
					+ "\' ,c=\'" + c + "\'";
		}
	}

	@Test
	public void test() {
		Hoge hoge1 = new Hoge();
		hoge1.setA(1);
		hoge1.setB("a");
		hoge1.setC(new Date());
		Hoge hoge2 = new Hoge();
		try {
			// Date Null対策
			// Dateがnullの場合エラーが起きるためNull OK に設定
			ConvertUtils.register(new DateConverter(null), Date.class);
			System.err.println("コピー元:" + hoge1);
			System.err.println("初期:" + hoge2);
			// hoge2がコピー先、hoge1がコピー元
			BeanUtils.copyProperties(hoge2, hoge1);
			System.err.println("コピー先:" + hoge2);
			// 設定を元に戻す
			ConvertUtils.deregister();
		} catch (IllegalAccessException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}