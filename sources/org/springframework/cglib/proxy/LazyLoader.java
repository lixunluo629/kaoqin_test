package org.springframework.cglib.proxy;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/proxy/LazyLoader.class */
public interface LazyLoader extends Callback {
    Object loadObject() throws Exception;
}
