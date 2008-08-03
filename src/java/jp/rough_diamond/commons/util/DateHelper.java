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
 * $Id$
 * $Header$
 */
package jp.rough_diamond.commons.util;

import java.util.Calendar;
import java.util.Date;

/**
 * CalendarオブジェクトをJSTLより使用しやすくするためのラッパークラス
**/
public class DateHelper {
    public DateHelper(Date date) {
        cal = Calendar.getInstance();
        cal.setTime(date);
    }

    public DateHelper(Calendar cal) {
        this.cal = (Calendar)cal.clone();
    }

    public int getDayOfMonth() {
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getWeekInt() {
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    private Calendar cal;
}