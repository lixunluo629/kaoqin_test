package org.springframework.data.type.classreading;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/type/classreading/MethodsMetadataReaderFactory.class */
public class MethodsMetadataReaderFactory extends SimpleMetadataReaderFactory {
    public MethodsMetadataReaderFactory() {
    }

    public MethodsMetadataReaderFactory(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    public MethodsMetadataReaderFactory(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override // org.springframework.core.type.classreading.SimpleMetadataReaderFactory, org.springframework.core.type.classreading.MetadataReaderFactory
    public MethodsMetadataReader getMetadataReader(String className) throws IOException {
        return (MethodsMetadataReader) super.getMetadataReader(className);
    }

    @Override // org.springframework.core.type.classreading.SimpleMetadataReaderFactory, org.springframework.core.type.classreading.MetadataReaderFactory
    public MethodsMetadataReader getMetadataReader(Resource resource) throws IOException {
        return new DefaultMethodsMetadataReader(resource, getResourceLoader().getClassLoader());
    }
}
