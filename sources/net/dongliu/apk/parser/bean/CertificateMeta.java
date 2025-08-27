package net.dongliu.apk.parser.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/bean/CertificateMeta.class */
public class CertificateMeta {
    private String signAlgorithm;
    private String signAlgorithmOID;
    private Date startDate;
    private Date endDate;
    private byte[] data;
    private String certBase64Md5;
    private String certMd5;

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getCertBase64Md5() {
        return this.certBase64Md5;
    }

    public void setCertBase64Md5(String certBase64Md5) {
        this.certBase64Md5 = certBase64Md5;
    }

    public String getCertMd5() {
        return this.certMd5;
    }

    public void setCertMd5(String certMd5) {
        this.certMd5 = certMd5;
    }

    public String getSignAlgorithm() {
        return this.signAlgorithm;
    }

    public void setSignAlgorithm(String signAlgorithm) {
        this.signAlgorithm = signAlgorithm;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSignAlgorithmOID() {
        return this.signAlgorithmOID;
    }

    public void setSignAlgorithmOID(String signAlgorithmOID) {
        this.signAlgorithmOID = signAlgorithmOID;
    }

    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "{signAlgorithm=" + this.signAlgorithm + ", certBase64Md5=" + this.certBase64Md5 + ", startDate=" + df.format(this.startDate) + ",endDate=" + df.format(this.endDate) + "}";
    }
}
