/*
 * Copyright (c) 2008, 2009
 *  Rough Diamond Co., Ltd.              -- http://www.rough-diamond.co.jp/
 *  Information Systems Institute, Ltd.  -- http://www.isken.co.jp/
 *  All rights reserved.
 */
package jp.rough_diamond.commons.util.jobmonitor;

import hudson.scheduler.CronTabList;

import java.util.Calendar;
import java.util.Date;

import antlr.ANTLRException;

public class HudsonCronTask implements CrontabTask {
	final Schedule schedule;
	Date nextTimestamp;
	final Job job;
	public HudsonCronTask(String scheduleString, Job job) {
		schedule = new Schedule(scheduleString);
		nextTimestamp = schedule.getNextTimestamp();
		this.job = job;
	}
	
	public void executeWhenPastNextTimestamp() {
		executeWhenPastNextTimestamp(new Date());
	}

	void executeWhenPastNextTimestamp(Date cur) {
		if(nextTimestamp.compareTo(cur) <= 0) {
			try {
				job.run();
			} finally {
				nextTimestamp = schedule.getNextTimestamp();
			}
		}
	}
	
	static class Schedule {
		CronTabList ctl;
		
		Schedule(String scheduleString) {
			try {
				ctl = CronTabList.create(scheduleString);
			} catch (ANTLRException e) {
				throw new RuntimeException(e);
			}
		}
		
		public Date getNextTimestamp() {
			return getNextTimestamp(new Date());
		}
		
		Date getNextTimestamp(Date d) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			Calendar ret = Calendar.getInstance();
			ret.setTime(d);
			ret.set(Calendar.MILLISECOND, 0);
			ret.set(Calendar.SECOND, 0);
			ret.add(Calendar.MINUTE, 1);
			while(!ctl.check(ret)) {
				ret.add(Calendar.MINUTE, 1);
			}
			return ret.getTime();
		}
	}
}