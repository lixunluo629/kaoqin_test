package org.springframework.cglib.transform.impl;

import org.springframework.asm.Type;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/transform/impl/InterceptFieldFilter.class */
public interface InterceptFieldFilter {
    boolean acceptRead(Type type, String str);

    boolean acceptWrite(Type type, String str);
}
