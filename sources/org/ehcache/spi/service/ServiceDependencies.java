package org.ehcache.spi.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/service/ServiceDependencies.class */
public @interface ServiceDependencies {
    Class<? extends Service>[] value();
}
