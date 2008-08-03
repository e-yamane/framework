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
/*
 * $Id$
 * $Header$
 */
package jp.rough_diamond.framework.web.filter.session_historical;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

@SuppressWarnings("unchecked")
class ColdSession implements Serializable {
	private static final long serialVersionUID = 1L;
	public ColdSession() {
    }

    static ColdSession freezeSession(
            HttpSession session, ServletContext context) {
        ColdSession ret = new ColdSession();
        ret.id = session.getId();
        Map map = new HashMap();
        Enumeration en = session.getAttributeNames();
        while(en.hasMoreElements()) {
            String name = (String)en.nextElement();
            Object o = session.getAttribute(name);
            if(o instanceof Serializable) {
                map.put(name, o);
            } else {
                context.log(name + " is not implements Serializable. skip value");
            }
        }
        ret.map = map;
        return ret;
    }

    public String getId() {
        return id;
    }

    public Map getSessionMap() {
        return map;
    }

    private String  id;
    private Map     map;
}