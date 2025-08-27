package org.apache.log4j.spi;

import org.apache.log4j.Logger;

/* loaded from: log4j-over-slf4j-1.7.26.jar:org/apache/log4j/spi/LoggerFactory.class */
public interface LoggerFactory {
    Logger makeNewLoggerInstance(String str);
}
