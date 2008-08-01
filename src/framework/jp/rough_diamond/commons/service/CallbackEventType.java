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

import jp.rough_diamond.commons.service.annotation.PostLoad;
import jp.rough_diamond.commons.service.annotation.PostPersist;
import jp.rough_diamond.commons.service.annotation.PostRemove;
import jp.rough_diamond.commons.service.annotation.PostUpdate;
import jp.rough_diamond.commons.service.annotation.PrePersist;
import jp.rough_diamond.commons.service.annotation.PreRemove;
import jp.rough_diamond.commons.service.annotation.PreUpdate;

/**
 * コールバックイベント
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-08 10:33:45 +0900 (豌ｴ, 08 2 2006) $
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
