/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * �����\�[�g���s��Order
 */
@SuppressWarnings("unchecked")
public class Asc extends Order {
	private static final long serialVersionUID = 1L;

	/**
	 * �����\�[�g���s��Order�𐶐�����
	 * @param propertyName	�v���p�e�B�� null�̏ꍇ��NullPointerException�𑗏o����
	 */
	public Asc(String propertyName, Class target, String aliase) {
		super(propertyName, target, aliase);
	}
}
