package org.aspectj.weaver.tools;

import org.aspectj.util.LangUtil;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/TraceFactory.class */
public abstract class TraceFactory {
    public static final String FACTORY_PROPERTY = "org.aspectj.tracing.factory";
    public static final String DEFAULT_FACTORY_NAME = "default";
    private static TraceFactory instance;
    public static final String DEBUG_PROPERTY = "org.aspectj.tracing.debug";
    protected static boolean debug = getBoolean(DEBUG_PROPERTY, false);

    static {
        String factoryName = System.getProperty(FACTORY_PROPERTY);
        if (factoryName != null) {
            try {
                if (factoryName.equals("default")) {
                    instance = new DefaultTraceFactory();
                } else {
                    Class factoryClass = Class.forName(factoryName);
                    instance = (TraceFactory) factoryClass.newInstance();
                }
            } catch (Throwable th) {
                if (debug) {
                    th.printStackTrace();
                }
            }
        }
        if (instance == null) {
            try {
                if (LangUtil.is15VMOrGreater()) {
                    Class factoryClass2 = Class.forName("org.aspectj.weaver.tools.Jdk14TraceFactory");
                    instance = (TraceFactory) factoryClass2.newInstance();
                } else {
                    Class factoryClass3 = Class.forName("org.aspectj.weaver.tools.CommonsTraceFactory");
                    instance = (TraceFactory) factoryClass3.newInstance();
                }
            } catch (Throwable th2) {
                if (debug) {
                    th2.printStackTrace();
                }
            }
        }
        if (instance == null) {
            instance = new DefaultTraceFactory();
        }
        if (debug) {
            System.err.println("TraceFactory.instance=" + instance);
        }
    }

    public Trace getTrace(Class clazz) {
        return instance.getTrace(clazz);
    }

    public static TraceFactory getTraceFactory() {
        return instance;
    }

    protected static boolean getBoolean(String name, boolean def) {
        String defaultValue = String.valueOf(def);
        String value = System.getProperty(name, defaultValue);
        return Boolean.valueOf(value).booleanValue();
    }
}
