package org.bouncycastle.jce;

import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/ECGOST3410NamedCurveTable.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/ECGOST3410NamedCurveTable.class */
public class ECGOST3410NamedCurveTable {
    public static ECNamedCurveParameterSpec getParameterSpec(String str) {
        ECDomainParameters byName = ECGOST3410NamedCurves.getByName(str);
        if (byName == null) {
            try {
                byName = ECGOST3410NamedCurves.getByOID(new DERObjectIdentifier(str));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        if (byName == null) {
            return null;
        }
        return new ECNamedCurveParameterSpec(str, byName.getCurve(), byName.getG(), byName.getN(), byName.getH(), byName.getSeed());
    }

    public static Enumeration getNames() {
        return ECGOST3410NamedCurves.getNames();
    }
}
