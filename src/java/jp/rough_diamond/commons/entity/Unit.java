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
 * ���ʎړx��Hibernate�}�b�s���O�N���X
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

    //base�����ȎQ�Ƃ̏ꍇrateStr�͋����I�ɂP���Z�b�g����
    //XXX �G���[�ɂ��Ă��ǂ��������ǃ��\�[�X����̂��ʓ|��������
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
    		//�Q�ƒl����v���Ă����BaseUnit
    		return true;
    	}
    	if(getId() == null || base.getId() == null) {
    		//�Q�ƒl����v���Ă��Ȃ��Ăǂ��炩��ID��null�Ȃ�s��Ȃ̂�false
    		return false;
    	}
    	return getId().longValue() == base.getId().longValue();
    }
}
