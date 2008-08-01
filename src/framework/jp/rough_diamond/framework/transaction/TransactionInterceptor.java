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
package jp.rough_diamond.framework.transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract public class TransactionInterceptor implements MethodInterceptor {
    private final static Log log = LogFactory.getLog(TransactionInterceptor.class);

    protected final static ThreadLocal<Boolean> rollbackOnly = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };
    
    public boolean isRollbackOnly() {
        return rollbackOnly.get(); 
    }

	public void setRollbackOnly() {
        log.debug("ロールバックおんりぃ～");
        rollbackOnly.set(Boolean.TRUE); 
    }
}
