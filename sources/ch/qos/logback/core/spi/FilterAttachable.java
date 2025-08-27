package ch.qos.logback.core.spi;

import ch.qos.logback.core.filter.Filter;
import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/spi/FilterAttachable.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/spi/FilterAttachable.class */
public interface FilterAttachable<E> {
    void addFilter(Filter<E> filter);

    void clearAllFilters();

    List<Filter<E>> getCopyOfAttachedFiltersList();

    FilterReply getFilterChainDecision(E e);
}
