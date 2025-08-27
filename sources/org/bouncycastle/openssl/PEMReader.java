package org.bouncycastle.openssl;

import com.moredian.onpremise.core.utils.RSAUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateFactory;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.EncryptionScheme;
import org.bouncycastle.asn1.pkcs.KeyDerivationFunc;
import org.bouncycastle.asn1.pkcs.PBEParameter;
import org.bouncycastle.asn1.pkcs.PBES2Parameters;
import org.bouncycastle.asn1.pkcs.PBKDF2Params;
import org.bouncycastle.asn1.pkcs.PKCS12PBEParams;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKeyStructure;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemHeader;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectParser;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.x509.X509V2AttributeCertificate;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader.class */
public class PEMReader extends PemReader {
    private final Map parsers;
    private PasswordFinder pFinder;

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$DSAKeyPairParser.class */
    private class DSAKeyPairParser extends KeyPairParser {
        public DSAKeyPairParser(String str) {
            super(str);
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws NoSuchAlgorithmException, IOException, NoSuchProviderException {
            try {
                ASN1Sequence keyPair = readKeyPair(pemObject);
                if (keyPair.size() != 6) {
                    throw new PEMException("malformed sequence in DSA private key");
                }
                DERInteger dERInteger = (DERInteger) keyPair.getObjectAt(1);
                DERInteger dERInteger2 = (DERInteger) keyPair.getObjectAt(2);
                DERInteger dERInteger3 = (DERInteger) keyPair.getObjectAt(3);
                DERInteger dERInteger4 = (DERInteger) keyPair.getObjectAt(4);
                DSAPrivateKeySpec dSAPrivateKeySpec = new DSAPrivateKeySpec(((DERInteger) keyPair.getObjectAt(5)).getValue(), dERInteger.getValue(), dERInteger2.getValue(), dERInteger3.getValue());
                DSAPublicKeySpec dSAPublicKeySpec = new DSAPublicKeySpec(dERInteger4.getValue(), dERInteger.getValue(), dERInteger2.getValue(), dERInteger3.getValue());
                KeyFactory keyFactory = KeyFactory.getInstance("DSA", this.provider);
                return new KeyPair(keyFactory.generatePublic(dSAPublicKeySpec), keyFactory.generatePrivate(dSAPrivateKeySpec));
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                throw new PEMException("problem creating DSA private key: " + e2.toString(), e2);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$ECDSAKeyPairParser.class */
    private class ECDSAKeyPairParser extends KeyPairParser {
        public ECDSAKeyPairParser(String str) {
            super(str);
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws NoSuchAlgorithmException, IOException, NoSuchProviderException {
            try {
                ECPrivateKeyStructure eCPrivateKeyStructure = new ECPrivateKeyStructure(readKeyPair(pemObject));
                AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, eCPrivateKeyStructure.getParameters());
                PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, eCPrivateKeyStructure.getDERObject());
                SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(algorithmIdentifier, eCPrivateKeyStructure.getPublicKey().getBytes());
                PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded());
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(subjectPublicKeyInfo.getEncoded());
                KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", this.provider);
                return new KeyPair(keyFactory.generatePublic(x509EncodedKeySpec), keyFactory.generatePrivate(pKCS8EncodedKeySpec));
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                throw new PEMException("problem creating EC private key: " + e2.toString(), e2);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$ECNamedCurveSpecParser.class */
    private class ECNamedCurveSpecParser implements PemObjectParser {
        private ECNamedCurveSpecParser() {
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(((DERObjectIdentifier) ASN1Object.fromByteArray(pemObject.getContent())).getId());
                if (parameterSpec == null) {
                    throw new IOException("object ID not found in EC curve table");
                }
                return parameterSpec;
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                throw new PEMException("exception extracting EC named curve: " + e2.toString());
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$EncryptedPrivateKeyParser.class */
    private class EncryptedPrivateKeyParser implements PemObjectParser {
        private String symProvider;
        private String asymProvider;

        public EncryptedPrivateKeyParser(String str, String str2) {
            this.symProvider = str;
            this.asymProvider = str2;
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {
            try {
                EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = EncryptedPrivateKeyInfo.getInstance(ASN1Object.fromByteArray(pemObject.getContent()));
                AlgorithmIdentifier encryptionAlgorithm = encryptedPrivateKeyInfo.getEncryptionAlgorithm();
                if (PEMReader.this.pFinder == null) {
                    throw new PEMException("no PasswordFinder specified");
                }
                if (PEMUtilities.isPKCS5Scheme2(encryptionAlgorithm.getAlgorithm())) {
                    PBES2Parameters pBES2Parameters = PBES2Parameters.getInstance(encryptionAlgorithm.getParameters());
                    KeyDerivationFunc keyDerivationFunc = pBES2Parameters.getKeyDerivationFunc();
                    EncryptionScheme encryptionScheme = pBES2Parameters.getEncryptionScheme();
                    PBKDF2Params pBKDF2Params = (PBKDF2Params) keyDerivationFunc.getParameters();
                    int iIntValue = pBKDF2Params.getIterationCount().intValue();
                    byte[] salt = pBKDF2Params.getSalt();
                    String id = encryptionScheme.getAlgorithm().getId();
                    SecretKey secretKeyGenerateSecretKeyForPKCS5Scheme2 = PEMUtilities.generateSecretKeyForPKCS5Scheme2(id, PEMReader.this.pFinder.getPassword(), salt, iIntValue);
                    Cipher cipher = Cipher.getInstance(id, this.symProvider);
                    AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(id, this.symProvider);
                    algorithmParameters.init(encryptionScheme.getParameters().getDERObject().getEncoded());
                    cipher.init(2, secretKeyGenerateSecretKeyForPKCS5Scheme2, algorithmParameters);
                    PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(cipher.doFinal(encryptedPrivateKeyInfo.getEncryptedData())));
                    return KeyFactory.getInstance(privateKeyInfo.getAlgorithmId().getAlgorithm().getId(), this.asymProvider).generatePrivate(new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded()));
                }
                if (PEMUtilities.isPKCS12(encryptionAlgorithm.getAlgorithm())) {
                    PKCS12PBEParams pKCS12PBEParams = PKCS12PBEParams.getInstance(encryptionAlgorithm.getParameters());
                    String id2 = encryptionAlgorithm.getAlgorithm().getId();
                    PBEKeySpec pBEKeySpec = new PBEKeySpec(PEMReader.this.pFinder.getPassword());
                    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(id2, this.symProvider);
                    PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(pKCS12PBEParams.getIV(), pKCS12PBEParams.getIterations().intValue());
                    Cipher cipher2 = Cipher.getInstance(id2, this.symProvider);
                    cipher2.init(2, secretKeyFactory.generateSecret(pBEKeySpec), pBEParameterSpec);
                    PrivateKeyInfo privateKeyInfo2 = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(cipher2.doFinal(encryptedPrivateKeyInfo.getEncryptedData())));
                    return KeyFactory.getInstance(privateKeyInfo2.getAlgorithmId().getAlgorithm().getId(), this.asymProvider).generatePrivate(new PKCS8EncodedKeySpec(privateKeyInfo2.getEncoded()));
                }
                if (!PEMUtilities.isPKCS5Scheme1(encryptionAlgorithm.getAlgorithm())) {
                    throw new PEMException("Unknown algorithm: " + encryptionAlgorithm.getAlgorithm());
                }
                PBEParameter pBEParameter = PBEParameter.getInstance(encryptionAlgorithm.getParameters());
                String id3 = encryptionAlgorithm.getAlgorithm().getId();
                PBEKeySpec pBEKeySpec2 = new PBEKeySpec(PEMReader.this.pFinder.getPassword());
                SecretKeyFactory secretKeyFactory2 = SecretKeyFactory.getInstance(id3, this.symProvider);
                PBEParameterSpec pBEParameterSpec2 = new PBEParameterSpec(pBEParameter.getSalt(), pBEParameter.getIterationCount().intValue());
                Cipher cipher3 = Cipher.getInstance(id3, this.symProvider);
                cipher3.init(2, secretKeyFactory2.generateSecret(pBEKeySpec2), pBEParameterSpec2);
                PrivateKeyInfo privateKeyInfo3 = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(cipher3.doFinal(encryptedPrivateKeyInfo.getEncryptedData())));
                return KeyFactory.getInstance(privateKeyInfo3.getAlgorithmId().getAlgorithm().getId(), this.asymProvider).generatePrivate(new PKCS8EncodedKeySpec(privateKeyInfo3.getEncoded()));
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                throw new PEMException("problem parsing ENCRYPTED PRIVATE KEY: " + e2.toString(), e2);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$KeyPairParser.class */
    private abstract class KeyPairParser implements PemObjectParser {
        protected String provider;

        public KeyPairParser(String str) {
            this.provider = str;
        }

        protected ASN1Sequence readKeyPair(PemObject pemObject) throws IOException {
            boolean z = false;
            String value = null;
            for (PemHeader pemHeader : pemObject.getHeaders()) {
                if (pemHeader.getName().equals("Proc-Type") && pemHeader.getValue().equals("4,ENCRYPTED")) {
                    z = true;
                } else if (pemHeader.getName().equals("DEK-Info")) {
                    value = pemHeader.getValue();
                }
            }
            byte[] content = pemObject.getContent();
            if (z) {
                if (PEMReader.this.pFinder == null) {
                    throw new PasswordException("No password finder specified, but a password is required");
                }
                char[] password = PEMReader.this.pFinder.getPassword();
                if (password == null) {
                    throw new PasswordException("Password is null, but a password is required");
                }
                StringTokenizer stringTokenizer = new StringTokenizer(value, ",");
                content = PEMUtilities.crypt(false, this.provider, content, password, stringTokenizer.nextToken(), Hex.decode(stringTokenizer.nextToken()));
            }
            try {
                return (ASN1Sequence) ASN1Object.fromByteArray(content);
            } catch (IOException e) {
                if (z) {
                    throw new PEMException("exception decoding - please check password and data.", e);
                }
                throw new PEMException(e.getMessage(), e);
            } catch (ClassCastException e2) {
                if (z) {
                    throw new PEMException("exception decoding - please check password and data.", e2);
                }
                throw new PEMException(e2.getMessage(), e2);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$PKCS10CertificationRequestParser.class */
    private class PKCS10CertificationRequestParser implements PemObjectParser {
        private PKCS10CertificationRequestParser() {
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return new PKCS10CertificationRequest(pemObject.getContent());
            } catch (Exception e) {
                throw new PEMException("problem parsing certrequest: " + e.toString(), e);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$PKCS7Parser.class */
    private class PKCS7Parser implements PemObjectParser {
        private PKCS7Parser() {
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return ContentInfo.getInstance(new ASN1InputStream(pemObject.getContent()).readObject());
            } catch (Exception e) {
                throw new PEMException("problem parsing PKCS7 object: " + e.toString(), e);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$PrivateKeyParser.class */
    private class PrivateKeyParser implements PemObjectParser {
        private String provider;

        public PrivateKeyParser(String str) {
            this.provider = str;
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return KeyFactory.getInstance(PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(pemObject.getContent())).getAlgorithmId().getAlgorithm().getId(), this.provider).generatePrivate(new PKCS8EncodedKeySpec(pemObject.getContent()));
            } catch (Exception e) {
                throw new PEMException("problem parsing PRIVATE KEY: " + e.toString(), e);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$PublicKeyParser.class */
    private class PublicKeyParser implements PemObjectParser {
        private String provider;

        public PublicKeyParser(String str) {
            this.provider = str;
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pemObject.getContent());
            for (String str : new String[]{"DSA", RSAUtils.RSA_KEY_ALGORITHM}) {
                try {
                    return KeyFactory.getInstance(str, this.provider).generatePublic(x509EncodedKeySpec);
                } catch (NoSuchAlgorithmException e) {
                } catch (NoSuchProviderException e2) {
                    throw new RuntimeException("can't find provider " + this.provider);
                } catch (InvalidKeySpecException e3) {
                }
            }
            return null;
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$RSAKeyPairParser.class */
    private class RSAKeyPairParser extends KeyPairParser {
        public RSAKeyPairParser(String str) {
            super(str);
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws NoSuchAlgorithmException, IOException, NoSuchProviderException {
            try {
                ASN1Sequence keyPair = readKeyPair(pemObject);
                if (keyPair.size() != 9) {
                    throw new PEMException("malformed sequence in RSA private key");
                }
                DERInteger dERInteger = (DERInteger) keyPair.getObjectAt(1);
                DERInteger dERInteger2 = (DERInteger) keyPair.getObjectAt(2);
                DERInteger dERInteger3 = (DERInteger) keyPair.getObjectAt(3);
                DERInteger dERInteger4 = (DERInteger) keyPair.getObjectAt(4);
                DERInteger dERInteger5 = (DERInteger) keyPair.getObjectAt(5);
                DERInteger dERInteger6 = (DERInteger) keyPair.getObjectAt(6);
                DERInteger dERInteger7 = (DERInteger) keyPair.getObjectAt(7);
                DERInteger dERInteger8 = (DERInteger) keyPair.getObjectAt(8);
                RSAPublicKeySpec rSAPublicKeySpec = new RSAPublicKeySpec(dERInteger.getValue(), dERInteger2.getValue());
                RSAPrivateCrtKeySpec rSAPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(dERInteger.getValue(), dERInteger2.getValue(), dERInteger3.getValue(), dERInteger4.getValue(), dERInteger5.getValue(), dERInteger6.getValue(), dERInteger7.getValue(), dERInteger8.getValue());
                KeyFactory keyFactory = KeyFactory.getInstance(RSAUtils.RSA_KEY_ALGORITHM, this.provider);
                return new KeyPair(keyFactory.generatePublic(rSAPublicKeySpec), keyFactory.generatePrivate(rSAPrivateCrtKeySpec));
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                throw new PEMException("problem creating RSA private key: " + e2.toString(), e2);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$RSAPublicKeyParser.class */
    private class RSAPublicKeyParser implements PemObjectParser {
        private String provider;

        public RSAPublicKeyParser(String str) {
            this.provider = str;
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                RSAPublicKeyStructure rSAPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence) new ASN1InputStream(pemObject.getContent()).readObject());
                return KeyFactory.getInstance(RSAUtils.RSA_KEY_ALGORITHM, this.provider).generatePublic(new RSAPublicKeySpec(rSAPublicKeyStructure.getModulus(), rSAPublicKeyStructure.getPublicExponent()));
            } catch (IOException e) {
                throw e;
            } catch (NoSuchProviderException e2) {
                throw new IOException("can't find provider " + this.provider);
            } catch (Exception e3) {
                throw new PEMException("problem extracting key: " + e3.toString(), e3);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$X509AttributeCertificateParser.class */
    private class X509AttributeCertificateParser implements PemObjectParser {
        private X509AttributeCertificateParser() {
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            return new X509V2AttributeCertificate(pemObject.getContent());
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$X509CRLParser.class */
    private class X509CRLParser implements PemObjectParser {
        private String provider;

        public X509CRLParser(String str) {
            this.provider = str;
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return CertificateFactory.getInstance("X.509", this.provider).generateCRL(new ByteArrayInputStream(pemObject.getContent()));
            } catch (Exception e) {
                throw new PEMException("problem parsing cert: " + e.toString(), e);
            }
        }
    }

    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/openssl/PEMReader$X509CertificateParser.class */
    private class X509CertificateParser implements PemObjectParser {
        private String provider;

        public X509CertificateParser(String str) {
            this.provider = str;
        }

        @Override // org.bouncycastle.util.io.pem.PemObjectParser
        public Object parseObject(PemObject pemObject) throws IOException {
            try {
                return CertificateFactory.getInstance("X.509", this.provider).generateCertificate(new ByteArrayInputStream(pemObject.getContent()));
            } catch (Exception e) {
                throw new PEMException("problem parsing cert: " + e.toString(), e);
            }
        }
    }

    public PEMReader(Reader reader) {
        this(reader, null, "BC");
    }

    public PEMReader(Reader reader, PasswordFinder passwordFinder) {
        this(reader, passwordFinder, "BC");
    }

    public PEMReader(Reader reader, PasswordFinder passwordFinder, String str) {
        this(reader, passwordFinder, str, str);
    }

    public PEMReader(Reader reader, PasswordFinder passwordFinder, String str, String str2) {
        super(reader);
        this.parsers = new HashMap();
        this.pFinder = passwordFinder;
        this.parsers.put("CERTIFICATE REQUEST", new PKCS10CertificationRequestParser());
        this.parsers.put("NEW CERTIFICATE REQUEST", new PKCS10CertificationRequestParser());
        this.parsers.put("CERTIFICATE", new X509CertificateParser(str2));
        this.parsers.put("X509 CERTIFICATE", new X509CertificateParser(str2));
        this.parsers.put("X509 CRL", new X509CRLParser(str2));
        this.parsers.put("PKCS7", new PKCS7Parser());
        this.parsers.put("ATTRIBUTE CERTIFICATE", new X509AttributeCertificateParser());
        this.parsers.put("EC PARAMETERS", new ECNamedCurveSpecParser());
        this.parsers.put("PUBLIC KEY", new PublicKeyParser(str2));
        this.parsers.put("RSA PUBLIC KEY", new RSAPublicKeyParser(str2));
        this.parsers.put("RSA PRIVATE KEY", new RSAKeyPairParser(str2));
        this.parsers.put("DSA PRIVATE KEY", new DSAKeyPairParser(str2));
        this.parsers.put("EC PRIVATE KEY", new ECDSAKeyPairParser(str2));
        this.parsers.put("ENCRYPTED PRIVATE KEY", new EncryptedPrivateKeyParser(str, str2));
        this.parsers.put("PRIVATE KEY", new PrivateKeyParser(str2));
    }

    public Object readObject() throws IOException {
        PemObject pemObject = readPemObject();
        if (pemObject == null) {
            return null;
        }
        String type = pemObject.getType();
        if (this.parsers.containsKey(type)) {
            return ((PemObjectParser) this.parsers.get(type)).parseObject(pemObject);
        }
        throw new IOException("unrecognised object: " + type);
    }
}
