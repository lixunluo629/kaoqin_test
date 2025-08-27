package org.apache.catalina.core;

import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Security;
import java.sql.DriverManager;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.SafeForkJoinWorkerThreadFactory;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.poi.util.IdentifierManager;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.compat.JreCompat;
import org.apache.tomcat.util.compat.JreVendor;
import org.apache.tomcat.util.res.StringManager;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/JreMemoryLeakPreventionListener.class */
public class JreMemoryLeakPreventionListener implements LifecycleListener {
    private static final Log log = LogFactory.getLog((Class<?>) JreMemoryLeakPreventionListener.class);
    private static final StringManager sm = StringManager.getManager(Constants.Package);
    private static final String FORK_JOIN_POOL_THREAD_FACTORY_PROPERTY = "java.util.concurrent.ForkJoinPool.common.threadFactory";
    private boolean appContextProtection = false;
    private boolean awtThreadProtection = false;
    private boolean gcDaemonProtection = true;
    private boolean securityPolicyProtection = true;
    private boolean securityLoginConfigurationProtection = true;
    private boolean tokenPollerProtection = true;
    private boolean urlCacheProtection = true;
    private boolean xmlParsingProtection = true;
    private boolean ldapPoolProtection = true;
    private boolean driverManagerProtection = true;
    private boolean forkJoinCommonPoolProtection = true;
    private String classesToInitialize = null;

    public boolean isAppContextProtection() {
        return this.appContextProtection;
    }

    public void setAppContextProtection(boolean appContextProtection) {
        this.appContextProtection = appContextProtection;
    }

    public boolean isAWTThreadProtection() {
        return this.awtThreadProtection;
    }

    public void setAWTThreadProtection(boolean awtThreadProtection) {
        this.awtThreadProtection = awtThreadProtection;
    }

    public boolean isGcDaemonProtection() {
        return this.gcDaemonProtection;
    }

    public void setGcDaemonProtection(boolean gcDaemonProtection) {
        this.gcDaemonProtection = gcDaemonProtection;
    }

    public boolean isSecurityPolicyProtection() {
        return this.securityPolicyProtection;
    }

    public void setSecurityPolicyProtection(boolean securityPolicyProtection) {
        this.securityPolicyProtection = securityPolicyProtection;
    }

    public boolean isSecurityLoginConfigurationProtection() {
        return this.securityLoginConfigurationProtection;
    }

    public void setSecurityLoginConfigurationProtection(boolean securityLoginConfigurationProtection) {
        this.securityLoginConfigurationProtection = securityLoginConfigurationProtection;
    }

    public boolean isTokenPollerProtection() {
        return this.tokenPollerProtection;
    }

    public void setTokenPollerProtection(boolean tokenPollerProtection) {
        this.tokenPollerProtection = tokenPollerProtection;
    }

    public boolean isUrlCacheProtection() {
        return this.urlCacheProtection;
    }

    public void setUrlCacheProtection(boolean urlCacheProtection) {
        this.urlCacheProtection = urlCacheProtection;
    }

    public boolean isXmlParsingProtection() {
        return this.xmlParsingProtection;
    }

    public void setXmlParsingProtection(boolean xmlParsingProtection) {
        this.xmlParsingProtection = xmlParsingProtection;
    }

    public boolean isLdapPoolProtection() {
        return this.ldapPoolProtection;
    }

    public void setLdapPoolProtection(boolean ldapPoolProtection) {
        this.ldapPoolProtection = ldapPoolProtection;
    }

    public boolean isDriverManagerProtection() {
        return this.driverManagerProtection;
    }

    public void setDriverManagerProtection(boolean driverManagerProtection) {
        this.driverManagerProtection = driverManagerProtection;
    }

    public boolean getForkJoinCommonPoolProtection() {
        return this.forkJoinCommonPoolProtection;
    }

    public void setForkJoinCommonPoolProtection(boolean forkJoinCommonPoolProtection) {
        this.forkJoinCommonPoolProtection = forkJoinCommonPoolProtection;
    }

    public String getClassesToInitialize() {
        return this.classesToInitialize;
    }

    public void setClassesToInitialize(String classesToInitialize) {
        this.classesToInitialize = classesToInitialize;
    }

    @Override // org.apache.catalina.LifecycleListener
    public void lifecycleEvent(LifecycleEvent event) {
        if (Lifecycle.BEFORE_INIT_EVENT.equals(event.getType())) {
            if (this.driverManagerProtection) {
                DriverManager.getDrivers();
            }
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
                if (this.appContextProtection && !JreCompat.isJre8Available()) {
                    ImageIO.getCacheDirectory();
                }
                if (this.awtThreadProtection && !JreCompat.isJre9Available()) {
                    Toolkit.getDefaultToolkit();
                }
                if (this.gcDaemonProtection && !JreCompat.isJre9Available()) {
                    try {
                        try {
                            Class<?> clazz = Class.forName("sun.misc.GC");
                            Method method = clazz.getDeclaredMethod("requestLatency", Long.TYPE);
                            method.invoke(null, Long.valueOf(IdentifierManager.MAX_ID));
                        } catch (ClassNotFoundException e) {
                            if (JreVendor.IS_ORACLE_JVM) {
                                log.error(sm.getString("jreLeakListener.gcDaemonFail"), e);
                            } else {
                                log.debug(sm.getString("jreLeakListener.gcDaemonFail"), e);
                            }
                        } catch (InvocationTargetException e2) {
                            ExceptionUtils.handleThrowable(e2.getCause());
                            log.error(sm.getString("jreLeakListener.gcDaemonFail"), e2);
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e3) {
                        log.error(sm.getString("jreLeakListener.gcDaemonFail"), e3);
                    }
                }
                if (this.securityPolicyProtection && !JreCompat.isJre8Available()) {
                    try {
                        try {
                            try {
                                Class<?> policyClass = Class.forName("javax.security.auth.Policy");
                                Method method2 = policyClass.getMethod("getPolicy", new Class[0]);
                                method2.invoke(null, new Object[0]);
                            } catch (ClassNotFoundException e4) {
                            } catch (IllegalArgumentException e5) {
                                log.warn(sm.getString("jreLeakListener.authPolicyFail"), e5);
                            }
                        } catch (SecurityException e6) {
                        } catch (InvocationTargetException e7) {
                            ExceptionUtils.handleThrowable(e7.getCause());
                            log.warn(sm.getString("jreLeakListener.authPolicyFail"), e7);
                        }
                    } catch (IllegalAccessException e8) {
                        log.warn(sm.getString("jreLeakListener.authPolicyFail"), e8);
                    } catch (NoSuchMethodException e9) {
                        log.warn(sm.getString("jreLeakListener.authPolicyFail"), e9);
                    }
                }
                if (this.securityLoginConfigurationProtection && !JreCompat.isJre8Available()) {
                    try {
                        Class.forName("javax.security.auth.login.Configuration", true, ClassLoader.getSystemClassLoader());
                    } catch (ClassNotFoundException e10) {
                    }
                }
                if (this.tokenPollerProtection && !JreCompat.isJre9Available()) {
                    Security.getProviders();
                }
                if (this.urlCacheProtection) {
                    try {
                        JreCompat.getInstance().disableCachingForJarUrlConnections();
                    } catch (IOException e11) {
                        log.error(sm.getString("jreLeakListener.jarUrlConnCacheFail"), e11);
                    }
                }
                if (this.xmlParsingProtection && !JreCompat.isJre9Available()) {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    try {
                        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                        Document document = documentBuilder.newDocument();
                        document.createElement("dummy");
                        DOMImplementationLS implementation = (DOMImplementationLS) document.getImplementation();
                        implementation.createLSSerializer().writeToString(document);
                        document.normalize();
                    } catch (ParserConfigurationException e12) {
                        log.error(sm.getString("jreLeakListener.xmlParseFail"), e12);
                    }
                }
                if (this.ldapPoolProtection && !JreCompat.isJre9Available()) {
                    try {
                        Class.forName("com.sun.jndi.ldap.LdapPoolManager");
                    } catch (ClassNotFoundException e13) {
                        if (JreVendor.IS_ORACLE_JVM) {
                            log.error(sm.getString("jreLeakListener.ldapPoolManagerFail"), e13);
                        } else {
                            log.debug(sm.getString("jreLeakListener.ldapPoolManagerFail"), e13);
                        }
                    }
                }
                if (this.forkJoinCommonPoolProtection && JreCompat.isJre8Available() && !JreCompat.isJre9Available() && System.getProperty(FORK_JOIN_POOL_THREAD_FACTORY_PROPERTY) == null) {
                    System.setProperty(FORK_JOIN_POOL_THREAD_FACTORY_PROPERTY, SafeForkJoinWorkerThreadFactory.class.getName());
                }
                if (this.classesToInitialize != null) {
                    StringTokenizer strTok = new StringTokenizer(this.classesToInitialize, ", \r\n\t");
                    while (strTok.hasMoreTokens()) {
                        String classNameToLoad = strTok.nextToken();
                        try {
                            Class.forName(classNameToLoad);
                        } catch (ClassNotFoundException e14) {
                            log.error(sm.getString("jreLeakListener.classToInitializeFail", classNameToLoad), e14);
                        }
                    }
                }
                Thread.currentThread().setContextClassLoader(loader);
            } catch (Throwable th) {
                Thread.currentThread().setContextClassLoader(loader);
                throw th;
            }
        }
    }
}
