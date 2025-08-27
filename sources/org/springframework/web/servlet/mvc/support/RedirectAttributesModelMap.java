package org.springframework.web.servlet.mvc.support;

import java.util.Collection;
import java.util.Map;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.DataBinder;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/support/RedirectAttributesModelMap.class */
public class RedirectAttributesModelMap extends ModelMap implements RedirectAttributes {
    private final DataBinder dataBinder;
    private final ModelMap flashAttributes;

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

    @Override // org.springframework.web.servlet.mvc.support.RedirectAttributes, org.springframework.ui.Model
    public /* bridge */ /* synthetic */ RedirectAttributes mergeAttributes(Map map) {
        return mergeAttributes((Map<String, ?>) map);
    }

    @Override // org.springframework.web.servlet.mvc.support.RedirectAttributes, org.springframework.ui.Model
    public /* bridge */ /* synthetic */ RedirectAttributes addAllAttributes(Collection collection) {
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

    public RedirectAttributesModelMap() {
        this(null);
    }

    public RedirectAttributesModelMap(DataBinder dataBinder) {
        this.flashAttributes = new ModelMap();
        this.dataBinder = dataBinder;
    }

    @Override // org.springframework.web.servlet.mvc.support.RedirectAttributes
    public Map<String, ?> getFlashAttributes() {
        return this.flashAttributes;
    }

    @Override // org.springframework.ui.Model
    public RedirectAttributesModelMap addAttribute(String attributeName, Object attributeValue) {
        super.addAttribute(attributeName, (Object) formatValue(attributeValue));
        return this;
    }

    private String formatValue(Object value) {
        if (value == null) {
            return null;
        }
        return this.dataBinder != null ? (String) this.dataBinder.convertIfNecessary(value, String.class) : value.toString();
    }

    @Override // org.springframework.ui.Model
    public RedirectAttributesModelMap addAttribute(Object attributeValue) {
        super.addAttribute(attributeValue);
        return this;
    }

    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public RedirectAttributesModelMap addAllAttributes(Collection<?> attributeValues) {
        super.addAllAttributes(attributeValues);
        return this;
    }

    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public RedirectAttributesModelMap addAllAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            for (String key : attributes.keySet()) {
                addAttribute(key, attributes.get(key));
            }
        }
        return this;
    }

    @Override // org.springframework.ui.ModelMap, org.springframework.ui.Model
    public RedirectAttributesModelMap mergeAttributes(Map<String, ?> attributes) {
        if (attributes != null) {
            for (String key : attributes.keySet()) {
                if (!containsKey(key)) {
                    addAttribute(key, attributes.get(key));
                }
            }
        }
        return this;
    }

    @Override // org.springframework.ui.Model
    public Map<String, Object> asMap() {
        return this;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        return super.put((Object) key, (Object) formatValue(value));
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends String, ? extends Object> map) {
        if (map != null) {
            for (String key : map.keySet()) {
                put(key, (Object) formatValue(map.get(key)));
            }
        }
    }

    @Override // org.springframework.web.servlet.mvc.support.RedirectAttributes
    public RedirectAttributes addFlashAttribute(String attributeName, Object attributeValue) {
        this.flashAttributes.addAttribute(attributeName, attributeValue);
        return this;
    }

    @Override // org.springframework.web.servlet.mvc.support.RedirectAttributes
    public RedirectAttributes addFlashAttribute(Object attributeValue) {
        this.flashAttributes.addAttribute(attributeValue);
        return this;
    }
}
