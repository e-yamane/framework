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

import java.util.MissingResourceException;

import javax.servlet.http.HttpServletRequest;

import jp.rough_diamond.commons.di.DIContainer;
import jp.rough_diamond.commons.di.DIContainerFactory;
import jp.rough_diamond.commons.resource.ResourceManager;
import jp.rough_diamond.framework.user.User;
import jp.rough_diamond.framework.user.UserController;

/**
 * Velocity便利ツール
 * @author e-yamane
 */
public class VelocityUtils {
	/**
	 * 現在のユーザーオブジェクトを返却する
	 * @return
	 */
	public User getUser() {
		return UserController.getController().getUser();
	}
	
	/**
	 * DIコンテナを返却する
	 * @return
	 */
	public DIContainer getDIContainer() {
		return DIContainerFactory.getDIContainer();
	}

	public String getApplicationPath(HttpServletRequest request) {
	    String scheme = request.getScheme();
	    scheme = scheme.toLowerCase();
	    String hostAndPort = request.getServerName() + ":" + request.getServerPort();
	    String applicationName = request.getContextPath();
	    if(applicationName.startsWith("/") || applicationName.length() == 0) {
	        return scheme + "://" + hostAndPort + applicationName;
	    } else {
	        return scheme + "://" + hostAndPort + "/" + applicationName;
	    }
	}
    
    /**
     * key にひもづくリソースを返す. リソースが存在しない場合は渡されたキーを返す
     * @param key
     * @return　リソース
     */
    public String message(String key) {
        try {
            return ResourceManager.getResource().getString(key);
        } catch(MissingResourceException mre) {
            return key;
        } 
    }
}
