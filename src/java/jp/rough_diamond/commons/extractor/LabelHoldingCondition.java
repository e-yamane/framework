/*
 * ====================================================================
 * 
 *  Copyright 2007 Eiji Yamane(yamane@super-gs.jp)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 */
/**
 * 
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
