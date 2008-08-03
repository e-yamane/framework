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

import java.io.IOException;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HistoricalSessionRemover implements HttpSessionListener {
	private static Log log = LogFactory.getLog(HistoricalSessionRemover.class);
	
    public void sessionCreated(HttpSessionEvent se) {
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        log.debug("HistoricalSessionRemover#sessionDestroyed");
        try {
            SessionHistoricalFilter.removeTimeoutSessionHistorical(se.getSession());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}