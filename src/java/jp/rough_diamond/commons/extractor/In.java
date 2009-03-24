/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.util.Collection;

/**
 * In(包含）Condition
 */
@SuppressWarnings("unchecked")
public class In extends ValueHoldingCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * In(包含）Conditionを生成する
	 * @param propertyName	プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param values		値群 nullの場合はNullPointerExceptionを送出する
	 */
	public In(String propertyName, Class target, String aliase, Collection values) {
		super(propertyName, target, aliase, values);
	}
}
