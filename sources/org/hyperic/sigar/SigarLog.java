package org.hyperic.sigar;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/SigarLog.class */
public class SigarLog {
    private static final int LOG_FATAL = 0;
    private static final int LOG_ERROR = 1;
    private static final int LOG_WARN = 2;
    private static final int LOG_INFO = 3;
    private static final int LOG_DEBUG = 4;

    private static native void setLogger(Sigar sigar, Logger logger);

    public static native void setLevel(Sigar sigar, int i);

    private static boolean isLogConfigured() {
        return Logger.getRootLogger().getAllAppenders().hasMoreElements();
    }

    private static Logger getLogger() {
        return getLogger("Sigar");
    }

    public static Logger getLogger(String name) {
        Logger log = Logger.getLogger(name);
        if (!isLogConfigured()) {
            BasicConfigurator.configure();
        }
        return log;
    }

    static void error(String msg, Throwable exc) {
        getLogger().error(msg, exc);
    }

    static void debug(String msg, Throwable exc) {
        getLogger().debug(msg, exc);
    }

    public static void enable(Sigar sigar) {
        Logger log = getLogger();
        Level level = log.getLevel();
        if (level == null) {
            level = Logger.getRootLogger().getLevel();
            if (level == null) {
                return;
            }
        }
        switch (level.toInt()) {
            case 10000:
                setLevel(sigar, 4);
                break;
            case 20000:
                setLevel(sigar, 3);
                break;
            case 30000:
                setLevel(sigar, 2);
                break;
            case 40000:
                setLevel(sigar, 1);
                break;
            case 50000:
                setLevel(sigar, 0);
                break;
        }
        setLogger(sigar, log);
    }

    public static void disable(Sigar sigar) {
        setLogger(sigar, null);
    }
}
