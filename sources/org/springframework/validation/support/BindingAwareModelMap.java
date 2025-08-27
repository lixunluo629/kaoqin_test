package org.springframework.validation.support;

import java.util.Map;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/support/BindingAwareModelMap.class */
public class BindingAwareModelMap extends ExtendedModelMap {
    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        removeBindingResultIfNecessary(key, value);
        return super.put((Object) key, value);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends String, ?> map) {
        for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
            removeBindingResultIfNecessary(entry.getKey(), entry.getValue());
        }
        super.putAll(map);
    }

    private void removeBindingResultIfNecessary(Object key, Object value) {
        if (key instanceof String) {
            String attributeName = (String) key;
            if (!attributeName.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
                String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + attributeName;
                BindingResult bindingResult = (BindingResult) get(bindingResultKey);
                if (bindingResult != null && bindingResult.getTarget() != value) {
                    remove(bindingResultKey);
                }
            }
        }
    }
}
