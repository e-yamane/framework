/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.testdata;

import jp.rough_diamond.commons.testing.DBInitializer;
import jp.rough_diamond.framework.service.ServiceLocator;

public class NumberingLoader extends DBInitializer {
	/**
	 * インスタンス
	 */
	public final static DBInitializer INSTANCE = ServiceLocator.getService(NumberingLoader.class);

	/**
	 * 初期処理
	 * @throws Exception
	 */
    public static void init() throws Exception {
        Resetter.reset();
        INSTANCE.load();
    }

    /**
     * ロードするファイル名群
     */
    final static String[] NAMES = new String[]{
        "jp/rough_diamond/commons/testdata/NUMBERING.xls",
    };

    public static String[] getNames() {
    	return NAMES;
    }
    @Override
	protected String[] getResourceNames() {
		return getNames();
	}
}
