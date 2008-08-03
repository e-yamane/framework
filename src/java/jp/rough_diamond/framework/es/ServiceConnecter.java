package jp.rough_diamond.framework.es;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �G���^�[�v���C�Y�T�[�r�X�R�l�N�^�[
 * @author e-yamane
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceConnecter {
	String serviceName();
	String inboundName() default "";
}
