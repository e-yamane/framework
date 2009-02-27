package jp.rough_diamond.commons.util.jobmonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * •¡”‚ÌJob‚ğW‡‚µ‚½‚à‚Ì 
 * @author e-yamane
 */
public class JobList implements Job {
	private final List<Job> jobList;
	public JobList(List<Job> list) {
		jobList = new ArrayList<Job>(list); 
	}
	
	@Override
	public void run() {
		for(Job job : jobList) {
			job.run();
		}
	}
}
