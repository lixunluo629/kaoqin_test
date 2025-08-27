package springfox.documentation.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import springfox.documentation.schema.configuration.ObjectMapperConfigured;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/ObjectMapperConfigurer.class */
public class ObjectMapperConfigurer implements BeanPostProcessor, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RequestMappingHandlerAdapter) {
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            adapter.setMessageConverters(configureMessageConverters(adapter.getMessageConverters()));
        }
        return bean;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override // org.springframework.context.ApplicationEventPublisherAware
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private List<HttpMessageConverter<?>> configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Iterable<MappingJackson2HttpMessageConverter> jackson2Converters = jackson2Converters(converters);
        if (Iterables.size(jackson2Converters) > 0) {
            for (MappingJackson2HttpMessageConverter each : jackson2Converters) {
                fireObjectMapperConfiguredEvent(each.getObjectMapper());
            }
        } else {
            converters.add(configuredMessageConverter());
        }
        return Lists.newArrayList(converters);
    }

    private Iterable<MappingJackson2HttpMessageConverter> jackson2Converters(List<HttpMessageConverter<?>> messageConverters) {
        return FluentIterable.from(messageConverters).filter(MappingJackson2HttpMessageConverter.class);
    }

    private MappingJackson2HttpMessageConverter configuredMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        messageConverter.setObjectMapper(objectMapper);
        fireObjectMapperConfiguredEvent(objectMapper);
        return messageConverter;
    }

    private void fireObjectMapperConfiguredEvent(ObjectMapper objectMapper) {
        this.applicationEventPublisher.publishEvent((ApplicationEvent) new ObjectMapperConfigured(this, objectMapper));
    }
}
