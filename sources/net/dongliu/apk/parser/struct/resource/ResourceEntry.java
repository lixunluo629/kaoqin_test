package net.dongliu.apk.parser.struct.resource;

import java.util.Locale;
import javax.annotation.Nullable;
import net.dongliu.apk.parser.struct.ResourceValue;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourceEntry.class */
public class ResourceEntry {
    private int size;
    public static final int FLAG_COMPLEX = 1;
    public static final int FLAG_PUBLIC = 2;
    private int flags;
    private String key;
    private ResourceValue value;

    public String toStringValue(ResourceTable resourceTable, Locale locale) {
        if (this.value != null) {
            return this.value.toStringValue(resourceTable, locale);
        }
        return "null";
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Nullable
    public ResourceValue getValue() {
        return this.value;
    }

    @Nullable
    public void setValue(@Nullable ResourceValue value) {
        this.value = value;
    }

    public String toString() {
        return "ResourceEntry{size=" + this.size + ", flags=" + this.flags + ", key='" + this.key + "', value=" + this.value + '}';
    }
}
