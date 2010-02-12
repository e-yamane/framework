/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.testing;

import jp.rough_diamond.commons.entity.ScalableNumber;
import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.testdata.NumberingLoader;
import jp.rough_diamond.commons.testdata.UnitLoader;

public class DataLoadingTestCaseTest extends DataLoadingTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		Loader.load(UnitLoader.class);
		Loader.load(NumberingLoader.class);
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
