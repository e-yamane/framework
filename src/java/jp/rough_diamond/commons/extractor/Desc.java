/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * 降順ソートを行うOrder
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (轣ｫ, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
public class Desc extends Order {
	private static final long serialVersionUID = 1L;

	/**
	 * 降順ソートを行うOrderを生成する
	 * @param propertyName	プロパティ名 nullの場合はNullPointerExceptionを送出する
	 */
	public Desc(String propertyName, Class target, String aliase) {
		super(propertyName, target, aliase);
	}
}
