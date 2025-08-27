package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/parsing/NullSourceExtractor.class */
public class NullSourceExtractor implements SourceExtractor {
    @Override // org.springframework.beans.factory.parsing.SourceExtractor
    public Object extractSource(Object sourceCandidate, Resource definitionResource) {
        return null;
    }
}
