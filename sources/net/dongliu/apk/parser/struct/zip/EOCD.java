package net.dongliu.apk.parser.struct.zip;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/zip/EOCD.class */
public class EOCD {
    public static final int SIGNATURE = 101010256;
    private short diskNum;
    private short cdStartDisk;
    private short cdRecordNum;
    private short totalCDRecordNum;
    private int cdSize;
    private int cdStart;
    private short commentLen;

    public short getDiskNum() {
        return this.diskNum;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = (short) diskNum;
    }

    public int getCdStartDisk() {
        return this.cdStartDisk & 65535;
    }

    public void setCdStartDisk(int cdStartDisk) {
        this.cdStartDisk = (short) cdStartDisk;
    }

    public int getCdRecordNum() {
        return this.cdRecordNum & 65535;
    }

    public void setCdRecordNum(int cdRecordNum) {
        this.cdRecordNum = (short) cdRecordNum;
    }

    public int getTotalCDRecordNum() {
        return this.totalCDRecordNum & 65535;
    }

    public void setTotalCDRecordNum(int totalCDRecordNum) {
        this.totalCDRecordNum = (short) totalCDRecordNum;
    }

    public long getCdSize() {
        return this.cdSize & 4294967295L;
    }

    public void setCdSize(long cdSize) {
        this.cdSize = (int) cdSize;
    }

    public long getCdStart() {
        return this.cdStart & 4294967295L;
    }

    public void setCdStart(long cdStart) {
        this.cdStart = (int) cdStart;
    }

    public int getCommentLen() {
        return this.commentLen & 65535;
    }

    public void setCommentLen(int commentLen) {
        this.commentLen = (short) commentLen;
    }
}
