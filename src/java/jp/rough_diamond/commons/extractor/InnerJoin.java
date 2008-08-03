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
 * 内部結合オブジェクト
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (轣ｫ, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
public class InnerJoin {
    public final Class target;
    public final String targetProperty;
    public final String targetAlias;
    public final Class joined;
    public final String joinedProperty;
    public final String joinedAlias;
    
    public InnerJoin(Class target, String targetProperty, String targetAlias, Class joined, String joinedProperty, String joinedAlias) {
        target.getClass();  //NOP NullPointerExceptionを送出させたいので
        joined.getClass();  //NOP NullPointerExceptionを送出させたいので
        this.target = target;
        this.targetProperty = targetProperty;
        this.targetAlias = targetAlias;
        this.joined = joined;
        this.joinedProperty = joinedProperty;
        this.joinedAlias = joinedAlias;
    }
}
