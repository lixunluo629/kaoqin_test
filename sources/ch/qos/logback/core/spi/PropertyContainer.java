package ch.qos.logback.core.spi;

import java.util.Map;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/spi/PropertyContainer.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/spi/PropertyContainer.class */
public interface PropertyContainer {
    String getProperty(String str);

    Map<String, String> getCopyOfPropertyMap();
}
