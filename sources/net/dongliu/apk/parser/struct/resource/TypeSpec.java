package net.dongliu.apk.parser.struct.resource;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/resource/TypeSpec.class */
public class TypeSpec {
    private long[] entryFlags;
    private String name;
    private short id;

    public TypeSpec(TypeSpecHeader header) {
        this.id = header.getId();
    }

    public boolean exists(int id) {
        return id < this.entryFlags.length;
    }

    public long[] getEntryFlags() {
        return this.entryFlags;
    }

    public void setEntryFlags(long[] entryFlags) {
        this.entryFlags = entryFlags;
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

    public String toString() {
        return "TypeSpec{name='" + this.name + "', id=" + ((int) this.id) + '}';
    }
}
