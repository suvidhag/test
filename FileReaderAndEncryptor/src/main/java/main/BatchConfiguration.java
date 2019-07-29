package main;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.expression.ParseException;

import listeners.ChunkExecutionListener;
import listeners.JobCompletionNotificationListener;
import listeners.StepExecutionNotificationListener;
import mapper.PassThroughLineMapper;
import processor.TextProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilders;

	@Autowired
	public StepBuilderFactory stepBuilders;

	@Value("${input.file.name}")
	private String inputFileName;

	@Value("${output.file.name}")
	private String outputFileName;

	@Value("${chunk-size}")
	private int chunkSize;

	@Value("${max-threads}")
	private int maxThreads;
	
	@StepScope
	@Bean
	public FlatFileItemReader<String> reader() throws UnexpectedInputException, ParseException {
		FlatFileItemReader<String> reader = new FlatFileItemReader<>();
		reader.setName("TextFileReader");
		reader.setResource(new FileSystemResource(inputFileName));
		reader.setLineMapper(new PassThroughLineMapper());
		reader.setLinesToSkip(1);
		return reader;
	}

	@StepScope
	@Bean
	public TextProcessor processor() {
		return new TextProcessor();
	}

	@StepScope
	@Bean
	public FlatFileItemWriter<String> fileWriter() {
		return new FlatFileItemWriterBuilder<String>().name("EncryptedFileWriter")
				.resource(new FileSystemResource(outputFileName))
				.lineAggregator(new PassThroughLineAggregator<>())
				.build();
	}

	@Bean(name = "process-attempt-job")
	public Job processAttemptJob() {
		return jobBuilders.get("process-attempt-job")
				.incrementer(new RunIdIncrementer())
				.listener(jobExecutionListener())
				.flow(step())
				.end().build();
	}
	
	@Bean
	public Step step() {
		return stepBuilders.get("step").<String, String>chunk(chunkSize)
				.reader(reader())
				.processor(processor())
				.writer(fileWriter())
				.taskExecutor(taskExecutor())
				.listener(stepExecutionListener())
				.listener(chunkListener())
				.throttleLimit(maxThreads).build();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(maxThreads);
		return taskExecutor;
	}
	
	@Bean
	public JobCompletionNotificationListener jobExecutionListener() {
		return new JobCompletionNotificationListener();
	}
	
	@Bean
	public StepExecutionNotificationListener stepExecutionListener() {
		return new StepExecutionNotificationListener();
	}
	
	@Bean
	public ChunkExecutionListener chunkListener() {
		return new ChunkExecutionListener();
	}
}
