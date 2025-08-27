package org.springframework.scheduling.commonj;

import commonj.timers.Timer;
import commonj.timers.TimerManager;
import java.util.LinkedList;
import java.util.List;
import javax.naming.NamingException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.Lifecycle;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/scheduling/commonj/TimerManagerFactoryBean.class */
public class TimerManagerFactoryBean extends TimerManagerAccessor implements FactoryBean<TimerManager>, InitializingBean, DisposableBean, Lifecycle {
    private ScheduledTimerListener[] scheduledTimerListeners;
    private final List<Timer> timers = new LinkedList();

    public void setScheduledTimerListeners(ScheduledTimerListener[] scheduledTimerListeners) {
        this.scheduledTimerListeners = scheduledTimerListeners;
    }

    @Override // org.springframework.scheduling.commonj.TimerManagerAccessor, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NamingException {
        Timer timerSchedule;
        super.afterPropertiesSet();
        if (this.scheduledTimerListeners != null) {
            TimerManager timerManager = getTimerManager();
            for (ScheduledTimerListener scheduledTask : this.scheduledTimerListeners) {
                if (scheduledTask.isOneTimeTask()) {
                    timerSchedule = timerManager.schedule(scheduledTask.getTimerListener(), scheduledTask.getDelay());
                } else if (scheduledTask.isFixedRate()) {
                    timerSchedule = timerManager.scheduleAtFixedRate(scheduledTask.getTimerListener(), scheduledTask.getDelay(), scheduledTask.getPeriod());
                } else {
                    timerSchedule = timerManager.schedule(scheduledTask.getTimerListener(), scheduledTask.getDelay(), scheduledTask.getPeriod());
                }
                Timer timer = timerSchedule;
                this.timers.add(timer);
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public TimerManager getObject() {
        return getTimerManager();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends TimerManager> getObjectType() {
        TimerManager timerManager = getTimerManager();
        return timerManager != null ? timerManager.getClass() : TimerManager.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.scheduling.commonj.TimerManagerAccessor, org.springframework.beans.factory.DisposableBean
    public void destroy() {
        for (Timer timer : this.timers) {
            try {
                timer.cancel();
            } catch (Throwable ex) {
                this.logger.warn("Could not cancel CommonJ Timer", ex);
            }
        }
        this.timers.clear();
        super.destroy();
    }
}
