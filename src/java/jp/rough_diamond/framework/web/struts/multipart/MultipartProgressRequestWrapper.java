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
package jp.rough_diamond.framework.web.struts.multipart;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import jp.rough_diamond.commons.io.ReadCountInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MultipartProgressRequestWrapper extends HttpServletRequestWrapper {
	private final static Log log = LogFactory.getLog(MultipartProgressRequestWrapper.class);
	
	static Map<String, MultipartProgressRequestWrapper> REQUEST_MAP;
	
	static {
		REQUEST_MAP = new HashMap<String, MultipartProgressRequestWrapper>();
	}
	
	public static void putRequest(String id, MultipartProgressRequestWrapper request) {
		log.debug("id:" + id);
		REQUEST_MAP.put(id, request);
	}
	
	public static MultipartProgressRequestWrapper getRequest(String id) {
		log.debug("id:" + id);
		return REQUEST_MAP.get(id);
	}
	
	public static void removeRequest(String id) {
		log.debug("id:" + id);
		REQUEST_MAP.remove(id);
	}
	
	public MultipartProgressRequestWrapper(HttpServletRequest arg0) {
		super(arg0);
	}

	private ServletInputStreamWrapper sis = null;

	@Override
	public ServletInputStreamWrapper getInputStream() throws IOException {
		if(sis == null) {
			sis = new ServletInputStreamWrapper(
					new ReadCountInputStream(super.getInputStream()));
		}
		return sis;
	}
	
	public long getReadSize() throws IOException {
		return getInputStream().getReadSize();
	}
	
	private static class ServletInputStreamWrapper extends ServletInputStream {
		private ReadCountInputStream is;

		ServletInputStreamWrapper(ReadCountInputStream is) {
			this.is = is;
		}

		@Override
		public int read() throws IOException {
			return is.read();
		}
		
		long getReadSize() {
			return is.getReadSize();
		}
	}
}
