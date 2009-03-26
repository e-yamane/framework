/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.entity.base;




import  java.io.Serializable;


/**
 * ナンバリングテーブルのHibernateマッピングベースクラス
 * @hibernate.class
 *    table="NUMBERING"
 *    realClass="jp.rough_diamond.commons.entity.Numbering"
**/

public abstract class BaseNumbering  implements Serializable {
    public BaseNumbering() {
    }

    /**
     * ＩＤ
    **/ 
    private String id;

    public final static String ID = "id";
    /**
     * ＩＤを取得する
     * @hibernate.id
     *    generator-class="assigned"
     *    column="ID"
     *    not-null="true"
     *    length="128"
     * @return ＩＤ
    **/

    public String getId() {
        return id;
    }

    /**
     * ＩＤを設定する
     * @param id  ＩＤ
    **/
    public void setId(String id) {
        this.id = id;
    }
    
    public int hashCode() {
        if(getId() == null) {
            return super.hashCode();
        } else {
            return getId().hashCode();
        }
    }
    
    public boolean equals(Object o) {
        if(o instanceof BaseNumbering) {
            if(hashCode() == o.hashCode()) {
                BaseNumbering obj = (BaseNumbering)o;
                if(getId() == null) {
                    return super.equals(o);
                }
                return getId().equals(obj.getId());
            }
        }
        return false;
    }

    /**
     * 現在割り当てている番号
    **/ 
    private Long nextNumber;
    public final static String NEXT_NUMBER = "nextNumber";

    /**
     * 現在割り当てている番号を取得する
     * @hibernate.property
     *    column="NEXT_NUMBER"
     *    not-null="true"
     * @return 現在割り当てている番号
    **/
    @jp.rough_diamond.commons.service.annotation.NotNull(property="Numbering.nextNumber")
    public Long getNextNumber() {
        return nextNumber;
    }

    /**
     * 現在割り当てている番号を設定する
     * @param nextNumber  現在割り当てている番号
    **/
    public void setNextNumber(Long nextNumber) {
        this.nextNumber = nextNumber;
    }



    
    private static final long serialVersionUID = 1L;
}
