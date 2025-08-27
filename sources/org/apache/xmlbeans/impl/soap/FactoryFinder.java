package org.apache.xmlbeans.impl.soap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.xmlbeans.SystemProperties;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/FactoryFinder.class */
class FactoryFinder {
    FactoryFinder() {
    }

    private static Object newInstance(String factoryClassName) throws SOAPException, ClassNotFoundException {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            Class factory = null;
            try {
                if (classloader == null) {
                    factory = Class.forName(factoryClassName);
                } else {
                    try {
                        factory = classloader.loadClass(factoryClassName);
                    } catch (ClassNotFoundException e) {
                    }
                }
                if (factory == null) {
                    factory = FactoryFinder.class.getClassLoader().loadClass(factoryClassName);
                }
                return factory.newInstance();
            } catch (ClassNotFoundException classnotfoundexception) {
                throw new SOAPException("Provider " + factoryClassName + " not found", classnotfoundexception);
            } catch (Exception exception) {
                throw new SOAPException("Provider " + factoryClassName + " could not be instantiated: " + exception, exception);
            }
        } catch (Exception exception2) {
            throw new SOAPException(exception2.toString(), exception2);
        }
    }

    static Object find(String factoryPropertyName, String defaultFactoryClassName) throws SOAPException, IOException {
        try {
            String factoryClassName = SystemProperties.getProperty(factoryPropertyName);
            if (factoryClassName != null) {
                return newInstance(factoryClassName);
            }
        } catch (SecurityException e) {
        }
        try {
            String propertiesFileName = SystemProperties.getProperty("java.home") + File.separator + "lib" + File.separator + "jaxm.properties";
            File file = new File(propertiesFileName);
            if (file.exists()) {
                FileInputStream fileInput = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(fileInput);
                fileInput.close();
                return newInstance(properties.getProperty(factoryPropertyName));
            }
        } catch (Exception e2) {
        }
        String factoryResource = "META-INF/services/" + factoryPropertyName;
        try {
            InputStream inputstream = getResource(factoryResource);
            if (inputstream != null) {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
                String factoryClassName2 = bufferedreader.readLine();
                bufferedreader.close();
                if (factoryClassName2 != null && !"".equals(factoryClassName2)) {
                    return newInstance(factoryClassName2);
                }
            }
        } catch (Exception e3) {
        }
        if (defaultFactoryClassName == null) {
            throw new SOAPException("Provider for " + factoryPropertyName + " cannot be found", null);
        }
        return newInstance(defaultFactoryClassName);
    }

    private static InputStream getResource(String factoryResource) {
        InputStream inputstream;
        ClassLoader classloader = null;
        try {
            classloader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException e) {
        }
        if (classloader == null) {
            inputstream = ClassLoader.getSystemResourceAsStream(factoryResource);
        } else {
            inputstream = classloader.getResourceAsStream(factoryResource);
        }
        if (inputstream == null) {
            inputstream = FactoryFinder.class.getResourceAsStream(factoryResource);
        }
        if (inputstream == null && FactoryFinder.class.getClassLoader() != null) {
            inputstream = FactoryFinder.class.getClassLoader().getResourceAsStream(factoryResource);
        }
        return inputstream;
    }
}
