package org.springframework.boot.autoconfigure.batch;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/batch/JobLauncherCommandLineRunner.class */
public class JobLauncherCommandLineRunner implements CommandLineRunner, ApplicationEventPublisherAware {
    private static final Log logger = LogFactory.getLog(JobLauncherCommandLineRunner.class);
    private JobLauncher jobLauncher;
    private JobRegistry jobRegistry;
    private JobExplorer jobExplorer;
    private String jobNames;
    private ApplicationEventPublisher publisher;
    private JobParametersConverter converter = new DefaultJobParametersConverter();
    private Collection<Job> jobs = Collections.emptySet();

    public JobLauncherCommandLineRunner(JobLauncher jobLauncher, JobExplorer jobExplorer) {
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
    }

    public void setJobNames(String jobNames) {
        this.jobNames = jobNames;
    }

    @Override // org.springframework.context.ApplicationEventPublisherAware
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Autowired(required = false)
    public void setJobRegistry(JobRegistry jobRegistry) {
        this.jobRegistry = jobRegistry;
    }

    @Autowired(required = false)
    public void setJobParametersConverter(JobParametersConverter converter) {
        this.converter = converter;
    }

    @Autowired(required = false)
    public void setJobs(Collection<Job> jobs) {
        this.jobs = jobs;
    }

    @Override // org.springframework.boot.CommandLineRunner
    public void run(String... args) throws JobInstanceAlreadyCompleteException, JobParametersNotFoundException, JobRestartException, JobParametersInvalidException, JobExecutionException, JobExecutionAlreadyRunningException {
        logger.info("Running default command line with: " + Arrays.asList(args));
        launchJobFromProperties(StringUtils.splitArrayElementsIntoProperties(args, SymbolConstants.EQUAL_SYMBOL));
    }

    protected void launchJobFromProperties(Properties properties) throws JobInstanceAlreadyCompleteException, JobParametersNotFoundException, JobRestartException, JobParametersInvalidException, JobExecutionException, JobExecutionAlreadyRunningException {
        JobParameters jobParameters = this.converter.getJobParameters(properties);
        executeLocalJobs(jobParameters);
        executeRegisteredJobs(jobParameters);
    }

    private JobParameters getNextJobParameters(Job job, JobParameters additionalParameters) {
        String name = job.getName();
        JobParameters parameters = new JobParameters();
        List<JobInstance> lastInstances = this.jobExplorer.getJobInstances(name, 0, 1);
        JobParametersIncrementer incrementer = job.getJobParametersIncrementer();
        Map<String, JobParameter> additionals = additionalParameters.getParameters();
        if (lastInstances.isEmpty()) {
            if (incrementer != null) {
                parameters = incrementer.getNext(new JobParameters());
            }
        } else {
            List<JobExecution> previousExecutions = this.jobExplorer.getJobExecutions(lastInstances.get(0));
            JobExecution previousExecution = previousExecutions.get(0);
            if (previousExecution == null) {
                if (incrementer != null) {
                    parameters = incrementer.getNext(new JobParameters());
                }
            } else if (isStoppedOrFailed(previousExecution) && job.isRestartable()) {
                parameters = previousExecution.getJobParameters();
                removeNonIdentifying(additionals);
            } else if (incrementer != null) {
                parameters = incrementer.getNext(previousExecution.getJobParameters());
            }
        }
        return merge(parameters, additionals);
    }

    private boolean isStoppedOrFailed(JobExecution execution) {
        BatchStatus status = execution.getStatus();
        return status == BatchStatus.STOPPED || status == BatchStatus.FAILED;
    }

    private void removeNonIdentifying(Map<String, JobParameter> parameters) {
        HashMap<String, JobParameter> copy = new HashMap<>(parameters);
        for (Map.Entry<String, JobParameter> parameter : copy.entrySet()) {
            if (!parameter.getValue().isIdentifying()) {
                parameters.remove(parameter.getKey());
            }
        }
    }

    private JobParameters merge(JobParameters parameters, Map<String, JobParameter> additionals) {
        Map<String, JobParameter> merged = new HashMap<>();
        merged.putAll(parameters.getParameters());
        merged.putAll(additionals);
        return new JobParameters(merged);
    }

    private void executeRegisteredJobs(JobParameters jobParameters) throws JobInstanceAlreadyCompleteException, JobParametersNotFoundException, JobRestartException, JobParametersInvalidException, JobExecutionException, JobExecutionAlreadyRunningException {
        if (this.jobRegistry != null && StringUtils.hasText(this.jobNames)) {
            String[] jobsToRun = this.jobNames.split(",");
            for (String jobName : jobsToRun) {
                try {
                    Job job = this.jobRegistry.getJob(jobName);
                    if (!this.jobs.contains(job)) {
                        execute(job, jobParameters);
                    }
                } catch (NoSuchJobException e) {
                    logger.debug("No job found in registry for job name: " + jobName);
                }
            }
        }
    }

    protected void execute(Job job, JobParameters jobParameters) throws JobInstanceAlreadyCompleteException, JobParametersNotFoundException, JobRestartException, JobParametersInvalidException, JobExecutionAlreadyRunningException {
        JobParameters nextParameters = getNextJobParameters(job, jobParameters);
        JobExecution execution = this.jobLauncher.run(job, nextParameters);
        if (this.publisher != null) {
            this.publisher.publishEvent((ApplicationEvent) new JobExecutionEvent(execution));
        }
    }

    private void executeLocalJobs(JobParameters jobParameters) throws JobInstanceAlreadyCompleteException, JobParametersNotFoundException, JobRestartException, JobParametersInvalidException, JobExecutionException, JobExecutionAlreadyRunningException {
        for (Job job : this.jobs) {
            if (StringUtils.hasText(this.jobNames)) {
                String[] jobsToRun = this.jobNames.split(",");
                if (!PatternMatchUtils.simpleMatch(jobsToRun, job.getName())) {
                    logger.debug("Skipped job: " + job.getName());
                }
            }
            execute(job, jobParameters);
        }
    }
}
