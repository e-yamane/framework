/*
 * ====================================================================
 * 
 *  Copyright 2007 Eiji Yamane(yamane@super-gs.jp)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 */
/**
 * 
 */
package jp.rough_diamond.commons.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.rough_diamond.commons.service.WhenVerifier;

/**
 * 各エンティティに対して検証を拡張するメソッドの注釈（アノテーション）
 * 本アノテーションを持つメソッドは以下のルールを遵守しなければならない
 *   ・戻り値のタイプがjp.adirect.trace.util.Messagesもしくはvoidであること
 *   　（但しvoidの場合は検証失敗を上位に通知できないので注意すること）
 *   ・引数は無しもしくは、Verifier.Whenを有すること
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-08 13:22:52 +0900 (豌ｴ, 08 2 2006) $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Verifier {
    /**
     * 検証タイミング
     * @return When.UPDATEが含まれている場合は更新時、When.INSERTが含まれている場合は登録時
     */
    WhenVerifier[] when() default {WhenVerifier.UPDATE, WhenVerifier.INSERT};

    /**
     * 検証優先度
     * @return 数値が高い検証から先に呼び出される
     * 同一優先度の場合はtoString()の文字列比較の昇順となる
     */
    int priority() default 0;
    
    /**
     * 事前検証を無視するかしないか？
     * @return trueの場合は以前にエラーがあっても検証を継続する。
     * ただし、対象となる検証メソッドよりも優先度の高い検証メソッドが事前検証を無視しない場合は、
     * 以降の検証は行われない
     */
    boolean isForceExec() default false;
}
