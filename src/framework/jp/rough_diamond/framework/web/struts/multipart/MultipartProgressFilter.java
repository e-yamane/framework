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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * アップロード時の進捗を知らせるためのフィルター
 * @author e-yamane
 */
public class MultipartProgressFilter implements Filter {

	public void init(FilterConfig config) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String uploadId = request.getParameter("uploadId");
		request = getWrappedRequest(uploadId, request);
		try {
			chain.doFilter(request, response);
		} finally {
			MultipartProgressRequestWrapper.removeRequest(uploadId);
		}
	}

	private ServletRequest getWrappedRequest(String uploadId, ServletRequest request) {
		if(uploadId == null) {
			return request;
		}
		if(!(request instanceof HttpServletRequest)) {
			return request;
		}
		HttpServletRequest hrequest = (HttpServletRequest)request;
        if (!"POST".equalsIgnoreCase(hrequest.getMethod())) {
            return (request);
        }
        String contentType = request.getContentType();
        if ((contentType != null) &&
            contentType.startsWith("multipart/form-data")) {
    		MultipartProgressRequestWrapper mprw = new MultipartProgressRequestWrapper(hrequest);
    		MultipartProgressRequestWrapper.putRequest(uploadId, mprw);
    		return mprw;
        } else {
            return (request);
        }
	}

	public void destroy() {
	}

}
