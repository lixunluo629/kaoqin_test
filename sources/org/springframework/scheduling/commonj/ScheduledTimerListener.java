package org.springframework.scheduling.commonj;

import commonj.timers.TimerListener;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/scheduling/commonj/ScheduledTimerListener.class */
public class ScheduledTimerListener {
    private TimerListener timerListener;
    private long delay;
    private long period;
    private boolean fixedRate;

    public ScheduledTimerListener() {
        this.delay = 0L;
        this.period = -1L;
        this.fixedRate = false;
    }

    public ScheduledTimerListener(TimerListener timerListener) {
        this.delay = 0L;
        this.period = -1L;
        this.fixedRate = false;
        this.timerListener = timerListener;
    }

    public ScheduledTimerListener(TimerListener timerListener, long delay) {
        this.delay = 0L;
        this.period = -1L;
        this.fixedRate = false;
        this.timerListener = timerListener;
        this.delay = delay;
    }

    public ScheduledTimerListener(TimerListener timerListener, long delay, long period, boolean fixedRate) {
        this.delay = 0L;
        this.period = -1L;
        this.fixedRate = false;
        this.timerListener = timerListener;
        this.delay = delay;
        this.period = period;
        this.fixedRate = fixedRate;
    }

    public ScheduledTimerListener(Runnable timerTask) {
        this.delay = 0L;
        this.period = -1L;
        this.fixedRate = false;
        setRunnable(timerTask);
    }

    public ScheduledTimerListener(Runnable timerTask, long delay) {
        this.delay = 0L;
        this.period = -1L;
        this.fixedRate = false;
        setRunnable(timerTask);
        this.delay = delay;
    }

    public ScheduledTimerListener(Runnable timerTask, long delay, long period, boolean fixedRate) {
        this.delay = 0L;
        this.period = -1L;
        this.fixedRate = false;
        setRunnable(timerTask);
        this.delay = delay;
        this.period = period;
        this.fixedRate = fixedRate;
    }

    public void setRunnable(Runnable timerTask) {
        this.timerListener = new DelegatingTimerListener(timerTask);
    }

    public void setTimerListener(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    public TimerListener getTimerListener() {
        return this.timerListener;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getDelay() {
        return this.delay;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public long getPeriod() {
        return this.period;
    }

    public boolean isOneTimeTask() {
        return this.period < 0;
    }

    public void setFixedRate(boolean fixedRate) {
        this.fixedRate = fixedRate;
    }

    public boolean isFixedRate() {
        return this.fixedRate;
    }
}
