package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.BigIntegers;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsDHKeyExchange.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsDHKeyExchange.class */
class TlsDHKeyExchange implements TlsKeyExchange {
    protected static final BigInteger ONE = BigInteger.valueOf(1);
    protected static final BigInteger TWO = BigInteger.valueOf(2);
    protected TlsClientContext context;
    protected int keyExchange;
    protected TlsSigner tlsSigner;
    protected TlsAgreementCredentials agreementCredentials;
    protected AsymmetricKeyParameter serverPublicKey = null;
    protected DHPublicKeyParameters dhAgreeServerPublicKey = null;
    protected DHPrivateKeyParameters dhAgreeClientPrivateKey = null;

    TlsDHKeyExchange(TlsClientContext tlsClientContext, int i) {
        switch (i) {
            case 3:
                this.tlsSigner = new TlsDSSSigner();
                break;
            case 4:
            case 6:
            case 8:
            default:
                throw new IllegalArgumentException("unsupported key exchange algorithm");
            case 5:
                this.tlsSigner = new TlsRSASigner();
                break;
            case 7:
            case 9:
                this.tlsSigner = null;
                break;
        }
        this.context = tlsClientContext;
        this.keyExchange = i;
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void skipServerCertificate() throws IOException {
        throw new TlsFatalAlert((short) 10);
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void processServerCertificate(Certificate certificate) throws IOException {
        X509CertificateStructure x509CertificateStructure = certificate.certs[0];
        try {
            this.serverPublicKey = PublicKeyFactory.createKey(x509CertificateStructure.getSubjectPublicKeyInfo());
            if (this.tlsSigner == null) {
                try {
                    this.dhAgreeServerPublicKey = validateDHPublicKey((DHPublicKeyParameters) this.serverPublicKey);
                    TlsUtils.validateKeyUsage(x509CertificateStructure, 8);
                } catch (ClassCastException e) {
                    throw new TlsFatalAlert((short) 46);
                }
            } else {
                if (!this.tlsSigner.isValidPublicKey(this.serverPublicKey)) {
                    throw new TlsFatalAlert((short) 46);
                }
                TlsUtils.validateKeyUsage(x509CertificateStructure, 128);
            }
        } catch (RuntimeException e2) {
            throw new TlsFatalAlert((short) 43);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void skipServerKeyExchange() throws IOException {
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void processServerKeyExchange(InputStream inputStream) throws IOException {
        throw new TlsFatalAlert((short) 10);
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void validateCertificateRequest(CertificateRequest certificateRequest) throws IOException {
        for (short s : certificateRequest.getCertificateTypes()) {
            switch (s) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 64:
                default:
                    throw new TlsFatalAlert((short) 47);
            }
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void skipClientCredentials() throws IOException {
        this.agreementCredentials = null;
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void processClientCredentials(TlsCredentials tlsCredentials) throws IOException {
        if (tlsCredentials instanceof TlsAgreementCredentials) {
            this.agreementCredentials = (TlsAgreementCredentials) tlsCredentials;
        } else if (!(tlsCredentials instanceof TlsSignerCredentials)) {
            throw new TlsFatalAlert((short) 80);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public void generateClientKeyExchange(OutputStream outputStream) throws IOException {
        if (this.agreementCredentials != null) {
            TlsUtils.writeUint24(0, outputStream);
        } else {
            generateEphemeralClientKeyExchange(this.dhAgreeServerPublicKey.getParameters(), outputStream);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsKeyExchange
    public byte[] generatePremasterSecret() throws IOException {
        return this.agreementCredentials != null ? this.agreementCredentials.generateAgreement(this.dhAgreeServerPublicKey) : calculateDHBasicAgreement(this.dhAgreeServerPublicKey, this.dhAgreeClientPrivateKey);
    }

    protected boolean areCompatibleParameters(DHParameters dHParameters, DHParameters dHParameters2) {
        return dHParameters.getP().equals(dHParameters2.getP()) && dHParameters.getG().equals(dHParameters2.getG());
    }

    protected byte[] calculateDHBasicAgreement(DHPublicKeyParameters dHPublicKeyParameters, DHPrivateKeyParameters dHPrivateKeyParameters) {
        DHBasicAgreement dHBasicAgreement = new DHBasicAgreement();
        dHBasicAgreement.init(this.dhAgreeClientPrivateKey);
        return BigIntegers.asUnsignedByteArray(dHBasicAgreement.calculateAgreement(this.dhAgreeServerPublicKey));
    }

    protected AsymmetricCipherKeyPair generateDHKeyPair(DHParameters dHParameters) {
        DHBasicKeyPairGenerator dHBasicKeyPairGenerator = new DHBasicKeyPairGenerator();
        dHBasicKeyPairGenerator.init(new DHKeyGenerationParameters(this.context.getSecureRandom(), dHParameters));
        return dHBasicKeyPairGenerator.generateKeyPair();
    }

    protected void generateEphemeralClientKeyExchange(DHParameters dHParameters, OutputStream outputStream) throws IOException {
        AsymmetricCipherKeyPair asymmetricCipherKeyPairGenerateDHKeyPair = generateDHKeyPair(dHParameters);
        this.dhAgreeClientPrivateKey = (DHPrivateKeyParameters) asymmetricCipherKeyPairGenerateDHKeyPair.getPrivate();
        byte[] bArrAsUnsignedByteArray = BigIntegers.asUnsignedByteArray(((DHPublicKeyParameters) asymmetricCipherKeyPairGenerateDHKeyPair.getPublic()).getY());
        TlsUtils.writeUint24(bArrAsUnsignedByteArray.length + 2, outputStream);
        TlsUtils.writeOpaque16(bArrAsUnsignedByteArray, outputStream);
    }

    protected DHPublicKeyParameters validateDHPublicKey(DHPublicKeyParameters dHPublicKeyParameters) throws IOException {
        BigInteger y = dHPublicKeyParameters.getY();
        DHParameters parameters = dHPublicKeyParameters.getParameters();
        BigInteger p = parameters.getP();
        BigInteger g = parameters.getG();
        if (!p.isProbablePrime(2)) {
            throw new TlsFatalAlert((short) 47);
        }
        if (g.compareTo(TWO) < 0 || g.compareTo(p.subtract(TWO)) > 0) {
            throw new TlsFatalAlert((short) 47);
        }
        if (y.compareTo(TWO) < 0 || y.compareTo(p.subtract(ONE)) > 0) {
            throw new TlsFatalAlert((short) 47);
        }
        return dHPublicKeyParameters;
    }
}
