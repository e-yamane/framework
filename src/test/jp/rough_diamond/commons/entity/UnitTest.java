package jp.rough_diamond.commons.entity;

import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.testdata.NumberingLoader;
import jp.rough_diamond.commons.testdata.UnitLoader;
import jp.rough_diamond.commons.testing.DataLoadingTestCase;

public class UnitTest extends DataLoadingTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		UnitLoader.init();
		NumberingLoader.init();
	}

	public void testGateRate() throws Exception {
		Unit u = BasicService.getService().findByPK(Unit.class, 1L);
		Number n = u.getRate();
		assertEquals("値が誤っています。", 2, n.intValue() + 1);
	}
	
	public void testInsertNormalCase() throws Exception {
		Unit u = new Unit();
		u.setName("ほげほげ");
		u.setBase(u);
		u.setRate(new ScalableNumber(1L, 0));
		u.setScale(1);
		BasicService.getService().insert(u);
	}
	
	public void testRiviseBaseRate() throws Exception {
		BasicService service = BasicService.getService();
		Unit u = service.findByPK(Unit.class, 1L);
		u.setName("xxx");
		u.setRate(new ScalableNumber(10L, 0));
		service.update(u);
		u = service.findByPK(Unit.class, 1L);
		assertEquals("更新されていません。", "xxx", u.getName());
		assertEquals("レートが補正されていません。", 1, u.getRate().intValue());
	}
	
	public void testIsBaseUnit() throws Exception {
		Unit u = new Unit();
		assertFalse(u.isBaseUnit());
		u.setBase(u);
		assertTrue(u.isBaseUnit());
		Unit u2 = new Unit();
		u.setBase(u2);
		assertFalse(u.isBaseUnit());
		u.setId(1L);
		u2.setId(2L);
		assertFalse(u.isBaseUnit());
		u2.setId(1L);
		assertTrue(u.isBaseUnit());
	}
}
