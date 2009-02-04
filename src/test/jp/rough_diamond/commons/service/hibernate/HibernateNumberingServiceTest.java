package jp.rough_diamond.commons.service.hibernate;

import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.commons.service.NumberingService;
import jp.rough_diamond.commons.testdata.NumberingLoader;
import jp.rough_diamond.commons.testdata.UnitLoader;
import jp.rough_diamond.commons.testing.DataLoadingTestCase;
import jp.rough_diamond.framework.service.ServiceLocator;

public class HibernateNumberingServiceTest extends DataLoadingTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		UnitLoader.init();
		NumberingLoader.init();
	}
	
	public void testGetNumberByString() throws Exception {
		NumberingService service = ServiceLocator.getService(HibernateNumberingService.class);
		assertEquals("�ԋp�l������Ă��܂��B", service.getNumber("yamane"), 1L);
		assertEquals("�ԋp�l������Ă��܂��B", service.getNumber("jp.rough_diamond.commons.entity.Unit"), 3L);
		assertEquals("�ԋp�l������Ă��܂��B", service.getNumber("hoge"), 11L);
	}
	
	public void testGetNumberByClass() throws Exception {
		NumberingService service = ServiceLocator.getService(HibernateNumberingService.class);
		assertEquals("�ԋp�l������Ă��܂��B", service.getNumber(Unit.class), 6L);
	}
}
