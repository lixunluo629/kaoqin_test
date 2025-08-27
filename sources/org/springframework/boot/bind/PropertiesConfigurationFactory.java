package org.springframework.boot.bind;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.support.ResourceEditorRegistrar;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.PropertySources;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/PropertiesConfigurationFactory.class */
public class PropertiesConfigurationFactory<T> implements FactoryBean<T>, ApplicationContextAware, MessageSourceAware, InitializingBean {
    private static final char[] EXACT_DELIMITERS = {'_', '.', '['};
    private static final char[] TARGET_NAME_DELIMITERS = {'_', '.'};
    private static final Log logger = LogFactory.getLog(PropertiesConfigurationFactory.class);
    private boolean ignoreInvalidFields;
    private PropertySources propertySources;
    private final T target;
    private Validator validator;
    private ApplicationContext applicationContext;
    private MessageSource messageSource;
    private String targetName;
    private ConversionService conversionService;
    private boolean ignoreUnknownFields = true;
    private boolean exceptionIfInvalid = true;
    private boolean hasBeenBound = false;
    private boolean ignoreNestedProperties = false;
    private boolean resolvePlaceholders = true;

    public PropertiesConfigurationFactory(T target) {
        Assert.notNull(target, "target must not be null");
        this.target = target;
    }

    public PropertiesConfigurationFactory(Class<?> cls) {
        Assert.notNull(cls, "type must not be null");
        this.target = (T) BeanUtils.instantiate(cls);
    }

    public void setIgnoreNestedProperties(boolean ignoreNestedProperties) {
        this.ignoreNestedProperties = ignoreNestedProperties;
    }

    public void setIgnoreUnknownFields(boolean ignoreUnknownFields) {
        this.ignoreUnknownFields = ignoreUnknownFields;
    }

    public void setIgnoreInvalidFields(boolean ignoreInvalidFields) {
        this.ignoreInvalidFields = ignoreInvalidFields;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override // org.springframework.context.MessageSourceAware
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setPropertySources(PropertySources propertySources) {
        this.propertySources = propertySources;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Deprecated
    public void setExceptionIfInvalid(boolean exceptionIfInvalid) {
        this.exceptionIfInvalid = exceptionIfInvalid;
    }

    public void setResolvePlaceholders(boolean resolvePlaceholders) {
        this.resolvePlaceholders = resolvePlaceholders;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        bindPropertiesToTarget();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        if (this.target == null) {
            return Object.class;
        }
        return this.target.getClass();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public T getObject() throws Exception {
        if (!this.hasBeenBound) {
            bindPropertiesToTarget();
        }
        return this.target;
    }

    public void bindPropertiesToTarget() throws BeansException, BindException {
        Assert.state(this.propertySources != null, "PropertySources should not be null");
        try {
            if (logger.isTraceEnabled()) {
                logger.trace("Property Sources: " + this.propertySources);
            }
            this.hasBeenBound = true;
            doBindPropertiesToTarget();
        } catch (BindException ex) {
            if (this.exceptionIfInvalid) {
                throw ex;
            }
            logger.error("Failed to load Properties validation bean. Your Properties may be invalid.", ex);
        }
    }

    private void doBindPropertiesToTarget() throws BeansException, BindException {
        RelaxedDataBinder dataBinder = this.targetName != null ? new RelaxedDataBinder(this.target, this.targetName) : new RelaxedDataBinder(this.target);
        if (this.validator != null && this.validator.supports(dataBinder.getTarget().getClass())) {
            dataBinder.setValidator(this.validator);
        }
        if (this.conversionService != null) {
            dataBinder.setConversionService(this.conversionService);
        }
        dataBinder.setAutoGrowCollectionLimit(Integer.MAX_VALUE);
        dataBinder.setIgnoreNestedProperties(this.ignoreNestedProperties);
        dataBinder.setIgnoreInvalidFields(this.ignoreInvalidFields);
        dataBinder.setIgnoreUnknownFields(this.ignoreUnknownFields);
        customizeBinder(dataBinder);
        if (this.applicationContext != null) {
            ResourceEditorRegistrar resourceEditorRegistrar = new ResourceEditorRegistrar(this.applicationContext, this.applicationContext.getEnvironment());
            resourceEditorRegistrar.registerCustomEditors(dataBinder);
        }
        Iterable<String> relaxedTargetNames = getRelaxedTargetNames();
        Set<String> names = getNames(relaxedTargetNames);
        PropertyValues propertyValues = getPropertySourcesPropertyValues(names, relaxedTargetNames);
        dataBinder.bind(propertyValues);
        if (this.validator != null) {
            dataBinder.validate();
        }
        checkForBindingErrors(dataBinder);
    }

    private Iterable<String> getRelaxedTargetNames() {
        if (this.target == null || !StringUtils.hasLength(this.targetName)) {
            return null;
        }
        return new RelaxedNames(this.targetName);
    }

    private Set<String> getNames(Iterable<String> prefixes) throws BeansException {
        Set<String> names = new LinkedHashSet<>();
        if (this.target != null) {
            PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(this.target.getClass());
            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                if (!name.equals("class")) {
                    RelaxedNames relaxedNames = RelaxedNames.forCamelCase(name);
                    if (prefixes == null) {
                        Iterator<String> it = relaxedNames.iterator();
                        while (it.hasNext()) {
                            names.add(it.next());
                        }
                    } else {
                        for (String prefix : prefixes) {
                            Iterator<String> it2 = relaxedNames.iterator();
                            while (it2.hasNext()) {
                                String relaxedName = it2.next();
                                names.add(prefix + "." + relaxedName);
                                names.add(prefix + "_" + relaxedName);
                            }
                        }
                    }
                }
            }
        }
        return names;
    }

    private PropertyValues getPropertySourcesPropertyValues(Set<String> names, Iterable<String> relaxedTargetNames) {
        PropertyNamePatternsMatcher includes = getPropertyNamePatternsMatcher(names, relaxedTargetNames);
        return new PropertySourcesPropertyValues(this.propertySources, names, includes, this.resolvePlaceholders);
    }

    private PropertyNamePatternsMatcher getPropertyNamePatternsMatcher(Set<String> names, Iterable<String> relaxedTargetNames) {
        if (this.ignoreUnknownFields && !isMapTarget()) {
            return new DefaultPropertyNamePatternsMatcher(EXACT_DELIMITERS, true, names);
        }
        if (relaxedTargetNames != null) {
            Set<String> relaxedNames = new HashSet<>();
            for (String relaxedTargetName : relaxedTargetNames) {
                relaxedNames.add(relaxedTargetName);
            }
            return new DefaultPropertyNamePatternsMatcher(TARGET_NAME_DELIMITERS, true, relaxedNames);
        }
        return PropertyNamePatternsMatcher.ALL;
    }

    private boolean isMapTarget() {
        return this.target != null && Map.class.isAssignableFrom(this.target.getClass());
    }

    private void checkForBindingErrors(RelaxedDataBinder dataBinder) throws BindException {
        BindingResult errors = dataBinder.getBindingResult();
        if (errors.hasErrors()) {
            logger.error("Properties configuration failed validation");
            for (ObjectError error : errors.getAllErrors()) {
                logger.error(this.messageSource != null ? this.messageSource.getMessage(error, Locale.getDefault()) + " (" + error + ")" : error);
            }
            if (this.exceptionIfInvalid) {
                throw new BindException(errors);
            }
        }
    }

    protected void customizeBinder(DataBinder dataBinder) {
    }
}
