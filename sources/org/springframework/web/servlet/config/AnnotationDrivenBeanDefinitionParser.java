package org.springframework.web.servlet.config;

import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.xml.DomUtils;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.w3c.dom.Element;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/AnnotationDrivenBeanDefinitionParser.class */
class AnnotationDrivenBeanDefinitionParser implements BeanDefinitionParser {
    public static final String CONTENT_NEGOTIATION_MANAGER_BEAN_NAME = "mvcContentNegotiationManager";
    private static final boolean jackson2Present;
    private static final boolean jackson2XmlPresent;
    private static final boolean gsonPresent;
    public static final String HANDLER_MAPPING_BEAN_NAME = RequestMappingHandlerMapping.class.getName();
    public static final String HANDLER_ADAPTER_BEAN_NAME = RequestMappingHandlerAdapter.class.getName();
    private static final boolean javaxValidationPresent = ClassUtils.isPresent("javax.validation.Validator", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());
    private static boolean romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());
    private static final boolean jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());

    AnnotationDrivenBeanDefinitionParser() {
    }

    static {
        jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", AnnotationDrivenBeanDefinitionParser.class.getClassLoader()) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());
        jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());
        gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());
    }

    @Override // org.springframework.beans.factory.xml.BeanDefinitionParser
    public BeanDefinition parse(Element element, ParserContext context) throws BeanDefinitionStoreException {
        Object source = context.extractSource(element);
        XmlReaderContext readerContext = context.getReaderContext();
        CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
        context.pushContainingComponent(compDefinition);
        RuntimeBeanReference contentNegotiationManager = getContentNegotiationManager(element, source, context);
        RootBeanDefinition handlerMappingDef = new RootBeanDefinition((Class<?>) RequestMappingHandlerMapping.class);
        handlerMappingDef.setSource(source);
        handlerMappingDef.setRole(2);
        handlerMappingDef.getPropertyValues().add("order", 0);
        handlerMappingDef.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
        if (element.hasAttribute("enable-matrix-variables")) {
            Boolean enableMatrixVariables = Boolean.valueOf(element.getAttribute("enable-matrix-variables"));
            handlerMappingDef.getPropertyValues().add("removeSemicolonContent", Boolean.valueOf(!enableMatrixVariables.booleanValue()));
        } else if (element.hasAttribute("enableMatrixVariables")) {
            Boolean enableMatrixVariables2 = Boolean.valueOf(element.getAttribute("enableMatrixVariables"));
            handlerMappingDef.getPropertyValues().add("removeSemicolonContent", Boolean.valueOf(!enableMatrixVariables2.booleanValue()));
        }
        configurePathMatchingProperties(handlerMappingDef, element, context);
        readerContext.getRegistry().registerBeanDefinition(HANDLER_MAPPING_BEAN_NAME, handlerMappingDef);
        RuntimeBeanReference corsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
        handlerMappingDef.getPropertyValues().add("corsConfigurations", corsRef);
        RuntimeBeanReference conversionService = getConversionService(element, source, context);
        RuntimeBeanReference validator = getValidator(element, source, context);
        RuntimeBeanReference messageCodesResolver = getMessageCodesResolver(element);
        RootBeanDefinition bindingDef = new RootBeanDefinition((Class<?>) ConfigurableWebBindingInitializer.class);
        bindingDef.setSource(source);
        bindingDef.setRole(2);
        bindingDef.getPropertyValues().add(ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME, conversionService);
        bindingDef.getPropertyValues().add("validator", validator);
        bindingDef.getPropertyValues().add("messageCodesResolver", messageCodesResolver);
        ManagedList<?> messageConverters = getMessageConverters(element, source, context);
        ManagedList<?> argumentResolvers = getArgumentResolvers(element, context);
        ManagedList<?> returnValueHandlers = getReturnValueHandlers(element, context);
        String asyncTimeout = getAsyncTimeout(element);
        RuntimeBeanReference asyncExecutor = getAsyncExecutor(element);
        ManagedList<?> callableInterceptors = getCallableInterceptors(element, source, context);
        ManagedList<?> deferredResultInterceptors = getDeferredResultInterceptors(element, source, context);
        RootBeanDefinition handlerAdapterDef = new RootBeanDefinition((Class<?>) RequestMappingHandlerAdapter.class);
        handlerAdapterDef.setSource(source);
        handlerAdapterDef.setRole(2);
        handlerAdapterDef.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
        handlerAdapterDef.getPropertyValues().add("webBindingInitializer", bindingDef);
        handlerAdapterDef.getPropertyValues().add("messageConverters", messageConverters);
        addRequestBodyAdvice(handlerAdapterDef);
        addResponseBodyAdvice(handlerAdapterDef);
        if (element.hasAttribute("ignore-default-model-on-redirect")) {
            Boolean ignoreDefaultModel = Boolean.valueOf(element.getAttribute("ignore-default-model-on-redirect"));
            handlerAdapterDef.getPropertyValues().add("ignoreDefaultModelOnRedirect", ignoreDefaultModel);
        } else if (element.hasAttribute("ignoreDefaultModelOnRedirect")) {
            Boolean ignoreDefaultModel2 = Boolean.valueOf(element.getAttribute("ignoreDefaultModelOnRedirect"));
            handlerAdapterDef.getPropertyValues().add("ignoreDefaultModelOnRedirect", ignoreDefaultModel2);
        }
        if (argumentResolvers != null) {
            handlerAdapterDef.getPropertyValues().add("customArgumentResolvers", argumentResolvers);
        }
        if (returnValueHandlers != null) {
            handlerAdapterDef.getPropertyValues().add("customReturnValueHandlers", returnValueHandlers);
        }
        if (asyncTimeout != null) {
            handlerAdapterDef.getPropertyValues().add("asyncRequestTimeout", asyncTimeout);
        }
        if (asyncExecutor != null) {
            handlerAdapterDef.getPropertyValues().add("taskExecutor", asyncExecutor);
        }
        handlerAdapterDef.getPropertyValues().add("callableInterceptors", callableInterceptors);
        handlerAdapterDef.getPropertyValues().add("deferredResultInterceptors", deferredResultInterceptors);
        readerContext.getRegistry().registerBeanDefinition(HANDLER_ADAPTER_BEAN_NAME, handlerAdapterDef);
        RootBeanDefinition uriContributorDef = new RootBeanDefinition((Class<?>) CompositeUriComponentsContributorFactoryBean.class);
        uriContributorDef.setSource(source);
        uriContributorDef.getPropertyValues().addPropertyValue(DispatcherServlet.HANDLER_ADAPTER_BEAN_NAME, handlerAdapterDef);
        uriContributorDef.getPropertyValues().addPropertyValue(ConfigurableApplicationContext.CONVERSION_SERVICE_BEAN_NAME, conversionService);
        readerContext.getRegistry().registerBeanDefinition(MvcUriComponentsBuilder.MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME, uriContributorDef);
        RootBeanDefinition csInterceptorDef = new RootBeanDefinition((Class<?>) ConversionServiceExposingInterceptor.class);
        csInterceptorDef.setSource(source);
        csInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, conversionService);
        RootBeanDefinition mappedInterceptorDef = new RootBeanDefinition((Class<?>) MappedInterceptor.class);
        mappedInterceptorDef.setSource(source);
        mappedInterceptorDef.setRole(2);
        mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, (Object) null);
        mappedInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, csInterceptorDef);
        String mappedInterceptorName = readerContext.registerWithGeneratedName(mappedInterceptorDef);
        RootBeanDefinition methodExceptionResolver = new RootBeanDefinition((Class<?>) ExceptionHandlerExceptionResolver.class);
        methodExceptionResolver.setSource(source);
        methodExceptionResolver.setRole(2);
        methodExceptionResolver.getPropertyValues().add("contentNegotiationManager", contentNegotiationManager);
        methodExceptionResolver.getPropertyValues().add("messageConverters", messageConverters);
        methodExceptionResolver.getPropertyValues().add("order", 0);
        addResponseBodyAdvice(methodExceptionResolver);
        if (argumentResolvers != null) {
            methodExceptionResolver.getPropertyValues().add("customArgumentResolvers", argumentResolvers);
        }
        if (returnValueHandlers != null) {
            methodExceptionResolver.getPropertyValues().add("customReturnValueHandlers", returnValueHandlers);
        }
        String methodExResolverName = readerContext.registerWithGeneratedName(methodExceptionResolver);
        RootBeanDefinition statusExceptionResolver = new RootBeanDefinition((Class<?>) ResponseStatusExceptionResolver.class);
        statusExceptionResolver.setSource(source);
        statusExceptionResolver.setRole(2);
        statusExceptionResolver.getPropertyValues().add("order", 1);
        String statusExResolverName = readerContext.registerWithGeneratedName(statusExceptionResolver);
        RootBeanDefinition defaultExceptionResolver = new RootBeanDefinition((Class<?>) DefaultHandlerExceptionResolver.class);
        defaultExceptionResolver.setSource(source);
        defaultExceptionResolver.setRole(2);
        defaultExceptionResolver.getPropertyValues().add("order", 2);
        String defaultExResolverName = readerContext.registerWithGeneratedName(defaultExceptionResolver);
        context.registerComponent(new BeanComponentDefinition(handlerMappingDef, HANDLER_MAPPING_BEAN_NAME));
        context.registerComponent(new BeanComponentDefinition(handlerAdapterDef, HANDLER_ADAPTER_BEAN_NAME));
        context.registerComponent(new BeanComponentDefinition(uriContributorDef, MvcUriComponentsBuilder.MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME));
        context.registerComponent(new BeanComponentDefinition(mappedInterceptorDef, mappedInterceptorName));
        context.registerComponent(new BeanComponentDefinition(methodExceptionResolver, methodExResolverName));
        context.registerComponent(new BeanComponentDefinition(statusExceptionResolver, statusExResolverName));
        context.registerComponent(new BeanComponentDefinition(defaultExceptionResolver, defaultExResolverName));
        MvcNamespaceUtils.registerDefaultComponents(context, source);
        context.popAndRegisterContainingComponent();
        return null;
    }

    protected void addRequestBodyAdvice(RootBeanDefinition beanDef) {
        if (jackson2Present) {
            beanDef.getPropertyValues().add("requestBodyAdvice", new RootBeanDefinition((Class<?>) JsonViewRequestBodyAdvice.class));
        }
    }

    protected void addResponseBodyAdvice(RootBeanDefinition beanDef) {
        if (jackson2Present) {
            beanDef.getPropertyValues().add("responseBodyAdvice", new RootBeanDefinition((Class<?>) JsonViewResponseBodyAdvice.class));
        }
    }

    private RuntimeBeanReference getConversionService(Element element, Object source, ParserContext context) throws BeanDefinitionStoreException {
        RuntimeBeanReference conversionServiceRef;
        if (element.hasAttribute("conversion-service")) {
            conversionServiceRef = new RuntimeBeanReference(element.getAttribute("conversion-service"));
        } else {
            RootBeanDefinition conversionDef = new RootBeanDefinition((Class<?>) FormattingConversionServiceFactoryBean.class);
            conversionDef.setSource(source);
            conversionDef.setRole(2);
            String conversionName = context.getReaderContext().registerWithGeneratedName(conversionDef);
            context.registerComponent(new BeanComponentDefinition(conversionDef, conversionName));
            conversionServiceRef = new RuntimeBeanReference(conversionName);
        }
        return conversionServiceRef;
    }

    private RuntimeBeanReference getValidator(Element element, Object source, ParserContext context) throws BeanDefinitionStoreException {
        if (element.hasAttribute("validator")) {
            return new RuntimeBeanReference(element.getAttribute("validator"));
        }
        if (javaxValidationPresent) {
            RootBeanDefinition validatorDef = new RootBeanDefinition("org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean");
            validatorDef.setSource(source);
            validatorDef.setRole(2);
            String validatorName = context.getReaderContext().registerWithGeneratedName(validatorDef);
            context.registerComponent(new BeanComponentDefinition(validatorDef, validatorName));
            return new RuntimeBeanReference(validatorName);
        }
        return null;
    }

    private RuntimeBeanReference getContentNegotiationManager(Element element, Object source, ParserContext context) throws BeanDefinitionStoreException {
        RuntimeBeanReference beanRef;
        if (element.hasAttribute("content-negotiation-manager")) {
            String name = element.getAttribute("content-negotiation-manager");
            beanRef = new RuntimeBeanReference(name);
        } else {
            RootBeanDefinition factoryBeanDef = new RootBeanDefinition((Class<?>) ContentNegotiationManagerFactoryBean.class);
            factoryBeanDef.setSource(source);
            factoryBeanDef.setRole(2);
            factoryBeanDef.getPropertyValues().add("mediaTypes", getDefaultMediaTypes());
            context.getReaderContext().getRegistry().registerBeanDefinition(CONTENT_NEGOTIATION_MANAGER_BEAN_NAME, factoryBeanDef);
            context.registerComponent(new BeanComponentDefinition(factoryBeanDef, CONTENT_NEGOTIATION_MANAGER_BEAN_NAME));
            beanRef = new RuntimeBeanReference(CONTENT_NEGOTIATION_MANAGER_BEAN_NAME);
        }
        return beanRef;
    }

    private void configurePathMatchingProperties(RootBeanDefinition handlerMappingDef, Element element, ParserContext context) {
        Element pathMatchingElement = DomUtils.getChildElementByTagName(element, "path-matching");
        if (pathMatchingElement != null) {
            Object source = context.extractSource(element);
            if (pathMatchingElement.hasAttribute("suffix-pattern")) {
                Boolean useSuffixPatternMatch = Boolean.valueOf(pathMatchingElement.getAttribute("suffix-pattern"));
                handlerMappingDef.getPropertyValues().add("useSuffixPatternMatch", useSuffixPatternMatch);
            }
            if (pathMatchingElement.hasAttribute("trailing-slash")) {
                Boolean useTrailingSlashMatch = Boolean.valueOf(pathMatchingElement.getAttribute("trailing-slash"));
                handlerMappingDef.getPropertyValues().add("useTrailingSlashMatch", useTrailingSlashMatch);
            }
            if (pathMatchingElement.hasAttribute("registered-suffixes-only")) {
                Boolean useRegisteredSuffixPatternMatch = Boolean.valueOf(pathMatchingElement.getAttribute("registered-suffixes-only"));
                handlerMappingDef.getPropertyValues().add("useRegisteredSuffixPatternMatch", useRegisteredSuffixPatternMatch);
            }
            RuntimeBeanReference pathHelperRef = null;
            if (pathMatchingElement.hasAttribute("path-helper")) {
                pathHelperRef = new RuntimeBeanReference(pathMatchingElement.getAttribute("path-helper"));
            }
            handlerMappingDef.getPropertyValues().add("urlPathHelper", MvcNamespaceUtils.registerUrlPathHelper(pathHelperRef, context, source));
            RuntimeBeanReference pathMatcherRef = null;
            if (pathMatchingElement.hasAttribute("path-matcher")) {
                pathMatcherRef = new RuntimeBeanReference(pathMatchingElement.getAttribute("path-matcher"));
            }
            handlerMappingDef.getPropertyValues().add("pathMatcher", MvcNamespaceUtils.registerPathMatcher(pathMatcherRef, context, source));
        }
    }

    private Properties getDefaultMediaTypes() {
        Properties defaultMediaTypes = new Properties();
        if (romePresent) {
            defaultMediaTypes.put("atom", "application/atom+xml");
            defaultMediaTypes.put("rss", MediaType.APPLICATION_RSS_XML_VALUE);
        }
        if (jaxb2Present || jackson2XmlPresent) {
            defaultMediaTypes.put("xml", "application/xml");
        }
        if (jackson2Present || gsonPresent) {
            defaultMediaTypes.put("json", "application/json");
        }
        return defaultMediaTypes;
    }

    private RuntimeBeanReference getMessageCodesResolver(Element element) {
        if (element.hasAttribute("message-codes-resolver")) {
            return new RuntimeBeanReference(element.getAttribute("message-codes-resolver"));
        }
        return null;
    }

    private String getAsyncTimeout(Element element) {
        Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
        if (asyncElement != null) {
            return asyncElement.getAttribute("default-timeout");
        }
        return null;
    }

    private RuntimeBeanReference getAsyncExecutor(Element element) {
        Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
        if (asyncElement != null && asyncElement.hasAttribute("task-executor")) {
            return new RuntimeBeanReference(asyncElement.getAttribute("task-executor"));
        }
        return null;
    }

    private ManagedList<?> getCallableInterceptors(Element element, Object source, ParserContext context) {
        Element interceptorsElement;
        ManagedList<Object> interceptors = new ManagedList<>();
        Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
        if (asyncElement != null && (interceptorsElement = DomUtils.getChildElementByTagName(asyncElement, "callable-interceptors")) != null) {
            interceptors.setSource(source);
            for (Element converter : DomUtils.getChildElementsByTagName(interceptorsElement, "bean")) {
                BeanDefinitionHolder beanDef = context.getDelegate().parseBeanDefinitionElement(converter);
                interceptors.add(context.getDelegate().decorateBeanDefinitionIfRequired(converter, beanDef));
            }
        }
        return interceptors;
    }

    private ManagedList<?> getDeferredResultInterceptors(Element element, Object source, ParserContext context) {
        Element interceptorsElement;
        ManagedList<Object> interceptors = new ManagedList<>();
        Element asyncElement = DomUtils.getChildElementByTagName(element, "async-support");
        if (asyncElement != null && (interceptorsElement = DomUtils.getChildElementByTagName(asyncElement, "deferred-result-interceptors")) != null) {
            interceptors.setSource(source);
            for (Element converter : DomUtils.getChildElementsByTagName(interceptorsElement, "bean")) {
                BeanDefinitionHolder beanDef = context.getDelegate().parseBeanDefinitionElement(converter);
                interceptors.add(context.getDelegate().decorateBeanDefinitionIfRequired(converter, beanDef));
            }
        }
        return interceptors;
    }

    private ManagedList<?> getArgumentResolvers(Element element, ParserContext context) {
        Element resolversElement = DomUtils.getChildElementByTagName(element, "argument-resolvers");
        if (resolversElement != null) {
            ManagedList<Object> resolvers = extractBeanSubElements(resolversElement, context);
            return wrapLegacyResolvers(resolvers, context);
        }
        return null;
    }

    private ManagedList<Object> wrapLegacyResolvers(List<Object> list, ParserContext context) throws IllegalArgumentException {
        ManagedList<Object> result = new ManagedList<>();
        for (Object object : list) {
            if (object instanceof BeanDefinitionHolder) {
                BeanDefinitionHolder beanDef = (BeanDefinitionHolder) object;
                String className = beanDef.getBeanDefinition().getBeanClassName();
                Class<?> clazz = ClassUtils.resolveClassName(className, context.getReaderContext().getBeanClassLoader());
                if (WebArgumentResolver.class.isAssignableFrom(clazz)) {
                    RootBeanDefinition adapter = new RootBeanDefinition((Class<?>) ServletWebArgumentResolverAdapter.class);
                    adapter.getConstructorArgumentValues().addIndexedArgumentValue(0, beanDef);
                    result.add(new BeanDefinitionHolder(adapter, beanDef.getBeanName() + "Adapter"));
                }
            }
            result.add(object);
        }
        return result;
    }

    private ManagedList<?> getReturnValueHandlers(Element element, ParserContext context) {
        Element handlers = DomUtils.getChildElementByTagName(element, "return-value-handlers");
        if (handlers != null) {
            return extractBeanSubElements(handlers, context);
        }
        return null;
    }

    private ManagedList<?> getMessageConverters(Element element, Object source, ParserContext context) {
        Element convertersElement = DomUtils.getChildElementByTagName(element, "message-converters");
        ManagedList<Object> messageConverters = new ManagedList<>();
        if (convertersElement != null) {
            messageConverters.setSource(source);
            for (Element beanElement : DomUtils.getChildElementsByTagName(convertersElement, "bean", "ref")) {
                Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
                messageConverters.add(object);
            }
        }
        if (convertersElement == null || Boolean.valueOf(convertersElement.getAttribute("register-defaults")).booleanValue()) {
            messageConverters.setSource(source);
            messageConverters.add(createConverterDefinition(ByteArrayHttpMessageConverter.class, source));
            RootBeanDefinition stringConverterDef = createConverterDefinition(StringHttpMessageConverter.class, source);
            stringConverterDef.getPropertyValues().add("writeAcceptCharset", false);
            messageConverters.add(stringConverterDef);
            messageConverters.add(createConverterDefinition(ResourceHttpMessageConverter.class, source));
            messageConverters.add(createConverterDefinition(SourceHttpMessageConverter.class, source));
            messageConverters.add(createConverterDefinition(AllEncompassingFormHttpMessageConverter.class, source));
            if (romePresent) {
                messageConverters.add(createConverterDefinition(AtomFeedHttpMessageConverter.class, source));
                messageConverters.add(createConverterDefinition(RssChannelHttpMessageConverter.class, source));
            }
            if (jackson2XmlPresent) {
                RootBeanDefinition jacksonConverterDef = createConverterDefinition(MappingJackson2XmlHttpMessageConverter.class, source);
                GenericBeanDefinition jacksonFactoryDef = createObjectMapperFactoryDefinition(source);
                jacksonFactoryDef.getPropertyValues().add("createXmlMapper", true);
                jacksonConverterDef.getConstructorArgumentValues().addIndexedArgumentValue(0, jacksonFactoryDef);
                messageConverters.add(jacksonConverterDef);
            } else if (jaxb2Present) {
                messageConverters.add(createConverterDefinition(Jaxb2RootElementHttpMessageConverter.class, source));
            }
            if (jackson2Present) {
                RootBeanDefinition jacksonConverterDef2 = createConverterDefinition(MappingJackson2HttpMessageConverter.class, source);
                jacksonConverterDef2.getConstructorArgumentValues().addIndexedArgumentValue(0, createObjectMapperFactoryDefinition(source));
                messageConverters.add(jacksonConverterDef2);
            } else if (gsonPresent) {
                messageConverters.add(createConverterDefinition(GsonHttpMessageConverter.class, source));
            }
        }
        return messageConverters;
    }

    private GenericBeanDefinition createObjectMapperFactoryDefinition(Object source) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(Jackson2ObjectMapperFactoryBean.class);
        beanDefinition.setSource(source);
        beanDefinition.setRole(2);
        return beanDefinition;
    }

    private RootBeanDefinition createConverterDefinition(Class<?> converterClass, Object source) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(converterClass);
        beanDefinition.setSource(source);
        beanDefinition.setRole(2);
        return beanDefinition;
    }

    private ManagedList<Object> extractBeanSubElements(Element parentElement, ParserContext context) {
        ManagedList<Object> list = new ManagedList<>();
        list.setSource(context.extractSource(parentElement));
        for (Element beanElement : DomUtils.getChildElementsByTagName(parentElement, "bean", "ref")) {
            Object object = context.getDelegate().parsePropertySubElement(beanElement, null);
            list.add(object);
        }
        return list;
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/config/AnnotationDrivenBeanDefinitionParser$CompositeUriComponentsContributorFactoryBean.class */
    static class CompositeUriComponentsContributorFactoryBean implements FactoryBean<CompositeUriComponentsContributor>, InitializingBean {
        private RequestMappingHandlerAdapter handlerAdapter;
        private ConversionService conversionService;
        private CompositeUriComponentsContributor uriComponentsContributor;

        CompositeUriComponentsContributorFactoryBean() {
        }

        public void setHandlerAdapter(RequestMappingHandlerAdapter handlerAdapter) {
            this.handlerAdapter = handlerAdapter;
        }

        public void setConversionService(ConversionService conversionService) {
            this.conversionService = conversionService;
        }

        @Override // org.springframework.beans.factory.InitializingBean
        public void afterPropertiesSet() {
            this.uriComponentsContributor = new CompositeUriComponentsContributor(this.handlerAdapter.getArgumentResolvers(), this.conversionService);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.beans.factory.FactoryBean
        public CompositeUriComponentsContributor getObject() {
            return this.uriComponentsContributor;
        }

        @Override // org.springframework.beans.factory.FactoryBean
        public Class<?> getObjectType() {
            return CompositeUriComponentsContributor.class;
        }

        @Override // org.springframework.beans.factory.FactoryBean
        public boolean isSingleton() {
            return true;
        }
    }
}
