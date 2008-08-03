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
package jp.rough_diamond.commons.extractor;

import java.io.Serializable;

/**
 * ソート条件
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (轣ｫ, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
abstract public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ソート対象プロパティ名
	 */
	public final String propertyName;
	
    /**
     * ターゲットクラス
     */
    public final Class target;
    
    /**
     * エイリアス
     */
    public final String aliase;
    
	/**
	 * ソート条件を生成する
	 * @param propertyName プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 */
	public Order(String propertyName, Class target, String aliase) {
		propertyName.getClass();		//NOP NullPointerExceptionを送出させたい場合
		this.propertyName = propertyName;
        this.target = target;
        this.aliase = aliase;
	}

	/**
	 * Ascオブジェクトを取得する
	 * @param propertyName	プロパティ名
	 * @return Ascオブジェクト
	 */
	public static Order asc(String propertyName) {
		return new Asc(propertyName, null, null);
	}
	
    /**
     * Ascオブジェクトを取得する
     * @param propertyName  プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @return Ascオブジェクト
     */
    public static Order asc(String propertyName, Class target, String aliase) {
        return new Asc(propertyName, target, aliase);
    }
    
	/**
	 * Descオブジェトを取得する
	 * @param propertyName プロパティ名
	 * @return	Descオブジェクト
	 */
	public static Order desc(String propertyName) {
		return new Desc(propertyName, null, null);
	}

    /**
     * Descオブジェトを取得する
     * @param propertyName プロパティ名
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
     * @return  Descオブジェクト
     */
    public static Order desc(String propertyName, Class target, String aliase) {
        return new Desc(propertyName, target, aliase);
    }
}
