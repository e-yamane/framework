/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * Like条件を表すCondition
 * ワイルドカードは事前に値として付与しておくこと
 */
@SuppressWarnings("unchecked")
public class Like<T extends Value> extends ValueHoldingCondition<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * Like条件を表すConditionを生成する
	 * @param propertyName	プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param value		値 nullの場合はNullPointerExceptionを送出する
	 * @deprecated		Like(T, Object)の使用を推奨します
	 */
	@Deprecated
	public Like(String propertyName, Class target, String aliase, Object value) {
		super(propertyName, target, aliase, value);
	}

	/**
	 * Like条件を表すConditionを生成する
	 * @param label		比較対照ラベル
	 * @param value		値 nullの場合はNullPointerExceptionを送出する
	 */
	public Like(T label, Object value) {
		super(label, value);
	}
}
