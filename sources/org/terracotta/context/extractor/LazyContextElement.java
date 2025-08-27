package org.terracotta.context.extractor;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.terracotta.context.ContextElement;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extractor/LazyContextElement.class */
class LazyContextElement implements ContextElement {
    public final Class identifier;
    public final Map<? extends String, AttributeGetter<? extends Object>> attributes;

    public LazyContextElement(Class identifier, Map<? extends String, AttributeGetter<? extends Object>> attributes) {
        this.identifier = identifier;
        this.attributes = new HashMap(attributes);
    }

    @Override // org.terracotta.context.ContextElement
    public Class identifier() {
        return this.identifier;
    }

    @Override // org.terracotta.context.ContextElement
    public Map<String, Object> attributes() {
        Map<String, Object> realized = new HashMap<>();
        for (Map.Entry<? extends String, AttributeGetter<? extends Object>> e : this.attributes.entrySet()) {
            realized.put(e.getKey(), e.getValue().get());
        }
        return Collections.unmodifiableMap(realized);
    }

    public String toString() {
        return identifier() + SymbolConstants.SPACE_SYMBOL + attributes();
    }
}
