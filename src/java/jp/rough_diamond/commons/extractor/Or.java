/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.util.Collection;

/**
 * And条件を表すCondition
 */
public class Or<T extends Value> extends CombineCondition<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * Orオブジェクトを生成する
	 * 結合される中身は空とする
	 */
	public Or() {
		super();
	}
	
	/**
	 * Orオブジェクトを生成する
	 * @param conditions	Condition群 nulllの場合はNullPointerExceptionをスローする
	 */
	public Or(Collection<Condition<T>> conditions) {
		super(conditions);
	}
}
