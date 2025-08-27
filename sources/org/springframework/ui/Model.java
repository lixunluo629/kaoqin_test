package org.springframework.ui;

import java.util.Collection;
import java.util.Map;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ui/Model.class */
public interface Model {
    Model addAttribute(String str, Object obj);

    Model addAttribute(Object obj);

    Model addAllAttributes(Collection<?> collection);

    Model addAllAttributes(Map<String, ?> map);

    Model mergeAttributes(Map<String, ?> map);

    boolean containsAttribute(String str);

    Map<String, Object> asMap();
}
