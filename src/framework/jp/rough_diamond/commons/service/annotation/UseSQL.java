package jp.rough_diamond.commons.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * サービス内でＳＱＬを利用していることを示すマーカーアノテーション
 * （将来的には何か使いどころあるかもね。。。） 
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface UseSQL {

}
