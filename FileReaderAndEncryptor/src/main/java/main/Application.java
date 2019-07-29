package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class Application {

	@Autowired
    JobLauncher jobLauncher;
      
    @Autowired
    Job job;
    
	public final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();

        SpringApplication.run(Application.class, args);
		time = System.currentTimeMillis() - time;
		logger.info("Runtime: {} seconds.", ((double)time/1000));
    }
    
    //@Scheduled(cron = "${batch.cron}")
    @Scheduled(fixedRate = 5000)
    public void perform() throws Exception
    {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
