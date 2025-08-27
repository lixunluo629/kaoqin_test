package org.springframework.boot.autoconfigure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.AbstractMessageBrokerConfiguration;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@ConditionalOnClass({WebSocketMessageBrokerConfigurer.class})
@ConditionalOnWebApplication
@AutoConfigureAfter({JacksonAutoConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/WebSocketMessagingAutoConfiguration.class */
public class WebSocketMessagingAutoConfiguration {

    @Configuration
    @ConditionalOnBean({DelegatingWebSocketMessageBrokerConfiguration.class, ObjectMapper.class})
    @ConditionalOnClass({ObjectMapper.class, AbstractMessageBrokerConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/WebSocketMessagingAutoConfiguration$WebSocketMessageConverterConfiguration.class */
    static class WebSocketMessageConverterConfiguration extends AbstractWebSocketMessageBrokerConfigurer {
        private final ObjectMapper objectMapper;

        WebSocketMessageConverterConfiguration(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        public void registerStompEndpoints(StompEndpointRegistry registry) {
        }

        public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
            MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
            converter.setObjectMapper(this.objectMapper);
            DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
            resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
            converter.setContentTypeResolver(resolver);
            messageConverters.add(new StringMessageConverter());
            messageConverters.add(new ByteArrayMessageConverter());
            messageConverters.add(converter);
            return false;
        }
    }
}
