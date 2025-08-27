package org.springframework.validation.beanvalidation;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.TraversableResolver;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidationProviderResolver;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;
import javax.validation.bootstrap.ProviderSpecificBootstrap;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/beanvalidation/LocalValidatorFactoryBean.class */
public class LocalValidatorFactoryBean extends SpringValidatorAdapter implements ValidatorFactory, ApplicationContextAware, InitializingBean, DisposableBean {
    private static final Method closeMethod = ClassUtils.getMethodIfAvailable(ValidatorFactory.class, "close", new Class[0]);
    private Class providerClass;
    private ValidationProviderResolver validationProviderResolver;
    private MessageInterpolator messageInterpolator;
    private TraversableResolver traversableResolver;
    private ConstraintValidatorFactory constraintValidatorFactory;
    private Resource[] mappingLocations;
    private ApplicationContext applicationContext;
    private ValidatorFactory validatorFactory;
    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final Map<String, String> validationPropertyMap = new HashMap();

    public void setProviderClass(Class providerClass) {
        this.providerClass = providerClass;
    }

    public void setValidationProviderResolver(ValidationProviderResolver validationProviderResolver) {
        this.validationProviderResolver = validationProviderResolver;
    }

    public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
        this.messageInterpolator = messageInterpolator;
    }

    public void setValidationMessageSource(MessageSource messageSource) {
        this.messageInterpolator = HibernateValidatorDelegate.buildMessageInterpolator(messageSource);
    }

    public void setTraversableResolver(TraversableResolver traversableResolver) {
        this.traversableResolver = traversableResolver;
    }

    public void setConstraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory) {
        this.constraintValidatorFactory = constraintValidatorFactory;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public void setMappingLocations(Resource... mappingLocations) {
        this.mappingLocations = mappingLocations;
    }

    public void setValidationProperties(Properties jpaProperties) {
        CollectionUtils.mergePropertiesIntoMap(jpaProperties, this.validationPropertyMap);
    }

    public void setValidationPropertyMap(Map<String, String> validationProperties) {
        if (validationProperties != null) {
            this.validationPropertyMap.putAll(validationProperties);
        }
    }

    public Map<String, String> getValidationPropertyMap() {
        return this.validationPropertyMap;
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws NoSuchMethodException, SecurityException {
        Configuration<?> configuration;
        if (this.providerClass != null) {
            ProviderSpecificBootstrap bootstrap = Validation.byProvider(this.providerClass);
            if (this.validationProviderResolver != null) {
                bootstrap = bootstrap.providerResolver(this.validationProviderResolver);
            }
            configuration = bootstrap.configure();
        } else {
            GenericBootstrap bootstrap2 = Validation.byDefaultProvider();
            if (this.validationProviderResolver != null) {
                bootstrap2 = bootstrap2.providerResolver(this.validationProviderResolver);
            }
            configuration = bootstrap2.configure();
        }
        if (this.applicationContext != null) {
            try {
                Method eclMethod = configuration.getClass().getMethod("externalClassLoader", ClassLoader.class);
                ReflectionUtils.invokeMethod(eclMethod, configuration, this.applicationContext.getClassLoader());
            } catch (NoSuchMethodException e) {
            }
        }
        MessageInterpolator targetInterpolator = this.messageInterpolator;
        if (targetInterpolator == null) {
            targetInterpolator = configuration.getDefaultMessageInterpolator();
        }
        configuration.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
        if (this.traversableResolver != null) {
            configuration.traversableResolver(this.traversableResolver);
        }
        ConstraintValidatorFactory targetConstraintValidatorFactory = this.constraintValidatorFactory;
        if (targetConstraintValidatorFactory == null && this.applicationContext != null) {
            targetConstraintValidatorFactory = new SpringConstraintValidatorFactory(this.applicationContext.getAutowireCapableBeanFactory());
        }
        if (targetConstraintValidatorFactory != null) {
            configuration.constraintValidatorFactory(targetConstraintValidatorFactory);
        }
        if (this.parameterNameDiscoverer != null) {
            configureParameterNameProviderIfPossible(configuration);
        }
        if (this.mappingLocations != null) {
            for (Resource location : this.mappingLocations) {
                try {
                    configuration.addMapping(location.getInputStream());
                } catch (IOException e2) {
                    throw new IllegalStateException("Cannot read mapping resource: " + location);
                }
            }
        }
        for (Map.Entry<String, String> entry : this.validationPropertyMap.entrySet()) {
            configuration.addProperty(entry.getKey(), entry.getValue());
        }
        postProcessConfiguration(configuration);
        this.validatorFactory = configuration.buildValidatorFactory();
        setTargetValidator(this.validatorFactory.getValidator());
    }

    private void configureParameterNameProviderIfPossible(Configuration<?> configuration) {
        try {
            Class<?> parameterNameProviderClass = ClassUtils.forName("javax.validation.ParameterNameProvider", getClass().getClassLoader());
            Method parameterNameProviderMethod = Configuration.class.getMethod("parameterNameProvider", parameterNameProviderClass);
            final Object defaultProvider = ReflectionUtils.invokeMethod(Configuration.class.getMethod("getDefaultParameterNameProvider", new Class[0]), configuration);
            final ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
            Object parameterNameProvider = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{parameterNameProviderClass}, new InvocationHandler() { // from class: org.springframework.validation.beanvalidation.LocalValidatorFactoryBean.1
                @Override // java.lang.reflect.InvocationHandler
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals("getParameterNames")) {
                        String[] result = null;
                        if (args[0] instanceof Constructor) {
                            result = discoverer.getParameterNames((Constructor<?>) args[0]);
                        } else if (args[0] instanceof Method) {
                            result = discoverer.getParameterNames((Method) args[0]);
                        }
                        if (result != null) {
                            return Arrays.asList(result);
                        }
                        try {
                            return method.invoke(defaultProvider, args);
                        } catch (InvocationTargetException ex) {
                            throw ex.getTargetException();
                        }
                    }
                    try {
                        return method.invoke(this, args);
                    } catch (InvocationTargetException ex2) {
                        throw ex2.getTargetException();
                    }
                }
            });
            ReflectionUtils.invokeMethod(parameterNameProviderMethod, configuration, parameterNameProvider);
        } catch (Throwable th) {
        }
    }

    protected void postProcessConfiguration(Configuration<?> configuration) {
    }

    @Override // javax.validation.ValidatorFactory
    public Validator getValidator() {
        Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
        return this.validatorFactory.getValidator();
    }

    @Override // javax.validation.ValidatorFactory
    public ValidatorContext usingContext() {
        Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
        return this.validatorFactory.usingContext();
    }

    @Override // javax.validation.ValidatorFactory
    public MessageInterpolator getMessageInterpolator() {
        Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
        return this.validatorFactory.getMessageInterpolator();
    }

    @Override // javax.validation.ValidatorFactory
    public TraversableResolver getTraversableResolver() {
        Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
        return this.validatorFactory.getTraversableResolver();
    }

    @Override // javax.validation.ValidatorFactory
    public ConstraintValidatorFactory getConstraintValidatorFactory() {
        Assert.notNull(this.validatorFactory, "No target ValidatorFactory set");
        return this.validatorFactory.getConstraintValidatorFactory();
    }

    @Override // org.springframework.validation.beanvalidation.SpringValidatorAdapter, javax.validation.Validator
    public <T> T unwrap(Class<T> cls) {
        if (cls == null || !ValidatorFactory.class.isAssignableFrom(cls)) {
            try {
                return (T) super.unwrap(cls);
            } catch (ValidationException e) {
            }
        }
        try {
            return (T) this.validatorFactory.unwrap(cls);
        } catch (ValidationException e2) {
            if (ValidatorFactory.class == cls) {
                return (T) this.validatorFactory;
            }
            throw e2;
        }
    }

    @Override // javax.validation.ValidatorFactory
    public void close() {
        if (closeMethod != null && this.validatorFactory != null) {
            ReflectionUtils.invokeMethod(closeMethod, this.validatorFactory);
        }
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        close();
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/beanvalidation/LocalValidatorFactoryBean$HibernateValidatorDelegate.class */
    private static class HibernateValidatorDelegate {
        private HibernateValidatorDelegate() {
        }

        public static MessageInterpolator buildMessageInterpolator(MessageSource messageSource) {
            return new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
        }
    }
}
