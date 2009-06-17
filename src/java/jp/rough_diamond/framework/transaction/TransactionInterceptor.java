/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.framework.transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract public class TransactionInterceptor implements MethodInterceptor {
    private final static Log log = LogFactory.getLog(TransactionInterceptor.class);

    protected boolean rollbackOnly = Boolean.FALSE;
    
    public boolean isRollbackOnly() {
        return rollbackOnly; 
    }

	public void setRollbackOnly() {
        log.debug("ロールバックおんりぃ～");
        rollbackOnly = Boolean.TRUE;
    }
}
