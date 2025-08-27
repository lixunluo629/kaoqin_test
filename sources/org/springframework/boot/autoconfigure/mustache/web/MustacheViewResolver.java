package org.springframework.boot.autoconfigure.mustache.web;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/web/MustacheViewResolver.class */
public class MustacheViewResolver extends AbstractTemplateViewResolver {
    private Mustache.Compiler compiler = Mustache.compiler();
    private String charset;

    public MustacheViewResolver() {
        setViewClass(requiredViewClass());
    }

    @Override // org.springframework.web.servlet.view.AbstractTemplateViewResolver, org.springframework.web.servlet.view.UrlBasedViewResolver
    protected Class<?> requiredViewClass() {
        return MustacheView.class;
    }

    public void setCompiler(Mustache.Compiler compiler) {
        this.compiler = compiler;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override // org.springframework.web.servlet.view.UrlBasedViewResolver, org.springframework.web.servlet.view.AbstractCachingViewResolver
    protected View loadView(String viewName, Locale locale) throws Exception {
        Resource resource = resolveResource(viewName, locale);
        if (resource == null) {
            return null;
        }
        MustacheView mustacheView = (MustacheView) super.loadView(viewName, locale);
        mustacheView.setTemplate(createTemplate(resource));
        return mustacheView;
    }

    private Resource resolveResource(String viewName, Locale locale) {
        return resolveFromLocale(viewName, getLocale(locale));
    }

    private Resource resolveFromLocale(String viewName, String locale) {
        Resource resource = getApplicationContext().getResource(getPrefix() + viewName + locale + getSuffix());
        if (resource == null || !resource.exists()) {
            if (locale.isEmpty()) {
                return null;
            }
            int index = locale.lastIndexOf("_");
            return resolveFromLocale(viewName, locale.substring(0, index));
        }
        return resource;
    }

    private String getLocale(Locale locale) {
        if (locale == null) {
            return "";
        }
        LocaleEditor localeEditor = new LocaleEditor();
        localeEditor.setValue(locale);
        return "_" + localeEditor.getAsText();
    }

    private Template createTemplate(Resource resource) throws IOException {
        Reader reader = getReader(resource);
        try {
            Template templateCompile = this.compiler.compile(reader);
            reader.close();
            return templateCompile;
        } catch (Throwable th) {
            reader.close();
            throw th;
        }
    }

    private Reader getReader(Resource resource) throws IOException {
        if (this.charset != null) {
            return new InputStreamReader(resource.getInputStream(), this.charset);
        }
        return new InputStreamReader(resource.getInputStream());
    }
}
