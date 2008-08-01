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
package jp.rough_diamond.commons.service;

import java.lang.reflect.Method;
import java.util.Comparator;

import jp.rough_diamond.commons.service.annotation.Verifier;

/**
 * VerifierがアノテートされているメソッドをソートするためのComparator
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-08 13:29:47 +0900 (豌ｴ, 08 2 2006) $
 */
public class VerifierSorter implements Comparator<Method> {
    public final static Comparator<Method> INSTANCE = new VerifierSorter();

    public int compare(Method o1, Method o2) {
        Verifier v1 = o1.getAnnotation(Verifier.class);
        Verifier v2 = o2.getAnnotation(Verifier.class);
        if(v1.priority() == v2.priority()) {
            return o1.toString().compareTo(o2.toString());
        } else {
            return v2.priority() - v1.priority();
        }
    }
}
