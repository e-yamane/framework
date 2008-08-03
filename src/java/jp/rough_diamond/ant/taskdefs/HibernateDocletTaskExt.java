package jp.rough_diamond.ant.taskdefs;

import org.apache.tools.ant.BuildException;

import xdoclet.modules.hibernate.HibernateCfgSubTask;
import xdoclet.modules.hibernate.HibernateDocletTask;

public class HibernateDocletTaskExt extends HibernateDocletTask {
	@Override
    public Object createDynamicElement(String name) throws BuildException {
		if("hibernatecfg".equals(name)) {
			HibernateCfgSubTask base = (HibernateCfgSubTask) super.createDynamicElement(name);
			HibernateCfgSubTaskExt ext = new HibernateCfgSubTaskExt(base);
			return ext;
		} else {
			return super.createDynamicElement(name);
		}
	}
}
