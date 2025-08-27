package org.springframework.boot.autoconfigure.mustache;

import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.FactoryBean;

@Deprecated
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/MustacheCompilerFactoryBean.class */
public class MustacheCompilerFactoryBean implements FactoryBean<Mustache.Compiler> {
    private String delims;
    private Mustache.TemplateLoader templateLoader;
    private Mustache.Formatter formatter;
    private Mustache.Escaper escaper;
    private Mustache.Collector collector;
    private Mustache.Compiler compiler;
    private String defaultValue;
    private Boolean emptyStringIsFalse;

    public void setDelims(String delims) {
        this.delims = delims;
    }

    public void setTemplateLoader(Mustache.TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    public void setFormatter(Mustache.Formatter formatter) {
        this.formatter = formatter;
    }

    public void setEscaper(Mustache.Escaper escaper) {
        this.escaper = escaper;
    }

    public void setCollector(Mustache.Collector collector) {
        this.collector = collector;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setEmptyStringIsFalse(Boolean emptyStringIsFalse) {
        this.emptyStringIsFalse = emptyStringIsFalse;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public Mustache.Compiler getObject() throws Exception {
        this.compiler = Mustache.compiler();
        if (this.delims != null) {
            this.compiler = this.compiler.withDelims(this.delims);
        }
        if (this.templateLoader != null) {
            this.compiler = this.compiler.withLoader(this.templateLoader);
        }
        if (this.formatter != null) {
            this.compiler = this.compiler.withFormatter(this.formatter);
        }
        if (this.escaper != null) {
            this.compiler = this.compiler.withEscaper(this.escaper);
        }
        if (this.collector != null) {
            this.compiler = this.compiler.withCollector(this.collector);
        }
        if (this.defaultValue != null) {
            this.compiler = this.compiler.defaultValue(this.defaultValue);
        }
        if (this.emptyStringIsFalse != null) {
            this.compiler = this.compiler.emptyStringIsFalse(this.emptyStringIsFalse.booleanValue());
        }
        return this.compiler;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return Mustache.Compiler.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return false;
    }
}
