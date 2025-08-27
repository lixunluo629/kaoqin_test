package org.springframework.web.servlet.view.groovy;

import groovy.text.markup.MarkupTemplateEngine;
import groovy.text.markup.TemplateConfiguration;
import groovy.text.markup.TemplateResolver;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/groovy/GroovyMarkupConfigurer.class */
public class GroovyMarkupConfigurer extends TemplateConfiguration implements GroovyMarkupConfig, ApplicationContextAware, InitializingBean {
    private String resourceLoaderPath = "classpath:";
    private MarkupTemplateEngine templateEngine;
    private ApplicationContext applicationContext;

    public void setResourceLoaderPath(String resourceLoaderPath) {
        this.resourceLoaderPath = resourceLoaderPath;
    }

    public String getResourceLoaderPath() {
        return this.resourceLoaderPath;
    }

    public void setTemplateEngine(MarkupTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override // org.springframework.web.servlet.view.groovy.GroovyMarkupConfig
    public MarkupTemplateEngine getTemplateEngine() {
        return this.templateEngine;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public void setLocale(Locale locale) {
        super.setLocale(locale);
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (this.templateEngine == null) {
            this.templateEngine = createTemplateEngine();
        }
    }

    protected MarkupTemplateEngine createTemplateEngine() throws IOException {
        if (this.templateEngine == null) {
            ClassLoader templateClassLoader = createTemplateClassLoader();
            this.templateEngine = new MarkupTemplateEngine(templateClassLoader, this, new LocaleTemplateResolver());
        }
        return this.templateEngine;
    }

    protected ClassLoader createTemplateClassLoader() throws IOException {
        String[] paths = StringUtils.commaDelimitedListToStringArray(getResourceLoaderPath());
        List<URL> urls = new ArrayList<>();
        for (String path : paths) {
            Resource[] resources = getApplicationContext().getResources(path);
            if (resources.length > 0) {
                for (Resource resource : resources) {
                    if (resource.exists()) {
                        urls.add(resource.getURL());
                    }
                }
            }
        }
        ClassLoader classLoader = getApplicationContext().getClassLoader();
        return urls.size() > 0 ? new URLClassLoader((URL[]) urls.toArray(new URL[urls.size()]), classLoader) : classLoader;
    }

    protected URL resolveTemplate(ClassLoader classLoader, String templatePath) throws IOException {
        MarkupTemplateEngine.TemplateResource resource = MarkupTemplateEngine.TemplateResource.parse(templatePath);
        Locale locale = LocaleContextHolder.getLocale();
        URL url = classLoader.getResource(resource.withLocale(locale.toString().replace("-", "_")).toString());
        if (url == null) {
            url = classLoader.getResource(resource.withLocale(locale.getLanguage()).toString());
        }
        if (url == null) {
            url = classLoader.getResource(resource.withLocale((String) null).toString());
        }
        if (url == null) {
            throw new IOException("Unable to load template:" + templatePath);
        }
        return url;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/view/groovy/GroovyMarkupConfigurer$LocaleTemplateResolver.class */
    private class LocaleTemplateResolver implements TemplateResolver {
        private ClassLoader classLoader;

        private LocaleTemplateResolver() {
        }

        public void configure(ClassLoader templateClassLoader, TemplateConfiguration configuration) {
            this.classLoader = templateClassLoader;
        }

        public URL resolveTemplate(String templatePath) throws IOException {
            return GroovyMarkupConfigurer.this.resolveTemplate(this.classLoader, templatePath);
        }
    }
}
