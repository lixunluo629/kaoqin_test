package org.springframework.context.annotation;

import java.util.List;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ProfileCondition.class */
class ProfileCondition implements Condition {
    ProfileCondition() {
    }

    @Override // org.springframework.context.annotation.Condition
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attrs;
        if (context.getEnvironment() != null && (attrs = metadata.getAllAnnotationAttributes(Profile.class.getName())) != null) {
            for (Object value : (List) attrs.get("value")) {
                if (context.getEnvironment().acceptsProfiles((String[]) value)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
