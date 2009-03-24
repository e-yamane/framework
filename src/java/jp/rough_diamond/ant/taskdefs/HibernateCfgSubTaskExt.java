/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.ant.taskdefs;

import java.util.Collection;
import java.util.StringTokenizer;

import org.apache.tools.ant.types.Parameter;

import xdoclet.XDocletException;
import xdoclet.modules.hibernate.HibernateCfgSubTask;

public class HibernateCfgSubTaskExt extends HibernateCfgSubTask {
	private static final long serialVersionUID = 1L;
	private HibernateCfgSubTask base;
	HibernateCfgSubTaskExt(HibernateCfgSubTask base) {
		this.base = base;
	}
	
	public void setOtherResources(String resources) {
		StringTokenizer tokenizer = new StringTokenizer(resources, ", ");
		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			Parameter p = new Parameter();
			p.setName("resource");
			p.setValue(token);
			System.out.println(token);
			addOtherMapping(p);
		}
	}
	
	@Override
	public void addConfiguredJndiProperty(Parameter jndiProperty) {
		base.addConfiguredJndiProperty(jndiProperty);
	}
	@Override
	public void addOtherMapping(Parameter otherMapping) {
		base.addOtherMapping(otherMapping);
	}
	@Override
	public void addOtherProperty(Parameter otherProperty) {
		base.addOtherProperty(otherProperty);
	}
	@Override
	public void execute() throws XDocletException {
		base.execute();
	}
	@Override
	public String getCacheProviderClass() {
		return base.getCacheProviderClass();
	}
	@Override
	public String getCglibUseReflectionOptimizer() {
		return base.getCglibUseReflectionOptimizer();
	}
	@Override
	public String getDataSource() {
		return base.getDataSource();
	}
	@Override
	public String getDefaultSchema() {
		return base.getDefaultSchema();
	}
	@Override
	public String getDialect() {
		return base.getDialect();
	}
	@Override
	public String getDriver() {
		return base.getDriver();
	}
	@Override
	public String getHbm2ddl() {
		return base.getHbm2ddl();
	}
	@Override
	public String getJdbcUrl() {
		return base.getJdbcUrl();
	}
	@Override
	public String getJndiName() {
		return base.getJndiName();
	}
	@Override
	@SuppressWarnings("unchecked")
	public Collection getJndiProperties() {
		return base.getJndiProperties();
	}
	@Override
	@SuppressWarnings("unchecked")
	public Collection getOtherMappings() {
		return base.getOtherMappings();
	}
	@Override
	@SuppressWarnings("unchecked")
	public Collection getOtherProperties() {
		return base.getOtherProperties();
	}
	@Override
	public String getPassword() {
		return base.getPassword();
	}
	@Override
	public String getPoolSize() {
		return base.getPoolSize();
	}
	@Override
	public boolean getShowSql() {
		return base.getShowSql();
	}
	@Override
	public String getTransactionManagerFactory() {
		return base.getTransactionManagerFactory();
	}
	@Override
	public String getTransactionManagerLookup() {
		return base.getTransactionManagerLookup();
	}
	@Override
	public String getTransactionManagerStrategy() {
		return base.getTransactionManagerStrategy();
	}
	@Override
	public boolean getUseOuterJoin() {
		return base.getUseOuterJoin();
	}
	@Override
	public String getUserName() {
		return base.getUserName();
	}
	@Override
	public String getUserTransactionName() {
		return base.getUserTransactionName();
	}
	@Override
	public String getVersion() {
		return base.getVersion();
	}
	@Override
	public void setCacheProviderClass(String string) {
		base.setCacheProviderClass(string);
	}
	@Override
	public void setCglibUseReflectionOptimizer(String string) {
		base.setCglibUseReflectionOptimizer(string);
	}
	@Override
	public void setDataSource(String dataSource) {
		base.setDataSource(dataSource);
	}
	@Override
	public void setDefaultSchema(String string) {
		base.setDefaultSchema(string);
	}
	@Override
	public void setDialect(String dialect) {
		base.setDialect(dialect);
	}
	@Override
	public void setDriver(String driver) {
		base.setDriver(driver);
	}
	@Override
	public void setHbm2ddl(String hbm2ddl) {
		base.setHbm2ddl(hbm2ddl);
	}
	@Override
	public void setJdbcUrl(String jdbcUrl) {
		base.setJdbcUrl(jdbcUrl);
	}
	@Override
	public void setJndiName(String jndiName) {
		base.setJndiName(jndiName);
	}
	@Override
	public void setPassword(String password) {
		base.setPassword(password);
	}
	@Override
	public void setPoolSize(String poolSize) {
		base.setPoolSize(poolSize);
	}
	@Override
	public void setShowSql(boolean showSql) {
		base.setShowSql(showSql);
	}
	@Override
	public void setTransactionManagerFactory(String string) {
		base.setTransactionManagerFactory(string);
	}
	@Override
	public void setTransactionManagerLookup(String transactionManagerLookup) {
		base.setTransactionManagerLookup(transactionManagerLookup);
	}
	@Override
	public void setTransactionManagerStrategy(String transactionManagerStrategy) {
		base.setTransactionManagerStrategy(transactionManagerStrategy);
	}
	@Override
	public void setUseOuterJoin(boolean useOuterJoin) {
		base.setUseOuterJoin(useOuterJoin);
	}
	@Override
	public void setUserName(String userName) {
		base.setUserName(userName);
	}
	@Override
	public void setUserTransactionName(String userTransactionName) {
		base.setUserTransactionName(userTransactionName);
	}
	@Override
	public void setVersion(HibernateCFGVersion version) {
		base.setVersion(version);
	}
	@Override
	public void validateOptions() throws XDocletException {
		base.validateOptions();
	}
}
