package org.springframework.ui.context.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ui.context.HierarchicalThemeSource;
import org.springframework.ui.context.Theme;
import org.springframework.ui.context.ThemeSource;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ui/context/support/ResourceBundleThemeSource.class */
public class ResourceBundleThemeSource implements HierarchicalThemeSource, BeanClassLoaderAware {
    private ThemeSource parentThemeSource;
    private String defaultEncoding;
    private Boolean fallbackToSystemLocale;
    private ClassLoader beanClassLoader;
    protected final Log logger = LogFactory.getLog(getClass());
    private String basenamePrefix = "";
    private final Map<String, Theme> themeCache = new ConcurrentHashMap();

    @Override // org.springframework.ui.context.HierarchicalThemeSource
    public void setParentThemeSource(ThemeSource parent) {
        this.parentThemeSource = parent;
        synchronized (this.themeCache) {
            for (Theme theme : this.themeCache.values()) {
                initParent(theme);
            }
        }
    }

    @Override // org.springframework.ui.context.HierarchicalThemeSource
    public ThemeSource getParentThemeSource() {
        return this.parentThemeSource;
    }

    public void setBasenamePrefix(String basenamePrefix) {
        this.basenamePrefix = basenamePrefix != null ? basenamePrefix : "";
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
        this.fallbackToSystemLocale = Boolean.valueOf(fallbackToSystemLocale);
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override // org.springframework.ui.context.ThemeSource
    public Theme getTheme(String themeName) {
        if (themeName == null) {
            return null;
        }
        Theme theme = this.themeCache.get(themeName);
        if (theme == null) {
            synchronized (this.themeCache) {
                theme = this.themeCache.get(themeName);
                if (theme == null) {
                    String basename = this.basenamePrefix + themeName;
                    MessageSource messageSource = createMessageSource(basename);
                    theme = new SimpleTheme(themeName, messageSource);
                    initParent(theme);
                    this.themeCache.put(themeName, theme);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Theme created: name '" + themeName + "', basename [" + basename + "]");
                    }
                }
            }
        }
        return theme;
    }

    protected MessageSource createMessageSource(String basename) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(basename);
        if (this.defaultEncoding != null) {
            messageSource.setDefaultEncoding(this.defaultEncoding);
        }
        if (this.fallbackToSystemLocale != null) {
            messageSource.setFallbackToSystemLocale(this.fallbackToSystemLocale.booleanValue());
        }
        if (this.beanClassLoader != null) {
            messageSource.setBeanClassLoader(this.beanClassLoader);
        }
        return messageSource;
    }

    protected void initParent(Theme theme) {
        Theme parentTheme;
        if (theme.getMessageSource() instanceof HierarchicalMessageSource) {
            HierarchicalMessageSource messageSource = (HierarchicalMessageSource) theme.getMessageSource();
            if (getParentThemeSource() != null && messageSource.getParentMessageSource() == null && (parentTheme = getParentThemeSource().getTheme(theme.getName())) != null) {
                messageSource.setParentMessageSource(parentTheme.getMessageSource());
            }
        }
    }
}
