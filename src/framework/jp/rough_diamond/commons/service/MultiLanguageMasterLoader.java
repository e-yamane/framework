/*
 * ====================================================================
 * 
 *  Copyright 2007 Eiji Yamane(e-yamane@rough-diamond.jp)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 */
/**
 * 
 */
package jp.rough_diamond.commons.service;

import java.util.List;

import jp.rough_diamond.commons.extractor.Condition;
import jp.rough_diamond.commons.extractor.Extractor;
import jp.rough_diamond.commons.extractor.Order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author e-yamane
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
