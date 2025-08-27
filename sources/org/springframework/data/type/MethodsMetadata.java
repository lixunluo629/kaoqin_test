package org.springframework.data.type;

import java.util.Set;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/type/MethodsMetadata.class */
public interface MethodsMetadata extends ClassMetadata {
    Set<MethodMetadata> getMethods();

    Set<MethodMetadata> getMethods(String str);
}
