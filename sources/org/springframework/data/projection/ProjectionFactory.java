package org.springframework.data.projection;

import java.util.List;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/ProjectionFactory.class */
public interface ProjectionFactory {
    <T> T createProjection(Class<T> cls, Object obj);

    <T> T createProjection(Class<T> cls);

    @Deprecated
    List<String> getInputProperties(Class<?> cls);

    ProjectionInformation getProjectionInformation(Class<?> cls);
}
