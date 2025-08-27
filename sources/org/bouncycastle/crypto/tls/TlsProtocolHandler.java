package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsProtocolHandler.class */
public class TlsProtocolHandler {
    private static final short CS_CLIENT_HELLO_SEND = 1;
    private static final short CS_SERVER_HELLO_RECEIVED = 2;
    private static final short CS_SERVER_CERTIFICATE_RECEIVED = 3;
    private static final short CS_SERVER_KEY_EXCHANGE_RECEIVED = 4;
    private static final short CS_CERTIFICATE_REQUEST_RECEIVED = 5;
    private static final short CS_SERVER_HELLO_DONE_RECEIVED = 6;
    private static final short CS_CLIENT_KEY_EXCHANGE_SEND = 7;
    private static final short CS_CERTIFICATE_VERIFY_SEND = 8;
    private static final short CS_CLIENT_CHANGE_CIPHER_SPEC_SEND = 9;
    private static final short CS_CLIENT_FINISHED_SEND = 10;
    private static final short CS_SERVER_CHANGE_CIPHER_SPEC_RECEIVED = 11;
    private static final short CS_DONE = 12;
    private static final String TLS_ERROR_MESSAGE = "Internal TLS error, this could be an attack";
    private ByteQueue applicationDataQueue;
    private ByteQueue changeCipherSpecQueue;
    private ByteQueue alertQueue;
    private ByteQueue handshakeQueue;
    private RecordStream rs;
    private SecureRandom random;
    private TlsInputStream tlsInputStream;
    private TlsOutputStream tlsOutputStream;
    private boolean closed;
    private boolean failedWithError;
    private boolean appDataReady;
    private Hashtable clientExtensions;
    private SecurityParameters securityParameters;
    private TlsClientContextImpl tlsClientContext;
    private TlsClient tlsClient;
    private int[] offeredCipherSuites;
    private short[] offeredCompressionMethods;
    private TlsKeyExchange keyExchange;
    private TlsAuthentication authentication;
    private CertificateRequest certificateRequest;
    private short connection_state;
    private static final Integer EXT_RenegotiationInfo = new Integer(65281);
    private static final byte[] emptybuf = new byte[0];

    private static SecureRandom createSecureRandom() {
        ThreadedSeedGenerator threadedSeedGenerator = new ThreadedSeedGenerator();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(threadedSeedGenerator.generateSeed(20, true));
        return secureRandom;
    }

    public TlsProtocolHandler(InputStream inputStream, OutputStream outputStream) {
        this(inputStream, outputStream, createSecureRandom());
    }

    public TlsProtocolHandler(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        this.applicationDataQueue = new ByteQueue();
        this.changeCipherSpecQueue = new ByteQueue();
        this.alertQueue = new ByteQueue();
        this.handshakeQueue = new ByteQueue();
        this.tlsInputStream = null;
        this.tlsOutputStream = null;
        this.closed = false;
        this.failedWithError = false;
        this.appDataReady = false;
        this.securityParameters = null;
        this.tlsClientContext = null;
        this.tlsClient = null;
        this.offeredCipherSuites = null;
        this.offeredCompressionMethods = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateRequest = null;
        this.connection_state = (short) 0;
        this.rs = new RecordStream(this, inputStream, outputStream);
        this.random = secureRandom;
    }

    protected void processData(short s, byte[] bArr, int i, int i2) throws IOException {
        switch (s) {
            case 20:
                this.changeCipherSpecQueue.addData(bArr, i, i2);
                processChangeCipherSpec();
                break;
            case 21:
                this.alertQueue.addData(bArr, i, i2);
                processAlert();
                break;
            case 22:
                this.handshakeQueue.addData(bArr, i, i2);
                processHandshake();
                break;
            case 23:
                if (!this.appDataReady) {
                    failWithError((short) 2, (short) 10);
                }
                this.applicationDataQueue.addData(bArr, i, i2);
                processApplicationData();
                break;
        }
    }

    private void processHandshake() throws IOException {
        boolean z;
        do {
            z = false;
            if (this.handshakeQueue.size() >= 4) {
                byte[] bArr = new byte[4];
                this.handshakeQueue.read(bArr, 0, 4, 0);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
                short uint8 = TlsUtils.readUint8(byteArrayInputStream);
                int uint24 = TlsUtils.readUint24(byteArrayInputStream);
                if (this.handshakeQueue.size() >= uint24 + 4) {
                    byte[] bArr2 = new byte[uint24];
                    this.handshakeQueue.read(bArr2, 0, uint24, 4);
                    this.handshakeQueue.removeData(uint24 + 4);
                    switch (uint8) {
                        case 0:
                        case 20:
                            break;
                        default:
                            this.rs.updateHandshakeData(bArr, 0, 4);
                            this.rs.updateHandshakeData(bArr2, 0, uint24);
                            break;
                    }
                    processHandshakeMessage(uint8, bArr2);
                    z = true;
                }
            }
        } while (z);
    }

    private void processHandshakeMessage(short s, byte[] bArr) throws IOException {
        Certificate certificate;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        switch (s) {
            case 0:
                if (this.connection_state == 12) {
                    sendAlert((short) 1, (short) 100);
                    break;
                }
                break;
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            default:
                failWithError((short) 2, (short) 10);
                break;
            case 2:
                switch (this.connection_state) {
                    case 1:
                        TlsUtils.checkVersion(byteArrayInputStream, this);
                        this.securityParameters.serverRandom = new byte[32];
                        TlsUtils.readFully(this.securityParameters.serverRandom, byteArrayInputStream);
                        byte[] opaque8 = TlsUtils.readOpaque8(byteArrayInputStream);
                        if (opaque8.length > 32) {
                            failWithError((short) 2, (short) 47);
                        }
                        this.tlsClient.notifySessionID(opaque8);
                        int uint16 = TlsUtils.readUint16(byteArrayInputStream);
                        if (!arrayContains(this.offeredCipherSuites, uint16) || uint16 == 255) {
                            failWithError((short) 2, (short) 47);
                        }
                        this.tlsClient.notifySelectedCipherSuite(uint16);
                        short uint8 = TlsUtils.readUint8(byteArrayInputStream);
                        if (!arrayContains(this.offeredCompressionMethods, uint8)) {
                            failWithError((short) 2, (short) 47);
                        }
                        this.tlsClient.notifySelectedCompressionMethod(uint8);
                        Hashtable hashtable = new Hashtable();
                        if (byteArrayInputStream.available() > 0) {
                            ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(TlsUtils.readOpaque16(byteArrayInputStream));
                            while (byteArrayInputStream2.available() > 0) {
                                Integer num = new Integer(TlsUtils.readUint16(byteArrayInputStream2));
                                byte[] opaque16 = TlsUtils.readOpaque16(byteArrayInputStream2);
                                if (!num.equals(EXT_RenegotiationInfo) && this.clientExtensions.get(num) == null) {
                                    failWithError((short) 2, (short) 110);
                                }
                                if (hashtable.containsKey(num)) {
                                    failWithError((short) 2, (short) 47);
                                }
                                hashtable.put(num, opaque16);
                            }
                        }
                        assertEmpty(byteArrayInputStream);
                        boolean zContainsKey = hashtable.containsKey(EXT_RenegotiationInfo);
                        if (zContainsKey && !Arrays.constantTimeAreEqual((byte[]) hashtable.get(EXT_RenegotiationInfo), createRenegotiationInfo(emptybuf))) {
                            failWithError((short) 2, (short) 40);
                        }
                        this.tlsClient.notifySecureRenegotiation(zContainsKey);
                        if (this.clientExtensions != null) {
                            this.tlsClient.processServerExtensions(hashtable);
                        }
                        this.keyExchange = this.tlsClient.getKeyExchange();
                        this.connection_state = (short) 2;
                        break;
                    default:
                        failWithError((short) 2, (short) 10);
                        break;
                }
            case 11:
                switch (this.connection_state) {
                    case 2:
                        Certificate certificate2 = Certificate.parse(byteArrayInputStream);
                        assertEmpty(byteArrayInputStream);
                        this.keyExchange.processServerCertificate(certificate2);
                        this.authentication = this.tlsClient.getAuthentication();
                        this.authentication.notifyServerCertificate(certificate2);
                        break;
                    default:
                        failWithError((short) 2, (short) 10);
                        break;
                }
                this.connection_state = (short) 3;
                break;
            case 12:
                switch (this.connection_state) {
                    case 2:
                        this.keyExchange.skipServerCertificate();
                        this.authentication = null;
                    case 3:
                        this.keyExchange.processServerKeyExchange(byteArrayInputStream);
                        assertEmpty(byteArrayInputStream);
                        break;
                    default:
                        failWithError((short) 2, (short) 10);
                        break;
                }
                this.connection_state = (short) 4;
                break;
            case 13:
                switch (this.connection_state) {
                    case 3:
                        this.keyExchange.skipServerKeyExchange();
                    case 4:
                        if (this.authentication == null) {
                            failWithError((short) 2, (short) 40);
                        }
                        int uint82 = TlsUtils.readUint8(byteArrayInputStream);
                        short[] sArr = new short[uint82];
                        for (int i = 0; i < uint82; i++) {
                            sArr[i] = TlsUtils.readUint8(byteArrayInputStream);
                        }
                        byte[] opaque162 = TlsUtils.readOpaque16(byteArrayInputStream);
                        assertEmpty(byteArrayInputStream);
                        Vector vector = new Vector();
                        ByteArrayInputStream byteArrayInputStream3 = new ByteArrayInputStream(opaque162);
                        while (byteArrayInputStream3.available() > 0) {
                            vector.addElement(X500Name.getInstance(ASN1Object.fromByteArray(TlsUtils.readOpaque16(byteArrayInputStream3))));
                        }
                        this.certificateRequest = new CertificateRequest(sArr, vector);
                        this.keyExchange.validateCertificateRequest(this.certificateRequest);
                        break;
                    default:
                        failWithError((short) 2, (short) 10);
                        break;
                }
                this.connection_state = (short) 5;
                break;
            case 14:
                switch (this.connection_state) {
                    case 3:
                        this.keyExchange.skipServerKeyExchange();
                        break;
                    case 4:
                    case 5:
                        break;
                    default:
                        failWithError((short) 2, (short) 40);
                        break;
                }
                assertEmpty(byteArrayInputStream);
                this.connection_state = (short) 6;
                TlsCredentials clientCredentials = null;
                if (this.certificateRequest == null) {
                    this.keyExchange.skipClientCredentials();
                } else {
                    clientCredentials = this.authentication.getClientCredentials(this.certificateRequest);
                    if (clientCredentials == null) {
                        this.keyExchange.skipClientCredentials();
                        certificate = Certificate.EMPTY_CHAIN;
                    } else {
                        this.keyExchange.processClientCredentials(clientCredentials);
                        certificate = clientCredentials.getCertificate();
                    }
                    sendClientCertificate(certificate);
                }
                sendClientKeyExchange();
                this.connection_state = (short) 7;
                if (clientCredentials != null && (clientCredentials instanceof TlsSignerCredentials)) {
                    sendCertificateVerify(((TlsSignerCredentials) clientCredentials).generateCertificateSignature(this.rs.getCurrentHash()));
                    this.connection_state = (short) 8;
                }
                byte[] bArr2 = {1};
                this.rs.writeMessage((short) 20, bArr2, 0, bArr2.length);
                this.connection_state = (short) 9;
                byte[] bArrGeneratePremasterSecret = this.keyExchange.generatePremasterSecret();
                this.securityParameters.masterSecret = TlsUtils.PRF(bArrGeneratePremasterSecret, "master secret", TlsUtils.concat(this.securityParameters.clientRandom, this.securityParameters.serverRandom), 48);
                Arrays.fill(bArrGeneratePremasterSecret, (byte) 0);
                this.rs.clientCipherSpecDecided(this.tlsClient.getCompression(), this.tlsClient.getCipher());
                byte[] bArrPRF = TlsUtils.PRF(this.securityParameters.masterSecret, ExporterLabel.client_finished, this.rs.getCurrentHash(), 12);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                TlsUtils.writeUint8((short) 20, byteArrayOutputStream);
                TlsUtils.writeOpaque24(bArrPRF, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                this.rs.writeMessage((short) 22, byteArray, 0, byteArray.length);
                this.connection_state = (short) 10;
                break;
            case 20:
                switch (this.connection_state) {
                    case 11:
                        byte[] bArr3 = new byte[12];
                        TlsUtils.readFully(bArr3, byteArrayInputStream);
                        assertEmpty(byteArrayInputStream);
                        if (!Arrays.constantTimeAreEqual(TlsUtils.PRF(this.securityParameters.masterSecret, ExporterLabel.server_finished, this.rs.getCurrentHash(), 12), bArr3)) {
                            failWithError((short) 2, (short) 40);
                        }
                        this.connection_state = (short) 12;
                        this.appDataReady = true;
                        break;
                    default:
                        failWithError((short) 2, (short) 10);
                        break;
                }
        }
    }

    private void processApplicationData() {
    }

    private void processAlert() throws IOException {
        while (this.alertQueue.size() >= 2) {
            byte[] bArr = new byte[2];
            this.alertQueue.read(bArr, 0, 2, 0);
            this.alertQueue.removeData(2);
            short s = bArr[0];
            short s2 = bArr[1];
            if (s == 2) {
                this.failedWithError = true;
                this.closed = true;
                try {
                    this.rs.close();
                } catch (Exception e) {
                }
                throw new IOException(TLS_ERROR_MESSAGE);
            }
            if (s2 == 0) {
                failWithError((short) 1, (short) 0);
            }
        }
    }

    private void processChangeCipherSpec() throws IOException {
        while (this.changeCipherSpecQueue.size() > 0) {
            byte[] bArr = new byte[1];
            this.changeCipherSpecQueue.read(bArr, 0, 1, 0);
            this.changeCipherSpecQueue.removeData(1);
            if (bArr[0] != 1) {
                failWithError((short) 2, (short) 10);
            }
            if (this.connection_state != 10) {
                failWithError((short) 2, (short) 40);
            }
            this.rs.serverClientSpecReceived();
            this.connection_state = (short) 11;
        }
    }

    private void sendClientCertificate(Certificate certificate) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short) 11, byteArrayOutputStream);
        certificate.encode(byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.rs.writeMessage((short) 22, byteArray, 0, byteArray.length);
    }

    private void sendClientKeyExchange() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short) 16, byteArrayOutputStream);
        this.keyExchange.generateClientKeyExchange(byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.rs.writeMessage((short) 22, byteArray, 0, byteArray.length);
    }

    private void sendCertificateVerify(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short) 15, byteArrayOutputStream);
        TlsUtils.writeUint24(bArr.length + 2, byteArrayOutputStream);
        TlsUtils.writeOpaque16(bArr, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.rs.writeMessage((short) 22, byteArray, 0, byteArray.length);
    }

    public void connect(CertificateVerifyer certificateVerifyer) throws IOException {
        connect(new LegacyTlsClient(certificateVerifyer));
    }

    public void connect(TlsClient tlsClient) throws IOException {
        if (tlsClient == null) {
            throw new IllegalArgumentException("'tlsClient' cannot be null");
        }
        if (this.tlsClient != null) {
            throw new IllegalStateException("connect can only be called once");
        }
        this.securityParameters = new SecurityParameters();
        this.securityParameters.clientRandom = new byte[32];
        this.random.nextBytes(this.securityParameters.clientRandom);
        TlsUtils.writeGMTUnixTime(this.securityParameters.clientRandom, 0);
        this.tlsClientContext = new TlsClientContextImpl(this.random, this.securityParameters);
        this.tlsClient = tlsClient;
        this.tlsClient.init(this.tlsClientContext);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeVersion(byteArrayOutputStream);
        byteArrayOutputStream.write(this.securityParameters.clientRandom);
        TlsUtils.writeUint8((short) 0, byteArrayOutputStream);
        this.offeredCipherSuites = this.tlsClient.getCipherSuites();
        this.clientExtensions = this.tlsClient.getClientExtensions();
        boolean z = this.clientExtensions == null || this.clientExtensions.get(EXT_RenegotiationInfo) == null;
        int length = this.offeredCipherSuites.length;
        if (z) {
            length++;
        }
        TlsUtils.writeUint16(2 * length, byteArrayOutputStream);
        TlsUtils.writeUint16Array(this.offeredCipherSuites, byteArrayOutputStream);
        if (z) {
            TlsUtils.writeUint16(255, byteArrayOutputStream);
        }
        this.offeredCompressionMethods = this.tlsClient.getCompressionMethods();
        TlsUtils.writeUint8((short) this.offeredCompressionMethods.length, byteArrayOutputStream);
        TlsUtils.writeUint8Array(this.offeredCompressionMethods, byteArrayOutputStream);
        if (this.clientExtensions != null) {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            Enumeration enumerationKeys = this.clientExtensions.keys();
            while (enumerationKeys.hasMoreElements()) {
                Integer num = (Integer) enumerationKeys.nextElement();
                writeExtension(byteArrayOutputStream2, num, (byte[]) this.clientExtensions.get(num));
            }
            TlsUtils.writeOpaque16(byteArrayOutputStream2.toByteArray(), byteArrayOutputStream);
        }
        ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short) 1, byteArrayOutputStream3);
        TlsUtils.writeUint24(byteArrayOutputStream.size(), byteArrayOutputStream3);
        byteArrayOutputStream3.write(byteArrayOutputStream.toByteArray());
        byte[] byteArray = byteArrayOutputStream3.toByteArray();
        safeWriteMessage((short) 22, byteArray, 0, byteArray.length);
        this.connection_state = (short) 1;
        while (this.connection_state != 12) {
            safeReadData();
        }
        this.tlsInputStream = new TlsInputStream(this);
        this.tlsOutputStream = new TlsOutputStream(this);
    }

    protected int readApplicationData(byte[] bArr, int i, int i2) throws IOException {
        while (this.applicationDataQueue.size() == 0) {
            if (this.closed) {
                if (this.failedWithError) {
                    throw new IOException(TLS_ERROR_MESSAGE);
                }
                return -1;
            }
            safeReadData();
        }
        int iMin = Math.min(i2, this.applicationDataQueue.size());
        this.applicationDataQueue.read(bArr, i, iMin, 0);
        this.applicationDataQueue.removeData(iMin);
        return iMin;
    }

    private void safeReadData() throws IOException {
        try {
            this.rs.readData();
        } catch (TlsFatalAlert e) {
            if (!this.closed) {
                failWithError((short) 2, e.getAlertDescription());
            }
            throw e;
        } catch (IOException e2) {
            if (!this.closed) {
                failWithError((short) 2, (short) 80);
            }
            throw e2;
        } catch (RuntimeException e3) {
            if (!this.closed) {
                failWithError((short) 2, (short) 80);
            }
            throw e3;
        }
    }

    private void safeWriteMessage(short s, byte[] bArr, int i, int i2) throws IOException {
        try {
            this.rs.writeMessage(s, bArr, i, i2);
        } catch (TlsFatalAlert e) {
            if (!this.closed) {
                failWithError((short) 2, e.getAlertDescription());
            }
            throw e;
        } catch (IOException e2) {
            if (!this.closed) {
                failWithError((short) 2, (short) 80);
            }
            throw e2;
        } catch (RuntimeException e3) {
            if (!this.closed) {
                failWithError((short) 2, (short) 80);
            }
            throw e3;
        }
    }

    protected void writeData(byte[] bArr, int i, int i2) throws IOException {
        if (this.closed) {
            if (!this.failedWithError) {
                throw new IOException("Sorry, connection has been closed, you cannot write more data");
            }
            throw new IOException(TLS_ERROR_MESSAGE);
        }
        safeWriteMessage((short) 23, emptybuf, 0, 0);
        do {
            int iMin = Math.min(i2, 16384);
            safeWriteMessage((short) 23, bArr, i, iMin);
            i += iMin;
            i2 -= iMin;
        } while (i2 > 0);
    }

    public OutputStream getOutputStream() {
        return this.tlsOutputStream;
    }

    public InputStream getInputStream() {
        return this.tlsInputStream;
    }

    private void failWithError(short s, short s2) throws IOException {
        if (this.closed) {
            throw new IOException(TLS_ERROR_MESSAGE);
        }
        this.closed = true;
        if (s == 2) {
            this.failedWithError = true;
        }
        sendAlert(s, s2);
        this.rs.close();
        if (s == 2) {
            throw new IOException(TLS_ERROR_MESSAGE);
        }
    }

    private void sendAlert(short s, short s2) throws IOException {
        this.rs.writeMessage((short) 21, new byte[]{(byte) s, (byte) s2}, 0, 2);
    }

    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        failWithError((short) 1, (short) 0);
    }

    protected void assertEmpty(ByteArrayInputStream byteArrayInputStream) throws IOException {
        if (byteArrayInputStream.available() > 0) {
            throw new TlsFatalAlert((short) 50);
        }
    }

    protected void flush() throws IOException {
        this.rs.flush();
    }

    private static boolean arrayContains(short[] sArr, short s) {
        for (short s2 : sArr) {
            if (s2 == s) {
                return true;
            }
        }
        return false;
    }

    private static boolean arrayContains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private static byte[] createRenegotiationInfo(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeOpaque8(bArr, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static void writeExtension(OutputStream outputStream, Integer num, byte[] bArr) throws IOException {
        TlsUtils.writeUint16(num.intValue(), outputStream);
        TlsUtils.writeOpaque16(bArr, outputStream);
    }
}
