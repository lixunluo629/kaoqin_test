package ch.qos.logback.core.filter;

import ch.qos.logback.core.spi.FilterReply;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/filter/AbstractMatcherFilter.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/filter/AbstractMatcherFilter.class */
public abstract class AbstractMatcherFilter<E> extends Filter<E> {
    protected FilterReply onMatch = FilterReply.NEUTRAL;
    protected FilterReply onMismatch = FilterReply.NEUTRAL;

    public final void setOnMatch(FilterReply reply) {
        this.onMatch = reply;
    }

    public final void setOnMismatch(FilterReply reply) {
        this.onMismatch = reply;
    }

    public final FilterReply getOnMatch() {
        return this.onMatch;
    }

    public final FilterReply getOnMismatch() {
        return this.onMismatch;
    }
}
