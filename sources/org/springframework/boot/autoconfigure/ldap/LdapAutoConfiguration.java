package org.springframework.boot.autoconfigure.ldap;

import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.LdapContextSource;

@EnableConfigurationProperties({LdapProperties.class})
@Configuration
@ConditionalOnClass({ContextSource.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/ldap/LdapAutoConfiguration.class */
public class LdapAutoConfiguration {
    private final LdapProperties properties;
    private final Environment environment;

    public LdapAutoConfiguration(LdapProperties properties, Environment environment) {
        this.properties = properties;
        this.environment = environment;
    }

    @ConditionalOnMissingBean
    @Bean
    public ContextSource ldapContextSource() {
        LdapContextSource source = new LdapContextSource();
        source.setUserDn(this.properties.getUsername());
        source.setPassword(this.properties.getPassword());
        source.setBase(this.properties.getBase());
        source.setUrls(this.properties.determineUrls(this.environment));
        source.setBaseEnvironmentProperties(Collections.unmodifiableMap(this.properties.getBaseEnvironment()));
        return source;
    }
}
