/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.extractor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 抽出条件格納オブジェクト
 */
@SuppressWarnings("unchecked")
public class Extractor implements Serializable {
	private static final long serialVersionUID = 1L;

    public final static int		DEFAULT_FETCH_SIZE = -1;

    /**
	 * 抽出対象エンティティクラス
	 */
	public final Class target;

    /**
     * 抽出対象エイリアス
     */
    public final String targetAlias;
    
	private List<Order> 	     						orders = new ArrayList<Order>();
	private List<Condition<Property>> 					condition = new ArrayList<Condition<Property>>();
    private List<InnerJoin>     						innerJoins = new ArrayList<InnerJoin>();
    private List<ExtractValue>  						values = new ArrayList<ExtractValue>();
    private List<Condition<? extends SummaryFunction>>	having = new ArrayList<Condition<? extends SummaryFunction>>();
    
	private int 	offset = 0;
	private int		limit = -1;
	private int		fetchSize = DEFAULT_FETCH_SIZE;
	
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
	public List<Condition<Property>> getConditionIterator() {
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
	 * フェッチサイズを取得する
	 * @return	フェッチサイズ
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * フェッチサイズを設定する
	 * @param fetchSize
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
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

	/**
	 * 抽出条件(having)を追加する
	 * @param con	抽出条件 nullの場合はNullPointerExceptionを送出する
	 */
	public void addHaving(Condition<? extends SummaryFunction> con) {
		con.getClass();
		having.add(con);
	}
	
	/**
	 * オーダー条件のIteratorを返却する
	 * @return オーダー条件のIterator
	 */
	public List<Condition<? extends SummaryFunction>> getHavingIterator() {
		return having;
	}
}
