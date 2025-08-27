package org.apache.xmlbeans.impl.store;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlOptions;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/QueryDelegate.class */
public final class QueryDelegate {
    private static final Map<String, Constructor<? extends QueryInterface>> _constructors = new HashMap();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/QueryDelegate$QueryInterface.class */
    public interface QueryInterface {
        List execQuery(Object obj, Map map);
    }

    private QueryDelegate() {
    }

    private static synchronized void init(String implClassName) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (implClassName == null) {
            implClassName = "org.apache.xmlbeans.impl.xquery.saxon.XBeansXQuery";
        }
        Class<?> cls = null;
        boolean engineAvailable = true;
        try {
            cls = Class.forName(implClassName);
        } catch (ClassNotFoundException e) {
            engineAvailable = false;
        } catch (NoClassDefFoundError e2) {
            engineAvailable = false;
        }
        if (engineAvailable) {
            try {
                Constructor<? extends QueryInterface> constructor = cls.getConstructor(String.class, String.class, Integer.class, XmlOptions.class);
                _constructors.put(implClassName, constructor);
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }
    }

    public static synchronized QueryInterface createInstance(String implClassName, String query, String contextVar, int boundary, XmlOptions xmlOptions) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (_constructors.get(implClassName) == null) {
            init(implClassName);
        }
        if (_constructors.get(implClassName) == null) {
            return null;
        }
        Constructor<? extends QueryInterface> constructor = _constructors.get(implClassName);
        try {
            return constructor.newInstance(query, contextVar, Integer.valueOf(boundary), xmlOptions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
