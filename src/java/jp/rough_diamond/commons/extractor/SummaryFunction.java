/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.extractor;

/**
 * �W�v�֐�
 */
abstract public class SummaryFunction implements Value {
	public final Value value;
	protected SummaryFunction(Value value) {
		this.value = value;
	}
}