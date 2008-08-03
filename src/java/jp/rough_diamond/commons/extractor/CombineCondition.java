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

import java.util.ArrayList;
import java.util.Collection;

/**
 * ������Condition����������Condition
 * @author $Author: Matsuda_Kazuto@ogis-ri.co.jp $
 * @date $Date: 2006-02-14 19:22:14 +0900 (火, 14 2 2006) $
 */
public abstract class CombineCondition extends Condition {
	private static final long serialVersionUID = 1L;
	private final Collection<Condition> conditions;

	/**
	 * CombineCondition�I�u�W�F�N�g�𐶐�����
	 * ��������钆�g�͊k�Ƃ���
	 */
	public CombineCondition() {
		conditions = new ArrayList<Condition>();
	}
	
	/**
	 * CombineCondition�I�u�W�F�N�g�𐶐�����
	 * @param conditions	Condition�Q nulll�̏ꍇ��NullPointerException���X���[����
	 */
	public CombineCondition(Collection<Condition> conditions) {
		conditions.size();	//NOP null�Ȃ狭���I�ɗ�O�𑗏o���������̂�
		this.conditions = conditions;
	}

	/**
	 * ��������Condition�̃C���e�[�^��ԋp����
	 * @return ��������Condition��Iterator
	 */
	public Collection<Condition> getConditions() {
		return conditions;
	}
	
	/**
	 * ������������𖖔��ɒǉ�����
	 * @param condition	����
	 */
	public void add(Condition condition) {
		conditions.add(condition);
	}

	/**
	 * �������������擾����
	 * @return ������������
	 */
	public int getSize() {
		return conditions.size();
	}
}
