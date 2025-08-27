package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/pattern/PostCompileProcessor.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/pattern/PostCompileProcessor.class */
public interface PostCompileProcessor<E> {
    void process(Context context, Converter<E> converter);
}
