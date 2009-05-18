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
    /**
     * @deprecated �{�v���p�e�B�͈ȍ~�g�p���܂���
     * �����I�ɍ폜����\��ł�
     */
    @Deprecated
    public final Class target;
    /**
     * @deprecated �{�v���p�e�B�͎g�p���܂���
     * �����I�ɍ폜����\��ł�
     */
    @Deprecated
    public final String aliase;
    /**
     * @deprecated �{�v���p�e�B�͎g�p���܂���
     * �����I�ɍ폜����\��ł�
     */
    @Deprecated
    public final String property;
    
    /**
     * ���o����l
     */
    public final Value value;
    
    /**
     * ���o�l���w�肷��
     * @param key
     * @param target
     * @param aliase
     * @param property
     * @deprecated �����I�ɂ͍폜����\��ł�
     */
    @Deprecated
    public ExtractValue(String key, Class target, String aliase, String property) {
        key.getClass();             //NOP NullPointerException�𑗏o�����邽��
        target.getClass();          //NOP NullPointerException�𑗏o�����邽��
        this.key = key;
        this.target = target;
        this.aliase = aliase;
        this.property = property;
        this.value = new Property(target, aliase, property);
    }
    
    public ExtractValue(String key, Value value) {
    	this.key = key;
    	this.value = value;
        this.target = null;
        this.aliase = null;
        this.property = null;
    }
}
