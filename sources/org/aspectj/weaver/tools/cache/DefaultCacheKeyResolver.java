package org.aspectj.weaver.tools.cache;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/cache/DefaultCacheKeyResolver.class */
public class DefaultCacheKeyResolver implements CacheKeyResolver {
    public static final String GENERATED_SUFFIX = ".generated";
    public static final String WEAVED_SUFFIX = ".weaved";

    @Override // org.aspectj.weaver.tools.cache.CacheKeyResolver
    public String createClassLoaderScope(ClassLoader cl, List<String> aspects) {
        String name = cl != null ? cl.getClass().getSimpleName() : "unknown";
        List<String> hashableStrings = new LinkedList<>();
        StringBuilder hashable = new StringBuilder(256);
        if (cl != null && (cl instanceof URLClassLoader)) {
            URL[] urls = ((URLClassLoader) cl).getURLs();
            for (URL url : urls) {
                hashableStrings.add(url.toString());
            }
        }
        hashableStrings.addAll(aspects);
        Collections.sort(hashableStrings);
        for (String url2 : hashableStrings) {
            hashable.append(url2);
        }
        byte[] bytes = hashable.toString().getBytes();
        String hash = crc(bytes);
        return name + '.' + hash;
    }

    private String crc(byte[] input) {
        CRC32 crc32 = new CRC32();
        crc32.update(input);
        return String.valueOf(crc32.getValue());
    }

    @Override // org.aspectj.weaver.tools.cache.CacheKeyResolver
    public String getGeneratedRegex() {
        return ".*.generated";
    }

    @Override // org.aspectj.weaver.tools.cache.CacheKeyResolver
    public String getWeavedRegex() {
        return ".*.weaved";
    }

    @Override // org.aspectj.weaver.tools.cache.CacheKeyResolver
    public String keyToClass(String key) {
        if (key.endsWith(GENERATED_SUFFIX)) {
            return key.replaceAll(".generated$", "");
        }
        if (key.endsWith(WEAVED_SUFFIX)) {
            return key.replaceAll("\\.[^.]+.weaved", "");
        }
        return key;
    }

    @Override // org.aspectj.weaver.tools.cache.CacheKeyResolver
    public CachedClassReference weavedKey(String className, byte[] original_bytes) {
        String hash = crc(original_bytes);
        return new CachedClassReference(className + "." + hash + WEAVED_SUFFIX, className);
    }

    @Override // org.aspectj.weaver.tools.cache.CacheKeyResolver
    public CachedClassReference generatedKey(String className) {
        return new CachedClassReference(className + GENERATED_SUFFIX, className);
    }
}
