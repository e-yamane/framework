/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * 内部結合オブジェクト
 */
@SuppressWarnings("unchecked")
public class InnerJoin {
    public final Class target;
    public final String targetProperty;
    public final String targetAlias;
    public final Class joined;
    public final String joinedProperty;
    public final String joinedAlias;
    
    public InnerJoin(Class target, String targetProperty, String targetAlias, Class joined, String joinedProperty, String joinedAlias) {
        target.getClass();  //NOP NullPointerExceptionを送出させたいので
        joined.getClass();  //NOP NullPointerExceptionを送出させたいので
        this.target = target;
        this.targetProperty = targetProperty;
        this.targetAlias = targetAlias;
        this.joined = joined;
        this.joinedProperty = joinedProperty;
        this.joinedAlias = joinedAlias;
    }
    
    public InnerJoin(Property target, Property join) {
    	this(target.target, target.property, target.aliase,
    			join.target, join.property, join.aliase);
    }
}
