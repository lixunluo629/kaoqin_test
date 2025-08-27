package org.bouncycastle.jce.provider;

import java.util.Date;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/provider/CertStatus.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/CertStatus.class */
class CertStatus {
    public static final int UNREVOKED = 11;
    public static final int UNDETERMINED = 12;
    int certStatus = 11;
    Date revocationDate = null;

    CertStatus() {
    }

    public Date getRevocationDate() {
        return this.revocationDate;
    }

    public void setRevocationDate(Date date) {
        this.revocationDate = date;
    }

    public int getCertStatus() {
        return this.certStatus;
    }

    public void setCertStatus(int i) {
        this.certStatus = i;
    }
}
