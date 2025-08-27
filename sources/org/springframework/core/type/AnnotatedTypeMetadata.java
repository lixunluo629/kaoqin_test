package org.springframework.core.type;

import java.util.Map;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/type/AnnotatedTypeMetadata.class */
public interface AnnotatedTypeMetadata {
    boolean isAnnotated(String str);

    Map<String, Object> getAnnotationAttributes(String str);

    Map<String, Object> getAnnotationAttributes(String str, boolean z);

    MultiValueMap<String, Object> getAllAnnotationAttributes(String str);

    MultiValueMap<String, Object> getAllAnnotationAttributes(String str, boolean z);
}
