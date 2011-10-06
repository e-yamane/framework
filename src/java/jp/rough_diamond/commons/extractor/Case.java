/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.extractor;

/**
 *
 */
public class Case implements Value {
	public final Condition<?> condition;
	public final Value thenValue;
	public final Value elseValue;
	
	public Case(Condition<?> condition, Value thenValue, Value elseValue) {
		this.condition = condition;
		this.thenValue = thenValue;
		this.elseValue = elseValue;
	}
}
