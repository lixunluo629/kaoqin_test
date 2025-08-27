package org.springframework.scheduling.quartz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.ListenerManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.xml.XMLSchedulingDataProcessor;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/scheduling/quartz/SchedulerAccessor.class */
public abstract class SchedulerAccessor implements ResourceLoaderAware {
    protected final Log logger = LogFactory.getLog(getClass());
    private boolean overwriteExistingJobs = false;
    private String[] jobSchedulingDataLocations;
    private List<JobDetail> jobDetails;
    private Map<String, Calendar> calendars;
    private List<Trigger> triggers;
    private SchedulerListener[] schedulerListeners;
    private JobListener[] globalJobListeners;
    private TriggerListener[] globalTriggerListeners;
    private PlatformTransactionManager transactionManager;
    protected ResourceLoader resourceLoader;

    protected abstract Scheduler getScheduler();

    public void setOverwriteExistingJobs(boolean overwriteExistingJobs) {
        this.overwriteExistingJobs = overwriteExistingJobs;
    }

    public void setJobSchedulingDataLocation(String jobSchedulingDataLocation) {
        this.jobSchedulingDataLocations = new String[]{jobSchedulingDataLocation};
    }

    public void setJobSchedulingDataLocations(String... jobSchedulingDataLocations) {
        this.jobSchedulingDataLocations = jobSchedulingDataLocations;
    }

    public void setJobDetails(JobDetail... jobDetails) {
        this.jobDetails = new ArrayList(Arrays.asList(jobDetails));
    }

    public void setCalendars(Map<String, Calendar> calendars) {
        this.calendars = calendars;
    }

    public void setTriggers(Trigger... triggers) {
        this.triggers = Arrays.asList(triggers);
    }

    public void setSchedulerListeners(SchedulerListener... schedulerListeners) {
        this.schedulerListeners = schedulerListeners;
    }

    public void setGlobalJobListeners(JobListener... globalJobListeners) {
        this.globalJobListeners = globalJobListeners;
    }

    public void setGlobalTriggerListeners(TriggerListener... globalTriggerListeners) {
        this.globalTriggerListeners = globalTriggerListeners;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.quartz.SchedulerException */
    protected void registerJobsAndTriggers() throws SchedulerException, TransactionException {
        TransactionStatus transactionStatus = null;
        if (this.transactionManager != null) {
            transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        }
        try {
            if (this.jobSchedulingDataLocations != null) {
                ClassLoadHelper clh = new ResourceLoaderClassLoadHelper(this.resourceLoader);
                clh.initialize();
                XMLSchedulingDataProcessor dataProcessor = new XMLSchedulingDataProcessor(clh);
                for (String location : this.jobSchedulingDataLocations) {
                    dataProcessor.processFileAndScheduleJobs(location, getScheduler());
                }
            }
            if (this.jobDetails != null) {
                for (JobDetail jobDetail : this.jobDetails) {
                    addJobToScheduler(jobDetail);
                }
            } else {
                this.jobDetails = new LinkedList();
            }
            if (this.calendars != null) {
                for (String calendarName : this.calendars.keySet()) {
                    Calendar calendar = this.calendars.get(calendarName);
                    getScheduler().addCalendar(calendarName, calendar, true, true);
                }
            }
            if (this.triggers != null) {
                for (Trigger trigger : this.triggers) {
                    addTriggerToScheduler(trigger);
                }
            }
            if (transactionStatus != null) {
                this.transactionManager.commit(transactionStatus);
            }
        } catch (Throwable th) {
            if (transactionStatus != null) {
                try {
                    this.transactionManager.rollback(transactionStatus);
                } catch (TransactionException tex) {
                    this.logger.error("Job registration exception overridden by rollback exception", th);
                    throw tex;
                }
            }
            if (th instanceof SchedulerException) {
                throw th;
            }
            if (th instanceof Exception) {
                throw new SchedulerException("Registration of jobs and triggers failed: " + th.getMessage(), th);
            }
            throw new SchedulerException("Registration of jobs and triggers failed: " + th.getMessage());
        }
    }

    private boolean addJobToScheduler(JobDetail jobDetail) throws SchedulerException {
        if (this.overwriteExistingJobs || getScheduler().getJobDetail(jobDetail.getKey()) == null) {
            getScheduler().addJob(jobDetail, true);
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00ea A[Catch: ObjectAlreadyExistsException -> 0x00f8, TryCatch #0 {ObjectAlreadyExistsException -> 0x00f8, blocks: (B:29:0x00aa, B:31:0x00b7, B:33:0x00be, B:35:0x00d0, B:36:0x00ea), top: B:47:0x00aa }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean addTriggerToScheduler(org.quartz.Trigger r5) throws org.quartz.SchedulerException {
        /*
            Method dump skipped, instructions count: 325
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.scheduling.quartz.SchedulerAccessor.addTriggerToScheduler(org.quartz.Trigger):boolean");
    }

    protected void registerListeners() throws SchedulerException {
        ListenerManager listenerManager = getScheduler().getListenerManager();
        if (this.schedulerListeners != null) {
            for (SchedulerListener listener : this.schedulerListeners) {
                listenerManager.addSchedulerListener(listener);
            }
        }
        if (this.globalJobListeners != null) {
            for (JobListener listener2 : this.globalJobListeners) {
                listenerManager.addJobListener(listener2);
            }
        }
        if (this.globalTriggerListeners != null) {
            for (TriggerListener listener3 : this.globalTriggerListeners) {
                listenerManager.addTriggerListener(listener3);
            }
        }
    }
}
