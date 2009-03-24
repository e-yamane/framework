/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * プロパティ名を保持するコンディション
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (轣ｫ, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
public abstract class LabelHoldingCondition extends Condition {
	private static final long serialVersionUID = 1L;

	/**
	 * プロパティ名
	 */
	public final String propertyName;
	
    /**
     * ターゲットクラス
     */
    public final Class target;
    
    /**
     * エイリアス
     */
    public final String aliase;
    
	/**
	 * プロパティ名を保持するConditionを生成する
	 * @param propertyName	プロパティ名 nullの場合はNullPointerExceptionを送出する
     * @param target    プロパティを保持しているエンティティクラス
     * @param aliase    エンティティの別名
	 */
	public LabelHoldingCondition(String propertyName, Class target, String aliase) {
		propertyName.length();	//NOP nullなら強制的に例外を送出させたいので
		this.propertyName = propertyName;
        this.target = target;
        this.aliase = aliase;
	}
}
