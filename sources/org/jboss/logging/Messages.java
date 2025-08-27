package org.jboss.logging;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;

/* loaded from: jboss-logging-3.3.2.Final.jar:org/jboss/logging/Messages.class */
public final class Messages {
    private Messages() {
    }

    public static <T> T getBundle(Class<T> cls) {
        return (T) getBundle(cls, LoggingLocale.getLocale());
    }

    public static <T> T getBundle(final Class<T> cls, final Locale locale) {
        return (T) AccessController.doPrivileged(new PrivilegedAction<T>() { // from class: org.jboss.logging.Messages.1
            @Override // java.security.PrivilegedAction
            public T run() throws NoSuchFieldException {
                String language = locale.getLanguage();
                String country = locale.getCountry();
                String variant = locale.getVariant();
                Class clsAsSubclass = null;
                if (variant != null && variant.length() > 0) {
                    try {
                        clsAsSubclass = Class.forName(Messages.join(cls.getName(), "$bundle", language, country, variant), true, cls.getClassLoader()).asSubclass(cls);
                    } catch (ClassNotFoundException e) {
                    }
                }
                if (clsAsSubclass == null && country != null && country.length() > 0) {
                    try {
                        clsAsSubclass = Class.forName(Messages.join(cls.getName(), "$bundle", language, country, null), true, cls.getClassLoader()).asSubclass(cls);
                    } catch (ClassNotFoundException e2) {
                    }
                }
                if (clsAsSubclass == null && language != null && language.length() > 0) {
                    try {
                        clsAsSubclass = Class.forName(Messages.join(cls.getName(), "$bundle", language, null, null), true, cls.getClassLoader()).asSubclass(cls);
                    } catch (ClassNotFoundException e3) {
                    }
                }
                if (clsAsSubclass == null) {
                    try {
                        clsAsSubclass = Class.forName(Messages.join(cls.getName(), "$bundle", null, null, null), true, cls.getClassLoader()).asSubclass(cls);
                    } catch (ClassNotFoundException e4) {
                        throw new IllegalArgumentException("Invalid bundle " + cls + " (implementation not found)");
                    }
                }
                try {
                    try {
                        return (T) cls.cast(clsAsSubclass.getField("INSTANCE").get(null));
                    } catch (IllegalAccessException e5) {
                        throw new IllegalArgumentException("Bundle implementation " + clsAsSubclass + " could not be instantiated", e5);
                    }
                } catch (NoSuchFieldException e6) {
                    throw new IllegalArgumentException("Bundle implementation " + clsAsSubclass + " has no instance field");
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String join(String interfaceName, String a, String b, String c, String d) {
        StringBuilder build = new StringBuilder();
        build.append(interfaceName).append('_').append(a);
        if (b != null && b.length() > 0) {
            build.append('_');
            build.append(b);
        }
        if (c != null && c.length() > 0) {
            build.append('_');
            build.append(c);
        }
        if (d != null && d.length() > 0) {
            build.append('_');
            build.append(d);
        }
        return build.toString();
    }
}
