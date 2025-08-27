package org.springframework.boot.autoconfigure.webservices;

import java.util.Map;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurationSupport;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

@EnableConfigurationProperties({WebServicesProperties.class})
@Configuration
@ConditionalOnClass({MessageDispatcherServlet.class})
@AutoConfigureAfter({EmbeddedServletContainerAutoConfiguration.class})
@ConditionalOnMissingBean({WsConfigurationSupport.class})
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/webservices/WebServicesAutoConfiguration.class */
public class WebServicesAutoConfiguration {
    private final WebServicesProperties properties;

    public WebServicesAutoConfiguration(WebServicesProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        String path = this.properties.getPath();
        String urlMapping = path.endsWith("/") ? path + "*" : path + ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER;
        ServletRegistrationBean registration = new ServletRegistrationBean(servlet, urlMapping);
        WebServicesProperties.Servlet servletProperties = this.properties.getServlet();
        registration.setLoadOnStartup(servletProperties.getLoadOnStartup());
        for (Map.Entry<String, String> entry : servletProperties.getInit().entrySet()) {
            registration.addInitParameter(entry.getKey(), entry.getValue());
        }
        return registration;
    }

    @Configuration
    @EnableWs
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/webservices/WebServicesAutoConfiguration$WsConfiguration.class */
    protected static class WsConfiguration {
        protected WsConfiguration() {
        }
    }
}
