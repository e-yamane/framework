/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * 正規表現(POSIX) Condition.
 * PostgreSQLにのみ対応しています。{char_length(substring(propartyName , value)) > 0}
 */
@SuppressWarnings("unchecked")
public class RegularExp extends ValueHoldingCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * 正規表現(POSIX) Condition を生成する。
	 * @param propertyName	プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 * @param value		値 nullの場合はNullPointerExceptionを送出する
	 */
	public RegularExp(String propertyName, Class target, String aliase, Object value) {
		super(propertyName, target, aliase, value);
	}

}
