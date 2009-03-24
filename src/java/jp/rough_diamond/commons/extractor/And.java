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
public class And extends CombineCondition {
	private static final long serialVersionUID = 1L;

	/**aaa
	 * Andオブジェクトを生成する
	 * 結合される中身は殻とする
	 */
	public And() {
		super();
	}
	
	/**
	 * Andオブジェクトを生成する
	 * @param conditions	Condition群 nulllの場合はNullPointerExceptionをスローする
	 */
	public And(Collection<Condition> conditions) {
		super(conditions);
	}
}
