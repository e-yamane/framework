/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * IsNotNull条件を表すCondition 
 */
@SuppressWarnings("unchecked")
public class IsNotNull<T extends Value> extends LabelHoldingCondition<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * IsNotNull条件を表すConditionを生成する
	 * @param propertyName プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @deprecated		IsNotNull(Value)の使用を推奨します
	 */
	@Deprecated
	public IsNotNull(String propertyName, Class target, String aliase) {
		super(propertyName, target, aliase);
	}

	/**
	 * IsNotNull条件を表すConditionを生成する
	 * @param label		比較対照ラベル
	 * @param value		値 nullの場合はNullPointerExceptionを送出する
	 */
	public IsNotNull(T label) {
		super(label);
	}
}
