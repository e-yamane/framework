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

/**
 * �X���b�h���[�J�����Ƀ��[�P�����Ǘ����郍�[�P���R���g���[���[
 * ���[�P�����Z�b�g����Ȃ��ꍇ�̓f�t�H���g���[�P����ԋp����
 * @author e-yamane
 */
public class LocaleControllerByThreadLocal extends LocaleController {
    private final ThreadLocal<Locale> locale = new ThreadLocal<Locale>() {
        @Override
        protected Locale initialValue() {
            return Locale.getDefault();
        }
    };
    
    @Override
    public Locale getLocale() {
        return locale.get();
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }
}
