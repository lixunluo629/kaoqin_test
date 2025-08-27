package org.aspectj.weaver.tools.cache;

import org.aspectj.weaver.tools.GeneratedClassHandler;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/GeneratedCachedClassHandler.class */
public class GeneratedCachedClassHandler implements GeneratedClassHandler {
    private final WeavedClassCache cache;
    private final GeneratedClassHandler nextGeneratedClassHandler;

    public GeneratedCachedClassHandler(WeavedClassCache cache, GeneratedClassHandler nextHandler) {
        this.cache = cache;
        this.nextGeneratedClassHandler = nextHandler;
    }

    @Override // org.aspectj.weaver.tools.GeneratedClassHandler
    public void acceptClass(String name, byte[] originalBytes, byte[] wovenBytes) {
        CachedClassReference ref = this.cache.createGeneratedCacheKey(name.replace('/', '.'));
        this.cache.put(ref, originalBytes, wovenBytes);
        if (this.nextGeneratedClassHandler != null) {
            this.nextGeneratedClassHandler.acceptClass(name, originalBytes, wovenBytes);
        }
    }
}
