package org.springframework.scheduling.commonj;

import commonj.timers.TimerManager;
import javax.naming.NamingException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.Lifecycle;
import org.springframework.jndi.JndiLocatorSupport;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/scheduling/commonj/TimerManagerAccessor.class */
public abstract class TimerManagerAccessor extends JndiLocatorSupport implements InitializingBean, DisposableBean, Lifecycle {
    private TimerManager timerManager;
    private String timerManagerName;
    private boolean shared = false;

    public void setTimerManager(TimerManager timerManager) {
        this.timerManager = timerManager;
    }

    public void setTimerManagerName(String timerManagerName) {
        this.timerManagerName = timerManagerName;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NamingException {
        if (this.timerManager == null) {
            if (this.timerManagerName == null) {
                throw new IllegalArgumentException("Either 'timerManager' or 'timerManagerName' must be specified");
            }
            this.timerManager = (TimerManager) lookup(this.timerManagerName, TimerManager.class);
        }
    }

    protected final TimerManager getTimerManager() {
        return this.timerManager;
    }

    @Override // org.springframework.context.Lifecycle
    public void start() {
        if (!this.shared) {
            this.timerManager.resume();
        }
    }

    @Override // org.springframework.context.Lifecycle
    public void stop() {
        if (!this.shared) {
            this.timerManager.suspend();
        }
    }

    @Override // org.springframework.context.Lifecycle
    public boolean isRunning() {
        return (this.timerManager.isSuspending() || this.timerManager.isStopping()) ? false : true;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        if (!this.shared) {
            this.timerManager.stop();
        }
    }
}
