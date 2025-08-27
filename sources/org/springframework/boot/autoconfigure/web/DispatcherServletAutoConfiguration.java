package org.springframework.boot.autoconfigure.web;

import java.util.Arrays;
import java.util.List;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ConditionalOnClass({DispatcherServlet.class})
@AutoConfigureAfter({EmbeddedServletContainerAutoConfiguration.class})
@AutoConfigureOrder(Integer.MIN_VALUE)
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/DispatcherServletAutoConfiguration.class */
public class DispatcherServletAutoConfiguration {
    public static final String DEFAULT_DISPATCHER_SERVLET_BEAN_NAME = "dispatcherServlet";
    public static final String DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME = "dispatcherServletRegistration";

    @EnableConfigurationProperties({WebMvcProperties.class})
    @Configuration
    @ConditionalOnClass({ServletRegistration.class})
    @Conditional({DefaultDispatcherServletCondition.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/DispatcherServletAutoConfiguration$DispatcherServletConfiguration.class */
    protected static class DispatcherServletConfiguration {
        private final WebMvcProperties webMvcProperties;

        public DispatcherServletConfiguration(WebMvcProperties webMvcProperties) {
            this.webMvcProperties = webMvcProperties;
        }

        @Bean(name = {"dispatcherServlet"})
        public DispatcherServlet dispatcherServlet() {
            DispatcherServlet dispatcherServlet = new DispatcherServlet();
            dispatcherServlet.setDispatchOptionsRequest(this.webMvcProperties.isDispatchOptionsRequest());
            dispatcherServlet.setDispatchTraceRequest(this.webMvcProperties.isDispatchTraceRequest());
            dispatcherServlet.setThrowExceptionIfNoHandlerFound(this.webMvcProperties.isThrowExceptionIfNoHandlerFound());
            return dispatcherServlet;
        }

        @ConditionalOnMissingBean(name = {DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME})
        @ConditionalOnBean({MultipartResolver.class})
        @Bean
        public MultipartResolver multipartResolver(MultipartResolver resolver) {
            return resolver;
        }
    }

    @EnableConfigurationProperties({WebMvcProperties.class})
    @Configuration
    @ConditionalOnClass({ServletRegistration.class})
    @Conditional({DispatcherServletRegistrationCondition.class})
    @Import({DispatcherServletConfiguration.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/DispatcherServletAutoConfiguration$DispatcherServletRegistrationConfiguration.class */
    protected static class DispatcherServletRegistrationConfiguration {
        private final ServerProperties serverProperties;
        private final WebMvcProperties webMvcProperties;
        private final MultipartConfigElement multipartConfig;

        public DispatcherServletRegistrationConfiguration(ServerProperties serverProperties, WebMvcProperties webMvcProperties, ObjectProvider<MultipartConfigElement> multipartConfigProvider) {
            this.serverProperties = serverProperties;
            this.webMvcProperties = webMvcProperties;
            this.multipartConfig = multipartConfigProvider.getIfAvailable();
        }

        @ConditionalOnBean(value = {DispatcherServlet.class}, name = {"dispatcherServlet"})
        @Bean(name = {DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME})
        public ServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet) {
            ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet, this.serverProperties.getServletMapping());
            registration.setName("dispatcherServlet");
            registration.setLoadOnStartup(this.webMvcProperties.getServlet().getLoadOnStartup());
            if (this.multipartConfig != null) {
                registration.setMultipartConfig(this.multipartConfig);
            }
            return registration;
        }
    }

    @Order(2147483637)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/DispatcherServletAutoConfiguration$DefaultDispatcherServletCondition.class */
    private static class DefaultDispatcherServletCondition extends SpringBootCondition {
        private DefaultDispatcherServletCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("Default DispatcherServlet", new Object[0]);
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            List<String> dispatchServletBeans = Arrays.asList(beanFactory.getBeanNamesForType(DispatcherServlet.class, false, false));
            if (dispatchServletBeans.contains("dispatcherServlet")) {
                return ConditionOutcome.noMatch(message.found("dispatcher servlet bean").items("dispatcherServlet"));
            }
            if (beanFactory.containsBean("dispatcherServlet")) {
                return ConditionOutcome.noMatch(message.found("non dispatcher servlet bean").items("dispatcherServlet"));
            }
            if (dispatchServletBeans.isEmpty()) {
                return ConditionOutcome.match(message.didNotFind("dispatcher servlet beans").atAll());
            }
            return ConditionOutcome.match(message.found("dispatcher servlet bean", "dispatcher servlet beans").items(ConditionMessage.Style.QUOTE, dispatchServletBeans).append("and none is named dispatcherServlet"));
        }
    }

    @Order(2147483637)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/DispatcherServletAutoConfiguration$DispatcherServletRegistrationCondition.class */
    private static class DispatcherServletRegistrationCondition extends SpringBootCondition {
        private DispatcherServletRegistrationCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            ConditionOutcome outcome = checkDefaultDispatcherName(beanFactory);
            if (!outcome.isMatch()) {
                return outcome;
            }
            return checkServletRegistration(beanFactory);
        }

        private ConditionOutcome checkDefaultDispatcherName(ConfigurableListableBeanFactory beanFactory) {
            List<String> servlets = Arrays.asList(beanFactory.getBeanNamesForType(DispatcherServlet.class, false, false));
            boolean containsDispatcherBean = beanFactory.containsBean("dispatcherServlet");
            if (containsDispatcherBean && !servlets.contains("dispatcherServlet")) {
                return ConditionOutcome.noMatch(startMessage().found("non dispatcher servlet").items("dispatcherServlet"));
            }
            return ConditionOutcome.match();
        }

        private ConditionOutcome checkServletRegistration(ConfigurableListableBeanFactory beanFactory) {
            ConditionMessage.Builder message = startMessage();
            List<String> registrations = Arrays.asList(beanFactory.getBeanNamesForType(ServletRegistrationBean.class, false, false));
            boolean containsDispatcherRegistrationBean = beanFactory.containsBean(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
            if (registrations.isEmpty()) {
                if (containsDispatcherRegistrationBean) {
                    return ConditionOutcome.noMatch(message.found("non servlet registration bean").items(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME));
                }
                return ConditionOutcome.match(message.didNotFind("servlet registration bean").atAll());
            }
            if (registrations.contains(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)) {
                return ConditionOutcome.noMatch(message.found("servlet registration bean").items(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME));
            }
            if (containsDispatcherRegistrationBean) {
                return ConditionOutcome.noMatch(message.found("non servlet registration bean").items(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME));
            }
            return ConditionOutcome.match(message.found("servlet registration beans").items(ConditionMessage.Style.QUOTE, registrations).append("and none is named dispatcherServletRegistration"));
        }

        private ConditionMessage.Builder startMessage() {
            return ConditionMessage.forCondition("DispatcherServlet Registration", new Object[0]);
        }
    }
}
