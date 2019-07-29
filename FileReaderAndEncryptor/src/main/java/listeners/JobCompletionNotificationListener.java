package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger logger = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
	
	@Value("${input.file.name}")
	private String inputFileName;

	@Value("${output.file.name}")
	private String outputFileName;

	@Override
	public void beforeJob(JobExecution jobExecution){
		super.beforeJob(jobExecution);
		
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
            System.out.println(e);
		}
		logger.info("Job Started");
	}
	
	@Override
	public void afterJob(JobExecution jobExecution){
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("Job Completed");
		}
	}

}
