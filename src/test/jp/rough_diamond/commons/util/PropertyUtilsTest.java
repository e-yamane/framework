package jp.rough_diamond.commons.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

import junit.framework.TestCase;

public class PropertyUtilsTest extends TestCase {
	@SuppressWarnings("unchecked")
	public void testCollectionToCollection() throws Exception {
		Bean1 bean1 = new Bean1();
		bean1.setList(new ArrayList<String>(Arrays.asList(new String[]{"abc", "xyz"})));
		Bean1 bean2 = new Bean1();
		PropertyUtils.copyProperties(bean1, bean2);
		assertEquals("コピーに失敗しています。", 2, bean2.getList().size());
		assertEquals("コピーに失敗しています。", "abc", bean2.getList().get(0));
		assertEquals("コピーに失敗しています。", "xyz", bean2.getList().get(1));
	}
	
	@SuppressWarnings("unchecked")
	public void testCollectionToArray() throws Exception {
		Bean1 bean1 = new Bean1();
		bean1.setList(Arrays.asList(new String[]{"abc", "xyz"}));
		Bean2 bean2 = new Bean2();
		PropertyUtils.copyProperties(bean1, bean2);
		assertEquals("コピーに失敗しています。", 2, bean2.getList().length);
		assertEquals("コピーに失敗しています。", "abc", bean2.getList()[0]);
		assertEquals("コピーに失敗しています。", "xyz", bean2.getList()[1]);
	}
	
	public static class Bean1 {
		List<String> list;

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}
	}
	
	public static class Bean2 {
		String[] list;

		public String[] getList() {
			return list;
		}

		public void setList(String[] list) {
			this.list = list;
		}
	}
	
	public static void main(String[] args) {
		int[] array = new int[1];
		Array.set(array, 0, new Integer(1));
		System.out.println(array[0]);
	}
}
