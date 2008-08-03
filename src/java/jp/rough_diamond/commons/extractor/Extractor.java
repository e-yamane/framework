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
import java.util.ArrayList;
import java.util.List;


/**
 * 抽出条件格納オブジェクト
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-14 17:11:42 +0900 (轣ｫ, 14 2 2006) $
 */
@SuppressWarnings("unchecked")
public class Extractor implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 抽出対象エンティティクラス
	 */
	public final Class target;

    /**
     * 抽出対象エイリアス
     */
    public final String targetAlias;
    
	private List<Order> 	     orders = new ArrayList<Order>();
	private List<Condition>     condition = new ArrayList<Condition>();
    private List<InnerJoin>     innerJoins = new ArrayList<InnerJoin>();
    private List<ExtractValue>  values = new ArrayList<ExtractValue>();
    
	private int 	offset = 0;
	private int	limit = -1;
	
	/**
	 * 抽出条件格納オブジェクトを生成する
	 * @param target 抽出対象エンティティクラス nullの場合はNullPointerExceptionを送出する
	 */
	public Extractor(Class target) {
        this(target, null);
	}

    /**
     * 抽出条件格納オブジェクトを生成する
     * @param target
     * @param alias
     */
    public Extractor(Class target, String alias) {
        target.getClass();  //NOP NullPointerExceptionを送出させたいため
        this.target = target;
        this.targetAlias = alias;
    }
    
	/**
	 * 抽出条件を追加する
	 * @param con	抽出条件 nullの場合はNullPointerExceptionを送出する
	 */
	public void add(Condition con) {
		con.getClass();
		condition.add(con);
	}
	
	/**
	 * ソート条件を追加する
	 * @param order ソート条件 nullの場合はNullPointerExceptionを送出する
	 */
	public void addOrder(Order order) {
		order.getClass();
		orders.add(order);
	}
	
	/**
	 * ソート条件のIteratorを返却する
	 * @return 抽出条件のIterator
	 */
	public List<Order> getOrderIterator() {
		return orders;
	}

	/**
	 * オーダー条件のIteratorを返却する
	 * @return オーダー条件のIterator
	 */
	public List<Condition> getConditionIterator() {
		return condition;
	}

	/**
	 * 抽出上限数を取得する
	 * @return 抽出上限数
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 抽出上限数を設定する
	 * @param limit	抽出上限数 ０以下の場合は抽出上限無しとする
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 抽出開始位置を取得する
	 * @return 抽出開始位置
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * 抽出開始位置を設定する
	 * @param offset	抽出開始位置 ０以下の場合は先頭から取得する
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
    
    /**
     * 内部結合オブジェクトを追加する
     * @param join
     */
    public void addInnerJoin(InnerJoin join) {
        innerJoins.add(join);
    }
    
    /**
     * 内部結合オブジェクト群を返却する
     */
    public List<InnerJoin> getInnerJoins() {
        return innerJoins;
    }
    
    /**
     * 抽出値を追加する
     * @param value
     */
    public void addExtractValue(ExtractValue value) {
        values.add(value);
    }
    
    /**
     * 抽出値群を返却する
     */
    public List<ExtractValue> getValues() {
        return values;
    }
}
