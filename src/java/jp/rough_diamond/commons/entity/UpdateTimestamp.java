/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */


package jp.rough_diamond.commons.entity;

import java.util.Date;

import jp.rough_diamond.commons.service.annotation.PrePersist;
import jp.rough_diamond.commons.service.annotation.PreUpdate;

/**
 * 更新日時情報のHibernateマッピングクラス
**/
public class UpdateTimestamp extends jp.rough_diamond.commons.entity.base.BaseUpdateTimestamp {
    private static final long serialVersionUID = 1L;
    public UpdateTimestamp() {
    	setRegistererDate(new Date());		//dummy
    	setLastModifiedDate(new Date());	//dummy
    }
    
    @PrePersist
    public void refreshRegistererDate() {
    	Date ts = new Date();
    	setRegistererDate(ts);
    	setLastModifiedDate(ts);
    }
    
    @PreUpdate
    public void refreshLastModifiedDate() {
    	setLastModifiedDate(new Date());
    }
}
