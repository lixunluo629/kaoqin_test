package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.tls.TlsProtocol;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsServerProtocol.class */
public class TlsServerProtocol extends TlsProtocol {
    protected TlsServer tlsServer;
    TlsServerContextImpl tlsServerContext;
    protected TlsKeyExchange keyExchange;
    protected TlsCredentials serverCredentials;
    protected CertificateRequest certificateRequest;
    protected short clientCertificateType;
    protected TlsHandshakeHash prepareFinishHash;

    public TlsServerProtocol(InputStream inputStream, OutputStream outputStream, SecureRandom secureRandom) {
        super(inputStream, outputStream, secureRandom);
        this.tlsServer = null;
        this.tlsServerContext = null;
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.clientCertificateType = (short) -1;
        this.prepareFinishHash = null;
    }

    public TlsServerProtocol(SecureRandom secureRandom) {
        super(secureRandom);
        this.tlsServer = null;
        this.tlsServerContext = null;
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.clientCertificateType = (short) -1;
        this.prepareFinishHash = null;
    }

    public void accept(TlsServer tlsServer) throws IOException {
        if (tlsServer == null) {
            throw new IllegalArgumentException("'tlsServer' cannot be null");
        }
        if (this.tlsServer != null) {
            throw new IllegalStateException("'accept' can only be called once");
        }
        this.tlsServer = tlsServer;
        this.securityParameters = new SecurityParameters();
        this.securityParameters.entity = 0;
        this.tlsServerContext = new TlsServerContextImpl(this.secureRandom, this.securityParameters);
        this.securityParameters.serverRandom = createRandomBlock(tlsServer.shouldUseGMTUnixTime(), this.tlsServerContext.getNonceRandomGenerator());
        this.tlsServer.init(this.tlsServerContext);
        this.recordStream.init(this.tlsServerContext);
        tlsServer.notifyCloseHandle(this);
        this.recordStream.setRestrictReadVersion(false);
        blockForHandshake();
    }

    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected void cleanupHandshake() {
        super.cleanupHandshake();
        this.keyExchange = null;
        this.serverCredentials = null;
        this.certificateRequest = null;
        this.prepareFinishHash = null;
    }

    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected TlsContext getContext() {
        return this.tlsServerContext;
    }

    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    AbstractTlsContext getContextAdmin() {
        return this.tlsServerContext;
    }

    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected TlsPeer getPeer() {
        return this.tlsServer;
    }

    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected void handleHandshakeMessage(short s, ByteArrayInputStream byteArrayInputStream) throws IOException {
        CertificateStatus certificateStatus;
        switch (s) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
            case 17:
            case 18:
            case 19:
            case 21:
            case 22:
            default:
                throw new TlsFatalAlert((short) 10);
            case 1:
                switch (this.connection_state) {
                    case 0:
                        receiveClientHelloMessage(byteArrayInputStream);
                        this.connection_state = (short) 1;
                        sendServerHelloMessage();
                        this.connection_state = (short) 2;
                        this.recordStream.notifyHelloComplete();
                        Vector serverSupplementalData = this.tlsServer.getServerSupplementalData();
                        if (serverSupplementalData != null) {
                            sendSupplementalDataMessage(serverSupplementalData);
                        }
                        this.connection_state = (short) 3;
                        this.keyExchange = this.tlsServer.getKeyExchange();
                        this.keyExchange.init(getContext());
                        this.serverCredentials = this.tlsServer.getCredentials();
                        Certificate certificate = null;
                        if (this.serverCredentials == null) {
                            this.keyExchange.skipServerCredentials();
                        } else {
                            this.keyExchange.processServerCredentials(this.serverCredentials);
                            certificate = this.serverCredentials.getCertificate();
                            sendCertificateMessage(certificate);
                        }
                        this.connection_state = (short) 4;
                        if (certificate == null || certificate.isEmpty()) {
                            this.allowCertificateStatus = false;
                        }
                        if (this.allowCertificateStatus && (certificateStatus = this.tlsServer.getCertificateStatus()) != null) {
                            sendCertificateStatusMessage(certificateStatus);
                        }
                        this.connection_state = (short) 5;
                        byte[] bArrGenerateServerKeyExchange = this.keyExchange.generateServerKeyExchange();
                        if (bArrGenerateServerKeyExchange != null) {
                            sendServerKeyExchangeMessage(bArrGenerateServerKeyExchange);
                        }
                        this.connection_state = (short) 6;
                        if (this.serverCredentials != null) {
                            this.certificateRequest = this.tlsServer.getCertificateRequest();
                            if (this.certificateRequest != null) {
                                if (TlsUtils.isTLSv12(getContext()) != (this.certificateRequest.getSupportedSignatureAlgorithms() != null)) {
                                    throw new TlsFatalAlert((short) 80);
                                }
                                this.keyExchange.validateCertificateRequest(this.certificateRequest);
                                sendCertificateRequestMessage(this.certificateRequest);
                                TlsUtils.trackHashAlgorithms(this.recordStream.getHandshakeHash(), this.certificateRequest.getSupportedSignatureAlgorithms());
                            }
                        }
                        this.connection_state = (short) 7;
                        sendServerHelloDoneMessage();
                        this.connection_state = (short) 8;
                        this.recordStream.getHandshakeHash().sealHashAlgorithms();
                        return;
                    case 16:
                        refuseRenegotiation();
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case 11:
                switch (this.connection_state) {
                    case 8:
                        this.tlsServer.processClientSupplementalData(null);
                        break;
                    case 9:
                        break;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                if (this.certificateRequest == null) {
                    throw new TlsFatalAlert((short) 10);
                }
                receiveCertificateMessage(byteArrayInputStream);
                this.connection_state = (short) 10;
                return;
            case 15:
                switch (this.connection_state) {
                    case 11:
                        if (!expectCertificateVerifyMessage()) {
                            throw new TlsFatalAlert((short) 10);
                        }
                        receiveCertificateVerifyMessage(byteArrayInputStream);
                        this.connection_state = (short) 12;
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case 16:
                switch (this.connection_state) {
                    case 8:
                        this.tlsServer.processClientSupplementalData(null);
                    case 9:
                        if (this.certificateRequest == null) {
                            this.keyExchange.skipClientCredentials();
                        } else {
                            if (TlsUtils.isTLSv12(getContext())) {
                                throw new TlsFatalAlert((short) 10);
                            }
                            if (!TlsUtils.isSSL(getContext())) {
                                notifyClientCertificate(Certificate.EMPTY_CHAIN);
                            } else if (this.peerCertificate == null) {
                                throw new TlsFatalAlert((short) 10);
                            }
                        }
                    case 10:
                        receiveClientKeyExchangeMessage(byteArrayInputStream);
                        this.connection_state = (short) 11;
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
            case 20:
                switch (this.connection_state) {
                    case 11:
                        if (expectCertificateVerifyMessage()) {
                            throw new TlsFatalAlert((short) 10);
                        }
                        break;
                    case 12:
                        break;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
                processFinishedMessage(byteArrayInputStream);
                this.connection_state = (short) 13;
                if (this.expectSessionTicket) {
                    sendNewSessionTicketMessage(this.tlsServer.getNewSessionTicket());
                }
                this.connection_state = (short) 14;
                sendChangeCipherSpecMessage();
                sendFinishedMessage();
                this.connection_state = (short) 15;
                completeHandshake();
                return;
            case 23:
                switch (this.connection_state) {
                    case 8:
                        this.tlsServer.processClientSupplementalData(readSupplementalDataMessage(byteArrayInputStream));
                        this.connection_state = (short) 9;
                        return;
                    default:
                        throw new TlsFatalAlert((short) 10);
                }
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsProtocol
    protected void handleAlertWarningMessage(short s) throws IOException {
        super.handleAlertWarningMessage(s);
        switch (s) {
            case 41:
                if (TlsUtils.isSSL(getContext()) && this.certificateRequest != null) {
                    switch (this.connection_state) {
                        case 8:
                            this.tlsServer.processClientSupplementalData(null);
                            break;
                        case 9:
                            break;
                    }
                    notifyClientCertificate(Certificate.EMPTY_CHAIN);
                    this.connection_state = (short) 10;
                    return;
                }
                throw new TlsFatalAlert((short) 10);
            default:
                return;
        }
    }

    protected void notifyClientCertificate(Certificate certificate) throws IOException {
        if (this.certificateRequest == null) {
            throw new IllegalStateException();
        }
        if (this.peerCertificate != null) {
            throw new TlsFatalAlert((short) 10);
        }
        this.peerCertificate = certificate;
        if (certificate.isEmpty()) {
            this.keyExchange.skipClientCredentials();
        } else {
            this.clientCertificateType = TlsUtils.getClientCertificateType(certificate, this.serverCredentials.getCertificate());
            this.keyExchange.processClientCertificate(certificate);
        }
        this.tlsServer.notifyClientCertificate(certificate);
    }

    protected void receiveCertificateMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        Certificate certificate = Certificate.parse(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        notifyClientCertificate(certificate);
    }

    protected void receiveCertificateVerifyMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        byte[] sessionHash;
        if (this.certificateRequest == null) {
            throw new IllegalStateException();
        }
        DigitallySigned digitallySigned = DigitallySigned.parse(getContext(), byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        try {
            SignatureAndHashAlgorithm algorithm = digitallySigned.getAlgorithm();
            if (TlsUtils.isTLSv12(getContext())) {
                TlsUtils.verifySupportedSignatureAlgorithm(this.certificateRequest.getSupportedSignatureAlgorithms(), algorithm);
                sessionHash = this.prepareFinishHash.getFinalHash(algorithm.getHash());
            } else {
                sessionHash = this.securityParameters.getSessionHash();
            }
            AsymmetricKeyParameter asymmetricKeyParameterCreateKey = PublicKeyFactory.createKey(this.peerCertificate.getCertificateAt(0).getSubjectPublicKeyInfo());
            TlsSigner tlsSignerCreateTlsSigner = TlsUtils.createTlsSigner(this.clientCertificateType);
            tlsSignerCreateTlsSigner.init(getContext());
            if (tlsSignerCreateTlsSigner.verifyRawSignature(algorithm, digitallySigned.getSignature(), asymmetricKeyParameterCreateKey, sessionHash)) {
            } else {
                throw new TlsFatalAlert((short) 51);
            }
        } catch (TlsFatalAlert e) {
            throw e;
        } catch (Exception e2) {
            throw new TlsFatalAlert((short) 51, e2);
        }
    }

    protected void receiveClientHelloMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        ProtocolVersion version = TlsUtils.readVersion(byteArrayInputStream);
        this.recordStream.setWriteVersion(version);
        if (version.isDTLS()) {
            throw new TlsFatalAlert((short) 47);
        }
        byte[] fully = TlsUtils.readFully(32, byteArrayInputStream);
        if (TlsUtils.readOpaque8(byteArrayInputStream).length > 32) {
            throw new TlsFatalAlert((short) 47);
        }
        int uint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (uint16 < 2 || (uint16 & 1) != 0) {
            throw new TlsFatalAlert((short) 50);
        }
        this.offeredCipherSuites = TlsUtils.readUint16Array(uint16 / 2, byteArrayInputStream);
        short uint8 = TlsUtils.readUint8(byteArrayInputStream);
        if (uint8 < 1) {
            throw new TlsFatalAlert((short) 47);
        }
        this.offeredCompressionMethods = TlsUtils.readUint8Array(uint8, byteArrayInputStream);
        this.clientExtensions = readExtensions(byteArrayInputStream);
        this.securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(this.clientExtensions);
        if (!this.securityParameters.isExtendedMasterSecret() && this.tlsServer.requiresExtendedMasterSecret()) {
            throw new TlsFatalAlert((short) 40);
        }
        getContextAdmin().setClientVersion(version);
        this.tlsServer.notifyClientVersion(version);
        this.tlsServer.notifyFallback(Arrays.contains(this.offeredCipherSuites, 22016));
        this.securityParameters.clientRandom = fully;
        this.tlsServer.notifyOfferedCipherSuites(this.offeredCipherSuites);
        this.tlsServer.notifyOfferedCompressionMethods(this.offeredCompressionMethods);
        if (Arrays.contains(this.offeredCipherSuites, 255)) {
            this.secure_renegotiation = true;
        }
        byte[] extensionData = TlsUtils.getExtensionData(this.clientExtensions, EXT_RenegotiationInfo);
        if (extensionData != null) {
            this.secure_renegotiation = true;
            if (!Arrays.constantTimeAreEqual(extensionData, createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                throw new TlsFatalAlert((short) 40);
            }
        }
        this.tlsServer.notifySecureRenegotiation(this.secure_renegotiation);
        if (this.clientExtensions != null) {
            TlsExtensionsUtils.getPaddingExtension(this.clientExtensions);
            this.tlsServer.processClientExtensions(this.clientExtensions);
        }
    }

    protected void receiveClientKeyExchangeMessage(ByteArrayInputStream byteArrayInputStream) throws IOException {
        this.keyExchange.processClientKeyExchange(byteArrayInputStream);
        assertEmpty(byteArrayInputStream);
        if (TlsUtils.isSSL(getContext())) {
            establishMasterSecret(getContext(), this.keyExchange);
        }
        this.prepareFinishHash = this.recordStream.prepareToFinish();
        this.securityParameters.sessionHash = getCurrentPRFHash(getContext(), this.prepareFinishHash, null);
        if (!TlsUtils.isSSL(getContext())) {
            establishMasterSecret(getContext(), this.keyExchange);
        }
        this.recordStream.setPendingConnectionState(getPeer().getCompression(), getPeer().getCipher());
    }

    protected void sendCertificateRequestMessage(CertificateRequest certificateRequest) throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, (short) 13);
        certificateRequest.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendCertificateStatusMessage(CertificateStatus certificateStatus) throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, (short) 22);
        certificateStatus.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendNewSessionTicketMessage(NewSessionTicket newSessionTicket) throws IOException {
        if (newSessionTicket == null) {
            throw new TlsFatalAlert((short) 80);
        }
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, (short) 4);
        newSessionTicket.encode(handshakeMessage);
        handshakeMessage.writeToRecordStream();
    }

    protected void sendServerHelloMessage() throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage(this, (short) 2);
        ProtocolVersion serverVersion = this.tlsServer.getServerVersion();
        if (!serverVersion.isEqualOrEarlierVersionOf(getContext().getClientVersion())) {
            throw new TlsFatalAlert((short) 80);
        }
        this.recordStream.setReadVersion(serverVersion);
        this.recordStream.setWriteVersion(serverVersion);
        this.recordStream.setRestrictReadVersion(true);
        getContextAdmin().setServerVersion(serverVersion);
        TlsUtils.writeVersion(serverVersion, handshakeMessage);
        handshakeMessage.write(this.securityParameters.serverRandom);
        TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, handshakeMessage);
        int selectedCipherSuite = this.tlsServer.getSelectedCipherSuite();
        if (!Arrays.contains(this.offeredCipherSuites, selectedCipherSuite) || selectedCipherSuite == 0 || CipherSuite.isSCSV(selectedCipherSuite) || !TlsUtils.isValidCipherSuiteForVersion(selectedCipherSuite, getContext().getServerVersion())) {
            throw new TlsFatalAlert((short) 80);
        }
        this.securityParameters.cipherSuite = selectedCipherSuite;
        short selectedCompressionMethod = this.tlsServer.getSelectedCompressionMethod();
        if (!Arrays.contains(this.offeredCompressionMethods, selectedCompressionMethod)) {
            throw new TlsFatalAlert((short) 80);
        }
        this.securityParameters.compressionAlgorithm = selectedCompressionMethod;
        TlsUtils.writeUint16(selectedCipherSuite, handshakeMessage);
        TlsUtils.writeUint8(selectedCompressionMethod, handshakeMessage);
        this.serverExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(this.tlsServer.getServerExtensions());
        if (this.secure_renegotiation) {
            if (null == TlsUtils.getExtensionData(this.serverExtensions, EXT_RenegotiationInfo)) {
                this.serverExtensions.put(EXT_RenegotiationInfo, createRenegotiationInfo(TlsUtils.EMPTY_BYTES));
            }
        }
        if (TlsUtils.isSSL(this.tlsServerContext)) {
            this.securityParameters.extendedMasterSecret = false;
        } else if (this.securityParameters.isExtendedMasterSecret()) {
            TlsExtensionsUtils.addExtendedMasterSecretExtension(this.serverExtensions);
        }
        if (!this.serverExtensions.isEmpty()) {
            this.securityParameters.encryptThenMAC = TlsExtensionsUtils.hasEncryptThenMACExtension(this.serverExtensions);
            this.securityParameters.maxFragmentLength = processMaxFragmentLengthExtension(this.clientExtensions, this.serverExtensions, (short) 80);
            this.securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(this.serverExtensions);
            this.allowCertificateStatus = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsExtensionsUtils.EXT_status_request, (short) 80);
            this.expectSessionTicket = !this.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(this.serverExtensions, TlsProtocol.EXT_SessionTicket, (short) 80);
            writeExtensions(handshakeMessage, this.serverExtensions);
        }
        this.securityParameters.prfAlgorithm = getPRFAlgorithm(getContext(), this.securityParameters.getCipherSuite());
        this.securityParameters.verifyDataLength = 12;
        applyMaxFragmentLengthExtension();
        handshakeMessage.writeToRecordStream();
    }

    protected void sendServerHelloDoneMessage() throws IOException {
        byte[] bArr = new byte[4];
        TlsUtils.writeUint8((short) 14, bArr, 0);
        TlsUtils.writeUint24(0, bArr, 1);
        writeHandshakeMessage(bArr, 0, bArr.length);
    }

    protected void sendServerKeyExchangeMessage(byte[] bArr) throws IOException {
        TlsProtocol.HandshakeMessage handshakeMessage = new TlsProtocol.HandshakeMessage((short) 12, bArr.length);
        handshakeMessage.write(bArr);
        handshakeMessage.writeToRecordStream();
    }

    protected boolean expectCertificateVerifyMessage() {
        return this.clientCertificateType >= 0 && TlsUtils.hasSigningCapability(this.clientCertificateType);
    }
}
