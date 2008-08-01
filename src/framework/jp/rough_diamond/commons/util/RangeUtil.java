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
 * 範囲チェックを行うユーティリティ
**/
public class RangeUtil {
    /**
     * 日付範囲チェック
    **/
    public static boolean isIncludeDay(Date start, Date end, Date target) {
        start = initDate(start).getTime();
        Calendar endCal = initDate(end);
        endCal.add(Calendar.DAY_OF_MONTH, 1);
        end = endCal.getTime();
        return isIncludeDate(start, end, target);
    }
    
    /**
     * 時刻範囲チェック
    **/
    public static boolean isIncludeMinute(Date start, Date end, Date target) {
        start = initMinute(start).getTime();
        Calendar endCal = initMinute(end);
        endCal.add(Calendar.MINUTE, 1);
        end = endCal.getTime();
        target = initMinute(target).getTime();
        return isIncludeDate(start, end, target);
    }

    private static boolean isIncludeDate(Date start, Date end, Date target) {
        return (
                (start.compareTo(target) <= 0) &&
                (target.compareTo(end) < 0));
    }
    
    private static Calendar initDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        initDate(cal);
        return cal;
    }
    
    private static void initDate(Calendar cal) {
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
    
    private static Calendar initMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        initMinute(cal);
        return cal;
    }
    
    private static void initMinute(Calendar cal) {
        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
}