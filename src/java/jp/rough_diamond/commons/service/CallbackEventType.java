/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.service;

import jp.rough_diamond.commons.service.annotation.PostLoad;
import jp.rough_diamond.commons.service.annotation.PostPersist;
import jp.rough_diamond.commons.service.annotation.PostRemove;
import jp.rough_diamond.commons.service.annotation.PostUpdate;
import jp.rough_diamond.commons.service.annotation.PrePersist;
import jp.rough_diamond.commons.service.annotation.PreRemove;
import jp.rough_diamond.commons.service.annotation.PreUpdate;

/**
 * コールバックイベント
 */
@SuppressWarnings("unchecked")
public enum CallbackEventType {
    PRE_PERSIST, POST_PERSIST, PRE_REMOVE, POST_REMOVE, PRE_UPDATE, POST_UPDATE, POST_LOAD;
    
    public Class getAnnotation() {
        switch(this) {
        case PRE_PERSIST:
            return PrePersist.class;
        case POST_PERSIST:
            return PostPersist.class;
        case PRE_REMOVE:
            return PreRemove.class;
        case POST_REMOVE:
            return PostRemove.class;
        case PRE_UPDATE:
            return PreUpdate.class;
        case POST_UPDATE:
            return PostUpdate.class;
        case POST_LOAD:
            return PostLoad.class;
        default:
            throw new RuntimeException();
        }
    }
}
