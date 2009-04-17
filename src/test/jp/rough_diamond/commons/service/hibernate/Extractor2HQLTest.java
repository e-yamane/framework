/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.service.hibernate;


import jp.rough_diamond.commons.entity.Numbering;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.service.BasicService;
import jp.rough_diamond.commons.testdata.NumberingLoader;
import jp.rough_diamond.commons.testing.DataLoadingTestCase;
import jp.rough_diamond.framework.service.Service;
import jp.rough_diamond.framework.service.ServiceLocator;

/**
 *
 */
public class Extractor2HQLTest extends DataLoadingTestCase {
	protected void setUp() throws Exception {
		super.setUp();
		NumberingLoader.init();
	}

	public void testFetchSizeの指定が正しく行われていること() throws Exception {
		//パスのみの確認です。
		//きちんとロジック分岐していることをCoberturaで目視で確認するのみ
		ServiceLocator.getService(CreateQueryService.class).getQuery(100);
		ServiceLocator.getService(CreateQueryService.class).getQuery(Extractor.DEFAULT_FETCH_SIZE);
		BasicService.getService().findAll(Numbering.class, 100);
		BasicService.getService().findAll(Numbering.class, Extractor.DEFAULT_FETCH_SIZE);
	}
	
	public static class CreateQueryService implements Service {
		public void getQuery(int fetchSize) {
			Extractor ex = new Extractor(Numbering.class);
			ex.setFetchSize(fetchSize);
			BasicService.getService().findByExtractor(ex);
		}
	}
}
