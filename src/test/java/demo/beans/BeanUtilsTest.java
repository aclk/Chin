package demo.beans;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * BeanUtils の使い方.
 *
 * 基本的に, データ型を String に変換して返します. 変換したくない場合は, {@link PropertyUtils} を使います.
 *
 * @author Kentaro Ohkouchi
 * @version $Revision$ $Date$
 */
public class BeanUtilsTest {
	private FooBar fooBar;

	@Before
	public void before() {
		fooBar = new FooBar();
	}

	/**
	 * {@link BeanUtils#cloneBean(java.lang.Object)} の使い方.
	 *
	 * Cloneable を implements してなくても Bean の複製ができます.
	 */
	@Test
	public final void testCloneBean() throws Exception {
		Bar bar = new Bar("bar");
		String excepted = "bar";
		// bar を複製する
		Bar actual = (Bar) BeanUtils.cloneBean(bar);
		assertEquals(excepted, actual.getExample());
	}

	/**
	 * {@link BeanUtils#copyProperties(java.lang.Object, java.lang.Object)} の使い方
	 *
	 * 同じプロパティ名のデータを orig から dest へコピーします. この場合, example というプロパティが双方に存在するので,
	 * example のデータがコピーされます.
	 */
	@Test
	public final void testCopyProperties() throws Exception {
		Bar orig = new Bar();
		ExampleClass dest = new ExampleClass();
		// 同じプロパティ名のデータを orig から dest へコピーする.
		BeanUtils.copyProperties(dest, orig);
		// orig が Map の場合は, プロパティ名と Map の key が一致した場合コピーされる.
		Map<String, String> map = new HashMap<String, String>();
		map.put("foo", "foo");
		BeanUtils.copyProperties(dest, map);
		// example の内容がコピーされる
		assertEquals(orig.getExample(), dest.getExample());
		// Map の内容もコピーされる
		assertEquals(map.get("foo"), dest.getFoo());
		// baz は null のまま
		assertNull(dest.getBaz());
	}

	/**
	 * {@link BeanUtils#copyProperty(java.lang.Object, java.lang.String, java.lang.Object)}
	 * の使い方.
	 *
	 * 指定したプロパティのデータをコピーします. ネストしたプロパティもコピーできます.
	 */
	@Test
	public final void testCopyProperty() throws Exception {
		Bar bar = new Bar();
		ExampleClass bean = new ExampleClass();
		// 指定したプロパティのデータを bean にコピーします.
		BeanUtils.copyProperty(bean, "foo", "foo");
		// ネストした Bean もコピーできます.
		BeanUtils.copyProperty(bean, "bar", bar);
		assertEquals("foo", bean.getFoo());
		assertEquals("example", bean.getBar().getExample());
	}

	/**
	 * {@link BeanUtils#describe(java.lang.Object)} の使い方.
	 *
	 * bean のプロパティ名を key, プロパティのデータを value とした Map を返します. プロパティのデータ型に関わらず, value
	 * は String になります...
	 */
	@Test
	public final void testDescribe() throws Exception {
		// value に格納されるデータを String にキャストして Map で返します.
		Map map = BeanUtils.describe(fooBar);
		assertEquals("foo", map.get("foo"));
	}

	/**
	 * {@link BeanUtils#getArrayProperty(java.lang.Object, java.lang.String)}
	 * の使い方.
	 *
	 * プロパティ名を指定して, プロパティの値を String の配列で返します. プロパティのデータ型が配列の場合は, String
	 * の配列に変換して返します. コレクションの場合も, String の配列に変換して返します. それ以外のデータ型の場合は,
	 * {@link Object#toString()} の値を String の配列に格納して返します.
	 */
	@Test
	public final void testGetArrayProperty() throws Exception {
		// プロパティ名を指定して, String の配列を返します.
		String[] strings = BeanUtils.getArrayProperty(fooBar, "strings");
		assertArrayEquals(fooBar.getStrings(), strings);
		// プロパティが配列では無い場合も String の配列が返ってきます.
		String[] foo = BeanUtils.getArrayProperty(fooBar, "foo");
		assertArrayEquals(new String[] { "foo" }, foo);
	}

	/**
	 * {@link BeanUtils#getIndexedProperty(java.lang.Object, java.lang.String)}
	 * の使い方.
	 *
	 * プロパティ名[インデックス番号] のように, インデックスを保持するプロパティから, インデックス番号を指定して 値を String で返します.
	 * インデックスを保持しないプロパティは, {@link IllegalArgumentException} になります.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testGetIndexedPropertyObjectString() throws Exception {
		// プロパティ名とインデックス番号を指定して String の値を返します.
		String actual = BeanUtils.getIndexedProperty(fooBar, "strings[2]");
		assertEquals("ghi", actual);
		// インデックスを保持しないプロパティは IllegalArgumentException になります.
		String foo = BeanUtils.getIndexedProperty(fooBar, "foo[0]");
	}

	/**
	 * {@link BeanUtils#getIndexedProperty(java.lang.Object, java.lang.String, int)}
	 * の使い方.
	 *
	 * {@link BeanUtils#getIndexedProperty(java.lang.Object, java.lang.String)}
	 * に 似ていますが, インデックス番号を int で指定するところが異なります.
	 */
	@Test
	public final void testGetIndexedPropertyObjectStringInt() throws Exception {
		// プロパティ名とインデックス番号を指定して, String の値を取得します.
		String actual = BeanUtils.getIndexedProperty(fooBar, "strings", 2);
		assertEquals("ghi", actual);
	}

	/**
	 * {@link BeanUtils#getMappedProperty(java.lang.Object, java.lang.String)}
	 * の使い方.
	 *
	 * プロパティが Map の場合, プロパティ名(key) のように指定して, 値を String で返します. プロパティが Map 以外の場合は,
	 * null を返します.
	 */
	@Test
	public final void testGetMappedPropertyObjectString() throws Exception {
		// プロパティ名と key を指定して, String の値を返します.
		String actual = BeanUtils.getMappedProperty(fooBar, "map(abc)");
		assertEquals("abc of value", actual);
		// プロパティが Map 以外の場合は, null を返します.
		assertNull(BeanUtils.getMappedProperty(fooBar, "foo(key)"));
	}

	/**
	 * {@link BeanUtils#getMappedProperty(java.lang.Object, java.lang.String, java.lang.String)}
	 * の使い方.
	 *
	 * {@link BeanUtils#getMappedProperty(java.lang.Object, java.lang.String)} に
	 * 似ていますが, key を第3引数で指定するところが異なります.
	 */
	@Test
	public final void testGetMappedPropertyObjectStringString()
			throws Exception {
		// プロパティ名と key を指定して, String の値を返します.
		String actual = BeanUtils.getMappedProperty(fooBar, "map", "abc");
		assertEquals("abc of value", actual);
	}

	/**
	 * {@link BeanUtils#getNestedProperty(java.lang.Object, java.lang.String)}
	 * の使い方.
	 *
	 * ネストした Bean のプロパティをドットシンタックスで取得できます. 値は String で返します.
	 */
	@Test
	public final void testGetNestedProperty() throws Exception {
		// ネストしたプロパティを取得します.
		String actual = BeanUtils.getNestedProperty(fooBar, "bar.example");
		assertEquals("example", actual);
	}

	/**
	 * {@link BeanUtils#getProperty(java.lang.Object, java.lang.String)} の使い方.
	 *
	 * 便利屋さんです. プロパティのデータ型に合わせた方法で, プロパティ名やインデックスを指定することで, プロパティの値を String
	 * で取得できます.
	 */
	@Test
	public final void testGetProperty() throws Exception {
		// プロパティ名を指定して String の値を返します.
		String foo = BeanUtils.getProperty(fooBar, "foo");
		assertEquals("foo", foo);
		// プロパティ名とインデックス番号を指定して String の値を返します.
		String strings = BeanUtils.getProperty(fooBar, "strings[2]");
		assertEquals("ghi", strings);
		// プロパティ名と key を指定して, String の値を返します.
		String map = BeanUtils.getProperty(fooBar, "map(abc)");
		assertEquals("abc of value", map);
		// ネストしたプロパティを取得します.
		String actual = BeanUtils.getProperty(fooBar, "bar.example");
		assertEquals("example", actual);
	}

	/**
	 * {@link BeanUtils#getSimpleProperty(java.lang.Object, java.lang.String)}
	 * の使い方.
	 *
	 * プロパティ名を指定して, String の値を返します.
	 * {@link BeanUtils#getProperty(java.lang.Object, java.lang.String)} とは挙動が
	 * 異なるため, 単純な Bean の値を取得するのに適しています.
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testGetSimpleProperty() throws Exception {
		// プロパティ名を指定して String の値を返します.
		String foo = BeanUtils.getSimpleProperty(fooBar, "foo");
		assertEquals("foo", foo);
		// プロパティが配列の場合は先頭の値を返します.
		String strings = BeanUtils.getSimpleProperty(fooBar, "strings");
		assertEquals("abc", strings);
		// プロパティが Map の場合はMap#toString() の値を返します.
		String map = BeanUtils.getSimpleProperty(fooBar, "map");
		assertEquals("{abc=abc of value, ghi=ghi of value, def=def of value}",
				map);
		// ネストしたプロパティは取得できません...
		try {
			BeanUtils.getSimpleProperty(fooBar, "bar.example");
		} catch (IllegalArgumentException e) {
			throw e;
		}
	}

	/**
	 * {@link BeanUtils#populate(java.lang.Object, java.util.Map)} の使い方.
	 *
	 * Map に格納した値を key に対応する Bean のプロパティに設定します. 値は, String にキャストして設定します.
	 */
	@Test
	public final void testPopulate() throws Exception {
		Map<String, Comparable> map = new HashMap<String, Comparable>();
		map.put("foo", "foo of value");
		map.put("bar.example", new Integer(1000));
		// Map の key に対応するプロパティに値を設定します.
		BeanUtils.populate(fooBar, map);
		assertEquals("foo of value", fooBar.getFoo());
		// Integer などの値も String にキャストされます.
		assertEquals("1000", fooBar.getBar().getExample());
	}

	/**
	 * {@link BeanUtils#setProperty(java.lang.Object, java.lang.String, java.lang.Object)}
	 * の使い方. <br/>
	 * {@link BeanUtils#getProperty(java.lang.Object, java.lang.String)}
	 * と同様の方法で, プロパティ名を指定することで, 任意のオブジェクトを String にキャストして Bean に設定します.
	 */
	@Test
	public final void testSetProperty() throws Exception {
		// プロパティ名を指定して String の値を設定します.
		BeanUtils.setProperty(fooBar, "foo", "foo of value");
		assertEquals("foo of value", fooBar.getFoo());
		// プロパティ名とインデックス番号を指定して String の値を設定します.
		BeanUtils.setProperty(fooBar, "strings[2]", "strings of two");
		String[] strings = fooBar.getStrings();
		assertEquals("strings of two", strings[2]);
		// プロパティ名と key を指定して, String の値を設定します.
		BeanUtils.setProperty(fooBar, "map(abc)", new Integer(1000));
		assertEquals("1000", fooBar.getMap().get("abc"));
		// ネストしたプロパティを設定します.
		BeanUtils.setProperty(fooBar, "bar.example", "bar of example");
		assertEquals("bar of example", fooBar.getBar().getExample());
	}
}