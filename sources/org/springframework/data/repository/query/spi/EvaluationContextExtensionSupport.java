package org.springframework.data.repository.query.spi;

import java.util.Collections;
import java.util.Map;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/spi/EvaluationContextExtensionSupport.class */
public abstract class EvaluationContextExtensionSupport implements EvaluationContextExtension {
    @Override // org.springframework.data.repository.query.spi.EvaluationContextExtension
    public Map<String, Object> getProperties() {
        return Collections.emptyMap();
    }

    @Override // org.springframework.data.repository.query.spi.EvaluationContextExtension
    public Map<String, Function> getFunctions() {
        return Collections.emptyMap();
    }

    @Override // org.springframework.data.repository.query.spi.EvaluationContextExtension
    public Object getRootObject() {
        return null;
    }
}
