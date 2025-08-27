package org.springframework.ui;

import java.util.Collection;
import java.util.Map;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ui/ExtendedModelMap.class */
public class ExtendedModelMap extends ModelMap implements Model {
    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public /* bridge */ /* synthetic */ ModelMap mergeAttributes(Map map) {
        return mergeAttributes((Map<String, ?>) map);
    }

    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public /* bridge */ /* synthetic */ ModelMap addAllAttributes(Map map) {
        return addAllAttributes((Map<String, ?>) map);
    }

    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public /* bridge */ /* synthetic */ ModelMap addAllAttributes(Collection collection) {
        return addAllAttributes((Collection<?>) collection);
    }

    @Override // org.springframework.ui.Model
    public /* bridge */ /* synthetic */ Model mergeAttributes(Map map) {
        return mergeAttributes((Map<String, ?>) map);
    }

    @Override // org.springframework.ui.Model
    public /* bridge */ /* synthetic */ Model addAllAttributes(Map map) {
        return addAllAttributes((Map<String, ?>) map);
    }

    @Override // org.springframework.ui.Model
    public /* bridge */ /* synthetic */ Model addAllAttributes(Collection collection) {
        return addAllAttributes((Collection<?>) collection);
    }

    @Override // org.springframework.ui.Model
    public ExtendedModelMap addAttribute(String attributeName, Object attributeValue) {
        super.addAttribute(attributeName, attributeValue);
        return this;
    }

    @Override // org.springframework.ui.Model
    public ExtendedModelMap addAttribute(Object attributeValue) {
        super.addAttribute(attributeValue);
        return this;
    }

    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public ExtendedModelMap addAllAttributes(Collection<?> attributeValues) {
        super.addAllAttributes(attributeValues);
        return this;
    }

    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public ExtendedModelMap addAllAttributes(Map<String, ?> attributes) {
        super.addAllAttributes(attributes);
        return this;
    }

    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public ExtendedModelMap mergeAttributes(Map<String, ?> attributes) {
        super.mergeAttributes(attributes);
        return this;
    }

    @Override // org.springframework.ui.Model
    public Map<String, Object> asMap() {
        return this;
    }
}
