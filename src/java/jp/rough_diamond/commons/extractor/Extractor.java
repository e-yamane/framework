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
	 * 戻りオブジェクトのタイプ
	 */
	public Class<?> returnType;
	
	/**
     * 抽出対象エイリアス
     */
    public final String targetAlias;
    
	private List<Order<? extends Value>> 	 			orders = new ArrayList<Order<? extends Value>>();
	private List<Condition<? extends Value>>			condition = new ArrayList<Condition<? extends Value>>();
    private List<InnerJoin>     						innerJoins = new ArrayList<InnerJoin>();
    private List<ExtractValue>  						values = new ArrayList<ExtractValue>();
    private List<Condition<? extends Value>>			having = new ArrayList<Condition<? extends Value>>();
    
	private int 	offset = 0;
	private int		limit = -1;
	private int		fetchSize = DEFAULT_FETCH_SIZE;
	private int		fetchDepth = -1;

	private boolean	distinct = false;
	
	private boolean isCachable = false;
	
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
	public Extractor add(Condition con) {
		con.getClass();
		condition.add(con);
		return this;
	}
	
	/**
	 * ソート条件を追加する
	 * @param order ソート条件 nullの場合はNullPointerExceptionを送出する
	 */
	public Extractor addOrder(Order<? extends Value> order) {
		order.getClass();
		orders.add(order);
		return this;
	}
	
	/**
	 * ソート条件のIteratorを返却する
	 * @return 抽出条件のIterator
	 */
	public List<Order<? extends Value>> getOrderIterator() {
		return orders;
	}

	/**
	 * オーダー条件のIteratorを返却する
	 * @return オーダー条件のIterator
	 */
	public List<Condition<? extends Value>> getConditionIterator() {
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
	 * このExtractorで生成されるクエリーの返却値のオブジェクトの取得する深さを返却する
	 * @return the fetchDepth
	 */
	public int getFetchDepth() {
		return fetchDepth;
	}

	/**
	 * このExtractorで生成されるクエリーの返却値のオブジェクトの取得する深さを指定する
	 * 負数の場合は実装クラスのデフォルトの値を使用する
	 * @param fetchDepth 参照オブジェクトの取得する深さ
	 */
	public void setFetchDepth(int fetchDepth) {
		this.fetchDepth = fetchDepth;
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
    public Extractor addExtractValue(ExtractValue value) {
        values.add(value);
        return this;
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
	public Extractor addHaving(Condition<? extends Value> con) {
		con.getClass();
		having.add(con);
		return this;
	}
	
	/**
	 * オーダー条件のIteratorを返却する
	 * @return オーダー条件のIterator
	 */
	public List<Condition<? extends Value>> getHavingIterator() {
		return having;
	}

	/**
	 * 戻りタイプを設定する
	 * @param returnType
	 */
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	/**
	 * trueの場合重複データの取得は行わない
	 * ただし、以下の場合はその限りでない
	 *  - ExtractValueを指定しておらずLockModeがNONEの場合はfalseでもtrueと扱う
	 *  　　　（実質的に重複はないと思われるが・・・既存コードにあるため）
	 * @return
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * 重複データの取得可否を設定する
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * このExtractorで生成されるクエリーの返却値をキャッシュするか否かを返却する
	 * @return
	 */
	public boolean isCachable() {
		return isCachable;
	}

	/**
	 * このExtractorで生成されるクエリーの返却値のキャッシュ可否を指定する
	 * @return
	 */
	public void setCachable(boolean isCachable) {
		this.isCachable = isCachable;
	}
}
