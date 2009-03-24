/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 複数のConditionを結合するCondition
 */
public abstract class CombineCondition extends Condition {
	private static final long serialVersionUID = 1L;
	private final Collection<Condition> conditions;

	/**
	 * CombineConditionオブジェクトを生成する
	 * 結合される中身は殻とする
	 */
	public CombineCondition() {
		conditions = new ArrayList<Condition>();
	}
	
	/**
	 * CombineConditionオブジェクトを生成する
	 * @param conditions	Condition群 nulllの場合はNullPointerExceptionをスローする
	 */
	public CombineCondition(Collection<Condition> conditions) {
		conditions.size();	//NOP nullなら強制的に例外を送出させたいので
		this.conditions = conditions;
	}

	/**
	 * 結合するConditionのイレテータを返却する
	 * @return 結合するConditionのIterator
	 */
	public Collection<Condition> getConditions() {
		return conditions;
	}
	
	/**
	 * 結合する条件を末尾に追加する
	 * @param condition	条件
	 */
	public void add(Condition condition) {
		conditions.add(condition);
	}

	/**
	 * 結合条件数を取得する
	 * @return 結合条件件数
	 */
	public int getSize() {
		return conditions.size();
	}
}
