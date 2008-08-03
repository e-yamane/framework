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

import java.io.Serializable;

/**
 * �\�[�g����
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (火, 14 2 2006) $
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
