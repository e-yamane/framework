/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.util.mule.transformer.test.base;
import java.io.Serializable;


/**
**/
abstract public class BaseChildBean  implements Serializable {
    private  String   yyy;

    private  String   zzz;

    /**
     * @return yyy
    **/
    public String getYyy() {
        return this.yyy;
    }

    /**
     * @return zzz
    **/
    public String getZzz() {
        return this.zzz;
    }


    /**
     * @param yyy
    **/
    public void setYyy(String yyy) {
        this.yyy = yyy;
    }

    /**
     * @param zzz
    **/
    public void setZzz(String zzz) {
        this.zzz = zzz;
    }

    public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("[yyy:");
      buf.append(yyy + "]");
      buf.append("[zzz:");
      buf.append(zzz + "]");
      return buf.toString();
    }

    private static final long serialVersionUID = 1L;
}
