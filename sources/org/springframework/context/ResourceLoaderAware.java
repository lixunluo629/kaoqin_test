package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.io.ResourceLoader;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/ResourceLoaderAware.class */
public interface ResourceLoaderAware extends Aware {
    void setResourceLoader(ResourceLoader resourceLoader);
}
