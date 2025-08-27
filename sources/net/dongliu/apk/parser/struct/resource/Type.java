package net.dongliu.apk.parser.struct.resource;

import java.nio.ByteBuffer;
import java.util.Locale;
import net.dongliu.apk.parser.struct.StringPool;
import net.dongliu.apk.parser.utils.Buffers;
import net.dongliu.apk.parser.utils.ParseUtils;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/Type.class */
public class Type {
    private String name;
    private short id;
    private Locale locale;
    private StringPool keyStringPool;
    private ByteBuffer buffer;
    private long[] offsets;
    private StringPool stringPool;
    private int density;

    public Type(TypeHeader header) {
        this.id = header.getId();
        ResTableConfig config = header.getConfig();
        this.locale = new Locale(config.getLanguage(), config.getCountry());
        this.density = config.getDensity();
    }

    public ResourceEntry getResourceEntry(int id) {
        if (id >= this.offsets.length || this.offsets[id] == 4294967295L) {
            return null;
        }
        Buffers.position(this.buffer, this.offsets[id]);
        return readResourceEntry();
    }

    private ResourceEntry readResourceEntry() {
        long beginPos = this.buffer.position();
        ResourceEntry resourceEntry = new ResourceEntry();
        resourceEntry.setSize(Buffers.readUShort(this.buffer));
        resourceEntry.setFlags(Buffers.readUShort(this.buffer));
        long keyRef = this.buffer.getInt();
        String key = this.keyStringPool.get((int) keyRef);
        resourceEntry.setKey(key);
        if ((resourceEntry.getFlags() & 1) != 0) {
            ResourceMapEntry resourceMapEntry = new ResourceMapEntry(resourceEntry);
            resourceMapEntry.setParent(Buffers.readUInt(this.buffer));
            resourceMapEntry.setCount(Buffers.readUInt(this.buffer));
            Buffers.position(this.buffer, beginPos + resourceEntry.getSize());
            ResourceTableMap[] resourceTableMaps = new ResourceTableMap[(int) resourceMapEntry.getCount()];
            for (int i = 0; i < resourceMapEntry.getCount(); i++) {
                resourceTableMaps[i] = readResourceTableMap();
            }
            resourceMapEntry.setResourceTableMaps(resourceTableMaps);
            return resourceMapEntry;
        }
        Buffers.position(this.buffer, beginPos + resourceEntry.getSize());
        resourceEntry.setValue(ParseUtils.readResValue(this.buffer, this.stringPool));
        return resourceEntry;
    }

    private ResourceTableMap readResourceTableMap() {
        ResourceTableMap resourceTableMap = new ResourceTableMap();
        resourceTableMap.setNameRef(Buffers.readUInt(this.buffer));
        resourceTableMap.setResValue(ParseUtils.readResValue(this.buffer, this.stringPool));
        if ((resourceTableMap.getNameRef() & 33554432) == 0 && (resourceTableMap.getNameRef() & 16777216) != 0) {
        }
        return resourceTableMap;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getId() {
        return this.id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public StringPool getKeyStringPool() {
        return this.keyStringPool;
    }

    public void setKeyStringPool(StringPool keyStringPool) {
        this.keyStringPool = keyStringPool;
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public long[] getOffsets() {
        return this.offsets;
    }

    public void setOffsets(long[] offsets) {
        this.offsets = offsets;
    }

    public StringPool getStringPool() {
        return this.stringPool;
    }

    public void setStringPool(StringPool stringPool) {
        this.stringPool = stringPool;
    }

    public int getDensity() {
        return this.density;
    }

    public String toString() {
        return "Type{name='" + this.name + "', id=" + ((int) this.id) + ", locale=" + this.locale + '}';
    }
}
