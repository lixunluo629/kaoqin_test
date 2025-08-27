package org.apache.xmlbeans;

import java.util.Hashtable;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/SystemProperties.class */
public class SystemProperties {
    protected static Hashtable propertyH;

    public static String getProperty(String key) {
        if (propertyH == null) {
            try {
                propertyH = System.getProperties();
            } catch (SecurityException e) {
                propertyH = new Hashtable();
                return null;
            }
        }
        return (String) propertyH.get(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String result = getProperty(key);
        if (result == null) {
            return defaultValue;
        }
        return result;
    }

    public static void setPropertyH(Hashtable aPropertyH) {
        propertyH = aPropertyH;
    }
}
