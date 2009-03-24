/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * ���o�p�����[�^
 */
@SuppressWarnings("unchecked")
public class ExtractValue {
    public final String key;
    public final Class target;
    public final String aliase;
    public final String property;
    
    /**
     * ���o�l���w�肷��
     * @param key
     * @param target
     * @param aliase
     * @param property
     */
    public ExtractValue(String key, Class target, String aliase, String property) {
        key.getClass();             //NOP NullPointerException�𑗏o�����邽��
        target.getClass();          //NOP NullPointerException�𑗏o�����邽��
        this.key = key;
        this.target = target;
        this.aliase = aliase;
        this.property = property;
    }
}
