package org.springframework.boot.autoconfigure.social;

import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/social/SocialAutoConfigurerAdapter.class */
public abstract class SocialAutoConfigurerAdapter extends SocialConfigurerAdapter {
    protected abstract ConnectionFactory<?> createConnectionFactory();

    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }
}
