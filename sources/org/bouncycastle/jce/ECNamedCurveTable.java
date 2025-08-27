package org.bouncycastle.jce;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/ECNamedCurveTable.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/ECNamedCurveTable.class */
public class ECNamedCurveTable {
    public static ECNamedCurveParameterSpec getParameterSpec(String str) {
        X9ECParameters byName = X962NamedCurves.getByName(str);
        if (byName == null) {
            try {
                byName = X962NamedCurves.getByOID(new DERObjectIdentifier(str));
            } catch (IllegalArgumentException e) {
            }
        }
        if (byName == null) {
            byName = SECNamedCurves.getByName(str);
            if (byName == null) {
                try {
                    byName = SECNamedCurves.getByOID(new DERObjectIdentifier(str));
                } catch (IllegalArgumentException e2) {
                }
            }
        }
        if (byName == null) {
            byName = TeleTrusTNamedCurves.getByName(str);
            if (byName == null) {
                try {
                    byName = TeleTrusTNamedCurves.getByOID(new DERObjectIdentifier(str));
                } catch (IllegalArgumentException e3) {
                }
            }
        }
        if (byName == null) {
            byName = NISTNamedCurves.getByName(str);
        }
        if (byName == null) {
            return null;
        }
        return new ECNamedCurveParameterSpec(str, byName.getCurve(), byName.getG(), byName.getN(), byName.getH(), byName.getSeed());
    }

    public static Enumeration getNames() {
        Vector vector = new Vector();
        addEnumeration(vector, X962NamedCurves.getNames());
        addEnumeration(vector, SECNamedCurves.getNames());
        addEnumeration(vector, NISTNamedCurves.getNames());
        addEnumeration(vector, TeleTrusTNamedCurves.getNames());
        return vector.elements();
    }

    private static void addEnumeration(Vector vector, Enumeration enumeration) {
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
    }
}
