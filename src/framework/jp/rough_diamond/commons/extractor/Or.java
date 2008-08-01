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

import java.util.Collection;

/**
 * And������\��Condition
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @Date $Date: 2006-02-08 10:33:45 +0900 (水, 08 2 2006) $
 */
public class Or extends CombineCondition {
	private static final long serialVersionUID = 1L;

	/**
	 * Or�I�u�W�F�N�g�𐶐�����
	 * ��������钆�g�͊k�Ƃ���
	 */
	public Or() {
		super();
	}
	
	/**
	 * Or�I�u�W�F�N�g�𐶐�����
	 * @param conditions	Condition�Q nulll�̏ꍇ��NullPointerException���X���[����
	 */
	public Or(Collection<Condition> conditions) {
		super(conditions);
	}
}
