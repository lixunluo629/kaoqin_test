package org.springframework.core.type.classreading;

import java.io.IOException;
import org.springframework.core.io.Resource;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/type/classreading/MetadataReaderFactory.class */
public interface MetadataReaderFactory {
    MetadataReader getMetadataReader(String str) throws IOException;

    MetadataReader getMetadataReader(Resource resource) throws IOException;
}
