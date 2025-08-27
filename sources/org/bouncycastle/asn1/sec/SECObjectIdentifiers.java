package org.bouncycastle.asn1.sec;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/sec/SECObjectIdentifiers.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/sec/SECObjectIdentifiers.class */
public interface SECObjectIdentifiers {
    public static final ASN1ObjectIdentifier ellipticCurve = new ASN1ObjectIdentifier("1.3.132.0");
    public static final ASN1ObjectIdentifier sect163k1 = ellipticCurve.branch("1");
    public static final ASN1ObjectIdentifier sect163r1 = ellipticCurve.branch("2");
    public static final ASN1ObjectIdentifier sect239k1 = ellipticCurve.branch("3");
    public static final ASN1ObjectIdentifier sect113r1 = ellipticCurve.branch("4");
    public static final ASN1ObjectIdentifier sect113r2 = ellipticCurve.branch("5");
    public static final ASN1ObjectIdentifier secp112r1 = ellipticCurve.branch("6");
    public static final ASN1ObjectIdentifier secp112r2 = ellipticCurve.branch("7");
    public static final ASN1ObjectIdentifier secp160r1 = ellipticCurve.branch("8");
    public static final ASN1ObjectIdentifier secp160k1 = ellipticCurve.branch("9");
    public static final ASN1ObjectIdentifier secp256k1 = ellipticCurve.branch("10");
    public static final ASN1ObjectIdentifier sect163r2 = ellipticCurve.branch("15");
    public static final ASN1ObjectIdentifier sect283k1 = ellipticCurve.branch("16");
    public static final ASN1ObjectIdentifier sect283r1 = ellipticCurve.branch("17");
    public static final ASN1ObjectIdentifier sect131r1 = ellipticCurve.branch("22");
    public static final ASN1ObjectIdentifier sect131r2 = ellipticCurve.branch("23");
    public static final ASN1ObjectIdentifier sect193r1 = ellipticCurve.branch("24");
    public static final ASN1ObjectIdentifier sect193r2 = ellipticCurve.branch("25");
    public static final ASN1ObjectIdentifier sect233k1 = ellipticCurve.branch("26");
    public static final ASN1ObjectIdentifier sect233r1 = ellipticCurve.branch("27");
    public static final ASN1ObjectIdentifier secp128r1 = ellipticCurve.branch("28");
    public static final ASN1ObjectIdentifier secp128r2 = ellipticCurve.branch("29");
    public static final ASN1ObjectIdentifier secp160r2 = ellipticCurve.branch(ANSIConstants.BLACK_FG);
    public static final ASN1ObjectIdentifier secp192k1 = ellipticCurve.branch(ANSIConstants.RED_FG);
    public static final ASN1ObjectIdentifier secp224k1 = ellipticCurve.branch(ANSIConstants.GREEN_FG);
    public static final ASN1ObjectIdentifier secp224r1 = ellipticCurve.branch(ANSIConstants.YELLOW_FG);
    public static final ASN1ObjectIdentifier secp384r1 = ellipticCurve.branch(ANSIConstants.BLUE_FG);
    public static final ASN1ObjectIdentifier secp521r1 = ellipticCurve.branch(ANSIConstants.MAGENTA_FG);
    public static final ASN1ObjectIdentifier sect409k1 = ellipticCurve.branch(ANSIConstants.CYAN_FG);
    public static final ASN1ObjectIdentifier sect409r1 = ellipticCurve.branch(ANSIConstants.WHITE_FG);
    public static final ASN1ObjectIdentifier sect571k1 = ellipticCurve.branch("38");
    public static final ASN1ObjectIdentifier sect571r1 = ellipticCurve.branch(ANSIConstants.DEFAULT_FG);
    public static final ASN1ObjectIdentifier secp192r1 = X9ObjectIdentifiers.prime192v1;
    public static final ASN1ObjectIdentifier secp256r1 = X9ObjectIdentifiers.prime256v1;
}
