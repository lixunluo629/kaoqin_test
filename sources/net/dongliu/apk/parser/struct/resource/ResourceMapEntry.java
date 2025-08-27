package net.dongliu.apk.parser.struct.resource;

import java.util.Arrays;
import java.util.Locale;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/ResourceMapEntry.class */
public class ResourceMapEntry extends ResourceEntry {
    private long parent;
    private long count;
    private ResourceTableMap[] resourceTableMaps;

    public ResourceMapEntry(ResourceEntry resourceEntry) {
        setSize(resourceEntry.getSize());
        setFlags(resourceEntry.getFlags());
        setKey(resourceEntry.getKey());
    }

    public long getParent() {
        return this.parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public ResourceTableMap[] getResourceTableMaps() {
        return this.resourceTableMaps;
    }

    public void setResourceTableMaps(ResourceTableMap[] resourceTableMaps) {
        this.resourceTableMaps = resourceTableMaps;
    }

    @Override // net.dongliu.apk.parser.struct.resource.ResourceEntry
    public String toStringValue(ResourceTable resourceTable, Locale locale) {
        if (this.resourceTableMaps.length > 0) {
            return this.resourceTableMaps[0].toString();
        }
        return null;
    }

    @Override // net.dongliu.apk.parser.struct.resource.ResourceEntry
    public String toString() {
        return "ResourceMapEntry{parent=" + this.parent + ", count=" + this.count + ", resourceTableMaps=" + Arrays.toString(this.resourceTableMaps) + '}';
    }
}
