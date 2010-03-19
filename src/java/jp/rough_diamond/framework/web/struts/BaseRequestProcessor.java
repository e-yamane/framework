/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.framework.web.struts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RequestProcessor;

public class BaseRequestProcessor extends RequestProcessor {
	private final static Log log = LogFactory.getLog(BaseRequestProcessor.class);

	@Override
	protected void doForward(String arg0, HttpServletRequest arg1, HttpServletResponse arg2) throws IOException, ServletException {
		log.debug(">>doForward");
		if(!VelocityViewer.doVelocity(arg0, getServletContext(), arg1, arg2)) {
			super.doForward(arg0, arg1, arg2);
		}
	}

	@Override
    protected boolean processRoles(HttpServletRequest request,
            HttpServletResponse response,
            ActionMapping mapping) throws IOException, ServletException {
        Action action = processActionCreate(request, response, mapping);
        if (action == null) {
            return super.processRoles(request, response, mapping);
        }
        if(!(action instanceof BaseAction)) {
            return super.processRoles(request, response, mapping);
        }
        ActionForm form = processActionForm(request, response, mapping);
        BaseAction baseAction = (BaseAction)action;
        ActionForward forward = baseAction.hasRole(form, request, response, mapping);
        if(forward != null) {
            processForwardConfig(request, response, forward);
            return false;
        }
		return super.processRoles(request, response, mapping);
	}	
	
    @Override
    protected ActionForward processActionPerform(
            HttpServletRequest request, 
            HttpServletResponse response, 
            Action action, 
            ActionForm form, 
            ActionMapping mapping) throws IOException, ServletException {
    	log.debug("processActionPerform:[" + action.getClass() + "|" + request.getParameter("command") + "]");
        request.setAttribute("currentForm", form);
        request.setAttribute("currentFormName", mapping.getName());
        request.setAttribute("currentPath", mapping.getPath());
        return super.processActionPerform(request, response, action, form, mapping);
    }
}
