package org.springframework.boot.autoconfigure.thymeleaf;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import java.lang.reflect.Method;
import java.util.Collection;
import javax.servlet.Servlet;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.conditionalcomments.dialect.ConditionalCommentsDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@EnableConfigurationProperties({ThymeleafProperties.class})
@Configuration
@ConditionalOnClass({SpringTemplateEngine.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration.class */
public class ThymeleafAutoConfiguration {

    @ConditionalOnMissingClass({"org.thymeleaf.templatemode.TemplateMode"})
    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$Thymeleaf2Configuration.class */
    static class Thymeleaf2Configuration {
        Thymeleaf2Configuration() {
        }

        @ConditionalOnMissingBean(name = {"defaultTemplateResolver"})
        @Configuration
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$Thymeleaf2Configuration$DefaultTemplateResolverConfiguration.class */
        static class DefaultTemplateResolverConfiguration extends AbstractTemplateResolverConfiguration {
            DefaultTemplateResolverConfiguration(ThymeleafProperties properties, ApplicationContext applicationContext) {
                super(properties, applicationContext);
            }

            @Bean
            public SpringResourceResourceResolver thymeleafResourceResolver() {
                return new SpringResourceResourceResolver();
            }
        }

        @Configuration
        @ConditionalOnClass({Servlet.class})
        @ConditionalOnWebApplication
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$Thymeleaf2Configuration$Thymeleaf2ViewResolverConfiguration.class */
        static class Thymeleaf2ViewResolverConfiguration extends AbstractThymeleafViewResolverConfiguration {
            Thymeleaf2ViewResolverConfiguration(ThymeleafProperties properties, SpringTemplateEngine templateEngine) {
                super(properties, templateEngine);
            }

            @Override // org.springframework.boot.autoconfigure.thymeleaf.AbstractThymeleafViewResolverConfiguration
            protected void configureTemplateEngine(ThymeleafViewResolver resolver, SpringTemplateEngine templateEngine) {
                resolver.setTemplateEngine(templateEngine);
            }
        }

        @Configuration
        @ConditionalOnClass({ConditionalCommentsDialect.class})
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$Thymeleaf2Configuration$ThymeleafConditionalCommentsDialectConfiguration.class */
        static class ThymeleafConditionalCommentsDialectConfiguration {
            ThymeleafConditionalCommentsDialectConfiguration() {
            }

            @ConditionalOnMissingBean
            @Bean
            public ConditionalCommentsDialect conditionalCommentsDialect() {
                return new ConditionalCommentsDialect();
            }
        }
    }

    @Configuration
    @ConditionalOnClass(name = {"org.thymeleaf.templatemode.TemplateMode"})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$Thymeleaf3Configuration.class */
    static class Thymeleaf3Configuration {
        Thymeleaf3Configuration() {
        }

        @ConditionalOnMissingBean(name = {"defaultTemplateResolver"})
        @Configuration
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$Thymeleaf3Configuration$DefaultTemplateResolverConfiguration.class */
        static class DefaultTemplateResolverConfiguration extends AbstractTemplateResolverConfiguration {
            DefaultTemplateResolverConfiguration(ThymeleafProperties properties, ApplicationContext applicationContext) {
                super(properties, applicationContext);
            }

            @Override // org.springframework.boot.autoconfigure.thymeleaf.AbstractTemplateResolverConfiguration
            @Bean
            public SpringResourceTemplateResolver defaultTemplateResolver() {
                SpringResourceTemplateResolver resolver = super.defaultTemplateResolver();
                Method setCheckExistence = ReflectionUtils.findMethod(resolver.getClass(), "setCheckExistence", Boolean.TYPE);
                ReflectionUtils.invokeMethod(setCheckExistence, resolver, Boolean.valueOf(getProperties().isCheckTemplate()));
                return resolver;
            }
        }

        @Configuration
        @ConditionalOnClass({Servlet.class})
        @ConditionalOnWebApplication
        /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$Thymeleaf3Configuration$Thymeleaf3ViewResolverConfiguration.class */
        static class Thymeleaf3ViewResolverConfiguration extends AbstractThymeleafViewResolverConfiguration {
            Thymeleaf3ViewResolverConfiguration(ThymeleafProperties properties, SpringTemplateEngine templateEngine) {
                super(properties, templateEngine);
            }

            @Override // org.springframework.boot.autoconfigure.thymeleaf.AbstractThymeleafViewResolverConfiguration
            protected void configureTemplateEngine(ThymeleafViewResolver resolver, SpringTemplateEngine templateEngine) {
                try {
                    Method setTemplateEngine = ReflectionUtils.findMethod(resolver.getClass(), "setTemplateEngine", Class.forName("org.thymeleaf.ITemplateEngine", true, resolver.getClass().getClassLoader()));
                    ReflectionUtils.invokeMethod(setTemplateEngine, resolver, templateEngine);
                } catch (ClassNotFoundException ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }
    }

    @ConditionalOnMissingBean({SpringTemplateEngine.class})
    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafDefaultConfiguration.class */
    protected static class ThymeleafDefaultConfiguration {
        private final Collection<ITemplateResolver> templateResolvers;
        private final Collection<IDialect> dialects;

        public ThymeleafDefaultConfiguration(Collection<ITemplateResolver> templateResolvers, ObjectProvider<Collection<IDialect>> dialectsProvider) {
            this.templateResolvers = templateResolvers;
            this.dialects = dialectsProvider.getIfAvailable();
        }

        @Bean
        public SpringTemplateEngine templateEngine() {
            SpringTemplateEngine engine = new SpringTemplateEngine();
            for (ITemplateResolver templateResolver : this.templateResolvers) {
                engine.addTemplateResolver(templateResolver);
            }
            if (!CollectionUtils.isEmpty((Collection<?>) this.dialects)) {
                for (IDialect dialect : this.dialects) {
                    engine.addDialect(dialect);
                }
            }
            return engine;
        }
    }

    @Configuration
    @ConditionalOnClass(name = {"nz.net.ultraq.thymeleaf.LayoutDialect"})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafWebLayoutConfiguration.class */
    protected static class ThymeleafWebLayoutConfiguration {
        protected ThymeleafWebLayoutConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        public LayoutDialect layoutDialect() {
            return new LayoutDialect();
        }
    }

    @Configuration
    @ConditionalOnClass({DataAttributeDialect.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$DataAttributeDialectConfiguration.class */
    protected static class DataAttributeDialectConfiguration {
        protected DataAttributeDialectConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        public DataAttributeDialect dialect() {
            return new DataAttributeDialect();
        }
    }

    @Configuration
    @ConditionalOnClass({SpringSecurityDialect.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafSecurityDialectConfiguration.class */
    protected static class ThymeleafSecurityDialectConfiguration {
        protected ThymeleafSecurityDialectConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        public SpringSecurityDialect securityDialect() {
            return new SpringSecurityDialect();
        }
    }

    @Configuration
    @ConditionalOnClass({Java8TimeDialect.class})
    @ConditionalOnJava(ConditionalOnJava.JavaVersion.EIGHT)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafJava8TimeDialect.class */
    protected static class ThymeleafJava8TimeDialect {
        protected ThymeleafJava8TimeDialect() {
        }

        @ConditionalOnMissingBean
        @Bean
        public Java8TimeDialect java8TimeDialect() {
            return new Java8TimeDialect();
        }
    }

    @Configuration
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafResourceHandlingConfig.class */
    protected static class ThymeleafResourceHandlingConfig {
        protected ThymeleafResourceHandlingConfig() {
        }

        @ConditionalOnMissingBean
        @ConditionalOnEnabledResourceChain
        @Bean
        public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
            return new ResourceUrlEncodingFilter();
        }
    }
}
