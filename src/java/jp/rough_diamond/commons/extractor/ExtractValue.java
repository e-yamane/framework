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
    public final Class target;
    public final String aliase;
    public final String property;
    
    /**
     * 抽出値を指定する
     * @param key
     * @param target
     * @param aliase
     * @param property
     */
    public ExtractValue(String key, Class target, String aliase, String property) {
        key.getClass();             //NOP NullPointerExceptionを送出させるため
        target.getClass();          //NOP NullPointerExceptionを送出させるため
        this.key = key;
        this.target = target;
        this.aliase = aliase;
        this.property = property;
    }
}
