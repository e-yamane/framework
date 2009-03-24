/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

/**
 * �v���p�e�B����ێ�����R���f�B�V����
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (火, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
public abstract class LabelHoldingCondition extends Condition {
	private static final long serialVersionUID = 1L;

	/**
	 * �v���p�e�B��
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
	 * �v���p�e�B����ێ�����Condition�𐶐�����
	 * @param propertyName	�v���p�e�B�� null�̏ꍇ��NullPointerException�𑗏o����
     * @param target    �v���p�e�B��ێ����Ă���G���e�B�e�B�N���X
     * @param aliase    �G���e�B�e�B�̕ʖ�
	 */
	public LabelHoldingCondition(String propertyName, Class target, String aliase) {
		propertyName.length();	//NOP null�Ȃ狭���I�ɗ�O�𑗏o���������̂�
		this.propertyName = propertyName;
        this.target = target;
        this.aliase = aliase;
	}
}
