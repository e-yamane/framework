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
 * 複数のConditionを結合するCondition
 * @author $Author: Matsuda_Kazuto@ogis-ri.co.jp $
 * @date $Date: 2006-02-14 19:22:14 +0900 (轣ｫ, 14 2 2006) $
 */
public abstract class CombineCondition extends Condition {
	private static final long serialVersionUID = 1L;
	private final Collection<Condition> conditions;

	/**
	 * CombineConditionオブジェクトを生成する
	 * 結合される中身は殻とする
	 */
	public CombineCondition() {
		conditions = new ArrayList<Condition>();
	}
	
	/**
	 * CombineConditionオブジェクトを生成する
	 * @param conditions	Condition群 nulllの場合はNullPointerExceptionをスローする
	 */
	public CombineCondition(Collection<Condition> conditions) {
		conditions.size();	//NOP nullなら強制的に例外を送出させたいので
		this.conditions = conditions;
	}

	/**
	 * 結合するConditionのイレテータを返却する
	 * @return 結合するConditionのIterator
	 */
	public Collection<Condition> getConditions() {
		return conditions;
	}
	
	/**
	 * 結合する条件を末尾に追加する
	 * @param condition	条件
	 */
	public void add(Condition condition) {
		conditions.add(condition);
	}

	/**
	 * 結合条件数を取得する
	 * @return 結合条件件数
	 */
	public int getSize() {
		return conditions.size();
	}
}
