package jp.rough_diamond.commons.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �T�[�r�X���łr�p�k�𗘗p���Ă��邱�Ƃ������}�[�J�[�A�m�e�[�V����
 * �i�����I�ɂ͉����g���ǂ��날�邩���ˁB�B�B�j 
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface UseSQL {

}
