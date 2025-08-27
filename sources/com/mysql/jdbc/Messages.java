package com.mysql.jdbc;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Messages.class */
public class Messages {
    private static final String BUNDLE_NAME = "com.mysql.jdbc.LocalizedErrorMessages";
    private static final ResourceBundle RESOURCE_BUNDLE;

    static {
        ResourceBundle temp = null;
        try {
            try {
                temp = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault(), Messages.class.getClassLoader());
                RESOURCE_BUNDLE = temp;
            } catch (Throwable t) {
                try {
                    temp = ResourceBundle.getBundle(BUNDLE_NAME);
                    RESOURCE_BUNDLE = temp;
                } catch (Throwable t2) {
                    RuntimeException rt = new RuntimeException("Can't load resource bundle due to underlying exception " + t.toString());
                    rt.initCause(t2);
                    throw rt;
                }
            }
        } catch (Throwable th) {
            RESOURCE_BUNDLE = temp;
            throw th;
        }
    }

    public static String getString(String key) {
        if (RESOURCE_BUNDLE == null) {
            throw new RuntimeException("Localized messages from resource bundle 'com.mysql.jdbc.LocalizedErrorMessages' not loaded during initialization of driver.");
        }
        try {
            if (key == null) {
                throw new IllegalArgumentException("Message key can not be null");
            }
            String message = RESOURCE_BUNDLE.getString(key);
            if (message == null) {
                message = "Missing error message for key '" + key + "'";
            }
            return message;
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    public static String getString(String key, Object[] args) {
        return MessageFormat.format(getString(key), args);
    }

    private Messages() {
    }
}
