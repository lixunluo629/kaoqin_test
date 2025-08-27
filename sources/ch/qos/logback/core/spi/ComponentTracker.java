package ch.qos.logback.core.spi;

import java.util.Collection;
import java.util.Set;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/spi/ComponentTracker.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/spi/ComponentTracker.class */
public interface ComponentTracker<C> {
    public static final int DEFAULT_TIMEOUT = 1800000;
    public static final int DEFAULT_MAX_COMPONENTS = Integer.MAX_VALUE;

    int getComponentCount();

    C find(String str);

    C getOrCreate(String str, long j);

    void removeStaleComponents(long j);

    void endOfLife(String str);

    Collection<C> allComponents();

    Set<String> allKeys();
}
