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
 * 適切なローケルを管理するコントローラー
 * @author e-yamane
 */
abstract public class LocaleController {
    /**
     * ローケルを取得する
     * @return ローケル
     */
    abstract public Locale getLocale();
    
    /**
     * ローケルを設定する
     * @param locale ローケル
     */
    abstract public void setLocale(Locale locale);
    
    /**
     * ローケルコントローラーを取得する
     * @return ローケルコントローラー
     */
    public static LocaleController getController() {
        return (LocaleController)DIContainerFactory.getDIContainer().getObject("localeController");
    }
}
