package jp.rough_diamond.commons.testing;

import jp.rough_diamond.commons.entity.ScalableNumber;
import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.testdata.UnitLoader;

public class DataLoadingTestCaseTest extends DataLoadingTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		UnitLoader.init();
	}

	public void testWhenInsert() throws Exception {
		Unit u = new Unit();
		u.setBase(u);
		u.setName("xxxxx");
		u.setRate(new ScalableNumber("1"));
		u.setScale(0);
		BasicService.getService().insert(u);
	}
	
	public void testWhenUpdate() throws Exception {
		Unit u = BasicService.getService().findByPK(Unit.class, 1L);
		u.setName(u.getName() + "xxx");
		BasicService.getService().update(u);
	}
	
	public void testWhenDelete() throws Exception {
		Unit u = BasicService.getService().findByPK(Unit.class, 5L);
		BasicService.getService().delete(u);
	}
	
	public void testWhenDeleteAll() throws Exception {
		BasicService.getService().deleteAll(Unit.class);
	}
	
	protected void tearDown() throws Exception {
		try {
			assertTrue("Unitがロールバック対象になっていません。", DBInitializer.modifiedClasses.contains(Unit.class));
		} finally {
			super.tearDown();
		}
	}
}
