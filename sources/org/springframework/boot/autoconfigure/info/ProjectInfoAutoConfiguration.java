package org.springframework.boot.autoconfigure.info;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.IOException;
import java.util.Properties;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

@EnableConfigurationProperties({ProjectInfoProperties.class})
@Configuration
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/info/ProjectInfoAutoConfiguration.class */
public class ProjectInfoAutoConfiguration {
    private final ProjectInfoProperties properties;

    public ProjectInfoAutoConfiguration(ProjectInfoProperties properties) {
        this.properties = properties;
    }

    @ConditionalOnMissingBean
    @Conditional({GitResourceAvailableCondition.class})
    @Bean
    public GitProperties gitProperties() throws Exception {
        return new GitProperties(loadFrom(this.properties.getGit().getLocation(), "git"));
    }

    @ConditionalOnMissingBean
    @ConditionalOnResource(resources = {"${spring.info.build.location:classpath:META-INF/build-info.properties}"})
    @Bean
    public BuildProperties buildProperties() throws Exception {
        return new BuildProperties(loadFrom(this.properties.getBuild().getLocation(), JsonPOJOBuilder.DEFAULT_BUILD_METHOD));
    }

    protected Properties loadFrom(Resource location, String prefix) throws IOException {
        String p = prefix.endsWith(".") ? prefix : prefix + ".";
        Properties source = PropertiesLoaderUtils.loadProperties(location);
        Properties target = new Properties();
        for (String key : source.stringPropertyNames()) {
            if (key.startsWith(p)) {
                target.put(key.substring(p.length()), source.get(key));
            }
        }
        return target;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/info/ProjectInfoAutoConfiguration$GitResourceAvailableCondition.class */
    static class GitResourceAvailableCondition extends SpringBootCondition {
        private final ResourceLoader defaultResourceLoader = new DefaultResourceLoader();

        GitResourceAvailableCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ResourceLoader loader = context.getResourceLoader();
            if (loader == null) {
                loader = this.defaultResourceLoader;
            }
            PropertyResolver propertyResolver = context.getEnvironment();
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(propertyResolver, "spring.info.git.");
            String location = resolver.getProperty("location");
            if (location == null) {
                RelaxedPropertyResolver resolver2 = new RelaxedPropertyResolver(propertyResolver, "spring.git.");
                location = resolver2.getProperty("properties");
                if (location == null) {
                    location = "classpath:git.properties";
                }
            }
            ConditionMessage.Builder message = ConditionMessage.forCondition("GitResource", new Object[0]);
            return loader.getResource(location).exists() ? ConditionOutcome.match(message.found("git info at").items(location)) : ConditionOutcome.noMatch(message.didNotFind("git info at").items(location));
        }
    }
}
