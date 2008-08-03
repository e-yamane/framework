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
/*
 * $Id: Pager.java 157 2006-02-14 10:22:14Z Matsuda_Kazuto@ogis-ri.co.jp $
 * $Header$
 */
package jp.rough_diamond.commons.pager;

import java.io.Serializable;
import java.util.List;

/**
 * �R���N�V�������y�[�W���O����C���^�t�F�[�X
 * @author $Author: Matsuda_Kazuto@ogis-ri.co.jp $
 */
public interface Pager<E> extends Serializable {
    /**
     * ���݂̃y�[�W���擾
    **/
    public int getCurrentPage();

    /**
     * ���y�[�W�����擾
     * @return �y�[�W����
    **/
    public int getPageSize();

    /**
     * �R���N�V�����̑������擾
     * @return �R���N�V��������
    **/
    public long getSize();
    
    /**
     * �P�y�[�W�ɕ\������R���N�V�����������擾
     * @return �P�y�[�W�ɕ\������R���N�V��������
    **/
    public int getSizePerPage();
    
    /**
     * ���݂̃y�[�W�ɕ\������R���N�V������\������
     * @return ���݂̃y�[�W�ɕ\������R���N�V����
    **/
    public List<E> getCurrentPageCollection();
    
    /**
     * �擪�y�[�W�`�F�b�N
     * @return �擪�y�[�W�̏ꍇ��true
    **/
    public boolean isFirst();
    
    /**
     * �ŏI�y�[�W�`�F�b�N
     * @return �ŏI�y�[�W�̏ꍇ��true
    **/
    public boolean isLast();
    
    /**
     * �O�y�[�W�֑J�ڂ���
     * @precondition �擪�y�[�W�ł͂Ȃ�����
    **/
    public void previous();
    
    /**
     * ���y�[�W�֑J�ڂ���
     * @precondition �ŏI�y�[�W�ł͂Ȃ�����
    **/
    public void next();
    
    /**
     * �w��y�[�W�֑J�ڂ���
     * @precondition 1 <= page <= getPageSize()
     * @param   page    �W�����v����y�[�W
    **/
    public void gotoPage(int page);
    
    /**
     * �\���J�n�y�[�W�����擾����
     * @return �\���J�n�y�[�W
    **/
    public int getWindFirst();
    
    /**
     * �\���I���y�[�W�����擾����
     * @return �\���I���y�[�W
    **/
    public int getWindFinish();
    
    /**
     * �\�����Ă���ŏ��̃G�������g�̈ʒu(1����)���擾����
     * @return �\�����Ă���ŏ��̃G�������g�̈ʒu
    **/
    public long getIndexAtFirstElement();
    
    /**
     * �\�����Ă���Ō�̃G�������g�̈ʒu(1���΁j��ԋp����
     * @return �\�����Ă���Ō�̃G�������g�̈ʒu
    **/
    public long getIndexAtLastElement();
}