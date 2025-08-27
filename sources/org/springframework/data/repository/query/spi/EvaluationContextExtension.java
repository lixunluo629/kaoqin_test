package org.springframework.data.repository.query.spi;

import java.util.Map;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/query/spi/EvaluationContextExtension.class */
public interface EvaluationContextExtension {
    String getExtensionId();

    Map<String, Object> getProperties();

    Map<String, Function> getFunctions();

    Object getRootObject();
}
