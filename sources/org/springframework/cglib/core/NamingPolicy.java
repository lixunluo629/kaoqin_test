package org.springframework.cglib.core;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/core/NamingPolicy.class */
public interface NamingPolicy {
    String getClassName(String str, String str2, Object obj, Predicate predicate);

    boolean equals(Object obj);
}
