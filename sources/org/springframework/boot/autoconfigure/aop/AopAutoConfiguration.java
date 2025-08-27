package org.springframework.boot.autoconfigure.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.Advice;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ConditionalOnClass({EnableAspectJAutoProxy.class, Aspect.class, Advice.class})
@ConditionalOnProperty(prefix = "spring.aop", name = {"auto"}, havingValue = "true", matchIfMissing = true)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/aop/AopAutoConfiguration.class */
public class AopAutoConfiguration {

    @EnableAspectJAutoProxy(proxyTargetClass = true)
    @Configuration
    @ConditionalOnProperty(prefix = "spring.aop", name = {AopNamespaceUtils.PROXY_TARGET_CLASS_ATTRIBUTE}, havingValue = "true", matchIfMissing = false)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/aop/AopAutoConfiguration$CglibAutoProxyConfiguration.class */
    public static class CglibAutoProxyConfiguration {
    }

    @EnableAspectJAutoProxy(proxyTargetClass = false)
    @Configuration
    @ConditionalOnProperty(prefix = "spring.aop", name = {AopNamespaceUtils.PROXY_TARGET_CLASS_ATTRIBUTE}, havingValue = "false", matchIfMissing = true)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/aop/AopAutoConfiguration$JdkDynamicAutoProxyConfiguration.class */
    public static class JdkDynamicAutoProxyConfiguration {
    }
}
