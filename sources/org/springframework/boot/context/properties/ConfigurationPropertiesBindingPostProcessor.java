package org.springframework.boot.context.properties;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.bridge.ISourceLocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBindingPostProcessor.class */
public class ConfigurationPropertiesBindingPostProcessor implements BeanPostProcessor, BeanFactoryAware, EnvironmentAware, ApplicationContextAware, InitializingBean, DisposableBean, ApplicationListener<ContextRefreshedEvent>, PriorityOrdered {
    public static final String VALIDATOR_BEAN_NAME = "configurationPropertiesValidator";
    private static final String[] VALIDATOR_CLASSES = {"javax.validation.Validator", "javax.validation.ValidatorFactory", "javax.validation.bootstrap.GenericBootstrap"};
    private static final Log logger = LogFactory.getLog(ConfigurationPropertiesBindingPostProcessor.class);
    private PropertySources propertySources;
    private Validator validator;
    private volatile Validator localValidator;
    private ConversionService conversionService;
    private DefaultConversionService defaultConversionService;
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    private ConfigurationBeanFactoryMetaData beans = new ConfigurationBeanFactoryMetaData();
    private Environment environment = new StandardEnvironment();
    private List<Converter<?, ?>> converters = Collections.emptyList();
    private List<GenericConverter> genericConverters = Collections.emptyList();
    private int order = ISourceLocation.NO_COLUMN;

    @Autowired(required = false)
    @ConfigurationPropertiesBinding
    public void setConverters(List<Converter<?, ?>> converters) {
        this.converters = converters;
    }

    @Autowired(required = false)
    @ConfigurationPropertiesBinding
    public void setGenericConverters(List<GenericConverter> converters) {
        this.genericConverters = converters;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setPropertySources(PropertySources propertySources) {
        this.propertySources = propertySources;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setBeanMetaDataStore(ConfigurationBeanFactoryMetaData beans) {
        this.beans = beans;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.context.EnvironmentAware
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (this.propertySources == null) {
            this.propertySources = deducePropertySources();
        }
        if (this.validator == null) {
            this.validator = (Validator) getOptionalBean(VALIDATOR_BEAN_NAME, Validator.class);
        }
        if (this.conversionService == null) {
            this.conversionService = (ConversionService) getOptionalBean(ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME, ConversionService.class);
        }
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        freeLocalValidator();
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        freeLocalValidator();
    }

    private void freeLocalValidator() {
        try {
            Validator validator = this.localValidator;
            this.localValidator = null;
            if (validator != null) {
                ((DisposableBean) validator).destroy();
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private PropertySources deducePropertySources() throws BeansException {
        PropertySourcesPlaceholderConfigurer configurer = getSinglePropertySourcesPlaceholderConfigurer();
        if (configurer != null) {
            return new FlatPropertySources(configurer.getAppliedPropertySources());
        }
        if (this.environment instanceof ConfigurableEnvironment) {
            MutablePropertySources propertySources = ((ConfigurableEnvironment) this.environment).getPropertySources();
            return new FlatPropertySources(propertySources);
        }
        logger.warn("Unable to obtain PropertySources from PropertySourcesPlaceholderConfigurer or Environment");
        return new MutablePropertySources();
    }

    private PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer() throws BeansException {
        if (this.beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) this.beanFactory;
            Map<String, PropertySourcesPlaceholderConfigurer> beans = listableBeanFactory.getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false, false);
            if (beans.size() == 1) {
                return beans.values().iterator().next();
            }
            if (beans.size() > 1 && logger.isWarnEnabled()) {
                logger.warn("Multiple PropertySourcesPlaceholderConfigurer beans registered " + beans.keySet() + ", falling back to Environment");
                return null;
            }
            return null;
        }
        return null;
    }

    private <T> T getOptionalBean(String str, Class<T> cls) {
        try {
            return (T) this.beanFactory.getBean(str, cls);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ConfigurationProperties annotation = (ConfigurationProperties) AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class);
        if (annotation != null) {
            postProcessBeforeInitialization(bean, beanName, annotation);
        }
        ConfigurationProperties annotation2 = (ConfigurationProperties) this.beans.findFactoryAnnotation(beanName, ConfigurationProperties.class);
        if (annotation2 != null) {
            postProcessBeforeInitialization(bean, beanName, annotation2);
        }
        return bean;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private void postProcessBeforeInitialization(Object bean, String beanName, ConfigurationProperties annotation) {
        PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory<>(bean);
        factory.setPropertySources(this.propertySources);
        factory.setApplicationContext(this.applicationContext);
        factory.setValidator(determineValidator(bean));
        factory.setConversionService(this.conversionService != null ? this.conversionService : getDefaultConversionService());
        if (annotation != null) {
            factory.setIgnoreInvalidFields(annotation.ignoreInvalidFields());
            factory.setIgnoreUnknownFields(annotation.ignoreUnknownFields());
            factory.setExceptionIfInvalid(annotation.exceptionIfInvalid());
            factory.setIgnoreNestedProperties(annotation.ignoreNestedProperties());
            if (StringUtils.hasLength(annotation.prefix())) {
                factory.setTargetName(annotation.prefix());
            }
        }
        try {
            factory.bindPropertiesToTarget();
        } catch (Exception ex) {
            String targetClass = ClassUtils.getShortName(bean.getClass());
            throw new BeanCreationException(beanName, "Could not bind properties to " + targetClass + " (" + getAnnotationDetails(annotation) + ")", ex);
        }
    }

    private String getAnnotationDetails(ConfigurationProperties annotation) {
        if (annotation == null) {
            return "";
        }
        StringBuilder details = new StringBuilder();
        details.append("prefix=").append(annotation.prefix());
        details.append(", ignoreInvalidFields=").append(annotation.ignoreInvalidFields());
        details.append(", ignoreUnknownFields=").append(annotation.ignoreUnknownFields());
        details.append(", ignoreNestedProperties=").append(annotation.ignoreNestedProperties());
        return details.toString();
    }

    private Validator determineValidator(Object bean) {
        Validator validator = getValidator();
        boolean supportsBean = validator != null && validator.supports(bean.getClass());
        if (ClassUtils.isAssignable(Validator.class, bean.getClass())) {
            if (supportsBean) {
                return new ChainingValidator(validator, (Validator) bean);
            }
            return (Validator) bean;
        }
        if (supportsBean) {
            return validator;
        }
        return null;
    }

    private Validator getValidator() {
        if (this.validator != null) {
            return this.validator;
        }
        if (this.localValidator == null && isJsr303Present()) {
            this.localValidator = new ValidatedLocalValidatorFactoryBean(this.applicationContext);
        }
        return this.localValidator;
    }

    private boolean isJsr303Present() {
        for (String validatorClass : VALIDATOR_CLASSES) {
            if (!ClassUtils.isPresent(validatorClass, this.applicationContext.getClassLoader())) {
                return false;
            }
        }
        return true;
    }

    private ConversionService getDefaultConversionService() throws BeansException {
        if (this.defaultConversionService == null) {
            DefaultConversionService conversionService = new DefaultConversionService();
            this.applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
            for (Converter<?, ?> converter : this.converters) {
                conversionService.addConverter(converter);
            }
            for (GenericConverter genericConverter : this.genericConverters) {
                conversionService.addConverter(genericConverter);
            }
            this.defaultConversionService = conversionService;
        }
        return this.defaultConversionService;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBindingPostProcessor$ValidatedLocalValidatorFactoryBean.class */
    private static class ValidatedLocalValidatorFactoryBean extends LocalValidatorFactoryBean {
        private static final Log logger = LogFactory.getLog(ConfigurationPropertiesBindingPostProcessor.class);

        ValidatedLocalValidatorFactoryBean(ApplicationContext applicationContext) {
            setApplicationContext(applicationContext);
            setMessageInterpolator(new MessageInterpolatorFactory().getObject());
            afterPropertiesSet();
        }

        @Override // org.springframework.validation.beanvalidation.SpringValidatorAdapter, org.springframework.validation.Validator
        public boolean supports(Class<?> type) {
            if (!super.supports(type)) {
                return false;
            }
            if (AnnotatedElementUtils.hasAnnotation(type, Validated.class)) {
                return true;
            }
            if (type.getPackage() != null && type.getPackage().getName().startsWith("org.springframework.boot")) {
                return false;
            }
            if (getConstraintsForClass(type).isBeanConstrained()) {
                logger.warn("The @ConfigurationProperties bean " + type + " contains validation constraints but had not been annotated with @Validated.");
                return true;
            }
            return true;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBindingPostProcessor$ChainingValidator.class */
    private static class ChainingValidator implements Validator {
        private Validator[] validators;

        ChainingValidator(Validator... validators) {
            Assert.notNull(validators, "Validators must not be null");
            this.validators = validators;
        }

        @Override // org.springframework.validation.Validator
        public boolean supports(Class<?> clazz) {
            for (Validator validator : this.validators) {
                if (validator.supports(clazz)) {
                    return true;
                }
            }
            return false;
        }

        @Override // org.springframework.validation.Validator
        public void validate(Object target, Errors errors) {
            for (Validator validator : this.validators) {
                if (validator.supports(target.getClass())) {
                    validator.validate(target, errors);
                }
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBindingPostProcessor$FlatPropertySources.class */
    private static class FlatPropertySources implements PropertySources {
        private PropertySources propertySources;

        FlatPropertySources(PropertySources propertySources) {
            this.propertySources = propertySources;
        }

        @Override // java.lang.Iterable
        public Iterator<PropertySource<?>> iterator() {
            MutablePropertySources result = getFlattened();
            return result.iterator();
        }

        @Override // org.springframework.core.env.PropertySources
        public boolean contains(String name) {
            return get(name) != null;
        }

        @Override // org.springframework.core.env.PropertySources
        public PropertySource<?> get(String name) {
            return getFlattened().get(name);
        }

        private MutablePropertySources getFlattened() {
            MutablePropertySources result = new MutablePropertySources();
            for (PropertySource<?> propertySource : this.propertySources) {
                flattenPropertySources(propertySource, result);
            }
            return result;
        }

        private void flattenPropertySources(PropertySource<?> propertySource, MutablePropertySources result) {
            Object source = propertySource.getSource();
            if (source instanceof ConfigurableEnvironment) {
                ConfigurableEnvironment environment = (ConfigurableEnvironment) source;
                Iterator<PropertySource<?>> it = environment.getPropertySources().iterator();
                while (it.hasNext()) {
                    PropertySource<?> childSource = it.next();
                    flattenPropertySources(childSource, result);
                }
                return;
            }
            result.addLast(propertySource);
        }
    }
}
