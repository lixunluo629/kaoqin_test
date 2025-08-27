package org.springframework.boot.liquibase;

import java.io.IOException;
import liquibase.servicelocator.DefaultPackageScanClassResolver;
import org.apache.commons.logging.Log;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/liquibase/SpringPackageScanClassResolver.class */
public class SpringPackageScanClassResolver extends DefaultPackageScanClassResolver {
    private final Log logger;

    public SpringPackageScanClassResolver(Log logger) {
        this.logger = logger;
    }

    protected void findAllClasses(String packageName, ClassLoader loader) {
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(loader);
        try {
            Resource[] resources = scan(loader, packageName);
            for (Resource resource : resources) {
                Class<?> clazz = loadClass(loader, metadataReaderFactory, resource);
                if (clazz != null) {
                    addFoundClass(clazz);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private Resource[] scan(ClassLoader loader, String packageName) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(loader);
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class";
        Resource[] resources = resolver.getResources(pattern);
        return resources;
    }

    private Class<?> loadClass(ClassLoader loader, MetadataReaderFactory readerFactory, Resource resource) {
        try {
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            return ClassUtils.forName(reader.getClassMetadata().getClassName(), loader);
        } catch (ClassNotFoundException ex) {
            handleFailure(resource, ex);
            return null;
        } catch (LinkageError ex2) {
            handleFailure(resource, ex2);
            return null;
        } catch (Throwable ex3) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Unexpected failure when loading class resource " + resource, ex3);
                return null;
            }
            return null;
        }
    }

    private void handleFailure(Resource resource, Throwable ex) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Ignoring candidate class resource " + resource + " due to " + ex);
        }
    }
}
