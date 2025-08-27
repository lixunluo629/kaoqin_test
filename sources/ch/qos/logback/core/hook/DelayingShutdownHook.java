package ch.qos.logback.core.hook;

import ch.qos.logback.core.util.Duration;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/hook/DelayingShutdownHook.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/hook/DelayingShutdownHook.class */
public class DelayingShutdownHook extends ShutdownHookBase {
    public static final Duration DEFAULT_DELAY = Duration.buildByMilliseconds(0.0d);
    private Duration delay = DEFAULT_DELAY;

    public Duration getDelay() {
        return this.delay;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    @Override // java.lang.Runnable
    public void run() throws InterruptedException {
        addInfo("Sleeping for " + this.delay);
        try {
            Thread.sleep(this.delay.getMilliseconds());
        } catch (InterruptedException e) {
        }
        super.stop();
    }
}
