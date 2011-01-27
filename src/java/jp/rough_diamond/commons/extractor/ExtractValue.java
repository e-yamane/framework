/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * 抽出パラメータ
 */
@SuppressWarnings("unchecked")
public class ExtractValue {
    public final String key;
    /**
     * @deprecated 本プロパティは以降使用しません
     * 将来的に削除する予定です
     */
	@Deprecated
    public final Class target;
    /**
     * @deprecated 本プロパティは使用しません
     * 将来的に削除する予定です
     */
    @Deprecated
    public final String aliase;
    /**
     * @deprecated 本プロパティは使用しません
     * 将来的に削除する予定です
     */
    @Deprecated
    public final String property;
    
    /**
     * 抽出する値
     */
    public final Value value;
    
    /**
     * この値のタイプを指定します。
     * 省略した場合、永続化フレームワークの指定する戻り値で返却されます。
     */
    public final Class<?> returnType;
    
    /**
     * 抽出値を指定する
     * @param key
     * @param target
     * @param aliase
     * @param property
     * @deprecated 将来的には削除する予定です
     */
    @Deprecated
    public ExtractValue(String key, Class target, String aliase, String property) {
    	this(key, target, aliase, property, null);
    }
    
    public ExtractValue(String key, Class target, String aliase, String property, Class<?> returnType) {
        key.getClass();             //NOP NullPointerExceptionを送出させるため
        target.getClass();          //NOP NullPointerExceptionを送出させるため
        this.key = key;
        this.target = target;
        this.aliase = aliase;
        this.property = property;
        this.value = new Property(target, aliase, property);
        this.returnType = returnType;
    }
    
    public ExtractValue(String key, Value value) {
    	this(key, value, null);
    }
    
    public ExtractValue(String key, Value value, Class<?> returnType) {
    	this.key = key;
    	this.value = value;
        this.target = null;
        this.aliase = null;
        this.property = null;
        this.returnType = returnType;
    }
}
