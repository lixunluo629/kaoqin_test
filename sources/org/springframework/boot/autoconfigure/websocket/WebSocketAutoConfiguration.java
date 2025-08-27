package org.springframework.boot.autoconfigure.websocket;

import io.undertow.websockets.jsr.Bootstrap;
import javax.servlet.Servlet;
import javax.websocket.server.ServerContainer;
import org.apache.catalina.startup.Tomcat;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfigureBefore({EmbeddedServletContainerAutoConfiguration.class})
@Configuration
@ConditionalOnClass({Servlet.class, ServerContainer.class})
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/WebSocketAutoConfiguration.class */
public class WebSocketAutoConfiguration {

    @Configuration
    @ConditionalOnClass(name = {"org.apache.tomcat.websocket.server.WsSci"}, value = {Tomcat.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/WebSocketAutoConfiguration$TomcatWebSocketConfiguration.class */
    static class TomcatWebSocketConfiguration {
        TomcatWebSocketConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"websocketContainerCustomizer"})
        @Bean
        @ConditionalOnJava(ConditionalOnJava.JavaVersion.SEVEN)
        public TomcatWebSocketContainerCustomizer websocketContainerCustomizer() {
            return new TomcatWebSocketContainerCustomizer();
        }
    }

    @Configuration
    @ConditionalOnClass({WebSocketServerContainerInitializer.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/WebSocketAutoConfiguration$JettyWebSocketConfiguration.class */
    static class JettyWebSocketConfiguration {
        JettyWebSocketConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"websocketContainerCustomizer"})
        @Bean
        public JettyWebSocketContainerCustomizer websocketContainerCustomizer() {
            return new JettyWebSocketContainerCustomizer();
        }
    }

    @Configuration
    @ConditionalOnClass({Bootstrap.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/WebSocketAutoConfiguration$UndertowWebSocketConfiguration.class */
    static class UndertowWebSocketConfiguration {
        UndertowWebSocketConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"websocketContainerCustomizer"})
        @Bean
        public UndertowWebSocketContainerCustomizer websocketContainerCustomizer() {
            return new UndertowWebSocketContainerCustomizer();
        }
    }
}
