package ch.qos.logback.core.sift;

import ch.qos.logback.core.spi.ContextAwareBase;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/sift/AbstractDiscriminator.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/sift/AbstractDiscriminator.class */
public abstract class AbstractDiscriminator<E> extends ContextAwareBase implements Discriminator<E> {
    protected boolean started;

    public void start() {
        this.started = true;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.started = false;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public boolean isStarted() {
        return this.started;
    }
}
