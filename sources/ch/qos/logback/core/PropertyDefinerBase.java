package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.PropertyDefiner;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/PropertyDefinerBase.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/PropertyDefinerBase.class */
public abstract class PropertyDefinerBase extends ContextAwareBase implements PropertyDefiner {
    protected static String booleanAsStr(boolean bool) {
        return bool ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
    }
}
