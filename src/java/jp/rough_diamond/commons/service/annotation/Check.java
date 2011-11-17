/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Check {
	/**
	 * ユニーク名
	 * @return
	 */
	String name() default "";
	
	/**
	 * ユニークチェックを行うプロパティ群
	 * エラーメッセージとしては、最初のプロパティが使われる
	 * @return
	 */
	String[] properties();
}
