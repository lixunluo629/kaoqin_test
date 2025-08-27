package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/SourceExtractor.class */
public interface SourceExtractor {
    Object extractSource(Object obj, Resource resource);
}
