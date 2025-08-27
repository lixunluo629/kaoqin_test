package org.springframework.boot.bind;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/YamlConfigurationFactory.class */
public class YamlConfigurationFactory<T> implements FactoryBean<T>, MessageSourceAware, InitializingBean {
    private static final Log logger = LogFactory.getLog(YamlConfigurationFactory.class);
    private final Class<?> type;
    private boolean exceptionIfInvalid;
    private String yaml;
    private Resource resource;
    private T configuration;
    private Validator validator;
    private MessageSource messageSource;
    private Map<Class<?>, Map<String, String>> propertyAliases = Collections.emptyMap();

    public YamlConfigurationFactory(Class<?> type) {
        Assert.notNull(type, "type must not be null");
        this.type = type;
    }

    @Override // org.springframework.context.MessageSourceAware
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setPropertyAliases(Map<Class<?>, Map<String, String>> propertyAliases) {
        this.propertyAliases = new HashMap(propertyAliases);
    }

    public void setYaml(String yaml) {
        this.yaml = yaml;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Deprecated
    public void setExceptionIfInvalid(boolean exceptionIfInvalid) {
        this.exceptionIfInvalid = exceptionIfInvalid;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (this.yaml == null) {
            Assert.state(this.resource != null, "Resource should not be null");
            this.yaml = StreamUtils.copyToString(this.resource.getInputStream(), Charset.defaultCharset());
        }
        Assert.state(this.yaml != null, "Yaml document should not be null: either set it directly or set the resource to load it from");
        try {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Yaml document is %n%s", this.yaml));
            }
            this.configuration = (T) new Yaml(new YamlJavaBeanPropertyConstructor(this.type, this.propertyAliases)).load(this.yaml);
            if (this.validator != null) {
                validate();
            }
        } catch (YAMLException e) {
            if (this.exceptionIfInvalid) {
                throw e;
            }
            logger.error("Failed to load YAML validation bean. Your YAML file may be invalid.", e);
        }
    }

    private void validate() throws BindException {
        BindingResult errors = new BeanPropertyBindingResult(this.configuration, "configuration");
        this.validator.validate(this.configuration, errors);
        if (errors.hasErrors()) {
            logger.error("YAML configuration failed validation");
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(getErrorMessage(error));
            }
            if (this.exceptionIfInvalid) {
                BindException summary = new BindException(errors);
                throw summary;
            }
        }
    }

    private Object getErrorMessage(ObjectError error) {
        if (this.messageSource != null) {
            Locale locale = Locale.getDefault();
            return this.messageSource.getMessage(error, locale) + " (" + error + ")";
        }
        return error;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        if (this.configuration == null) {
            return Object.class;
        }
        return this.configuration.getClass();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public T getObject() throws Exception {
        if (this.configuration == null) {
            afterPropertiesSet();
        }
        return this.configuration;
    }
}
