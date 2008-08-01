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
package jp.rough_diamond.commons.velocity;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import jp.rough_diamond.commons.di.DIContainerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.app.event.MethodExceptionEventHandler;

@SuppressWarnings("unchecked")
public class VelocityWrapper {
	private final static Log log = LogFactory.getLog(VelocityWrapper.class);
	
	private static VelocityWrapper instance;
	
	public static VelocityWrapper getInstance() {
		if(instance == null) {
			init();
		}
		return instance;
	}
	
	public VelocityEngine getEngine() {
		return engine;
	}
	public String getText(String templateName, Map context) {
		return getText(templateName, context, true);
	}
	
	public String getText(String templateName, Map context, boolean isTagLanguage) {
		try {
			Map tmp = new HashMap(defaultContext);
			if(context != null) {
				tmp.putAll(context);
			}
			Template template = engine.getTemplate(templateName);
			VelocityContext vContext = new VelocityContext();
			Iterator iterator = tmp.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry entry = (Map.Entry)iterator.next();
				vContext.put((String)entry.getKey(), entry.getValue());
			}
	        EventCartridge ec = new EventCartridge();
	        ec.addEventHandler(MEEH);
	        ec.attachToContext(vContext);
			StringWriter sw = new StringWriter();
			template.merge(vContext, sw);
			return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private synchronized static void init() {
		if(instance == null) {
			instance = (VelocityWrapper)DIContainerFactory.getDIContainer().getObject("velocityWrapper");
		}
	}

	private Properties 	props;
	private Map        	defaultContext;
	private VelocityEngine	engine;
	
    /**
	 * DIコンテナからの生成を想定
	 */
	public VelocityWrapper(String propText) {
		this(propText, new HashMap());
	}
	
    /**
	 * DIコンテナからの生成を想定
	 */
	public VelocityWrapper(String propText, Map defaultContext) {
		try {
			log.debug(propText);
			byte[] propArray = propText.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(propArray);
			props = new Properties();
			props.load(bais);
			engine = new VelocityEngine();
			engine.init(props);
			this.defaultContext = defaultContext;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
    private static MethodExceptionEventHandler MEEH = new MethodExceptionEventHandler() {
        public Object methodException(Class claz, String method, Exception e) throws java.lang.Exception {
            log.warn("Class「" + claz.getName() + "」のメソッド「" + method + "」で以下の例外が発生しました。", e);
            throw e;
        }
    };
}
