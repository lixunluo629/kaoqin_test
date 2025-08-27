package org.aspectj.weaver.tools;

import java.util.HashMap;
import java.util.Map;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/DefaultMatchingContext.class */
public class DefaultMatchingContext implements MatchingContext {
    private Map contextMap = new HashMap();

    @Override // org.aspectj.weaver.tools.MatchingContext
    public boolean hasContextBinding(String contextParameterName) {
        return this.contextMap.containsKey(contextParameterName);
    }

    @Override // org.aspectj.weaver.tools.MatchingContext
    public Object getBinding(String contextParameterName) {
        return this.contextMap.get(contextParameterName);
    }

    public void addContextBinding(String name, Object value) {
        this.contextMap.put(name, value);
    }

    public void removeContextBinding(String name) {
        this.contextMap.remove(name);
    }
}
