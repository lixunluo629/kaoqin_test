package org.apache.commons.logging.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

/* loaded from: commons-logging-1.0.4.jar:org/apache/commons/logging/impl/LogFactoryImpl.class */
public class LogFactoryImpl extends LogFactory {
    public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
    protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
    private static final String LOG_INTERFACE = "org.apache.commons.logging.Log";
    private String logClassName;
    protected Class[] logConstructorSignature;
    protected Method logMethod;
    protected Class[] logMethodSignature;
    static Class class$java$lang$String;
    static Class class$org$apache$commons$logging$LogFactory;
    protected Hashtable attributes = new Hashtable();
    protected Hashtable instances = new Hashtable();
    protected Constructor logConstructor = null;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public LogFactoryImpl() {
        Class clsClass$;
        Class clsClass$2;
        Class[] clsArr = new Class[1];
        if (class$java$lang$String == null) {
            clsClass$ = class$("java.lang.String");
            class$java$lang$String = clsClass$;
        } else {
            clsClass$ = class$java$lang$String;
        }
        clsArr[0] = clsClass$;
        this.logConstructorSignature = clsArr;
        this.logMethod = null;
        Class[] clsArr2 = new Class[1];
        if (class$org$apache$commons$logging$LogFactory == null) {
            clsClass$2 = class$(LogFactory.FACTORY_PROPERTY);
            class$org$apache$commons$logging$LogFactory = clsClass$2;
        } else {
            clsClass$2 = class$org$apache$commons$logging$LogFactory;
        }
        clsArr2[0] = clsClass$2;
        this.logMethodSignature = clsArr2;
    }

    @Override // org.apache.commons.logging.LogFactory
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override // org.apache.commons.logging.LogFactory
    public String[] getAttributeNames() {
        Vector names = new Vector();
        Enumeration keys = this.attributes.keys();
        while (keys.hasMoreElements()) {
            names.addElement((String) keys.nextElement());
        }
        String[] results = new String[names.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = (String) names.elementAt(i);
        }
        return results;
    }

    @Override // org.apache.commons.logging.LogFactory
    public Log getInstance(Class clazz) throws LogConfigurationException {
        return getInstance(clazz.getName());
    }

    @Override // org.apache.commons.logging.LogFactory
    public Log getInstance(String name) throws LogConfigurationException {
        Log instance = (Log) this.instances.get(name);
        if (instance == null) {
            instance = newInstance(name);
            this.instances.put(name, instance);
        }
        return instance;
    }

    @Override // org.apache.commons.logging.LogFactory
    public void release() {
        this.instances.clear();
    }

    @Override // org.apache.commons.logging.LogFactory
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    @Override // org.apache.commons.logging.LogFactory
    public void setAttribute(String name, Object value) {
        if (value == null) {
            this.attributes.remove(name);
        } else {
            this.attributes.put(name, value);
        }
    }

    protected String getLogClassName() {
        if (this.logClassName != null) {
            return this.logClassName;
        }
        this.logClassName = (String) getAttribute("org.apache.commons.logging.Log");
        if (this.logClassName == null) {
            this.logClassName = (String) getAttribute(LOG_PROPERTY_OLD);
        }
        if (this.logClassName == null) {
            try {
                this.logClassName = System.getProperty("org.apache.commons.logging.Log");
            } catch (SecurityException e) {
            }
        }
        if (this.logClassName == null) {
            try {
                this.logClassName = System.getProperty(LOG_PROPERTY_OLD);
            } catch (SecurityException e2) {
            }
        }
        if (this.logClassName == null && isLog4JAvailable()) {
            this.logClassName = "org.apache.commons.logging.impl.Log4JLogger";
        }
        if (this.logClassName == null && isJdk14Available()) {
            this.logClassName = "org.apache.commons.logging.impl.Jdk14Logger";
        }
        if (this.logClassName == null && isJdk13LumberjackAvailable()) {
            this.logClassName = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
        }
        if (this.logClassName == null) {
            this.logClassName = "org.apache.commons.logging.impl.SimpleLog";
        }
        return this.logClassName;
    }

    protected Constructor getLogConstructor() throws LogConfigurationException {
        if (this.logConstructor != null) {
            return this.logConstructor;
        }
        String logClassName = getLogClassName();
        try {
            Class logInterface = getClass().getClassLoader().loadClass("org.apache.commons.logging.Log");
            Class logClass = loadClass(logClassName);
            if (logClass == null) {
                throw new LogConfigurationException(new StringBuffer().append("No suitable Log implementation for ").append(logClassName).toString());
            }
            if (!logInterface.isAssignableFrom(logClass)) {
                Class[] interfaces = logClass.getInterfaces();
                for (Class cls : interfaces) {
                    if ("org.apache.commons.logging.Log".equals(cls.getName())) {
                        throw new LogConfigurationException("Invalid class loader hierarchy.  You have more than one version of 'org.apache.commons.logging.Log' visible, which is not allowed.");
                    }
                }
                throw new LogConfigurationException(new StringBuffer().append("Class ").append(logClassName).append(" does not implement '").append("org.apache.commons.logging.Log").append("'.").toString());
            }
            try {
                this.logMethod = logClass.getMethod("setLogFactory", this.logMethodSignature);
            } catch (Throwable th) {
                this.logMethod = null;
            }
            try {
                this.logConstructor = logClass.getConstructor(this.logConstructorSignature);
                return this.logConstructor;
            } catch (Throwable t) {
                throw new LogConfigurationException(new StringBuffer().append("No suitable Log constructor ").append(this.logConstructorSignature).append(" for ").append(logClassName).toString(), t);
            }
        } catch (Throwable t2) {
            throw new LogConfigurationException(t2);
        }
    }

    private static Class loadClass(String name) throws ClassNotFoundException {
        Object result = AccessController.doPrivileged((PrivilegedAction<Object>) new PrivilegedAction(name) { // from class: org.apache.commons.logging.impl.LogFactoryImpl.1
            private final String val$name;

            {
                this.val$name = name;
            }

            @Override // java.security.PrivilegedAction
            public Object run() throws LogConfigurationException {
                ClassLoader threadCL = LogFactory.getContextClassLoader();
                if (threadCL != null) {
                    try {
                        return threadCL.loadClass(this.val$name);
                    } catch (ClassNotFoundException e) {
                    }
                }
                try {
                    return Class.forName(this.val$name);
                } catch (ClassNotFoundException e2) {
                    return e2;
                }
            }
        });
        if (result instanceof Class) {
            return (Class) result;
        }
        throw ((ClassNotFoundException) result);
    }

    protected boolean isJdk13LumberjackAvailable() {
        try {
            loadClass("java.util.logging.Logger");
            loadClass("org.apache.commons.logging.impl.Jdk13LumberjackLogger");
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    protected boolean isJdk14Available() {
        try {
            loadClass("java.util.logging.Logger");
            loadClass("org.apache.commons.logging.impl.Jdk14Logger");
            Class throwable = loadClass("java.lang.Throwable");
            if (throwable.getDeclaredMethod("getStackTrace", null) == null) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    protected boolean isLog4JAvailable() {
        try {
            loadClass("org.apache.log4j.Logger");
            loadClass("org.apache.commons.logging.impl.Log4JLogger");
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    protected Log newInstance(String name) throws LogConfigurationException {
        try {
            Object[] params = {name};
            Log instance = (Log) getLogConstructor().newInstance(params);
            if (this.logMethod != null) {
                params[0] = this;
                this.logMethod.invoke(instance, params);
            }
            return instance;
        } catch (InvocationTargetException e) {
            Throwable c = e.getTargetException();
            if (c != null) {
                throw new LogConfigurationException(c);
            }
            throw new LogConfigurationException(e);
        } catch (Throwable t) {
            throw new LogConfigurationException(t);
        }
    }
}
