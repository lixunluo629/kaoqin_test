package org.bouncycastle.asn1.misc;

import org.apache.tomcat.websocket.Constants;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/misc/MiscObjectIdentifiers.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/misc/MiscObjectIdentifiers.class */
public interface MiscObjectIdentifiers {
    public static final ASN1ObjectIdentifier netscape = new ASN1ObjectIdentifier("2.16.840.1.113730.1");
    public static final ASN1ObjectIdentifier netscapeCertType = netscape.branch("1");
    public static final ASN1ObjectIdentifier netscapeBaseURL = netscape.branch("2");
    public static final ASN1ObjectIdentifier netscapeRevocationURL = netscape.branch("3");
    public static final ASN1ObjectIdentifier netscapeCARevocationURL = netscape.branch("4");
    public static final ASN1ObjectIdentifier netscapeRenewalURL = netscape.branch("7");
    public static final ASN1ObjectIdentifier netscapeCApolicyURL = netscape.branch("8");
    public static final ASN1ObjectIdentifier netscapeSSLServerName = netscape.branch("12");
    public static final ASN1ObjectIdentifier netscapeCertComment = netscape.branch(Constants.WS_VERSION_HEADER_VALUE);
    public static final ASN1ObjectIdentifier verisign = new ASN1ObjectIdentifier("2.16.840.1.113733.1");
    public static final ASN1ObjectIdentifier verisignCzagExtension = verisign.branch("6.3");
    public static final ASN1ObjectIdentifier verisignDnbDunsNumber = verisign.branch("6.15");
    public static final ASN1ObjectIdentifier novell = new ASN1ObjectIdentifier("2.16.840.1.113719");
    public static final ASN1ObjectIdentifier novellSecurityAttribs = novell.branch("1.9.4.1");
    public static final ASN1ObjectIdentifier entrust = new ASN1ObjectIdentifier("1.2.840.113533.7");
    public static final ASN1ObjectIdentifier entrustVersionExtension = entrust.branch("65.0");
}
