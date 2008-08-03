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
/*
 * $Id: Pager.java 157 2006-02-14 10:22:14Z Matsuda_Kazuto@ogis-ri.co.jp $
 * $Header$
 */
package jp.rough_diamond.commons.pager;

import java.io.Serializable;
import java.util.List;

/**
 * コレクションをページングするインタフェース
 * @author $Author: Matsuda_Kazuto@ogis-ri.co.jp $
 */
public interface Pager<E> extends Serializable {
    /**
     * 現在のページを取得
    **/
    public int getCurrentPage();

    /**
     * 総ページ数を取得
     * @return ページ総数
    **/
    public int getPageSize();

    /**
     * コレクションの総数を取得
     * @return コレクション総数
    **/
    public long getSize();
    
    /**
     * １ページに表示するコレクション件数を取得
     * @return １ページに表示するコレクション件数
    **/
    public int getSizePerPage();
    
    /**
     * 現在のページに表示するコレクションを表示する
     * @return 現在のページに表示するコレクション
    **/
    public List<E> getCurrentPageCollection();
    
    /**
     * 先頭ページチェック
     * @return 先頭ページの場合にtrue
    **/
    public boolean isFirst();
    
    /**
     * 最終ページチェック
     * @return 最終ページの場合にtrue
    **/
    public boolean isLast();
    
    /**
     * 前ページへ遷移する
     * @precondition 先頭ページではないこと
    **/
    public void previous();
    
    /**
     * 次ページへ遷移する
     * @precondition 最終ページではないこと
    **/
    public void next();
    
    /**
     * 指定ページへ遷移する
     * @precondition 1 <= page <= getPageSize()
     * @param   page    ジャンプするページ
    **/
    public void gotoPage(int page);
    
    /**
     * 表示開始ページ数を取得する
     * @return 表示開始ページ
    **/
    public int getWindFirst();
    
    /**
     * 表示終了ページ数を取得する
     * @return 表示終了ページ
    **/
    public int getWindFinish();
    
    /**
     * 表示している最初のエレメントの位置(1相対)を取得する
     * @return 表示している最初のエレメントの位置
    **/
    public long getIndexAtFirstElement();
    
    /**
     * 表示している最後のエレメントの位置(1相対）を返却する
     * @return 表示している最後のエレメントの位置
    **/
    public long getIndexAtLastElement();
}