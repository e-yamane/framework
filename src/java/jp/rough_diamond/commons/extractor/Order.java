/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.io.Serializable;

/**
 * ソート条件
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
