/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * Gt(＞）Condition
 */
@SuppressWarnings("unchecked")
public class Gt extends ValueHoldingCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * Gt(＞）Conditionを生成する
	 * @param propertyName	プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param value		値 nullの場合はNullPointerExceptionを送出する
	 */
	public Gt(String propertyName, Class target, String aliase, Object value) {
		super(propertyName, target, aliase, value);
	}
}
