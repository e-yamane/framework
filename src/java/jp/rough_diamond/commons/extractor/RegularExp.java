/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * ���K�\��(POSIX) Condition.
 * PostgreSQL�ɂ̂ݑΉ����Ă��܂��B{char_length(substring(propartyName , value)) > 0}
 */
@SuppressWarnings("unchecked")
public class RegularExp extends ValueHoldingCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * ���K�\��(POSIX) Condition �𐶐�����B
	 * @param propertyName	�v���p�e�B�� null�̏ꍇ��NullPointerException�𑗏o����
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
	 * @param value		�l null�̏ꍇ��NullPointerException�𑗏o����
	 */
	public RegularExp(String propertyName, Class target, String aliase, Object value) {
		super(propertyName, target, aliase, value);
	}

}
