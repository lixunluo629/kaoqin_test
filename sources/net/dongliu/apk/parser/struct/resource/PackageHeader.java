package net.dongliu.apk.parser.struct.resource;

import net.dongliu.apk.parser.struct.ChunkHeader;
import net.dongliu.apk.parser.utils.Unsigned;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/PackageHeader.class */
public class PackageHeader extends ChunkHeader {
    private int id;
    private String name;
    private int typeStrings;
    private int lastPublicType;
    private int keyStrings;
    private int lastPublicKey;

    public PackageHeader(int headerSize, long chunkSize) {
        super(512, headerSize, chunkSize);
    }

    public long getId() {
        return Unsigned.toLong(this.id);
    }

    public void setId(long id) {
        this.id = Unsigned.toUInt(id);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeStrings() {
        return this.typeStrings;
    }

    public void setTypeStrings(long typeStrings) {
        this.typeStrings = Unsigned.ensureUInt(typeStrings);
    }

    public int getLastPublicType() {
        return this.lastPublicType;
    }

    public void setLastPublicType(long lastPublicType) {
        this.lastPublicType = Unsigned.ensureUInt(lastPublicType);
    }

    public int getKeyStrings() {
        return this.keyStrings;
    }

    public void setKeyStrings(long keyStrings) {
        this.keyStrings = Unsigned.ensureUInt(keyStrings);
    }

    public int getLastPublicKey() {
        return this.lastPublicKey;
    }

    public void setLastPublicKey(long lastPublicKey) {
        this.lastPublicKey = Unsigned.ensureUInt(lastPublicKey);
    }
}
