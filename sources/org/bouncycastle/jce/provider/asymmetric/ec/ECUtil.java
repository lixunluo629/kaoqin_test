package org.bouncycastle.jce.provider.asymmetric.ec;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.jce.provider.ProviderUtil;
import org.bouncycastle.jce.spec.ECParameterSpec;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/asymmetric/ec/ECUtil.class */
public class ECUtil {
    static int[] convertMidTerms(int[] iArr) {
        int[] iArr2 = new int[3];
        if (iArr.length == 1) {
            iArr2[0] = iArr[0];
        } else {
            if (iArr.length != 3) {
                throw new IllegalArgumentException("Only Trinomials and pentanomials supported");
            }
            if (iArr[0] < iArr[1] && iArr[0] < iArr[2]) {
                iArr2[0] = iArr[0];
                if (iArr[1] < iArr[2]) {
                    iArr2[1] = iArr[1];
                    iArr2[2] = iArr[2];
                } else {
                    iArr2[1] = iArr[2];
                    iArr2[2] = iArr[1];
                }
            } else if (iArr[1] < iArr[2]) {
                iArr2[0] = iArr[1];
                if (iArr[0] < iArr[2]) {
                    iArr2[1] = iArr[0];
                    iArr2[2] = iArr[2];
                } else {
                    iArr2[1] = iArr[2];
                    iArr2[2] = iArr[0];
                }
            } else {
                iArr2[0] = iArr[2];
                if (iArr[0] < iArr[1]) {
                    iArr2[1] = iArr[0];
                    iArr2[2] = iArr[1];
                } else {
                    iArr2[1] = iArr[1];
                    iArr2[2] = iArr[0];
                }
            }
        }
        return iArr2;
    }

    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) throws InvalidKeyException {
        if (!(publicKey instanceof ECPublicKey)) {
            if (!(publicKey instanceof java.security.interfaces.ECPublicKey)) {
                throw new InvalidKeyException("cannot identify EC public key.");
            }
            java.security.interfaces.ECPublicKey eCPublicKey = (java.security.interfaces.ECPublicKey) publicKey;
            ECParameterSpec eCParameterSpecConvertSpec = EC5Util.convertSpec(eCPublicKey.getParams(), false);
            return new ECPublicKeyParameters(EC5Util.convertPoint(eCPublicKey.getParams(), eCPublicKey.getW(), false), new ECDomainParameters(eCParameterSpecConvertSpec.getCurve(), eCParameterSpecConvertSpec.getG(), eCParameterSpecConvertSpec.getN(), eCParameterSpecConvertSpec.getH(), eCParameterSpecConvertSpec.getSeed()));
        }
        ECPublicKey eCPublicKey2 = (ECPublicKey) publicKey;
        ECParameterSpec parameters = eCPublicKey2.getParameters();
        if (parameters != null) {
            return new ECPublicKeyParameters(eCPublicKey2.getQ(), new ECDomainParameters(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH(), parameters.getSeed()));
        }
        ECParameterSpec ecImplicitlyCa = ProviderUtil.getEcImplicitlyCa();
        return new ECPublicKeyParameters(((JCEECPublicKey) eCPublicKey2).engineGetQ(), new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN(), ecImplicitlyCa.getH(), ecImplicitlyCa.getSeed()));
    }

    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof ECPrivateKey)) {
            throw new InvalidKeyException("can't identify EC private key.");
        }
        ECPrivateKey eCPrivateKey = (ECPrivateKey) privateKey;
        ECParameterSpec parameters = eCPrivateKey.getParameters();
        if (parameters == null) {
            parameters = ProviderUtil.getEcImplicitlyCa();
        }
        return new ECPrivateKeyParameters(eCPrivateKey.getD(), new ECDomainParameters(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH(), parameters.getSeed()));
    }

    public static DERObjectIdentifier getNamedCurveOid(String str) {
        DERObjectIdentifier oid = X962NamedCurves.getOID(str);
        if (oid == null) {
            oid = SECNamedCurves.getOID(str);
            if (oid == null) {
                oid = NISTNamedCurves.getOID(str);
            }
            if (oid == null) {
                oid = TeleTrusTNamedCurves.getOID(str);
            }
            if (oid == null) {
                oid = ECGOST3410NamedCurves.getOID(str);
            }
        }
        return oid;
    }

    public static X9ECParameters getNamedCurveByOid(DERObjectIdentifier dERObjectIdentifier) {
        X9ECParameters byOID = X962NamedCurves.getByOID(dERObjectIdentifier);
        if (byOID == null) {
            byOID = SECNamedCurves.getByOID(dERObjectIdentifier);
            if (byOID == null) {
                byOID = NISTNamedCurves.getByOID(dERObjectIdentifier);
            }
            if (byOID == null) {
                byOID = TeleTrusTNamedCurves.getByOID(dERObjectIdentifier);
            }
        }
        return byOID;
    }

    public static String getCurveName(DERObjectIdentifier dERObjectIdentifier) {
        String name = X962NamedCurves.getName(dERObjectIdentifier);
        if (name == null) {
            name = SECNamedCurves.getName(dERObjectIdentifier);
            if (name == null) {
                name = NISTNamedCurves.getName(dERObjectIdentifier);
            }
            if (name == null) {
                name = TeleTrusTNamedCurves.getName(dERObjectIdentifier);
            }
            if (name == null) {
                name = ECGOST3410NamedCurves.getName(dERObjectIdentifier);
            }
        }
        return name;
    }
}
