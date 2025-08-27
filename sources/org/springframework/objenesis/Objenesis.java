package org.springframework.objenesis;

import org.springframework.objenesis.instantiator.ObjectInstantiator;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/objenesis/Objenesis.class */
public interface Objenesis {
    <T> T newInstance(Class<T> cls);

    <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> cls);
}
