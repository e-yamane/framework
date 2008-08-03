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
package jp.rough_diamond.framework.user;

import java.util.LinkedHashSet;

import jp.rough_diamond.commons.di.DIContainerFactory;

/**
 * ���[�U�[���̊Ǘ����s��
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-08 10:33:45 +0900 (水, 08 2 2006) $
 */
public abstract class UserController {
	/**
	 * ���[�U�[�I�u�W�F�N�g���擾����
	 * @return ���[�U�[�I�u�W�F�N�g
	 */
	abstract public User getUser();
	
	/**
	 * ���[�U�[�I�u�W�F�N�g��ݒ肷��
	 * @param user ���[�U�[
	 */
	abstract public void setUser(User user);
	
    private LinkedHashSet<UserChangeListener> listeners = new LinkedHashSet<UserChangeListener>();

    /**
     * ���[�U�[�ύX�ʒm���X�i��o�^����
     * @param listener
     */
    public void addListener(UserChangeListener listener) {
        listeners.add(listener);
    }
    
    /**
     * ���[�U�[�ύX�ʒm���X�i�𖕏�����
     * @param listener
     */
    public void removeListener(UserChangeListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * ���[�U�[�ύX�ʒm���X�i�֕ύX�v����ʒm����
     * @param oldUser
     * @param newUser
     */
    protected void notify(Object oldUser, Object newUser) {
        for(UserChangeListener listener : listeners) {
            listener.notify(oldUser, newUser);
        }
    }
    
    /**
     * ���[�U�[�Ǘ��I�u�W�F�N�g���擾����
     * @return ���[�U�[�Ǘ��I�u�W�F�N�g
     */
	public static UserController getController() {
		return (UserController)DIContainerFactory.getDIContainer().getObject("userController");
	}
}
