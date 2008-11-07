package jp.rough_diamond.commons.entity;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import jp.rough_diamond.commons.resource.Message;
import jp.rough_diamond.commons.resource.Messages;
import jp.rough_diamond.commons.resource.ResourceManager;
import jp.rough_diamond.commons.service.annotation.PrePersist;
import jp.rough_diamond.commons.service.annotation.PreUpdate;
import jp.rough_diamond.commons.service.annotation.Verifier;

/**
 * 数量尺度のHibernateマッピングクラス
**/
public class Unit extends jp.rough_diamond.commons.entity.base.BaseUnit {
    private static final long serialVersionUID = 1L;
    
    public BigDecimal getRate() {
    	return new BigDecimal(getRateStr());
    }

    @Verifier
    public Messages checkRate() {
    	Messages ret = new Messages();
    	try {
    		new BigDecimal(getRateStr());
    	} catch(NumberFormatException e) {
    		ResourceBundle rb = ResourceManager.getResource();
    		ret.add("Unit.rateStr", new Message("errors.format.number", rb.getString("Unit.rateStr")));
    	}
    	return ret;
    }

    //baseが自己参照の場合rateStrは強制的に１をセットする
    //XXX エラーにしても良かったけどリソースきるのが面倒だったｗ
    @PrePersist
    @PreUpdate
    public void riviseBaseRate() {
    	if(isBaseUnit()) {
    		setRateStr("1");
    	}
    }
    
    public boolean isBaseUnit() {
    	Unit base = getBase();
    	if(base == null) {
    		return false;
    	}
    	if(this == base) {
    		//参照値が一致していればBaseUnit
    		return true;
    	}
    	if(getId() == null || base.getId() == null) {
    		//参照値が一致していなくてどちらかのIDがnullなら不定なのでfalse
    		return false;
    	}
    	return getId().longValue() == base.getId().longValue();
    }
}
