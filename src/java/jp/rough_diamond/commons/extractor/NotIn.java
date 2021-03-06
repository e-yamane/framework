/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.util.Collection;

/**
 * NotIn(補集合）Condition
 */
@SuppressWarnings("unchecked")
public class NotIn<T extends Value> extends ValueHoldingCondition<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * NotIn(補集合）Conditionを生成する
	 * @param propertyName	プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param values		値群 nullの場合はNullPointerExceptionを送出する
	 * @deprecated		NotIn(T, Object...)の使用を推奨します
	 */
	@Deprecated
	public NotIn(String propertyName, Class target, String aliase, Collection values) {
		super(propertyName, target, aliase, values);
	}

	/**
	 * NotIn(補集合）Conditionを生成する
	 * @param label		比較対照ラベル
	 * @param value		値 nullの場合はNullPointerExceptionを送出する
	 */
	public NotIn(T label, Collection value) {
		super(label, value);
	}
}
