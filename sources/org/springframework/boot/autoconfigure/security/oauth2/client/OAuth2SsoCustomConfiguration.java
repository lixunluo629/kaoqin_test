package org.springframework.boot.autoconfigure.security.oauth2.client;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

@Configuration
@Conditional({EnableOAuth2SsoCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2SsoCustomConfiguration.class */
public class OAuth2SsoCustomConfiguration implements ImportAware, BeanPostProcessor, ApplicationContextAware {
    private Class<?> configType;
    private ApplicationContext applicationContext;

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override // org.springframework.context.annotation.ImportAware
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.configType = ClassUtils.resolveClassName(importMetadata.getClassName(), null);
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (this.configType.isAssignableFrom(bean.getClass()) && (bean instanceof WebSecurityConfigurerAdapter)) {
            ProxyFactory factory = new ProxyFactory();
            factory.setTarget(bean);
            factory.addAdvice(new SsoSecurityAdapter(this.applicationContext));
            bean = factory.getProxy();
        }
        return bean;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2SsoCustomConfiguration$SsoSecurityAdapter.class */
    private static class SsoSecurityAdapter implements MethodInterceptor {
        private SsoSecurityConfigurer configurer;

        SsoSecurityAdapter(ApplicationContext applicationContext) {
            this.configurer = new SsoSecurityConfigurer(applicationContext);
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(MethodInvocation invocation) throws Exception {
            if (invocation.getMethod().getName().equals("init")) {
                Method method = ReflectionUtils.findMethod(WebSecurityConfigurerAdapter.class, "getHttp");
                ReflectionUtils.makeAccessible(method);
                HttpSecurity http = (HttpSecurity) ReflectionUtils.invokeMethod(method, invocation.getThis());
                this.configurer.configure(http);
            }
            return invocation.proceed();
        }
    }
}
