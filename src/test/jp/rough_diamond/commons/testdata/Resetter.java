/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.testdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.rough_diamond.commons.testing.DBInitializer;
import jp.rough_diamond.framework.service.ServiceLocator;

public class Resetter extends DBInitializer {

	private final static String[] NAMES;
	
	static {
		List<String> tmp = new ArrayList<String>();
		tmp.addAll(Arrays.asList(UnitLoader.getNames()));
		tmp.addAll(Arrays.asList(NumberingLoader.getNames()));
		NAMES = tmp.toArray(new String[tmp.size()]);
    };

    private static boolean isFirst = true;
    public static void reset() throws Exception {
        if(isFirst) {
            Resetter resetter = ServiceLocator.getService(Resetter.class);
            resetter.delete();
            isFirst = false;
        } 
    }
    
    @Override
    protected String[] getResourceNames() {
        return NAMES;
    }

    public static void main(String[] args) throws Exception {
        Resetter resetter = ServiceLocator.getService(Resetter.class);
        resetter.cleanInsert();
        System.out.println("-------------- Finish --------------");
    }
}
