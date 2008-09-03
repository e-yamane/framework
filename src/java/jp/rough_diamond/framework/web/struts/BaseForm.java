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
import java.io.ObjectInputStream;

import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;

import jp.rough_diamond.commons.resource.Messages;

import org.apache.struts.action.ActionMapping;

/**
 * 何か有った時用
 * @author e-yamane
 */
abstract public class BaseForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	transient ThreadLocal<Messages> mgs = new ThreadLocalMessages();
	transient ThreadLocal<Messages> errs = new ThreadLocalMessages();
    private static class ThreadLocalMessages extends ThreadLocal<Messages> {
    	@Override
    	protected Messages initialValue() {
    		return new Messages();
    	}
    }
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	in.defaultReadObject();
    	mgs = new ThreadLocalMessages();
    	errs = new ThreadLocalMessages();
    }
    
    public Messages getMessage(){
        return this.mgs.get();
    }

    protected void setMessage(Messages mgs) {
        this.mgs.set(mgs);
    }

    public Messages getErrors(){
        return this.errs.get();
    }

    protected void setErrors(Messages mgs) {
        this.errs.set(mgs);
    }

    @Override
    public void reset(ActionMapping map, HttpServletRequest req){
        setMessage(new Messages());
        setErrors(new Messages());
    }
}
