package jp.rough_diamond.ant.taskdefs;

import java.io.File;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;

import xdoclet.modules.hibernate.HibernateCfgSubTask;
import xdoclet.modules.hibernate.HibernateDocletTask;

public class HibernateDocletTaskExt extends HibernateDocletTask {
	public void setFileSetDirs(String filesetDirs) {
		System.out.println("xyz:" + filesetDirs);
		String pattern = getProject().getProperty("hibernate.pattern");
		StringTokenizer tokenizer = new StringTokenizer(filesetDirs, ";");
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			System.out.println(token);
			FileSet fs = new FileSet();
			fs.setDir(new File(token));
			fs.setIncludes(pattern);
			addFileset(fs);
		}
	}

	@Override
    public Object createDynamicElement(String name) throws BuildException {
		System.out.println("ZZZZZZZZZ:" + name);
		if("hibernatecfg".equals(name)) {
			HibernateCfgSubTask base = (HibernateCfgSubTask) super.createDynamicElement(name);
			HibernateCfgSubTaskExt ext = new HibernateCfgSubTaskExt(base);
			return ext;
		} else {
			return super.createDynamicElement(name);
		}
	}
}
