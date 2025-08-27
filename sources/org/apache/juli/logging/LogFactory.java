package org.apache.juli.logging;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.LogManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/juli/logging/LogFactory.class */
public class LogFactory {
    private static final LogFactory singleton = new LogFactory();
    private final Constructor<? extends Log> discoveredLogConstructor;

    /* JADX WARN: Multi-variable type inference failed */
    private LogFactory() throws NoSuchMethodException, SecurityException {
        FileSystems.getDefault();
        ServiceLoader<Log> logLoader = ServiceLoader.load(Log.class);
        Constructor<? extends Log> m = null;
        Iterator i$ = logLoader.iterator();
        if (i$.hasNext()) {
            Log log = i$.next();
            try {
                m = log.getClass().getConstructor(String.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new Error(e);
            }
        }
        this.discoveredLogConstructor = m;
    }

    public Log getInstance(String name) throws LogConfigurationException {
        if (this.discoveredLogConstructor == null) {
            return DirectJDKLog.getInstance(name);
        }
        try {
            return this.discoveredLogConstructor.newInstance(name);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
            throw new LogConfigurationException(e);
        }
    }

    public Log getInstance(Class<?> clazz) throws LogConfigurationException {
        return getInstance(clazz.getName());
    }

    public static LogFactory getFactory() throws LogConfigurationException {
        return singleton;
    }

    public static Log getLog(Class<?> clazz) throws LogConfigurationException {
        return getFactory().getInstance(clazz);
    }

    public static Log getLog(String name) throws LogConfigurationException {
        return getFactory().getInstance(name);
    }

    public static void release(ClassLoader classLoader) throws SecurityException {
        if (!LogManager.getLogManager().getClass().getName().equals("java.util.logging.LogManager")) {
            LogManager.getLogManager().reset();
        }
    }
}
