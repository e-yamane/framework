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
package jp.rough_diamond.commons.pager;

import java.util.List;

import jp.rough_diamond.commons.lang.Range;

public abstract class AbstractNonCachePager<E> extends AbstractPager<E> {
	private static final long serialVersionUID = 1L;
	public long getSize() {
		if(isNeedRefresh()) {
			refresh();
		}
		return getCount();
	}

	public List<E> getCurrentPageCollection() {
		if(isNeedRefresh()) {
			refresh();
		}
		return getList();
	}

	public void forceRefresh() {
		setNeedRefresh(true);
	}
	
	private int lastSizePerPage;
	protected void refresh() {
		if(isNeedRefresh()) {
			int offset = (getCurrentPage() - 1) * getSizePerPage();
			refresh(offset, getSizePerPage());
			setNeedRefresh(false);
			if(getCurrentPageCollection().size() == 0 && getCurrentPage() != 1) {
				if(log.isDebugEnabled()) {
					log.debug("ページあたりの表示数等の変更によって現在ページ数＞最大ページ数になりました");
					log.debug("最大ページ数：" + getPageSize());
					log.debug("現在のページ番号:" + getCurrentPage());
				}
				int pageNo = recalculatePageNo();
				log.debug("再計算後のページ番号：" + pageNo);
				gotoPage(pageNo);
				setNeedRefresh(true);
				refresh();
			}
			lastSizePerPage = getSizePerPage();
		}
	}

	private int recalculatePageNo() {
		int lastFirstIndex = lastSizePerPage * getCurrentPage();
		int currentSizePerPage = getSizePerPage();
		int maxPageSize = getPageSize();
		for(int i = maxPageSize ; i != 1 ; i--) {
			int min = (i - 1) * currentSizePerPage + 1;
			int max = (i) * currentSizePerPage;
			Range<Integer> range = new Range<Integer>(min, max);
			if(range.isIncludion(lastFirstIndex)) {
				return i;
			}
		}
		return 1;
	}
	
	private boolean 	isNeedRefresh = true;

	public boolean isNeedRefresh() {
		return isNeedRefresh;
	}

	public void setNeedRefresh(boolean isNeedRefresh) {
		this.isNeedRefresh = isNeedRefresh;
	}

	abstract protected long getCount();
	abstract protected List<E> getList();
	abstract protected void refresh(int offset, int limit);

	public int getSelectingPage() {
		return getCurrentPage();
	}
	
	public void setSelectingPage(int pageNo) {
		if(pageNo != getCurrentPage()) {
			if(pageNo > getPageSize()) {
				gotoPage(getPageSize());
			} else {
				gotoPage(pageNo);
			}
		}
	}

	@Override
	protected void postGotoPage(int page) {
		super.postGotoPage(page);
		setNeedRefresh(true);
	}
    
        
    private int sizePerPage;
    
    public int getSizePerPage() {
        return sizePerPage;
    }
    
    public void setSizePerPage(int sizePerPage) {
        this.sizePerPage = sizePerPage;
        setNeedRefresh(true);
        refresh();
    }
}
