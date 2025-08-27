package org.terracotta.context;

import java.util.Map;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/ContextElement.class */
public interface ContextElement {
    Class identifier();

    Map<String, Object> attributes();
}
