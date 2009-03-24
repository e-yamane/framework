/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.framework.web.struts;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.rough_diamond.commons.di.DIContainerFactory;
import jp.rough_diamond.commons.resource.Messages;
import jp.rough_diamond.framework.user.RoleJudge;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * 何か有った時用
 */
abstract public class BaseAction extends DispatchAction {
    private final static Log log = LogFactory.getLog(BaseAction.class);

    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        try {
            return super.execute(arg0, arg1, arg2, arg3);
        } catch(Error e) {
            //Errorはstruts例外機構に組み込まれないので。。。
            throw new RuntimeException(e);
        }
    }

    //Struts 1.2.8のメソッドコピー
    @Override
    protected ActionForward dispatchMethod(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response,
            String name) throws Exception {
        if (name == null) {
        	name = "unspecified";
//            return this.unspecified(mapping, form, request, response);
        }

        Method method = null;
        try {
            method = getMethod(name);
            //↓追加
            if(!hasRole(method)) {
            	return mapping.findForward("forbiddenAccess");
            }
            NoCache noCache = method.getAnnotation(NoCache.class);
            if(noCache != null) {
                log.debug("キャッシュ無効リクエストです。");
                response.addHeader("Pragma", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                response.addHeader("Expires", "-1");
            }
            ContentType ct = method.getAnnotation(ContentType.class);
            if(ct != null) {
            	log.debug("ContentType:" + ct.value());
            	response.setContentType(ct.value());
            }
            //↑追加
        } catch(NoSuchMethodException e) {
            String message =
                    messages.getMessage("dispatch.method", mapping.getPath(), name);
            log.error(message, e);
            throw e;
        }

        ActionForward forward = null;
        try {
            Object args[] = {mapping, form, request, response};
            forward = (ActionForward) method.invoke(this, args);
            if(form instanceof BaseForm) {
            	BaseForm baseForm = (BaseForm)form;
            	Messages msg;
            	msg = baseForm.getMessage();
                if(msg.hasError()) {
                    saveMessages(request, MessagesTranslator.translate(msg));
                }
            	msg = baseForm.getErrors();
                if(msg.hasError()) {
                    saveErrors(request, MessagesTranslator.translate(msg));
                }
            }
        } catch(ClassCastException e) {
            String message =
                    messages.getMessage("dispatch.return", mapping.getPath(), name);
            log.error(message, e);
            throw e;

        } catch(IllegalAccessException e) {
            String message =
                    messages.getMessage("dispatch.error", mapping.getPath(), name);
            log.error(message, e);
            throw e;

        } catch(InvocationTargetException e) {
            // Rethrow the target exception if possible so that the
            // exception handling machinery can deal with it
            Throwable t = e.getTargetException();
            if (t instanceof Exception) {
                throw ((Exception) t);
            } else {
                String message =
                        messages.getMessage("dispatch.error", mapping.getPath(), name);
                log.error(message, e);
                throw new ServletException(t);
            }
        }

        // Return the returned ActionForward instance
        return (forward);
    }

	private boolean hasRole(Method method) {
		AllowRole classRole = this.getClass().getAnnotation(AllowRole.class);
		AllowRole methodRole = method.getAnnotation(AllowRole.class);
		if(methodRole != null) {
			return hasRole(methodRole);
		} else {
			return hasRole(classRole);
		}
	}

	private boolean hasRole(AllowRole allowRole) {
		if(allowRole == null) {
			return false;
		}
		if(allowRole.isAllAccess()) {
			return true;
		} else {
			RoleJudge judge = (RoleJudge)DIContainerFactory.getDIContainer().getObject("roleJudge");
			return judge.hasRole(allowRole.allowedRoles());
		}
	}
}
