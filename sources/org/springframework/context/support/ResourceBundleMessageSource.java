package org.springframework.context.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.util.ClassUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/ResourceBundleMessageSource.class */
public class ResourceBundleMessageSource extends AbstractResourceBasedMessageSource implements BeanClassLoaderAware {
    private ClassLoader bundleClassLoader;
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private final Map<String, Map<Locale, ResourceBundle>> cachedResourceBundles = new HashMap();
    private final Map<ResourceBundle, Map<String, Map<Locale, MessageFormat>>> cachedBundleMessageFormats = new HashMap();

    public void setBundleClassLoader(ClassLoader classLoader) {
        this.bundleClassLoader = classLoader;
    }

    protected ClassLoader getBundleClassLoader() {
        return this.bundleClassLoader != null ? this.bundleClassLoader : this.beanClassLoader;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override // org.springframework.context.support.AbstractMessageSource
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        String result;
        Set<String> basenames = getBasenameSet();
        for (String basename : basenames) {
            ResourceBundle bundle = getResourceBundle(basename, locale);
            if (bundle != null && (result = getStringOrNull(bundle, code)) != null) {
                return result;
            }
        }
        return null;
    }

    @Override // org.springframework.context.support.AbstractMessageSource
    protected MessageFormat resolveCode(String code, Locale locale) {
        MessageFormat messageFormat;
        Set<String> basenames = getBasenameSet();
        for (String basename : basenames) {
            ResourceBundle bundle = getResourceBundle(basename, locale);
            if (bundle != null && (messageFormat = getMessageFormat(bundle, code, locale)) != null) {
                return messageFormat;
            }
        }
        return null;
    }

    protected ResourceBundle getResourceBundle(String basename, Locale locale) {
        ResourceBundle bundle;
        if (getCacheMillis() >= 0) {
            return doGetBundle(basename, locale);
        }
        synchronized (this.cachedResourceBundles) {
            Map<Locale, ResourceBundle> localeMap = this.cachedResourceBundles.get(basename);
            if (localeMap != null && (bundle = localeMap.get(locale)) != null) {
                return bundle;
            }
            try {
                ResourceBundle bundle2 = doGetBundle(basename, locale);
                if (localeMap == null) {
                    localeMap = new HashMap();
                    this.cachedResourceBundles.put(basename, localeMap);
                }
                localeMap.put(locale, bundle2);
                return bundle2;
            } catch (MissingResourceException ex) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("ResourceBundle [" + basename + "] not found for MessageSource: " + ex.getMessage());
                }
                return null;
            }
        }
    }

    protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
        return ResourceBundle.getBundle(basename, locale, getBundleClassLoader(), new MessageSourceControl());
    }

    protected ResourceBundle loadBundle(Reader reader) throws IOException {
        return new PropertyResourceBundle(reader);
    }

    protected MessageFormat getMessageFormat(ResourceBundle bundle, String code, Locale locale) throws MissingResourceException {
        MessageFormat result;
        synchronized (this.cachedBundleMessageFormats) {
            Map<String, Map<Locale, MessageFormat>> codeMap = this.cachedBundleMessageFormats.get(bundle);
            Map<Locale, MessageFormat> localeMap = null;
            if (codeMap != null) {
                localeMap = codeMap.get(code);
                if (localeMap != null && (result = localeMap.get(locale)) != null) {
                    return result;
                }
            }
            String msg = getStringOrNull(bundle, code);
            if (msg != null) {
                if (codeMap == null) {
                    codeMap = new HashMap();
                    this.cachedBundleMessageFormats.put(bundle, codeMap);
                }
                if (localeMap == null) {
                    localeMap = new HashMap();
                    codeMap.put(code, localeMap);
                }
                MessageFormat result2 = createMessageFormat(msg, locale);
                localeMap.put(locale, result2);
                return result2;
            }
            return null;
        }
    }

    protected String getStringOrNull(ResourceBundle bundle, String key) {
        if (bundle.containsKey(key)) {
            try {
                return bundle.getString(key);
            } catch (MissingResourceException e) {
                return null;
            }
        }
        return null;
    }

    public String toString() {
        return getClass().getName() + ": basenames=" + getBasenameSet();
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/support/ResourceBundleMessageSource$MessageSourceControl.class */
    private class MessageSourceControl extends ResourceBundle.Control {
        private MessageSourceControl() {
        }

        @Override // java.util.ResourceBundle.Control
        public ResourceBundle newBundle(String baseName, Locale locale, String format, final ClassLoader loader, final boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            if (format.equals("java.properties")) {
                String bundleName = toBundleName(baseName, locale);
                final String resourceName = toResourceName(bundleName, "properties");
                try {
                    InputStream stream = (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() { // from class: org.springframework.context.support.ResourceBundleMessageSource.MessageSourceControl.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public InputStream run() throws IOException {
                            URLConnection connection;
                            InputStream is = null;
                            if (reload) {
                                URL url = loader.getResource(resourceName);
                                if (url != null && (connection = url.openConnection()) != null) {
                                    connection.setUseCaches(false);
                                    is = connection.getInputStream();
                                }
                            } else {
                                is = loader.getResourceAsStream(resourceName);
                            }
                            return is;
                        }
                    });
                    if (stream != null) {
                        String encoding = ResourceBundleMessageSource.this.getDefaultEncoding();
                        if (encoding == null) {
                            encoding = "ISO-8859-1";
                        }
                        try {
                            ResourceBundle resourceBundleLoadBundle = ResourceBundleMessageSource.this.loadBundle(new InputStreamReader(stream, encoding));
                            stream.close();
                            return resourceBundleLoadBundle;
                        } catch (Throwable th) {
                            stream.close();
                            throw th;
                        }
                    }
                    return null;
                } catch (PrivilegedActionException ex) {
                    throw ((IOException) ex.getException());
                }
            }
            return super.newBundle(baseName, locale, format, loader, reload);
        }

        @Override // java.util.ResourceBundle.Control
        public Locale getFallbackLocale(String baseName, Locale locale) {
            if (ResourceBundleMessageSource.this.isFallbackToSystemLocale()) {
                return super.getFallbackLocale(baseName, locale);
            }
            return null;
        }

        @Override // java.util.ResourceBundle.Control
        public long getTimeToLive(String baseName, Locale locale) {
            long cacheMillis = ResourceBundleMessageSource.this.getCacheMillis();
            return cacheMillis >= 0 ? cacheMillis : super.getTimeToLive(baseName, locale);
        }

        @Override // java.util.ResourceBundle.Control
        public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime) {
            if (super.needsReload(baseName, locale, format, loader, bundle, loadTime)) {
                synchronized (ResourceBundleMessageSource.this.cachedBundleMessageFormats) {
                    ResourceBundleMessageSource.this.cachedBundleMessageFormats.remove(bundle);
                }
                return true;
            }
            return false;
        }
    }
}
