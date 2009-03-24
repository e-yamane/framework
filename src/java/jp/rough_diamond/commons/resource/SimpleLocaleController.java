/* * Copyright (c) 2008, 2009 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/ *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/ *  All rights reserved. */package jp.rough_diamond.commons.resource;import java.util.Locale;/** * JavaVM毎にローエルを管理するローケルコントローラー * ローケルがセットされていない場合はデフォルトローケルを返却する */public class SimpleLocaleController extends LocaleController {    private Locale locale = Locale.getDefault();    @Override    public Locale getLocale() {        return locale;    }    @Override    public void setLocale(Locale locale) {        Locale old = getLocale();        if(!old.equals(locale)) {            this.locale = locale;            System.out.println("デフォルトローケルの変更：" + locale);            Locale.setDefault(locale);        }    }}