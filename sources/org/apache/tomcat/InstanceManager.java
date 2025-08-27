package org.apache.tomcat;

import java.lang.reflect.InvocationTargetException;
import javax.naming.NamingException;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/InstanceManager.class */
public interface InstanceManager {
    Object newInstance(Class<?> cls) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, NamingException, IllegalArgumentException, InvocationTargetException;

    Object newInstance(String str) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, NamingException, IllegalArgumentException, InvocationTargetException;

    Object newInstance(String str, ClassLoader classLoader) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, NamingException, IllegalArgumentException, InvocationTargetException;

    void newInstance(Object obj) throws IllegalAccessException, NamingException, InvocationTargetException;

    void destroyInstance(Object obj) throws IllegalAccessException, InvocationTargetException;
}
