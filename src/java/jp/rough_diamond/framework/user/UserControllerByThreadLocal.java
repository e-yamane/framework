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

public class UserControllerByThreadLocal extends UserController {
	private static ThreadLocal<User> tl = new ThreadLocal<User>();
	
	public User getUser() {
		return tl.get();
	}

	public void setUser(User user) {
        Object old = getUser();
		tl.set(user);
        if(user != old) {
            //�I�u�W�F�N�g�I�ɓ��l�ł��Q�ƒl�ɕω�������Βʒm����
            //�I�u�W�F�N�g�I�ɓ������ǂ����̔��f��Listener�ɂ䂾�˂�
            notify(old, user);
        }
//        if(old != null) {
//            if(!old.equals(user)) {
//                notify(old, user);
//            }
//        } else if(user != null) {
//            notify(old, user);
//        }
	}
}
