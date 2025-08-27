package org.slf4j.spi;

import org.slf4j.ILoggerFactory;

/* loaded from: slf4j-api-1.7.26.jar:org/slf4j/spi/LoggerFactoryBinder.class */
public interface LoggerFactoryBinder {
    ILoggerFactory getLoggerFactory();

    String getLoggerFactoryClassStr();
}
