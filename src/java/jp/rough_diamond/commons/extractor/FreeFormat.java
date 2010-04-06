/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * フリーフォーマット
 * 例えば計算式などを直接投入したい場合に使用する
 */
public class FreeFormat implements Value {
	public final String format;
	public final List<?> values;
	public FreeFormat(String format, List<?> values) {
		format.getClass();		//NOP	NullPointerExceptionを送出させたい場合
		this.format = format;
		if(values == null) {
			this.values = Collections.unmodifiableList(new ArrayList<Object>());
		} else {
			this.values = values;
		}
	}
	public FreeFormat(String format, Object... values) {
		this(format, Arrays.asList(values));
	}
}
