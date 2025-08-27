package org.springframework.cache.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/NameMatchCacheOperationSource.class */
public class NameMatchCacheOperationSource implements CacheOperationSource, Serializable {
    protected static final Log logger = LogFactory.getLog(NameMatchCacheOperationSource.class);
    private Map<String, Collection<CacheOperation>> nameMap = new LinkedHashMap();

    public void setNameMap(Map<String, Collection<CacheOperation>> nameMap) {
        for (Map.Entry<String, Collection<CacheOperation>> entry : nameMap.entrySet()) {
            addCacheMethod(entry.getKey(), entry.getValue());
        }
    }

    public void addCacheMethod(String methodName, Collection<CacheOperation> ops) {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding method [" + methodName + "] with cache operations [" + ops + "]");
        }
        this.nameMap.put(methodName, ops);
    }

    @Override // org.springframework.cache.interceptor.CacheOperationSource
    public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) {
        String methodName = method.getName();
        Collection<CacheOperation> ops = this.nameMap.get(methodName);
        if (ops == null) {
            String bestNameMatch = null;
            for (String mappedName : this.nameMap.keySet()) {
                if (isMatch(methodName, mappedName) && (bestNameMatch == null || bestNameMatch.length() <= mappedName.length())) {
                    ops = this.nameMap.get(mappedName);
                    bestNameMatch = mappedName;
                }
            }
        }
        return ops;
    }

    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NameMatchCacheOperationSource)) {
            return false;
        }
        NameMatchCacheOperationSource otherTas = (NameMatchCacheOperationSource) other;
        return ObjectUtils.nullSafeEquals(this.nameMap, otherTas.nameMap);
    }

    public int hashCode() {
        return NameMatchCacheOperationSource.class.hashCode();
    }

    public String toString() {
        return getClass().getName() + ": " + this.nameMap;
    }
}
