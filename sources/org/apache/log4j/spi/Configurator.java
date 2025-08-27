package org.apache.log4j.spi;

import java.net.URL;

/* loaded from: log4j-over-slf4j-1.7.26.jar:org/apache/log4j/spi/Configurator.class */
public interface Configurator {
    public static final String INHERITED = "inherited";
    public static final String NULL = "null";

    void doConfigure(URL url, LoggerRepository loggerRepository);
}
