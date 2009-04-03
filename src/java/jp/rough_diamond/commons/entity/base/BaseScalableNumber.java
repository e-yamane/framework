/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */

package jp.rough_diamond.commons.entity.base;




import  java.io.Serializable;


/**
 * �����_�ʒu�w�萔�l��Hibernate�}�b�s���O�x�[�X�N���X
**/
public abstract class BaseScalableNumber extends java.lang.Number implements Serializable {
    public BaseScalableNumber() {
    }


    /**
     * ��(����)
    **/ 
    private Long value;
    public final static String VALUE = "value";

    /**
     * ��(����)���擾����
     * @hibernate.property
     *    column="VALUE"
     *    not-null="true"
     *    length="20"
     * @return ��(����)
    **/
    public Long getValue() {
        return value;
    }

    /**
     * ��(����)��ݒ肷��
     * @param value  ��(����)
    **/
    public void setValue(Long value) {
        this.value = value;
    }


    /**
     * �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ�������
    **/ 
    private Integer scale;
    public final static String SCALE = "scale";

    /**
     * �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ���������擾����
     * @hibernate.property
     *    column="SCALE"
     *    not-null="true"
     *    length="10"
     * @return �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ�������
    **/
    public Integer getScale() {
        return scale;
    }

    /**
     * �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ��������ݒ肷��
     * @param scale  �����_�ʒu�B���̐��Ȃ獶�ցA���̐��Ȃ�E�ֈړ�������
    **/
    public void setScale(Integer scale) {
        this.scale = scale;
    }



    
    private static final long serialVersionUID = 1L;
}
