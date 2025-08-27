package org.springframework.boot.autoconfigure.hateoas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.LinkDiscoverers;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.plugin.core.Plugin;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableConfigurationProperties({HateoasProperties.class})
@Configuration
@ConditionalOnClass({Resource.class, RequestMapping.class, Plugin.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class, RepositoryRestMvcAutoConfiguration.class})
@ConditionalOnWebApplication
@Import({HypermediaHttpMessageConverterConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hateoas/HypermediaAutoConfiguration.class */
public class HypermediaAutoConfiguration {

    @EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
    @Configuration
    @ConditionalOnClass({ObjectMapper.class})
    @ConditionalOnMissingBean({LinkDiscoverers.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hateoas/HypermediaAutoConfiguration$HypermediaConfiguration.class */
    protected static class HypermediaConfiguration {
        protected HypermediaConfiguration() {
        }

        @Bean
        public static HalObjectMapperConfigurer halObjectMapperConfigurer() {
            return new HalObjectMapperConfigurer();
        }
    }

    @ConditionalOnMissingBean({EntityLinks.class})
    @Configuration
    @EnableEntityLinks
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hateoas/HypermediaAutoConfiguration$EntityLinksConfiguration.class */
    protected static class EntityLinksConfiguration {
        protected EntityLinksConfiguration() {
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/hateoas/HypermediaAutoConfiguration$HalObjectMapperConfigurer.class */
    private static class HalObjectMapperConfigurer implements BeanPostProcessor, BeanFactoryAware {
        private BeanFactory beanFactory;

        private HalObjectMapperConfigurer() {
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if ((bean instanceof ObjectMapper) && "_halObjectMapper".equals(beanName)) {
                postProcessHalObjectMapper((ObjectMapper) bean);
            }
            return bean;
        }

        private void postProcessHalObjectMapper(ObjectMapper objectMapper) {
            try {
                Jackson2ObjectMapperBuilder builder = (Jackson2ObjectMapperBuilder) this.beanFactory.getBean(Jackson2ObjectMapperBuilder.class);
                builder.configure(objectMapper);
            } catch (NoSuchBeanDefinitionException e) {
            }
        }

        @Override // org.springframework.beans.factory.config.BeanPostProcessor
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override // org.springframework.beans.factory.BeanFactoryAware
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }
}
