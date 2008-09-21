//$Id: BeansBaseTemplate.vm,v 1.1 2005/10/27 15:43:53 yamane Exp $
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
