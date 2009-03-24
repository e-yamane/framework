/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 件数とオブジェクト群を保持するValueObject
 */
public class FindResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * オブジェクト群
     */
    public final List<T>    list;
    
    /**
     * 総数
     */
    public final long       count;

    /**
     * オブジェクトを生成する
     * @param list  オブジェクト群
     * @param count 総数
     */
    public FindResult(List<T> list, long count) {
        this.list = Collections.unmodifiableList(list);
        this.count = count;
    }
}
