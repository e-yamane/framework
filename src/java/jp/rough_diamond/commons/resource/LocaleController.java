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
/**
 * 
 */
package jp.rough_diamond.commons.resource;

import java.util.Locale;

import jp.rough_diamond.commons.di.DIContainerFactory;

/**
 * �K�؂ȃ��[�P�����Ǘ�����R���g���[���[
 * @author e-yamane
 */
abstract public class LocaleController {
    /**
     * ���[�P�����擾����
     * @return ���[�P��
     */
    abstract public Locale getLocale();
    
    /**
     * ���[�P����ݒ肷��
     * @param locale ���[�P��
     */
    abstract public void setLocale(Locale locale);
    
    /**
     * ���[�P���R���g���[���[���擾����
     * @return ���[�P���R���g���[���[
     */
    public static LocaleController getController() {
        return (LocaleController)DIContainerFactory.getDIContainer().getObject("localeController");
    }
}
