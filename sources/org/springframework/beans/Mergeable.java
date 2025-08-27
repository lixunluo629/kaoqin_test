package org.springframework.beans;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/Mergeable.class */
public interface Mergeable {
    boolean isMergeEnabled();

    Object merge(Object obj);
}
