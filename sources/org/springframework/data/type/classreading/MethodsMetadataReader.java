package org.springframework.data.type.classreading;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.data.type.MethodsMetadata;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/type/classreading/MethodsMetadataReader.class */
public interface MethodsMetadataReader extends MetadataReader {
    MethodsMetadata getMethodsMetadata();
}
