package org.apache.log4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.spi.LoggerFactory;

/* loaded from: log4j-over-slf4j-1.7.26.jar:org/apache/log4j/Log4jLoggerFactory.class */
class Log4jLoggerFactory {
    private static ConcurrentMap<String, Logger> log4jLoggers = new ConcurrentHashMap();

    Log4jLoggerFactory() {
    }

    public static Logger getLogger(String name) {
        Logger instance = log4jLoggers.get(name);
        if (instance != null) {
            return instance;
        }
        Logger newInstance = new Logger(name);
        Logger oldInstance = log4jLoggers.putIfAbsent(name, newInstance);
        return oldInstance == null ? newInstance : oldInstance;
    }

    public static Logger getLogger(String name, LoggerFactory loggerFactory) {
        Logger instance = log4jLoggers.get(name);
        if (instance != null) {
            return instance;
        }
        Logger newInstance = loggerFactory.makeNewLoggerInstance(name);
        Logger oldInstance = log4jLoggers.putIfAbsent(name, newInstance);
        return oldInstance == null ? newInstance : oldInstance;
    }
}
