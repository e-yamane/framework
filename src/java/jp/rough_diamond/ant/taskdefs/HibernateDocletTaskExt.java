/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
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
			setFilePatterns(fs, pattern);
//			fs.setIncludes(pattern);
			addFileset(fs);
		}
	}

	static void setFilePatterns(FileSet fs, String pattern) {
		String[] elements = pattern.split(";");
		System.out.println(elements.length);
		StringBuilder includesSB = new StringBuilder();
		StringBuilder excludesSB = new StringBuilder();
		for(String el : elements) {
			Patterns p = new Patterns(el);
			StringBuilder tmp = (p.isExclude) ? excludesSB : includesSB;
			tmp.append(p.pattern);
			tmp.append(",");
		}
		fs.setIncludes(includesSB.toString());
		fs.setExcludes(excludesSB.toString());
	}
	
	static class Patterns {
		final boolean 	isExclude;
		final String 	pattern;
		Patterns(String patternElement) {
			String[] split = patternElement.split(":");
			if(split.length == 1) {
				isExclude = false;
				pattern = split[0];
			} else {
				isExclude = "e".equals(split[0].toLowerCase());
				pattern = split[1];
			}
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
