package jp.rough_diamond.commons.service;

import junit.framework.TestCase;

public class UnitConversionServiceTest extends TestCase {
	public void testGetInstance() {
		UnitConversionService service = UnitConversionService.getService();
		assertTrue("�f�t�H���g�̃C���X�^���X�^�C�v������Ă��܂��B", service instanceof SimpleUnitConversionService);
	}
}
