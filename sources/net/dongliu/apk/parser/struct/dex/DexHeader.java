package net.dongliu.apk.parser.struct.dex;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/dex/DexHeader.class */
public class DexHeader {
    public static final int kSHA1DigestLen = 20;
    public static final int kSHA1DigestOutputLen = 41;
    private int version;
    private byte[] signature;
    private long fileSize;
    private long headerSize;
    private long linkSize;
    private long linkOff;
    private long mapOff;
    private int stringIdsSize;
    private long stringIdsOff;
    private int typeIdsSize;
    private long typeIdsOff;
    private int protoIdsSize;
    private long protoIdsOff;
    private int fieldIdsSize;
    private long fieldIdsOff;
    private int methodIdsSize;
    private long methodIdsOff;
    private int classDefsSize;
    private long classDefsOff;
    private int dataSize;
    private long dataOff;

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getHeaderSize() {
        return this.headerSize;
    }

    public void setHeaderSize(long headerSize) {
        this.headerSize = headerSize;
    }

    public long getLinkSize() {
        return this.linkSize;
    }

    public void setLinkSize(long linkSize) {
        this.linkSize = linkSize;
    }

    public long getLinkOff() {
        return this.linkOff;
    }

    public void setLinkOff(long linkOff) {
        this.linkOff = linkOff;
    }

    public long getMapOff() {
        return this.mapOff;
    }

    public void setMapOff(long mapOff) {
        this.mapOff = mapOff;
    }

    public int getStringIdsSize() {
        return this.stringIdsSize;
    }

    public void setStringIdsSize(int stringIdsSize) {
        this.stringIdsSize = stringIdsSize;
    }

    public long getStringIdsOff() {
        return this.stringIdsOff;
    }

    public void setStringIdsOff(long stringIdsOff) {
        this.stringIdsOff = stringIdsOff;
    }

    public int getTypeIdsSize() {
        return this.typeIdsSize;
    }

    public void setTypeIdsSize(int typeIdsSize) {
        this.typeIdsSize = typeIdsSize;
    }

    public long getTypeIdsOff() {
        return this.typeIdsOff;
    }

    public void setTypeIdsOff(long typeIdsOff) {
        this.typeIdsOff = typeIdsOff;
    }

    public int getProtoIdsSize() {
        return this.protoIdsSize;
    }

    public void setProtoIdsSize(int protoIdsSize) {
        this.protoIdsSize = protoIdsSize;
    }

    public long getProtoIdsOff() {
        return this.protoIdsOff;
    }

    public void setProtoIdsOff(long protoIdsOff) {
        this.protoIdsOff = protoIdsOff;
    }

    public int getFieldIdsSize() {
        return this.fieldIdsSize;
    }

    public void setFieldIdsSize(int fieldIdsSize) {
        this.fieldIdsSize = fieldIdsSize;
    }

    public long getFieldIdsOff() {
        return this.fieldIdsOff;
    }

    public void setFieldIdsOff(long fieldIdsOff) {
        this.fieldIdsOff = fieldIdsOff;
    }

    public int getMethodIdsSize() {
        return this.methodIdsSize;
    }

    public void setMethodIdsSize(int methodIdsSize) {
        this.methodIdsSize = methodIdsSize;
    }

    public long getMethodIdsOff() {
        return this.methodIdsOff;
    }

    public void setMethodIdsOff(long methodIdsOff) {
        this.methodIdsOff = methodIdsOff;
    }

    public int getClassDefsSize() {
        return this.classDefsSize;
    }

    public void setClassDefsSize(int classDefsSize) {
        this.classDefsSize = classDefsSize;
    }

    public long getClassDefsOff() {
        return this.classDefsOff;
    }

    public void setClassDefsOff(long classDefsOff) {
        this.classDefsOff = classDefsOff;
    }

    public int getDataSize() {
        return this.dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public long getDataOff() {
        return this.dataOff;
    }

    public void setDataOff(long dataOff) {
        this.dataOff = dataOff;
    }
}
