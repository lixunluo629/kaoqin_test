package org.bouncycastle.openssl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemGenerationException;
import org.bouncycastle.util.io.pem.PemHeader;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.x509.X509AttributeCertificate;
import org.bouncycastle.x509.X509V2AttributeCertificate;

/* JADX WARN: Classes with same name are omitted:
  bcpkix-jdk15on-1.64.jar:org/bouncycastle/openssl/MiscPEMGenerator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/MiscPEMGenerator.class */
public class MiscPEMGenerator implements PemObjectGenerator {
    private Object obj;
    private String algorithm;
    private char[] password;
    private SecureRandom random;
    private Provider provider;

    public MiscPEMGenerator(Object obj) {
        this.obj = obj;
    }

    public MiscPEMGenerator(Object obj, String str, char[] cArr, SecureRandom secureRandom, Provider provider) {
        this.obj = obj;
        this.algorithm = str;
        this.password = cArr;
        this.random = secureRandom;
        this.provider = provider;
    }

    public MiscPEMGenerator(Object obj, String str, char[] cArr, SecureRandom secureRandom, String str2) throws NoSuchProviderException {
        this.obj = obj;
        this.algorithm = str;
        this.password = cArr;
        this.random = secureRandom;
        if (str2 != null) {
            this.provider = Security.getProvider(str2);
            if (this.provider == null) {
                throw new NoSuchProviderException("cannot find provider: " + str2);
            }
        }
    }

    private PemObject createPemObject(Object obj) throws IOException, CRLException {
        String str;
        byte[] encoded;
        if (obj instanceof PemObject) {
            return (PemObject) obj;
        }
        if (obj instanceof PemObjectGenerator) {
            return ((PemObjectGenerator) obj).generate();
        }
        if (obj instanceof X509Certificate) {
            str = "CERTIFICATE";
            try {
                encoded = ((X509Certificate) obj).getEncoded();
            } catch (CertificateEncodingException e) {
                throw new PemGenerationException("Cannot encode object: " + e.toString());
            }
        } else if (obj instanceof X509CRL) {
            str = "X509 CRL";
            try {
                encoded = ((X509CRL) obj).getEncoded();
            } catch (CRLException e2) {
                throw new PemGenerationException("Cannot encode object: " + e2.toString());
            }
        } else {
            if (obj instanceof KeyPair) {
                return createPemObject(((KeyPair) obj).getPrivate());
            }
            if (obj instanceof PrivateKey) {
                PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo((ASN1Sequence) ASN1Object.fromByteArray(((Key) obj).getEncoded()));
                if (obj instanceof RSAPrivateKey) {
                    str = "RSA PRIVATE KEY";
                    encoded = privateKeyInfo.getPrivateKey().getEncoded();
                } else if (obj instanceof DSAPrivateKey) {
                    str = "DSA PRIVATE KEY";
                    DSAParameter dSAParameter = DSAParameter.getInstance(privateKeyInfo.getAlgorithmId().getParameters());
                    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
                    aSN1EncodableVector.add(new DERInteger(0));
                    aSN1EncodableVector.add(new DERInteger(dSAParameter.getP()));
                    aSN1EncodableVector.add(new DERInteger(dSAParameter.getQ()));
                    aSN1EncodableVector.add(new DERInteger(dSAParameter.getG()));
                    BigInteger x = ((DSAPrivateKey) obj).getX();
                    aSN1EncodableVector.add(new DERInteger(dSAParameter.getG().modPow(x, dSAParameter.getP())));
                    aSN1EncodableVector.add(new DERInteger(x));
                    encoded = new DERSequence(aSN1EncodableVector).getEncoded();
                } else {
                    if (!((PrivateKey) obj).getAlgorithm().equals("ECDSA")) {
                        throw new IOException("Cannot identify private key");
                    }
                    str = "EC PRIVATE KEY";
                    encoded = privateKeyInfo.getPrivateKey().getEncoded();
                }
            } else if (obj instanceof PublicKey) {
                str = "PUBLIC KEY";
                encoded = ((PublicKey) obj).getEncoded();
            } else if (obj instanceof X509AttributeCertificate) {
                str = "ATTRIBUTE CERTIFICATE";
                encoded = ((X509V2AttributeCertificate) obj).getEncoded();
            } else if (obj instanceof PKCS10CertificationRequest) {
                str = "CERTIFICATE REQUEST";
                encoded = ((PKCS10CertificationRequest) obj).getEncoded();
            } else {
                if (!(obj instanceof ContentInfo)) {
                    throw new PemGenerationException("unknown object passed - can't encode.");
                }
                str = "PKCS7";
                encoded = ((ContentInfo) obj).getEncoded();
            }
        }
        return new PemObject(str, encoded);
    }

    private String getHexEncoded(byte[] bArr) throws IOException {
        byte[] bArrEncode = Hex.encode(bArr);
        char[] cArr = new char[bArrEncode.length];
        for (int i = 0; i != bArrEncode.length; i++) {
            cArr[i] = (char) bArrEncode[i];
        }
        return new String(cArr);
    }

    private PemObject createPemObject(Object obj, String str, char[] cArr, SecureRandom secureRandom) throws IOException {
        if (obj instanceof KeyPair) {
            return createPemObject(((KeyPair) obj).getPrivate(), str, cArr, secureRandom);
        }
        String str2 = null;
        byte[] encoded = null;
        if (obj instanceof RSAPrivateCrtKey) {
            str2 = "RSA PRIVATE KEY";
            RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) obj;
            encoded = new RSAPrivateKeyStructure(rSAPrivateCrtKey.getModulus(), rSAPrivateCrtKey.getPublicExponent(), rSAPrivateCrtKey.getPrivateExponent(), rSAPrivateCrtKey.getPrimeP(), rSAPrivateCrtKey.getPrimeQ(), rSAPrivateCrtKey.getPrimeExponentP(), rSAPrivateCrtKey.getPrimeExponentQ(), rSAPrivateCrtKey.getCrtCoefficient()).getEncoded();
        } else if (obj instanceof DSAPrivateKey) {
            str2 = "DSA PRIVATE KEY";
            DSAPrivateKey dSAPrivateKey = (DSAPrivateKey) obj;
            DSAParams params = dSAPrivateKey.getParams();
            ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
            aSN1EncodableVector.add(new DERInteger(0));
            aSN1EncodableVector.add(new DERInteger(params.getP()));
            aSN1EncodableVector.add(new DERInteger(params.getQ()));
            aSN1EncodableVector.add(new DERInteger(params.getG()));
            BigInteger x = dSAPrivateKey.getX();
            aSN1EncodableVector.add(new DERInteger(params.getG().modPow(x, params.getP())));
            aSN1EncodableVector.add(new DERInteger(x));
            encoded = new DERSequence(aSN1EncodableVector).getEncoded();
        } else if ((obj instanceof PrivateKey) && "ECDSA".equals(((PrivateKey) obj).getAlgorithm())) {
            str2 = "EC PRIVATE KEY";
            encoded = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(((PrivateKey) obj).getEncoded())).getPrivateKey().getEncoded();
        }
        if (str2 == null || encoded == null) {
            throw new IllegalArgumentException("Object type not supported: " + obj.getClass().getName());
        }
        String upperCase = Strings.toUpperCase(str);
        if (upperCase.equals("DESEDE")) {
            upperCase = "DES-EDE3-CBC";
        }
        byte[] bArr = new byte[upperCase.startsWith("AES-") ? 16 : 8];
        secureRandom.nextBytes(bArr);
        byte[] bArrCrypt = PEMUtilities.crypt(true, this.provider, encoded, cArr, upperCase, bArr);
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(new PemHeader("Proc-Type", "4,ENCRYPTED"));
        arrayList.add(new PemHeader("DEK-Info", upperCase + "," + getHexEncoded(bArr)));
        return new PemObject(str2, arrayList, bArrCrypt);
    }

    @Override // org.bouncycastle.util.io.pem.PemObjectGenerator
    public PemObject generate() throws PemGenerationException {
        try {
            return this.algorithm != null ? createPemObject(this.obj, this.algorithm, this.password, this.random) : createPemObject(this.obj);
        } catch (IOException e) {
            throw new PemGenerationException("encoding exception: " + e.getMessage(), e);
        }
    }
}
