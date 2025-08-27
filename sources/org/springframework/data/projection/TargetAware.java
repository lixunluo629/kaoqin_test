package org.springframework.data.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.aop.RawTargetAccess;
import org.springframework.aop.TargetClassAware;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/TargetAware.class */
public interface TargetAware extends TargetClassAware, RawTargetAccess {
    @Override // org.springframework.aop.TargetClassAware
    @JsonIgnore
    Class<?> getTargetClass();

    @JsonIgnore
    Object getTarget();

    @JsonIgnore
    Class<?> getDecoratedClass();
}
