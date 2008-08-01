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

/**
 * �폜��C�x���g�R�[���o�b�N�A�m�e�[�V����
 * @author $Author: Yamane_Eiji@bp.ogis-ri.co.jp $
 * @date $Date: 2006-02-08 10:33:45 +0900 (水, 08 2 2006) $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostRemove {
    /**
     * �Ăяo���D��x
     * @return ���l���������؂����ɌĂяo�����
     * ����D��x�̏ꍇ��toString()�̕������r�̏����ƂȂ�
     */
    int priority() default 0;
}
