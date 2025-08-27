package org.springframework.plugin.core.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.plugin.core.Plugin;

@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PluginRegistriesBeanDefinitionRegistrar.class})
/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/config/EnablePluginRegistries.class */
public @interface EnablePluginRegistries {
    Class<? extends Plugin<?>>[] value();
}
