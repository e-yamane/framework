/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.service;

import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.testdata.NumberingLoader;
import jp.rough_diamond.commons.testdata.UnitLoader;
import jp.rough_diamond.commons.testing.DataLoadingTestCase;

/**
 *
 */
public class BasicServiceTest extends DataLoadingTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		NumberingLoader.init();
		UnitLoader.init();
	}

	public void testFindByExtractorWithCount() throws Exception {
		Extractor e = new Extractor(Unit.class);
		e.setLimit(1);
		FindResult<Unit> fr = BasicService.getService().findByExtractorWithCount(e);
		assertEquals("‘S‘ÌŒ”‚ªŒë‚Á‚Ä‚Ü‚·B", 5, fr.count);
		assertEquals("æ“¾Œ”‚ªŒë‚Á‚Ä‚¢‚Ü‚·B", 1, fr.list.size());
	}
}
