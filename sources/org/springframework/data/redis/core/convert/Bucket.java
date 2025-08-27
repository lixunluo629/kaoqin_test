package org.springframework.data.redis.core.convert;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/Bucket.class */
public class Bucket {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    private final Map<String, byte[]> data;

    public Bucket() {
        this.data = new LinkedHashMap();
    }

    Bucket(Map<String, byte[]> data) {
        Assert.notNull(data, "Inital data must not be null!");
        this.data = new LinkedHashMap(data.size());
        this.data.putAll(data);
    }

    public void put(String path, byte[] value) {
        Assert.hasText(path, "Path to property must not be null or empty.");
        this.data.put(path, value);
    }

    public byte[] get(String path) {
        Assert.hasText(path, "Path to property must not be null or empty.");
        return this.data.get(path);
    }

    public Set<Map.Entry<String, byte[]>> entrySet() {
        return this.data.entrySet();
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public int size() {
        return this.data.size();
    }

    public Collection<byte[]> values() {
        return this.data.values();
    }

    public Set<String> keySet() {
        return this.data.keySet();
    }

    public Map<String, byte[]> asMap() {
        return Collections.unmodifiableMap(this.data);
    }

    public Bucket extract(String prefix) {
        Bucket partial = new Bucket();
        for (Map.Entry<String, byte[]> entry : this.data.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                partial.put(entry.getKey(), entry.getValue());
            }
        }
        return partial;
    }

    public Set<String> extractAllKeysFor(String path) {
        if (!StringUtils.hasText(path)) {
            return keySet();
        }
        Pattern pattern = Pattern.compile("^(" + Pattern.quote(path) + ")\\.\\[.*?\\]");
        Set<String> keys = new LinkedHashSet<>();
        for (Map.Entry<String, byte[]> entry : this.data.entrySet()) {
            Matcher matcher = pattern.matcher(entry.getKey());
            if (matcher.find()) {
                keys.add(matcher.group());
            }
        }
        return keys;
    }

    public Map<byte[], byte[]> rawMap() {
        Map<byte[], byte[]> raw = new LinkedHashMap<>(this.data.size());
        for (Map.Entry<String, byte[]> entry : this.data.entrySet()) {
            if (entry.getValue() != null) {
                raw.put(entry.getKey().getBytes(CHARSET), entry.getValue());
            }
        }
        return raw;
    }

    public static Bucket newBucketFromRawMap(Map<byte[], byte[]> source) {
        Bucket bucket = new Bucket();
        if (source == null) {
            return bucket;
        }
        for (Map.Entry<byte[], byte[]> entry : source.entrySet()) {
            bucket.put(new String(entry.getKey(), CHARSET), entry.getValue());
        }
        return bucket;
    }

    public static Bucket newBucketFromStringMap(Map<String, String> source) {
        Bucket bucket = new Bucket();
        if (source == null) {
            return bucket;
        }
        for (Map.Entry<String, String> entry : source.entrySet()) {
            bucket.put(entry.getKey(), StringUtils.hasText(entry.getValue()) ? entry.getValue().getBytes(CHARSET) : new byte[0]);
        }
        return bucket;
    }

    public String toString() {
        return "Bucket [data=" + safeToString() + "]";
    }

    private String safeToString() {
        Map<String, String> serialized = new LinkedHashMap<>();
        for (Map.Entry<String, byte[]> entry : this.data.entrySet()) {
            if (entry.getValue() != null) {
                serialized.put(entry.getKey(), toUtf8String(entry.getValue()));
            } else {
                serialized.put(entry.getKey(), null);
            }
        }
        return serialized.toString();
    }

    private String toUtf8String(byte[] raw) {
        try {
            return new String(raw, CHARSET);
        } catch (Exception e) {
            return null;
        }
    }
}
