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
package jp.rough_diamond.commons.resource;

import org.apache.commons.lang.StringUtils;

/**
 * Messagesオブジェクトを内包するException
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-03-10 14:59:54 +0900 (驥?, 10 3 2006) $
 */
public class MessagesIncludingException extends Exception {
    private static final long serialVersionUID = 1L;
    private final Messages msg;
    
    public MessagesIncludingException(Messages msg) {
        super();
        this.msg = msg;
    }

    public MessagesIncludingException(String message, Throwable cause, Messages msg) {
        super(message, cause);
        this.msg = msg;
    }

    public MessagesIncludingException(String message, Messages msg) {
        super(message);
        this.msg = msg;
    }

    public MessagesIncludingException(Throwable cause, Messages msg) {
        super(cause);
        this.msg = msg;
    }
    
    public Messages getMessages() {
        return msg;
    }

    @Override
    public String getMessage() {
        String ret = super.getMessage();
        if(StringUtils.isEmpty(ret)) {
            return getMessages().toString();
        } else {
            return ret;
        }
    }
}
