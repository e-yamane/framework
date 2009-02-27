package jp.rough_diamond.commons.util.jobmonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * cronちっくなジョブモニター
 * @author e-yamane
 *
 */
public class CronJobMonitor implements JobMonitor {
	Timer timer = null;
	final List<CronTask> tasks;
	final long period;
	public CronJobMonitor(List<CronTask> tasks) {
		this(tasks, 10000);	//10秒間隔
	}

	public CronJobMonitor(List<CronTask> tasks, long period) {
		this.tasks = new ArrayList<CronTask>(tasks);
		this.period = period;
	}
	
	@Override
	public synchronized void start() {
		if(timer != null) {
			return;
		}
		timer = new Timer(true);
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				for(CronTask task : tasks) {
					task.executeWhenPastNextTimestamp();
				}
			}
		}, 0, period);
	}

	@Override
	public synchronized void stop() {
		if(timer == null) {
			return;
		}
		timer.cancel();
		timer = null;
	}
}
