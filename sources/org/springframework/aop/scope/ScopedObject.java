package org.springframework.aop.scope;

import org.springframework.aop.RawTargetAccess;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/scope/ScopedObject.class */
public interface ScopedObject extends RawTargetAccess {
    Object getTargetObject();

    void removeFromScope();
}
