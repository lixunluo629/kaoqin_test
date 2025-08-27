package org.apache.xmlbeans.impl.store;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.impl.common.XPath;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/PathDelegate.class */
public final class PathDelegate {
    private static HashMap _constructors = new HashMap();

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/PathDelegate$SelectPathInterface.class */
    public interface SelectPathInterface {
        List selectPath(Object obj);
    }

    private PathDelegate() {
    }

    private static synchronized void init(String implClassName) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (implClassName == null) {
            implClassName = "org.apache.xmlbeans.impl.xpath.saxon.XBeansXPath";
        }
        Class selectPathInterfaceImpl = null;
        boolean engineAvailable = true;
        try {
            selectPathInterfaceImpl = Class.forName(implClassName);
        } catch (ClassNotFoundException e) {
            engineAvailable = false;
        } catch (NoClassDefFoundError e2) {
            engineAvailable = false;
        }
        if (engineAvailable) {
            try {
                Constructor constructor = selectPathInterfaceImpl.getConstructor(String.class, String.class, Map.class, String.class);
                _constructors.put(implClassName, constructor);
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }
    }

    public static synchronized SelectPathInterface createInstance(String implClassName, String xpath, String contextVar, Map namespaceMap) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        if (_constructors.get(implClassName) == null) {
            init(implClassName);
        }
        if (_constructors.get(implClassName) == null) {
            return null;
        }
        Constructor constructor = (Constructor) _constructors.get(implClassName);
        try {
            Object defaultNS = namespaceMap.get(XPath._DEFAULT_ELT_NS);
            if (defaultNS != null) {
                namespaceMap.remove(XPath._DEFAULT_ELT_NS);
            }
            return (SelectPathInterface) constructor.newInstance(xpath, contextVar, namespaceMap, (String) defaultNS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
