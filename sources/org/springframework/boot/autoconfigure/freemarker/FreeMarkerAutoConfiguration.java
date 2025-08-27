package org.springframework.boot.autoconfigure.freemarker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@EnableConfigurationProperties({FreeMarkerProperties.class})
@Configuration
@ConditionalOnClass({freemarker.template.Configuration.class, FreeMarkerConfigurationFactory.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/freemarker/FreeMarkerAutoConfiguration.class */
public class FreeMarkerAutoConfiguration {
    private static final Log logger = LogFactory.getLog(FreeMarkerAutoConfiguration.class);
    private final ApplicationContext applicationContext;
    private final FreeMarkerProperties properties;

    public FreeMarkerAutoConfiguration(ApplicationContext applicationContext, FreeMarkerProperties properties) {
        this.applicationContext = applicationContext;
        this.properties = properties;
    }

    @PostConstruct
    public void checkTemplateLocationExists() {
        if (this.properties.isCheckTemplateLocation()) {
            TemplateLocation templatePathLocation = null;
            List<TemplateLocation> locations = new ArrayList<>();
            String[] templateLoaderPath = this.properties.getTemplateLoaderPath();
            int length = templateLoaderPath.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String templateLoaderPath2 = templateLoaderPath[i];
                TemplateLocation location = new TemplateLocation(templateLoaderPath2);
                locations.add(location);
                if (!location.exists(this.applicationContext)) {
                    i++;
                } else {
                    templatePathLocation = location;
                    break;
                }
            }
            if (templatePathLocation == null) {
                logger.warn("Cannot find template location(s): " + locations + " (please add some templates, check your FreeMarker configuration, or set spring.freemarker.checkTemplateLocation=false)");
            }
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/freemarker/FreeMarkerAutoConfiguration$FreeMarkerConfiguration.class */
    protected static class FreeMarkerConfiguration {

        @Autowired
        protected FreeMarkerProperties properties;

        protected FreeMarkerConfiguration() {
        }

        protected void applyProperties(FreeMarkerConfigurationFactory factory) {
            factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
            factory.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
            factory.setDefaultEncoding(this.properties.getCharsetName());
            Properties settings = new Properties();
            settings.putAll(this.properties.getSettings());
            factory.setFreemarkerSettings(settings);
        }
    }

    @Configuration
    @ConditionalOnNotWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/freemarker/FreeMarkerAutoConfiguration$FreeMarkerNonWebConfiguration.class */
    public static class FreeMarkerNonWebConfiguration extends FreeMarkerConfiguration {
        @ConditionalOnMissingBean
        @Bean
        public FreeMarkerConfigurationFactoryBean freeMarkerConfiguration() {
            FreeMarkerConfigurationFactoryBean freeMarkerFactoryBean = new FreeMarkerConfigurationFactoryBean();
            applyProperties(freeMarkerFactoryBean);
            return freeMarkerFactoryBean;
        }
    }

    @Configuration
    @ConditionalOnClass({Servlet.class, FreeMarkerConfigurer.class})
    @ConditionalOnWebApplication
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/freemarker/FreeMarkerAutoConfiguration$FreeMarkerWebConfiguration.class */
    public static class FreeMarkerWebConfiguration extends FreeMarkerConfiguration {
        @ConditionalOnMissingBean({FreeMarkerConfig.class})
        @Bean
        public FreeMarkerConfigurer freeMarkerConfigurer() {
            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
            applyProperties(configurer);
            return configurer;
        }

        @Bean
        public freemarker.template.Configuration freeMarkerConfiguration(FreeMarkerConfig configurer) {
            return configurer.getConfiguration();
        }

        @ConditionalOnMissingBean(name = {"freeMarkerViewResolver"})
        @ConditionalOnProperty(name = {"spring.freemarker.enabled"}, matchIfMissing = true)
        @Bean
        public FreeMarkerViewResolver freeMarkerViewResolver() {
            FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
            this.properties.applyToViewResolver(resolver);
            return resolver;
        }

        @ConditionalOnMissingBean
        @ConditionalOnEnabledResourceChain
        @Bean
        public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
            return new ResourceUrlEncodingFilter();
        }
    }
}
