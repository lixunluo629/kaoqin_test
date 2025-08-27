package org.springframework.data.repository.init;

import org.springframework.core.io.Resource;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/ResourceReader.class */
public interface ResourceReader {

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/ResourceReader$Type.class */
    public enum Type {
        XML,
        JSON
    }

    Object readFrom(Resource resource, ClassLoader classLoader) throws Exception;
}
