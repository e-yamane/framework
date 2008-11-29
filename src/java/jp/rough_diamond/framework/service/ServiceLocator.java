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
/*
 * �쐬��: 2005/02/14
 *
 */
package jp.rough_diamond.framework.service;

/**
 * �T�[�r�X���擾���郉�b�p�[�N���X
 * ��x�擾�ł����N���X�̓L���b�V�������B
 * @author $Author$
 * @date $Date$
 */
@SuppressWarnings("unchecked")
public class ServiceLocator {
	public final static String SERVICE_LOCATOR_KEY = "serviceLocator"; 
	public final static String SERVICE_FINDER_KEY = "serviceFinder";

	/**
     * �T�[�r�X���擾����
	 * @param <T>	�ԋp����C���X�^���X�̃^�C�v
     * @param cl    �擾�ΏۃT�[�r�X�N���X
     * @return      �T�[�r�X 
     */
	public static <T extends Service> T getService(Class<T> cl) {
		return getService(cl, cl);
    }	
	
	/**
	 * �T�[�r�X���擾����
	 * @param <T>				�ԋp����C���X�^���X�̃^�C�v
	 * @param cl				�T�[�r�X�̊�_�ƂȂ�C���X�^���X�̃^�C�v
	 * @param defaultClassName	DI�R���e�i�ɐݒ肪�����ꍇ�Ɏ��̉�����C���X�^���X�̃^�C�v��	
     * @return      �T�[�r�X 
	 */
	public static <T extends Service> T getService(Class<T> cl, String defaultClassName) {
		Class<? extends T> defaultClass;
		try {
			defaultClass = (Class<? extends T>)Class.forName(defaultClassName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return getService(cl, defaultClass);
	}
	
	/**
	 * �T�[�r�X���擾����
	 * @param <T>				�ԋp����C���X�^���X�̃^�C�v
	 * @param cl				�T�[�r�X�̊�_�ƂȂ�C���X�^���X�̃^�C�v
	 * @param defaultClass		DI�R���e�i�ɐݒ肪�����ꍇ�Ɏ��̉�����C���X�^���X�̃^�C�v	
     * @return      �T�[�r�X 
	 */
	public static <T extends Service> T getService(Class<T> cl, Class<? extends T> defaultClass) {
    	return ServiceLocatorLogic.getServiceLocatorLogic().getService(cl, defaultClass);
	}
}
