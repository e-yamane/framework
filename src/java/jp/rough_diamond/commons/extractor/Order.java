/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.io.Serializable;

/**
 * �\�[�g����
 */
@SuppressWarnings("unchecked")
abstract public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * �\�[�g�Ώۃv���p�e�B��
	 */
	public final String propertyName;
	
    /**
     * �^�[�Q�b�g�N���X
     */
    public final Class target;
    
    /**
     * �G�C���A�X
     */
    public final String aliase;
    
	/**
	 * �\�[�g�����𐶐�����
	 * @param propertyName �v���p�e�B�� null�̏ꍇ��NullPointerException�𑗏o����
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
	 */
	public Order(String propertyName, Class target, String aliase) {
		propertyName.getClass();		//NOP NullPointerException�𑗏o���������ꍇ
		this.propertyName = propertyName;
        this.target = target;
        this.aliase = aliase;
	}

	/**
	 * Asc�I�u�W�F�N�g���擾����
	 * @param propertyName	�v���p�e�B��
	 * @return Asc�I�u�W�F�N�g
	 */
	public static Order asc(String propertyName) {
		return new Asc(propertyName, null, null);
	}
	
    /**
     * Asc�I�u�W�F�N�g���擾����
     * @param propertyName  �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @return Asc�I�u�W�F�N�g
     */
    public static Order asc(String propertyName, Class target, String aliase) {
        return new Asc(propertyName, target, aliase);
    }
    
	/**
	 * Desc�I�u�W�F�g���擾����
	 * @param propertyName �v���p�e�B��
	 * @return	Desc�I�u�W�F�N�g
	 */
	public static Order desc(String propertyName) {
		return new Desc(propertyName, null, null);
	}

    /**
     * Desc�I�u�W�F�g���擾����
     * @param propertyName �v���p�e�B��
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
     * @return  Desc�I�u�W�F�N�g
     */
    public static Order desc(String propertyName, Class target, String aliase) {
        return new Desc(propertyName, target, aliase);
    }
}
