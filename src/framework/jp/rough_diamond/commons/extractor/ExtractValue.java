/*
 * ====================================================================
 * 
 *  Copyright 2007 Eiji Yamane(yamane@super-gs.jp)
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
package jp.rough_diamond.commons.extractor;

/**
 * ���o�p�����[�^
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (火, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
public class ExtractValue {
    public final String key;
    public final Class target;
    public final String aliase;
    public final String property;
    
    /**
     * ���o�l���w�肷��
     * @param key
     * @param target
     * @param aliase
     * @param property
     */
    public ExtractValue(String key, Class target, String aliase, String property) {
        key.getClass();             //NOP NullPointerException�𑗏o�����邽��
        target.getClass();          //NOP NullPointerException�𑗏o�����邽��
        this.key = key;
        this.target = target;
        this.aliase = aliase;
        this.property = property;
    }
}
