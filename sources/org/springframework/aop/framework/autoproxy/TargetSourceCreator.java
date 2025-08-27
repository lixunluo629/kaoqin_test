package org.springframework.aop.framework.autoproxy;

import org.springframework.aop.TargetSource;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/autoproxy/TargetSourceCreator.class */
public interface TargetSourceCreator {
    TargetSource getTargetSource(Class<?> cls, String str);
}
