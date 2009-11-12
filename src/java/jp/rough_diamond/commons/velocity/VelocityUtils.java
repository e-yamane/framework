/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.velocity;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.MissingResourceException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.rough_diamond.commons.di.DIContainer;
import jp.rough_diamond.commons.di.DIContainerFactory;
import jp.rough_diamond.commons.entity.Amount;
import jp.rough_diamond.commons.entity.Unit;
import jp.rough_diamond.commons.resource.ResourceManager;
import jp.rough_diamond.framework.user.User;
import jp.rough_diamond.framework.user.UserController;

/**
 * Velocity便利ツール
 */
public class VelocityUtils {
	private final static Log log = LogFactory.getLog(VelocityUtils.class);
	/**
	 * 現在のユーザーオブジェクトを返却する
	 * @return
	 */
	public User getUser() {
		return UserController.getController().getUser();
	}
	
	/**
	 * DIコンテナを返却する
	 * @return
	 */
	public DIContainer getDIContainer() {
		return DIContainerFactory.getDIContainer();
	}

	public String getApplicationPath(HttpServletRequest request) {
	    String scheme = request.getScheme();
	    scheme = scheme.toLowerCase();
	    String hostAndPort = request.getServerName() + ":" + request.getServerPort();
	    String applicationName = request.getContextPath();
	    if(applicationName.startsWith("/") || applicationName.length() == 0) {
	        return scheme + "://" + hostAndPort + applicationName;
	    } else {
	        return scheme + "://" + hostAndPort + "/" + applicationName;
	    }
	}
    
    /**
     * key にひもづくリソースを返す. リソースが存在しない場合は渡されたキーを返す
     * @param key
     * @return　リソース
     */
    public String message(String key) {
        try {
            return ResourceManager.getResource().getString(key);
        } catch(MissingResourceException mre) {
            return key;
        } 
    }

	public String formatAmount(Amount amount) {
		return formatAmount(amount, true, false);
	}

	public String formatAmount(Amount amount, boolean isPrefix, boolean isSuffix) {
		return formatAmount(amount, isPrefix, isSuffix, "#,##0", amount
				.getUnit().getScale());
	}

	public String formatAmount(Amount amount, String formatOfIntegralPart) {
		return formatAmount(amount, true, false, formatOfIntegralPart, amount
				.getUnit().getScale());
	}

	public String formatAmount(Amount amount, int scale) {
		return formatAmount(amount, true, false, "#,##0", scale);
	}

	public String formatAmount(Amount amount, boolean isPrefix,
			boolean isSuffix, String formatOfIntegralPart) {
		return formatAmount(amount, isPrefix, isSuffix, formatOfIntegralPart,
				amount.getUnit().getScale());
	}

	public String formatAmount(Amount amount, boolean isPrefix,
			boolean isSuffix, int scale) {
		return formatAmount(amount, isPrefix, isSuffix, "#,##0", scale);
	}

	public String formatAmount(Amount amount, String formatOfIntegralPart,
			int scale) {
		return formatAmount(amount, true, false, formatOfIntegralPart, scale);
	}

	/**
	 * 量をフォーマッティングする
	 * 
	 * @param amount
	 *            量
	 * @param isPrefix
	 *            単位名を先頭に付与する
	 * @param isSuffix
	 *            単位名を末尾に付与する
	 * @param formatOfIntegralPart
	 *            整数部のフォーマット
	 * @param scale
	 *            小数点以下の桁数
	 * @return
	 * @exception IllegalArgumentException
	 *                isPrefix、isSuffixともにtrueの場合
	 * @author imai
	 */
	public String formatAmount(Amount amount, boolean isPrefix,
			boolean isSuffix, String formatOfIntegralPart, int scale) {
		if (isPrefix == true && isSuffix == true) {
			throw new IllegalArgumentException();
		}
		Unit unit = amount.getUnit();
		StringBuilder formatSB = new StringBuilder(formatOfIntegralPart);
		if (scale > 0) {
			formatSB.append(".");
			char[] array = new char[scale];
			Arrays.fill(array, '0');
			formatSB.append(array);
		}
		log.debug(formatSB.toString());
		DecimalFormat df = new DecimalFormat(formatSB.toString());
		StringBuilder sb = new StringBuilder();
		if (isPrefix) {
			sb.append(unit.getName());
		}
		sb.append(df.format(amount.getQuantity().decimal()));
		if (isSuffix) {
			sb.append(unit.getName());
		}
		return sb.toString();
	}
}
