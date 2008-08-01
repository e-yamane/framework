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
package jp.rough_diamond.framework.web.struts;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.rough_diamond.commons.velocity.VelocityWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.velocity.tools.view.ToolboxManager;
import org.apache.velocity.tools.view.context.ChainedContext;

public class VelocityViewer {
	private final static Log log = LogFactory.getLog(VelocityViewer.class);
	
	public static boolean doVelocity(String templateName, ServletContext context, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(!templateName.endsWith(".vm")) {
			return false;
		}
		if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper) request).getRequest();
        }
		String text = getText(templateName, context, request, response);
		response.setCharacterEncoding((String)VelocityWrapper.getInstance().getEngine().getProperty("output.encoding"));
		response.getWriter().print(text);
		response.getWriter().flush();
		return true;
	}
	
	public static String getText(String templateName, ServletContext context, HttpServletRequest request, HttpServletResponse response) {
		ContextGenerator cg = new ContextGenerator(context, request, response);
		VelocityWrapper wrapper = VelocityWrapper.getInstance();
		String text = wrapper.getText(templateName, cg.map);
		return text;
	}
	
	@SuppressWarnings("unchecked")
	private static class ContextGenerator {
		Map map; 

		public ContextGenerator(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
			Map tmp = new HashMap();

			ToolboxManager manager = VelocityToolsPlugIn.getManager();
			if(manager != null) {
		        ChainedContext ctx = 
		            new ChainedContext(VelocityWrapper.getInstance().getEngine(), request, response, context);
				tmp.putAll(manager.getToolbox(ctx));
			}

			setContext(tmp, context);
			HttpSession session = request.getSession();
			if(session != null) {
				setContext(tmp, session);
			}
			setContext(tmp, request);
			Enumeration en = request.getParameterNames();
			while(en.hasMoreElements()) {
				String name = (String)en.nextElement();
				tmp.put("param." + name, request.getParameterValues(name));
			}
			tmp.put("application", context);
			tmp.put("request", request);
			tmp.put("response", response);
			tmp.put("session", session);

			map = Collections.unmodifiableMap(tmp);
		}
	
	    void setContext(Map map, Object o) {
	        try {
	            Method m1 = o.getClass().getMethod("getAttributeNames", new Class<?>[0]);
	            Method m2 = o.getClass().getMethod("getAttribute", new Class<?>[]{String.class});
	            Enumeration en = (Enumeration)m1.invoke(o);
	            Object[] params = new Object[1];
	            while(en.hasMoreElements()) {
	                String name = (String)en.nextElement();
	                params[0] = name;
	                Object value = m2.invoke(o, params);
	                map.put(name, value);
	            }
	        } catch(Exception ex) {
	            log.warn("正常の場合でも本例外は発生することがあります。", ex);
	        }
	    }
	}
}
