/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * 外部エンティティと結合するための条件
 * 外部エンティティのプロパティを抽出条件に加える場合は、
 * 本オブジェクトに条件を追加していくこと 
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-08 10:33:45 +0900 (豌ｴ, 08 2 2006) $
 */
public class Join extends CombineCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * エンティティ名
	 */
	public final String entityName;
	
	/**
	 * 外部エンティティと結合するための条件を生成する
	 * @param entityName エンティティ名 nullの場合はNullPointerExceptionを送出する
	 */
	public Join(String entityName) {
		super();
		entityName.getClass();	//NOP nullなら強制的に例外を送出させたいので
		this.entityName = entityName;
	}
}
