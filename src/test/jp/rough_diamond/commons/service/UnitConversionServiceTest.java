package jp.rough_diamond.commons.service;

import junit.framework.TestCase;

public class UnitConversionServiceTest extends TestCase {
	public void testGetInstance() {
		UnitConversionService service = UnitConversionService.getService();
		assertTrue("デフォルトのインスタンスタイプが誤っています。", service instanceof SimpleUnitConversionService);
	}
}
