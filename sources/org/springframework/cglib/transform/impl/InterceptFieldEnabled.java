package org.springframework.cglib.transform.impl;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/transform/impl/InterceptFieldEnabled.class */
public interface InterceptFieldEnabled {
    void setInterceptFieldCallback(InterceptFieldCallback interceptFieldCallback);

    InterceptFieldCallback getInterceptFieldCallback();
}
