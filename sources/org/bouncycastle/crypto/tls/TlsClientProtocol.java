package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsClientProtocol.class */
public class TlsClientProtocol extends TlsProtocol {
    protected TlsClient tlsClient;
    TlsClientContextImpl tlsClientContext;
    protected byte[] selectedSessionID;
    protected TlsKeyExchange keyExchange;
    protected TlsAuthentication authentication;
    protected CertificateStatus certificateStatus;
    protected CertificateRequest certificateRequest;

    public TlsClientProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
        this.tlsClient = null;
        this.tlsClientContext = null;
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }

    public TlsClientProtocol(SecureRandom secureRandom) {
        super(secureRandom);
        this.tlsClient = null;
        this.tlsClientContext = null;
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    public void connect(TlsClient tlsClient) throws IOException {
        SessionParameters sessionParametersExportSessionParameters;
        if (tlsClient == null) {
            throw new IllegalArgumentException("'tlsClient' cannot be null");
        }
        if (this.tlsClient != null) {
            throw new IllegalStateException("'connect' can only be called once");
        }
        this.tlsClient = tlsClient;
        this.securityParameters = new SecurityParameters();
        this.securityParameters.entity = 1;
        this.tlsClientContext = new TlsClientContextImpl(this.secureRandom, this.securityParameters);
        this.securityParameters.clientRandom = createRandomBlock(tlsClient.shouldUseGMTUnixTime(), this.tlsClientContext.getNonceRandomGenerator());
        this.tlsClient.init(this.tlsClientContext);
        this.recordStream.init(this.tlsClientContext);
        tlsClient.notifyCloseHandle(this);
        TlsSession sessionToResume = tlsClient.getSessionToResume();
        if (sessionToResume != null && sessionToResume.isResumable() && (sessionParametersExportSessionParameters = sessionToResume.exportSessionParameters()) != null && sessionParametersExportSessionParameters.isExtendedMasterSecret()) {
            this.tlsSession = sessionToResume;
            this.sessionParameters = sessionParametersExportSessionParameters;
        }
        sendClientHelloMessage();
        this.connection_state = (short) 1;
        blockForHandshake();
    }

    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected void cleanupHandshake() {
        super.cleanupHandshake();
        this.selectedSessionID = null;
        this.keyExchange = null;
        this.authentication = null;
        this.certificateStatus = null;
        this.certificateRequest = null;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected TlsContext getContext() {
        return this.tlsClientContext;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.crypto.tls.AbstractTlsContext, org.bouncycastle.crypto.tls.TlsClientContextImpl] */
    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    AbstractTlsContext getContextAdmin() {
        return this.tlsClientContext;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.crypto.tls.TlsClient, org.bouncycastle.crypto.tls.TlsPeer] */
    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected TlsPeer getPeer() {
        return this.tlsClient;
    }

    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected void handleHandshakeMessage(short s, ByteArrayInputStream byteArrayInputStream) throws IOException {
        if (this.resumedSession) {
            if (s != 20 || this.connection_state != 2) {
                throw new TlsFatalAlert((short) 10);
            }
            processFinishedMessage(byteArrayInputStream);
            this.connection_state = (short) 15;
            sendChangeCipherSpecMessage();
            sendFinishedMessage();
            this.connection_state = (short) 13;
            completeHandshake();
            return;
        }
        switch (s) {
            case 0:
                assertEmpty(byteArrayInputStream);
                if (this.connection_state == 16) {
                    refuseRenegotiation();
                    return;
                }
                return;
            case 1:
            case 3:
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
            case 21:
            default:
                throw new TlsFatalAlert((short) 10);
            case 2:
                switch (this.connection_state) {
                    case 1:
                        receiveServerHelloMessage(byteArrayInputStream);
                        this.connection_state = (short) 2;
                        this.recordStream.notifyHelloComplete();
                        applyMaxFragmentLengthExtension();
                        if (this.resumedSession) {
                            this.securityParameters.masterSecret = Arrays.clone(this.sessionParameters.getMasterSecret());
                            this.recordStream.setPendingConnectionState(getPeer().getCompression(), getPeer().getCipher());
                            return;
                        } else {
                            invalidateSession();
                            if (this.selectedSessionID.length > 0) {
                                this.tlsSession = new TlsSessionImpl(this.selectedSessionID, null);
                                return;
                            }
                            return;
                        }
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case 4:
                switch (this.connection_state) {
                    case 13:
                        if (!this.expectSessionTicket) {
                            throw new TlsFatalAlert((short) 10);
                        }
                        invalidateSession();
                        receiveNewSessionTicketMessage(byteArrayInputStream);
                        this.connection_state = (short) 14;
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case 11:
                switch (this.connection_state) {
                    case 2:
                        handleSupplementalData(null);
                        break;
                    case 3:
                        break;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                this.peerCertificate = Certificate.parse(byteArrayInputStream);
                assertEmpty(byteArrayInputStream);
                if (this.peerCertificate == null || this.peerCertificate.isEmpty()) {
                    this.allowCertificateStatus = false;
                }
                this.keyExchange.processServerCertificate(this.peerCertificate);
                this.authentication = this.tlsClient.getAuthentication();
                this.authentication.notifyServerCertificate(this.peerCertificate);
                this.connection_state = (short) 4;
                return;
            case 12:
                switch (this.connection_state) {
                    case 2:
                        handleSupplementalData(null);
                    case 3:
                        this.keyExchange.skipServerCredentials();
                        this.authentication = null;
                    case 4:
                    case 5:
                        this.keyExchange.processServerKeyExchange(byteArrayInputStream);
                        assertEmpty(byteArrayInputStream);
                        this.connection_state = (short) 6;
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case 13:
                switch (this.connection_state) {
                    case 4:
                    case 5:
                        this.keyExchange.skipServerKeyExchange();
                        break;
                    case 6:
                        break;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                if (this.authentication == null) {
                    throw new TlsFatalAlert((short) 40);
                }
                this.certificateRequest = CertificateRequest.parse(getContext(), byteArrayInputStream);
                assertEmpty(byteArrayInputStream);
                this.keyExchange.validateCertificateRequest(this.certificateRequest);
                TlsUtils.trackHashAlgorithms(this.recordStream.getHandshakeHash(), this.certificateRequest.getSupportedSignatureAlgorithms());
                this.connection_state = (short) 7;
                return;
            case 14:
                switch (this.connection_state) {
                    case 2:
                        handleSupplementalData(null);
                    case 3:
                        this.keyExchange.skipServerCredentials();
                        this.authentication = null;
                    case 4:
                    case 5:
                        this.keyExchange.skipServerKeyExchange();
                    case 6:
                    case 7:
                        assertEmpty(byteArrayInputStream);
                        this.connection_state = (short) 8;
                        this.recordStream.getHandshakeHash().sealHashAlgorithms();
                        Vector clientSupplementalData = this.tlsClient.getClientSupplementalData();
                        if (clientSupplementalData != null) {
                            sendSupplementalDataMessage(clientSupplementalData);
                        }
                        this.connection_state = (short) 9;
                        TlsCredentials clientCredentials = null;
                        if (this.certificateRequest == null) {
                            this.keyExchange.skipClientCredentials();
                        } else {
                            clientCredentials = this.authentication.getClientCredentials(this.certificateRequest);
                            if (clientCredentials == null) {
                                this.keyExchange.skipClientCredentials();
                                sendCertificateMessage(Certificate.EMPTY_CHAIN);
                            } else {
                                this.keyExchange.processClientCredentials(clientCredentials);
                                sendCertificateMessage(clientCredentials.getCertificate());
                            }
                        }
                        this.connection_state = (short) 10;
                        sendClientKeyExchangeMessage();
                        this.connection_state = (short) 11;
                        if (TlsUtils.isSSL(getContext())) {
                            establishMasterSecret(getContext(), this.keyExchange);
                        }
                        TlsHandshakeHash tlsHandshakeHashPrepareToFinish = this.recordStream.prepareToFinish();
                        this.securityParameters.sessionHash = getCurrentPRFHash(getContext(), tlsHandshakeHashPrepareToFinish, null);
                        if (!TlsUtils.isSSL(getContext())) {
                            establishMasterSecret(getContext(), this.keyExchange);
                        }
                        this.recordStream.setPendingConnectionState(getPeer().getCompression(), getPeer().getCipher());
                        if (clientCredentials != null && (clientCredentials instanceof TlsSignerCredentials)) {
                            TlsSignerCredentials tlsSignerCredentials = (TlsSignerCredentials) clientCredentials;
                            SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(getContext(), tlsSignerCredentials);
                            sendCertificateVerifyMessage(new DigitallySigned(signatureAndHashAlgorithm, tlsSignerCredentials.generateCertificateSignature(signatureAndHashAlgorithm == null ? this.securityParameters.getSessionHash() : tlsHandshakeHashPrepareToFinish.getFinalHash(signatureAndHashAlgorithm.getHash()))));
                            this.connection_state = (short) 12;
                        }
                        sendChangeCipherSpecMessage();
                        sendFinishedMessage();
                        this.connection_state = (short) 13;
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                break;
            case 20:
                switch (this.connection_state) {
                    case 13:
                        if (this.expectSessionTicket) {
                            throw new TlsFatalAlert((short) 10);
                        }
                        break;
                    case 14:
                        break;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                processFinishedMessage(byteArrayInputStream);
                this.connection_state = (short) 15;
                completeHandshake();
                return;
            case 22:
                switch (this.connection_state) {
                    case 4:
                        if (!this.allowCertificateStatus) {
                            throw new TlsFatalAlert((short) 10);
                        }
                        this.certificateStatus = CertificateStatus.parse(byteArrayInputStream);
                        assertEmpty(byteArrayInputStream);
                        this.connection_state = (short) 5;
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case 23:
                switch (this.connection_state) {
                    case 2:
                        handleSupplementalData(readSupplementalDataMessage(byteArrayInputStream));
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
        }
    }

    protected void handleSupplementalData(Vector vector) throws IOException {
        this.tlsClient.processServerSupplementalData(vector);
        this.connection_state = (short) 3;
        this.keyExchange = this.tlsClient.getKeyExchange();
        this.keyExchange.init(getContext());
    }

    protected void receiveNewSessionTicketMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        NewSessionTicket newSessionTicket = NewSessionTicket.parse(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        this.tlsClient.notifyNewSessionTicket(newSessionTicket);
    }

    /* JADX WARN: Type inference failed for: r1v30, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    protected void receiveServerHelloMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        ProtocolVersion version = TlsUtils.readVersion(byteArrayInputStream);
        if (version.isDTLS()) {
            throw new TlsFatalAlert((short) 47);
        }
        if (!version.equals(this.recordStream.getReadVersion())) {
            throw new TlsFatalAlert((short) 47);
        }
        if (!version.isEqualOrEarlierVersionOf(getContext().getClientVersion())) {
            throw new TlsFatalAlert((short) 47);
        }
        this.recordStream.setWriteVersion(version);
        getContextAdmin().setServerVersion(version);
        this.tlsClient.notifyServerVersion(version);
        this.securityParameters.serverRandom = TlsUtils.readFully(32, byteArrayInputStream);
        this.selectedSessionID = TlsUtils.readOpaque8(byteArrayInputStream);
        if (this.selectedSessionID.length > 32) {
            throw new TlsFatalAlert((short) 47);
        }
        this.tlsClient.notifySessionID(this.selectedSessionID);
        this.resumedSession = this.selectedSessionID.length > 0 && this.tlsSession != null && Arrays.areEqual(this.selectedSessionID, this.tlsSession.getSessionID());
        int uint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (!Arrays.contains(this.offeredCipherSuites, uint16) || uint16 == 0 || CipherSuite.isSCSV(uint16) || !TlsUtils.isValidCipherSuiteForVersion(uint16, getContext().getServerVersion())) {
            throw new TlsFatalAlert((short) 47);
        }
        this.tlsClient.notifySelectedCipherSuite(uint16);
        short uint8 = TlsUtils.readUint8(byteArrayInputStream);
        if (!Arrays.contains(this.offeredCompressionMethods, uint8)) {
            throw new TlsFatalAlert((short) 47);
        }
        this.tlsClient.notifySelectedCompressionMethod(uint8);
        this.serverExtensions = readExtensions(byteArrayInputStream);
        this.securityParameters.extendedMasterSecret = !TlsUtils.isSSL(this.tlsClientContext) && TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.serverExtensions);
        if (!this.securityParameters.isExtendedMasterSecret() && (this.resumedSession || this.tlsClient.requiresExtendedMasterSecret())) {
            throw new TlsFatalAlert((short) 40);
        }
        if (this.serverExtensions != null) {
            Enumeration enumerationKeys = this.serverExtensions.keys();
            while (enumerationKeys.hasMoreElements()) {
                Integer num = (Integer) enumerationKeys.nextElement();
                if (!num.equals(EXT_RenegotiationInfo)) {
                    if (null == TlsUtils.getExtensionData(this.clientExtensions, num)) {
                        throw new TlsFatalAlert((short) 110);
                    }
                    if (this.resumedSession) {
                    }
                }
            }
        }
        byte[] extensionData = TlsUtils.getExtensionData(this.serverExtensions, EXT_RenegotiationInfo);
        if (extensionData != null) {
            this.secure_renegotiation = true;
            if (!Arrays.constantTimeAreEqual(extensionData, createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                throw new TlsFatalAlert((short) 40);
            }
        }
        this.tlsClient.notifySecureRenegotiation(this.secure_renegotiation);
        Hashtable hashtable = this.clientExtensions;
        Hashtable serverExtensions = this.serverExtensions;
        if (this.resumedSession) {
            if (uint16 != this.sessionParameters.getCipherSuite() || uint8 != this.sessionParameters.getCompressionAlgorithm()) {
                throw new TlsFatalAlert((short) 47);
            }
            hashtable = null;
            serverExtensions = this.sessionParameters.readServerExtensions();
        }
        this.securityParameters.cipherSuite = uint16;
        this.securityParameters.compressionAlgorithm = uint8;
        if (serverExtensions != null && !serverExtensions.isEmpty()) {
            boolean zHasEncryptThenMACExtension = TlsExtensionsUtils.hasEncryptThenMACExtension(serverExtensions);
            if (zHasEncryptThenMACExtension && !TlsUtils.isBlockCipherSuite(uint16)) {
                throw new TlsFatalAlert((short) 47);
            }
            this.securityParameters.encryptThenMAC = zHasEncryptThenMACExtension;
            this.securityParameters.maxFragmentLength = processMaxFragmentLengthExtension(hashtable, serverExtensions, (short) 47);
            this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(serverExtensions);
            this.allowCertificateStatus = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(serverExtensions, TlsExtensionsUtils.EXT_status_request, (short) 47);
            this.expectSessionTicket = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(serverExtensions, TlsProtocol.EXT_SessionTicket, (short) 47);
        }
        if (hashtable != null) {
            this.tlsClient.processServerExtensions(serverExtensions);
        }
        this.securityParameters.prfAlgorithm = getPRFAlgorithm(getContext(), this.securityParameters.getCipherSuite());
        this.securityParameters.verifyDataLength = 12;
    }

    protected void sendCertificateVerifyMessage(DigitallySigned digitallySigned) throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, (short) 15);
        digitallySigned.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendClientHelloMessage() throws IOException {
        this.recordStream.setWriteVersion(this.tlsClient.getClientHelloRecordLayerVersion());
        ProtocolVersion clientVersion = this.tlsClient.getClientVersion();
        if (clientVersion.isDTLS()) {
            throw new TlsFatalAlert((short) 80);
        }
        getContextAdmin().setClientVersion(clientVersion);
        byte[] sessionID = TlsUtils.EMPTY_BYTES;
        if (this.tlsSession != null) {
            sessionID = this.tlsSession.getSessionID();
            if (sessionID == null || sessionID.length > 32) {
                sessionID = TlsUtils.EMPTY_BYTES;
            }
        }
        boolean zIsFallback = this.tlsClient.isFallback();
        this.offeredCipherSuites = this.tlsClient.getCipherSuites();
        this.offeredCompressionMethods = this.tlsClient.getCompressionMethods();
        if (sessionID.length > 0 && this.sessionParameters != null && (!this.sessionParameters.isExtendedMasterSecret() || !Arrays.contains(this.offeredCipherSuites, this.sessionParameters.getCipherSuite()) || !Arrays.contains(this.offeredCompressionMethods, this.sessionParameters.getCompressionAlgorithm()))) {
            sessionID = TlsUtils.EMPTY_BYTES;
        }
        this.clientExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.tlsClient.getClientExtensions());
        if (!clientVersion.isSSL()) {
            TlsExtensionsUtils.addExtendedMasterSecretExtension(this.clientExtensions);
        }
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, (short) 1);
        TlsUtils.writeVersion(clientVersion, handshakeMessage);
        handshakeMessage.write(this.securityParameters.getClientRandom());
        TlsUtils.writeOpaque8(sessionID, handshakeMessage);
        boolean z = null == TlsUtils.getExtensionData(this.clientExtensions, EXT_RenegotiationInfo);
        boolean z2 = !Arrays.contains(this.offeredCipherSuites, 255);
        if (z && z2) {
            this.offeredCipherSuites = Arrays.append(this.offeredCipherSuites, 255);
        }
        if (zIsFallback && !Arrays.contains(this.offeredCipherSuites, 22016)) {
            this.offeredCipherSuites = Arrays.append(this.offeredCipherSuites, 22016);
        }
        TlsUtils.writeUint16ArrayWithUint16Length(this.offeredCipherSuites, handshakeMessage);
        TlsUtils.writeUint8ArrayWithUint8Length(this.offeredCompressionMethods, handshakeMessage);
        writeExtensions(handshakeMessage, this.clientExtensions);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendClientKeyExchangeMessage() throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, (short) 16);
        this.keyExchange.generateClientKeyExchange(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }
}
