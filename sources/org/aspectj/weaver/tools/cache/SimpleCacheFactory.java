package org.aspectj.weaver.tools.cache;

import java.io.File;
import org.aspectj.weaver.Dump;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/SimpleCacheFactory.class */
public class SimpleCacheFactory {
    public static final String CACHE_ENABLED_PROPERTY = "aj.weaving.cache.enabled";
    public static final String CACHE_DIR = "aj.weaving.cache.dir";
    public static final String CACHE_IMPL = "aj.weaving.cache.impl";
    public static final boolean BYDEFAULT = false;
    public static final String PATH_DEFAULT = "/tmp/";
    public static String path = PATH_DEFAULT;
    public static Boolean enabled = false;
    private static boolean determinedIfEnabled = false;
    private static SimpleCache lacache = null;

    public static synchronized SimpleCache createSimpleCache() {
        if (lacache == null) {
            if (!determinedIfEnabled) {
                determineIfEnabled();
            }
            if (!enabled.booleanValue()) {
                return null;
            }
            try {
                path = System.getProperty("aj.weaving.cache.dir");
                if (path == null) {
                    path = PATH_DEFAULT;
                }
            } catch (Throwable t) {
                path = PATH_DEFAULT;
                t.printStackTrace();
                Dump.dumpWithException(t);
            }
            File f = new File(path);
            if (!f.exists()) {
                f.mkdir();
            }
            lacache = new SimpleCache(path, enabled.booleanValue());
        }
        return lacache;
    }

    private static void determineIfEnabled() {
        try {
            String property = System.getProperty("aj.weaving.cache.enabled");
            if (property != null && property.equalsIgnoreCase("true")) {
                String impl = System.getProperty("aj.weaving.cache.impl");
                if (SimpleCache.IMPL_NAME.equals(impl)) {
                    enabled = true;
                } else {
                    enabled = false;
                }
            } else {
                enabled = false;
            }
        } catch (Throwable t) {
            enabled = false;
            System.err.println("Error creating cache");
            t.printStackTrace();
            Dump.dumpWithException(t);
        }
        determinedIfEnabled = true;
    }

    public static boolean isEnabled() {
        if (!determinedIfEnabled) {
            determineIfEnabled();
        }
        return enabled.booleanValue();
    }
}
