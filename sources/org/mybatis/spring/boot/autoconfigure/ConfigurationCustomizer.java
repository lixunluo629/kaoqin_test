package org.mybatis.spring.boot.autoconfigure;

import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-spring-boot-autoconfigure-1.3.2.jar:org/mybatis/spring/boot/autoconfigure/ConfigurationCustomizer.class */
public interface ConfigurationCustomizer {
    void customize(Configuration configuration);
}
