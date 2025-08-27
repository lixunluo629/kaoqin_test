package org.springframework.boot.autoconfigure.security;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@AutoConfigureAfter({SecurityAutoConfiguration.class})
@ConditionalOnMissingBean({WebSecurityConfiguration.class})
@ConditionalOnProperty(prefix = "security.basic", name = {"enabled"}, havingValue = "false")
@ConditionalOnWebApplication
@ConditionalOnClass({EnableWebSecurity.class})
@ConditionalOnBean({WebSecurityConfigurerAdapter.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/FallbackWebSecurityAutoConfiguration.class */
public class FallbackWebSecurityAutoConfiguration {
}
