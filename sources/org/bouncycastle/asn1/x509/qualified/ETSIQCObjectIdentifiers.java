package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/qualified/ETSIQCObjectIdentifiers.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/qualified/ETSIQCObjectIdentifiers.class */
public interface ETSIQCObjectIdentifiers {
    public static final ASN1ObjectIdentifier id_etsi_qcs = new ASN1ObjectIdentifier("0.4.0.1862.1");
    public static final ASN1ObjectIdentifier id_etsi_qcs_QcCompliance = id_etsi_qcs.branch("1");
    public static final ASN1ObjectIdentifier id_etsi_qcs_LimiteValue = id_etsi_qcs.branch("2");
    public static final ASN1ObjectIdentifier id_etsi_qcs_RetentionPeriod = id_etsi_qcs.branch("3");
    public static final ASN1ObjectIdentifier id_etsi_qcs_QcSSCD = id_etsi_qcs.branch("4");
}
