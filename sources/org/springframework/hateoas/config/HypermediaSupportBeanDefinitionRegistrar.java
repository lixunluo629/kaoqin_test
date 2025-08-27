package org.springframework.hateoas.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.LinkDiscoverers;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.core.AnnotationRelProvider;
import org.springframework.hateoas.core.DefaultRelProvider;
import org.springframework.hateoas.core.DelegatingRelProvider;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.plugin.core.support.PluginRegistryFactoryBean;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/HypermediaSupportBeanDefinitionRegistrar.class */
class HypermediaSupportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final String DELEGATING_REL_PROVIDER_BEAN_NAME = "_relProvider";
    private static final String LINK_DISCOVERER_REGISTRY_BEAN_NAME = "_linkDiscovererRegistry";
    private static final String HAL_OBJECT_MAPPER_BEAN_NAME = "_halObjectMapper";
    private static final String MESSAGE_SOURCE_BEAN_NAME = "linkRelationMessageSource";
    private static final boolean JACKSON2_PRESENT = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null);
    private static final boolean JSONPATH_PRESENT = ClassUtils.isPresent("com.jayway.jsonpath.JsonPath", null);
    private static final boolean EVO_PRESENT = ClassUtils.isPresent("org.atteo.evo.inflector.English", null);
    private final ImportBeanDefinitionRegistrar linkBuilderBeanDefinitionRegistrar = new LinkBuilderBeanDefinitionRegistrar();

    HypermediaSupportBeanDefinitionRegistrar() {
    }

    @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        this.linkBuilderBeanDefinitionRegistrar.registerBeanDefinitions(metadata, registry);
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableHypermediaSupport.class.getName());
        Collection<EnableHypermediaSupport.HypermediaType> types = Arrays.asList((EnableHypermediaSupport.HypermediaType[]) attributes.get("type"));
        for (EnableHypermediaSupport.HypermediaType type : types) {
            if (JSONPATH_PRESENT) {
                AbstractBeanDefinition linkDiscovererBeanDefinition = getLinkDiscovererBeanDefinition(type);
                BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(linkDiscovererBeanDefinition, BeanDefinitionReaderUtils.generateBeanName(linkDiscovererBeanDefinition, registry)), registry);
            }
        }
        if (types.contains(EnableHypermediaSupport.HypermediaType.HAL) && JACKSON2_PRESENT) {
            BeanDefinitionBuilder halQueryMapperBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) ObjectMapper.class);
            registerSourcedBeanDefinition(halQueryMapperBuilder, metadata, registry, HAL_OBJECT_MAPPER_BEAN_NAME);
            BeanDefinitionBuilder customizerBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) DefaultObjectMapperCustomizer.class);
            registerSourcedBeanDefinition(customizerBeanDefinition, metadata, registry);
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) Jackson2ModuleRegisteringBeanPostProcessor.class);
            registerSourcedBeanDefinition(builder, metadata, registry);
        }
        if (!types.isEmpty()) {
            BeanDefinitionBuilder linkDiscoverersRegistryBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) PluginRegistryFactoryBean.class);
            linkDiscoverersRegistryBuilder.addPropertyValue("type", LinkDiscoverer.class);
            registerSourcedBeanDefinition(linkDiscoverersRegistryBuilder, metadata, registry, LINK_DISCOVERER_REGISTRY_BEAN_NAME);
            BeanDefinitionBuilder linkDiscoverersBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) LinkDiscoverers.class);
            linkDiscoverersBuilder.addConstructorArgReference(LINK_DISCOVERER_REGISTRY_BEAN_NAME);
            registerSourcedBeanDefinition(linkDiscoverersBuilder, metadata, registry);
        }
        registerRelProviderPluginRegistryAndDelegate(registry);
    }

    private static void registerRelProviderPluginRegistryAndDelegate(BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        Class<?> defaultRelProviderType = EVO_PRESENT ? EvoInflectorRelProvider.class : DefaultRelProvider.class;
        RootBeanDefinition defaultRelProviderBeanDefinition = new RootBeanDefinition(defaultRelProviderType);
        registry.registerBeanDefinition("defaultRelProvider", defaultRelProviderBeanDefinition);
        RootBeanDefinition annotationRelProviderBeanDefinition = new RootBeanDefinition((Class<?>) AnnotationRelProvider.class);
        registry.registerBeanDefinition("annotationRelProvider", annotationRelProviderBeanDefinition);
        BeanDefinitionBuilder registryFactoryBeanBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) PluginRegistryFactoryBean.class);
        registryFactoryBeanBuilder.addPropertyValue("type", RelProvider.class);
        registryFactoryBeanBuilder.addPropertyValue("exclusions", DelegatingRelProvider.class);
        AbstractBeanDefinition registryBeanDefinition = registryFactoryBeanBuilder.getBeanDefinition();
        registry.registerBeanDefinition("relProviderPluginRegistry", registryBeanDefinition);
        BeanDefinitionBuilder delegateBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) DelegatingRelProvider.class);
        delegateBuilder.addConstructorArgValue(registryBeanDefinition);
        AbstractBeanDefinition beanDefinition = delegateBuilder.getBeanDefinition();
        beanDefinition.setPrimary(true);
        registry.registerBeanDefinition(DELEGATING_REL_PROVIDER_BEAN_NAME, beanDefinition);
    }

    private AbstractBeanDefinition getLinkDiscovererBeanDefinition(EnableHypermediaSupport.HypermediaType type) {
        switch (type) {
            case HAL:
                AbstractBeanDefinition definition = new RootBeanDefinition((Class<?>) HalLinkDiscoverer.class);
                definition.setSource(this);
                return definition;
            default:
                throw new IllegalStateException(String.format("Unsupported hypermedia type %s!", type));
        }
    }

    private static String registerSourcedBeanDefinition(BeanDefinitionBuilder builder, AnnotationMetadata metadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        String generateBeanName = BeanDefinitionReaderUtils.generateBeanName(beanDefinition, registry);
        return registerSourcedBeanDefinition(builder, metadata, registry, generateBeanName);
    }

    private static String registerSourcedBeanDefinition(BeanDefinitionBuilder builder, AnnotationMetadata metadata, BeanDefinitionRegistry registry, String name) throws BeanDefinitionStoreException {
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        beanDefinition.setSource(metadata);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, name);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
        return name;
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/HypermediaSupportBeanDefinitionRegistrar$Jackson2ModuleRegisteringBeanPostProcessor.class */
    static class Jackson2ModuleRegisteringBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
        private AutowireCapableBeanFactory beanFactory;

        Jackson2ModuleRegisteringBeanPostProcessor() {
        }

        @Override // org.springframework.beans.factory.BeanFactoryAware
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            Assert.isInstanceOf(AutowireCapableBeanFactory.class, beanFactory, "BeanFactory must be an AutowireCapableBeanFactory!");
            this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof RequestMappingHandlerAdapter) {
                RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
                adapter.setMessageConverters(potentiallyRegisterModule(adapter.getMessageConverters()));
            }
            if (bean instanceof RequestMappingHandlerAdapter) {
                RequestMappingHandlerAdapter adapter2 = (RequestMappingHandlerAdapter) bean;
                adapter2.setMessageConverters(potentiallyRegisterModule(adapter2.getMessageConverters()));
            }
            if (bean instanceof RestTemplate) {
                RestTemplate template = (RestTemplate) bean;
                template.setMessageConverters(potentiallyRegisterModule(template.getMessageConverters()));
            }
            return bean;
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        private List<HttpMessageConverter<?>> potentiallyRegisterModule(List<HttpMessageConverter<?>> converters) {
            for (HttpMessageConverter<?> converter : converters) {
                if (converter instanceof MappingJackson2HttpMessageConverter) {
                    MappingJackson2HttpMessageConverter halConverterCandidate = (MappingJackson2HttpMessageConverter) converter;
                    ObjectMapper objectMapper = halConverterCandidate.getObjectMapper();
                    if (Jackson2HalModule.isAlreadyRegisteredIn(objectMapper)) {
                        return converters;
                    }
                }
            }
            CurieProvider curieProvider = getCurieProvider(this.beanFactory);
            RelProvider relProvider = (RelProvider) this.beanFactory.getBean(HypermediaSupportBeanDefinitionRegistrar.DELEGATING_REL_PROVIDER_BEAN_NAME, RelProvider.class);
            ObjectMapper halObjectMapper = (ObjectMapper) this.beanFactory.getBean(HypermediaSupportBeanDefinitionRegistrar.HAL_OBJECT_MAPPER_BEAN_NAME, ObjectMapper.class);
            MessageSourceAccessor linkRelationMessageSource = (MessageSourceAccessor) this.beanFactory.getBean(HypermediaSupportBeanDefinitionRegistrar.MESSAGE_SOURCE_BEAN_NAME, MessageSourceAccessor.class);
            halObjectMapper.registerModule(new Jackson2HalModule());
            halObjectMapper.setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(relProvider, curieProvider, linkRelationMessageSource, this.beanFactory));
            TypeConstrainedMappingJackson2HttpMessageConverter typeConstrainedMappingJackson2HttpMessageConverter = new TypeConstrainedMappingJackson2HttpMessageConverter(ResourceSupport.class);
            typeConstrainedMappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
            typeConstrainedMappingJackson2HttpMessageConverter.setObjectMapper(halObjectMapper);
            List<HttpMessageConverter<?>> result = new ArrayList<>(converters.size());
            result.add(typeConstrainedMappingJackson2HttpMessageConverter);
            result.addAll(converters);
            return result;
        }

        private static CurieProvider getCurieProvider(BeanFactory factory) {
            try {
                return (CurieProvider) factory.getBean(CurieProvider.class);
            } catch (NoSuchBeanDefinitionException e) {
                return null;
            }
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/HypermediaSupportBeanDefinitionRegistrar$DefaultObjectMapperCustomizer.class */
    private static class DefaultObjectMapperCustomizer implements BeanPostProcessor {
        private DefaultObjectMapperCustomizer() {
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (!HypermediaSupportBeanDefinitionRegistrar.HAL_OBJECT_MAPPER_BEAN_NAME.equals(beanName)) {
                return bean;
            }
            ObjectMapper mapper = (ObjectMapper) bean;
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            return mapper;
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
    }
}
