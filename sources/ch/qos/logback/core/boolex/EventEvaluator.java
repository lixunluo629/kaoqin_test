package ch.qos.logback.core.boolex;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/boolex/EventEvaluator.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/boolex/EventEvaluator.class */
public interface EventEvaluator<E> extends ContextAware, LifeCycle {
    boolean evaluate(E e) throws NullPointerException, EvaluationException;

    String getName();

    void setName(String str);
}
