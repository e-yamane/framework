//$Id: BeansBaseTemplate.vm,v 1.1 2005/10/27 15:43:53 yamane Exp $
package jp.rough_diamond.commons.util.mule.transformer.test.base;
import java.io.Serializable;

import jp.rough_diamond.commons.util.mule.transformer.test.ChildBean;

/**
**/
abstract public class BaseParentBean  implements Serializable {
    private  ChildBean   child;

    private  String   xxx;

    /**
     * @return child
    **/
    public ChildBean getChild() {
        return this.child;
    }

    /**
     * @return xxx
    **/
    public String getXxx() {
        return this.xxx;
    }


    /**
     * @param child
    **/
    public void setChild(ChildBean child) {
        this.child = child;
    }

    /**
     * @param xxx
    **/
    public void setXxx(String xxx) {
        this.xxx = xxx;
    }

    public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("[child:");
      buf.append(child + "]");
      buf.append("[xxx:");
      buf.append(xxx + "]");
      return buf.toString();
    }

    private static final long serialVersionUID = 1L;
}
