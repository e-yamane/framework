/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.service;

import java.util.List;

import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.extractor.Order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class MultiLanguageMasterLoader {
    private final static Log log = LogFactory.getLog(MultiLanguageMasterLoader.class);

    public static <T> List<T> getList(Class<T> type, String countryCode) {
        log.debug("contryCode:" + countryCode);
        Extractor extractor = new Extractor(type);
        extractor.add(Condition.eq("comp_id.locationId", countryCode));
        extractor.addOrder(Order.asc("comp_id.id"));
        return BasicService.getService().findByExtractor(extractor);
    }
    
    public static <T> T getMaster(Class<T> type, String id, String countryCode) {
        log.debug("contryCode:" + countryCode);
        Extractor extractor = new Extractor(type);
        extractor.add(Condition.eq("comp_id.id", id));
        extractor.add(Condition.eq("comp_id.locationId", countryCode));
        extractor.addOrder(Order.asc("comp_id.id"));
        List<T> tmp = BasicService.getService().findByExtractor(extractor); 
        return (tmp.size() == 0) ? null : tmp.get(0);
    }
}
