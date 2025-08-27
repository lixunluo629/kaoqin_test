package javax.xml.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/FactoryFinder.class */
class FactoryFinder {
    private static boolean debug;
    static Class class$javax$xml$stream$FactoryFinder;

    /* renamed from: javax.xml.stream.FactoryFinder$1, reason: invalid class name */
    /* loaded from: stax-api-1.0.1.jar:javax/xml/stream/FactoryFinder$1.class */
    static class AnonymousClass1 {
    }

    FactoryFinder() {
    }

    static {
        debug = false;
        try {
            debug = System.getProperty("xml.stream.debug") != null;
        } catch (Exception e) {
        }
    }

    private static void debugPrintln(String msg) {
        if (debug) {
            System.err.println(new StringBuffer().append("STREAM: ").append(msg).toString());
        }
    }

    private static ClassLoader findClassLoader() throws ClassNotFoundException, FactoryConfigurationError {
        Class clsClass$;
        ClassLoader classLoader;
        Class clsClass$2;
        Class clsClass$3;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            if (class$javax$xml$stream$FactoryFinder == null) {
                clsClass$3 = class$("javax.xml.stream.FactoryFinder");
                class$javax$xml$stream$FactoryFinder = clsClass$3;
            } else {
                clsClass$3 = class$javax$xml$stream$FactoryFinder;
            }
            Class clazz = Class.forName(stringBuffer.append(clsClass$3.getName()).append("$ClassLoaderFinderConcrete").toString());
            ClassLoaderFinder clf = (ClassLoaderFinder) clazz.newInstance();
            classLoader = clf.getContextClassLoader();
        } catch (ClassNotFoundException e) {
            if (class$javax$xml$stream$FactoryFinder == null) {
                clsClass$2 = class$("javax.xml.stream.FactoryFinder");
                class$javax$xml$stream$FactoryFinder = clsClass$2;
            } else {
                clsClass$2 = class$javax$xml$stream$FactoryFinder;
            }
            classLoader = clsClass$2.getClassLoader();
        } catch (Exception x) {
            throw new FactoryConfigurationError(x.toString(), x);
        } catch (LinkageError e2) {
            if (class$javax$xml$stream$FactoryFinder == null) {
                clsClass$ = class$("javax.xml.stream.FactoryFinder");
                class$javax$xml$stream$FactoryFinder = clsClass$;
            } else {
                clsClass$ = class$javax$xml$stream$FactoryFinder;
            }
            classLoader = clsClass$.getClassLoader();
        }
        return classLoader;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private static Object newInstance(String className, ClassLoader classLoader) throws ClassNotFoundException, FactoryConfigurationError {
        Class spiClass;
        try {
            if (classLoader == null) {
                spiClass = Class.forName(className);
            } else {
                spiClass = classLoader.loadClass(className);
            }
            return spiClass.newInstance();
        } catch (ClassNotFoundException x) {
            throw new FactoryConfigurationError(new StringBuffer().append("Provider ").append(className).append(" not found").toString(), x);
        } catch (Exception x2) {
            throw new FactoryConfigurationError(new StringBuffer().append("Provider ").append(className).append(" could not be instantiated: ").append(x2).toString(), x2);
        }
    }

    static Object find(String factoryId) throws FactoryConfigurationError {
        return find(factoryId, null);
    }

    static Object find(String factoryId, String fallbackClassName) throws ClassNotFoundException, FactoryConfigurationError {
        ClassLoader classLoader = findClassLoader();
        return find(factoryId, fallbackClassName, classLoader);
    }

    static Object find(String factoryId, String fallbackClassName, ClassLoader classLoader) throws IOException, FactoryConfigurationError {
        InputStream is;
        try {
            String systemProp = System.getProperty(factoryId);
            if (systemProp != null) {
                debugPrintln(new StringBuffer().append("found system property").append(systemProp).toString());
                return newInstance(systemProp, classLoader);
            }
        } catch (SecurityException e) {
        }
        try {
            String javah = System.getProperty("java.home");
            String configFile = new StringBuffer().append(javah).append(File.separator).append("lib").append(File.separator).append("jaxp.properties").toString();
            File f = new File(configFile);
            if (f.exists()) {
                Properties props = new Properties();
                props.load(new FileInputStream(f));
                String factoryClassName = props.getProperty(factoryId);
                if (factoryClassName != null && factoryClassName.length() > 0) {
                    debugPrintln(new StringBuffer().append("found java.home property ").append(factoryClassName).toString());
                    return newInstance(factoryClassName, classLoader);
                }
            }
        } catch (Exception ex) {
            if (debug) {
                ex.printStackTrace();
            }
        }
        String serviceId = new StringBuffer().append("META-INF/services/").append(factoryId).toString();
        try {
            if (classLoader == null) {
                is = ClassLoader.getSystemResourceAsStream(serviceId);
            } else {
                is = classLoader.getResourceAsStream(serviceId);
            }
            if (is != null) {
                debugPrintln(new StringBuffer().append("found ").append(serviceId).toString());
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String factoryClassName2 = rd.readLine();
                rd.close();
                if (factoryClassName2 != null && !"".equals(factoryClassName2)) {
                    debugPrintln(new StringBuffer().append("loaded from services: ").append(factoryClassName2).toString());
                    return newInstance(factoryClassName2, classLoader);
                }
            }
        } catch (Exception ex2) {
            if (debug) {
                ex2.printStackTrace();
            }
        }
        if (fallbackClassName == null) {
            throw new FactoryConfigurationError(new StringBuffer().append("Provider for ").append(factoryId).append(" cannot be found").toString(), (Exception) null);
        }
        debugPrintln(new StringBuffer().append("loaded from fallback value: ").append(fallbackClassName).toString());
        return newInstance(fallbackClassName, classLoader);
    }

    /* loaded from: stax-api-1.0.1.jar:javax/xml/stream/FactoryFinder$ClassLoaderFinder.class */
    private static abstract class ClassLoaderFinder {
        abstract ClassLoader getContextClassLoader();

        private ClassLoaderFinder() {
        }

        ClassLoaderFinder(AnonymousClass1 x0) {
            this();
        }
    }

    /* loaded from: stax-api-1.0.1.jar:javax/xml/stream/FactoryFinder$ClassLoaderFinderConcrete.class */
    static class ClassLoaderFinderConcrete extends ClassLoaderFinder {
        ClassLoaderFinderConcrete() {
            super(null);
        }

        @Override // javax.xml.stream.FactoryFinder.ClassLoaderFinder
        ClassLoader getContextClassLoader() {
            return Thread.currentThread().getContextClassLoader();
        }
    }
}
