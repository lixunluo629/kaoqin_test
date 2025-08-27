package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.tls.DTLSReliableHandshake;
import org.bouncycastle.crypto.tls.SessionParameters;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSClientProtocol.class */
public class DTLSClientProtocol extends DTLSProtocol {

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSClientProtocol$ClientHandshakeState.class */
    protected static class ClientHandshakeState {
        TlsClient client = null;
        TlsClientContextImpl clientContext = null;
        TlsSession tlsSession = null;
        SessionParameters sessionParameters = null;
        SessionParameters.Builder sessionParametersBuilder = null;
        int[] offeredCipherSuites = null;
        Hashtable clientExtensions = null;
        Hashtable serverExtensions = null;
        byte[] selectedSessionID = null;
        boolean resumedSession = false;
        boolean secure_renegotiation = false;
        boolean allowCertificateStatus = false;
        boolean expectSessionTicket = false;
        TlsKeyExchange keyExchange = null;
        TlsAuthentication authentication = null;
        CertificateStatus certificateStatus = null;
        CertificateRequest certificateRequest = null;
        TlsCredentials clientCredentials = null;

        protected ClientHandshakeState() {
        }
    }

    public DTLSClientProtocol(SecureRandom secureRandom) {
        super(secureRandom);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    public DTLSTransport connect(TlsClient tlsClient, DatagramTransport datagramTransport) throws IOException {
        SessionParameters sessionParametersExportSessionParameters;
        if (tlsClient == 0) {
            throw new IllegalArgumentException("'client' cannot be null");
        }
        if (datagramTransport == null) {
            throw new IllegalArgumentException("'transport' cannot be null");
        }
        SecurityParameters securityParameters = new SecurityParameters();
        securityParameters.entity = 1;
        ClientHandshakeState clientHandshakeState = new ClientHandshakeState();
        clientHandshakeState.client = tlsClient;
        clientHandshakeState.clientContext = new TlsClientContextImpl(this.secureRandom, securityParameters);
        securityParameters.clientRandom = TlsProtocol.createRandomBlock(tlsClient.shouldUseGMTUnixTime(), clientHandshakeState.clientContext.getNonceRandomGenerator());
        tlsClient.init(clientHandshakeState.clientContext);
        DTLSRecordLayer dTLSRecordLayer = new DTLSRecordLayer(datagramTransport, clientHandshakeState.clientContext, tlsClient, (short) 22);
        tlsClient.notifyCloseHandle(dTLSRecordLayer);
        TlsSession sessionToResume = clientHandshakeState.client.getSessionToResume();
        if (sessionToResume != null && sessionToResume.isResumable() && (sessionParametersExportSessionParameters = sessionToResume.exportSessionParameters()) != null && sessionParametersExportSessionParameters.isExtendedMasterSecret()) {
            clientHandshakeState.tlsSession = sessionToResume;
            clientHandshakeState.sessionParameters = sessionParametersExportSessionParameters;
        }
        try {
            try {
                try {
                    DTLSTransport dTLSTransportClientHandshake = clientHandshake(clientHandshakeState, dTLSRecordLayer);
                    securityParameters.clear();
                    return dTLSTransportClientHandshake;
                } catch (IOException e) {
                    abortClientHandshake(clientHandshakeState, dTLSRecordLayer, (short) 80);
                    throw e;
                } catch (RuntimeException e2) {
                    abortClientHandshake(clientHandshakeState, dTLSRecordLayer, (short) 80);
                    throw new TlsFatalAlert((short) 80, e2);
                }
            } catch (TlsFatalAlert e3) {
                abortClientHandshake(clientHandshakeState, dTLSRecordLayer, e3.getAlertDescription());
                throw e3;
            }
        } catch (Throwable th) {
            securityParameters.clear();
            throw th;
        }
    }

    protected void abortClientHandshake(ClientHandshakeState clientHandshakeState, DTLSRecordLayer dTLSRecordLayer, short s) {
        dTLSRecordLayer.fail(s);
        invalidateSession(clientHandshakeState);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v115, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r0v170, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r0v174, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r0v78, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r0v83, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r0v89, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r1v19, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r1v30, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r2v1, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r2v13, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r2v17, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r2v63, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    /* JADX WARN: Type inference failed for: r2v68, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    protected DTLSTransport clientHandshake(ClientHandshakeState clientHandshakeState, DTLSRecordLayer dTLSRecordLayer) throws IOException {
        SecurityParameters securityParameters = clientHandshakeState.clientContext.getSecurityParameters();
        DTLSReliableHandshake dTLSReliableHandshake = new DTLSReliableHandshake(clientHandshakeState.clientContext, dTLSRecordLayer);
        byte[] bArrGenerateClientHello = generateClientHello(clientHandshakeState, clientHandshakeState.client);
        dTLSRecordLayer.setWriteVersion(ProtocolVersion.DTLSv10);
        dTLSReliableHandshake.sendMessage((short) 1, bArrGenerateClientHello);
        DTLSReliableHandshake.Message messageReceiveMessage = dTLSReliableHandshake.receiveMessage();
        while (true) {
            DTLSReliableHandshake.Message message = messageReceiveMessage;
            if (message.getType() != 3) {
                if (message.getType() != 2) {
                    throw new TlsFatalAlert((short) 10);
                }
                ProtocolVersion readVersion = dTLSRecordLayer.getReadVersion();
                reportServerVersion(clientHandshakeState, readVersion);
                dTLSRecordLayer.setWriteVersion(readVersion);
                processServerHello(clientHandshakeState, message.getBody());
                dTLSReliableHandshake.notifyHelloComplete();
                applyMaxFragmentLengthExtension(dTLSRecordLayer, securityParameters.maxFragmentLength);
                if (clientHandshakeState.resumedSession) {
                    securityParameters.masterSecret = Arrays.clone(clientHandshakeState.sessionParameters.getMasterSecret());
                    dTLSRecordLayer.initPendingEpoch(clientHandshakeState.client.getCipher());
                    processFinished(dTLSReliableHandshake.receiveMessageBody((short) 20), TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, ExporterLabel.server_finished, TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                    dTLSReliableHandshake.sendMessage((short) 20, TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, ExporterLabel.client_finished, TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                    dTLSReliableHandshake.finish();
                    clientHandshakeState.clientContext.setResumableSession(clientHandshakeState.tlsSession);
                    clientHandshakeState.client.notifyHandshakeComplete();
                    return new DTLSTransport(dTLSRecordLayer);
                }
                invalidateSession(clientHandshakeState);
                if (clientHandshakeState.selectedSessionID.length > 0) {
                    clientHandshakeState.tlsSession = new TlsSessionImpl(clientHandshakeState.selectedSessionID, null);
                }
                DTLSReliableHandshake.Message messageReceiveMessage2 = dTLSReliableHandshake.receiveMessage();
                if (messageReceiveMessage2.getType() == 23) {
                    processServerSupplementalData(clientHandshakeState, messageReceiveMessage2.getBody());
                    messageReceiveMessage2 = dTLSReliableHandshake.receiveMessage();
                } else {
                    clientHandshakeState.client.processServerSupplementalData(null);
                }
                clientHandshakeState.keyExchange = clientHandshakeState.client.getKeyExchange();
                clientHandshakeState.keyExchange.init(clientHandshakeState.clientContext);
                Certificate certificateProcessServerCertificate = null;
                if (messageReceiveMessage2.getType() == 11) {
                    certificateProcessServerCertificate = processServerCertificate(clientHandshakeState, messageReceiveMessage2.getBody());
                    messageReceiveMessage2 = dTLSReliableHandshake.receiveMessage();
                } else {
                    clientHandshakeState.keyExchange.skipServerCredentials();
                }
                if (certificateProcessServerCertificate == null || certificateProcessServerCertificate.isEmpty()) {
                    clientHandshakeState.allowCertificateStatus = false;
                }
                if (messageReceiveMessage2.getType() == 22) {
                    processCertificateStatus(clientHandshakeState, messageReceiveMessage2.getBody());
                    messageReceiveMessage2 = dTLSReliableHandshake.receiveMessage();
                }
                if (messageReceiveMessage2.getType() == 12) {
                    processServerKeyExchange(clientHandshakeState, messageReceiveMessage2.getBody());
                    messageReceiveMessage2 = dTLSReliableHandshake.receiveMessage();
                } else {
                    clientHandshakeState.keyExchange.skipServerKeyExchange();
                }
                if (messageReceiveMessage2.getType() == 13) {
                    processCertificateRequest(clientHandshakeState, messageReceiveMessage2.getBody());
                    TlsUtils.trackHashAlgorithms(dTLSReliableHandshake.getHandshakeHash(), clientHandshakeState.certificateRequest.getSupportedSignatureAlgorithms());
                    messageReceiveMessage2 = dTLSReliableHandshake.receiveMessage();
                }
                if (messageReceiveMessage2.getType() != 14) {
                    throw new TlsFatalAlert((short) 10);
                }
                if (messageReceiveMessage2.getBody().length != 0) {
                    throw new TlsFatalAlert((short) 50);
                }
                dTLSReliableHandshake.getHandshakeHash().sealHashAlgorithms();
                Vector clientSupplementalData = clientHandshakeState.client.getClientSupplementalData();
                if (clientSupplementalData != null) {
                    dTLSReliableHandshake.sendMessage((short) 23, generateSupplementalData(clientSupplementalData));
                }
                if (clientHandshakeState.certificateRequest != null) {
                    clientHandshakeState.clientCredentials = clientHandshakeState.authentication.getClientCredentials(clientHandshakeState.certificateRequest);
                    Certificate certificate = null;
                    if (clientHandshakeState.clientCredentials != null) {
                        certificate = clientHandshakeState.clientCredentials.getCertificate();
                    }
                    if (certificate == null) {
                        certificate = Certificate.EMPTY_CHAIN;
                    }
                    dTLSReliableHandshake.sendMessage((short) 11, generateCertificate(certificate));
                }
                if (clientHandshakeState.clientCredentials != null) {
                    clientHandshakeState.keyExchange.processClientCredentials(clientHandshakeState.clientCredentials);
                } else {
                    clientHandshakeState.keyExchange.skipClientCredentials();
                }
                dTLSReliableHandshake.sendMessage((short) 16, generateClientKeyExchange(clientHandshakeState));
                TlsHandshakeHash tlsHandshakeHashPrepareToFinish = dTLSReliableHandshake.prepareToFinish();
                securityParameters.sessionHash = TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, tlsHandshakeHashPrepareToFinish, null);
                TlsProtocol.establishMasterSecret(clientHandshakeState.clientContext, clientHandshakeState.keyExchange);
                dTLSRecordLayer.initPendingEpoch(clientHandshakeState.client.getCipher());
                if (clientHandshakeState.clientCredentials != null && (clientHandshakeState.clientCredentials instanceof TlsSignerCredentials)) {
                    TlsSignerCredentials tlsSignerCredentials = (TlsSignerCredentials) clientHandshakeState.clientCredentials;
                    SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(clientHandshakeState.clientContext, tlsSignerCredentials);
                    dTLSReliableHandshake.sendMessage((short) 15, generateCertificateVerify(clientHandshakeState, new DigitallySigned(signatureAndHashAlgorithm, tlsSignerCredentials.generateCertificateSignature(signatureAndHashAlgorithm == null ? securityParameters.getSessionHash() : tlsHandshakeHashPrepareToFinish.getFinalHash(signatureAndHashAlgorithm.getHash())))));
                }
                dTLSReliableHandshake.sendMessage((short) 20, TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, ExporterLabel.client_finished, TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                if (clientHandshakeState.expectSessionTicket) {
                    DTLSReliableHandshake.Message messageReceiveMessage3 = dTLSReliableHandshake.receiveMessage();
                    if (messageReceiveMessage3.getType() != 4) {
                        throw new TlsFatalAlert((short) 10);
                    }
                    processNewSessionTicket(clientHandshakeState, messageReceiveMessage3.getBody());
                }
                processFinished(dTLSReliableHandshake.receiveMessageBody((short) 20), TlsUtils.calculateVerifyData(clientHandshakeState.clientContext, ExporterLabel.server_finished, TlsProtocol.getCurrentPRFHash(clientHandshakeState.clientContext, dTLSReliableHandshake.getHandshakeHash(), null)));
                dTLSReliableHandshake.finish();
                if (clientHandshakeState.tlsSession != null) {
                    clientHandshakeState.sessionParameters = new SessionParameters.Builder().setCipherSuite(securityParameters.getCipherSuite()).setCompressionAlgorithm(securityParameters.getCompressionAlgorithm()).setExtendedMasterSecret(securityParameters.isExtendedMasterSecret()).setMasterSecret(securityParameters.getMasterSecret()).setPeerCertificate(certificateProcessServerCertificate).setPSKIdentity(securityParameters.getPSKIdentity()).setSRPIdentity(securityParameters.getSRPIdentity()).setServerExtensions(clientHandshakeState.serverExtensions).build();
                    clientHandshakeState.tlsSession = TlsUtils.importSession(clientHandshakeState.tlsSession.getSessionID(), clientHandshakeState.sessionParameters);
                    clientHandshakeState.clientContext.setResumableSession(clientHandshakeState.tlsSession);
                }
                clientHandshakeState.client.notifyHandshakeComplete();
                return new DTLSTransport(dTLSRecordLayer);
            }
            if (!dTLSRecordLayer.getReadVersion().isEqualOrEarlierVersionOf(clientHandshakeState.clientContext.getClientVersion())) {
                throw new TlsFatalAlert((short) 47);
            }
            dTLSRecordLayer.setReadVersion(null);
            byte[] bArrPatchClientHelloWithCookie = patchClientHelloWithCookie(bArrGenerateClientHello, processHelloVerifyRequest(clientHandshakeState, message.getBody()));
            dTLSReliableHandshake.resetHandshakeMessagesDigest();
            dTLSReliableHandshake.sendMessage((short) 1, bArrPatchClientHelloWithCookie);
            messageReceiveMessage = dTLSReliableHandshake.receiveMessage();
        }
    }

    protected byte[] generateCertificateVerify(ClientHandshakeState clientHandshakeState, DigitallySigned digitallySigned) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        digitallySigned.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateClientHello(ClientHandshakeState clientHandshakeState, TlsClient tlsClient) throws IOException {
        ProtocolVersion clientVersion = tlsClient.getClientVersion();
        if (!clientVersion.isDTLS()) {
            throw new TlsFatalAlert((short) 80);
        }
        TlsClientContextImpl tlsClientContextImpl = clientHandshakeState.clientContext;
        tlsClientContextImpl.setClientVersion(clientVersion);
        SecurityParameters securityParameters = tlsClientContextImpl.getSecurityParameters();
        byte[] sessionID = TlsUtils.EMPTY_BYTES;
        if (clientHandshakeState.tlsSession != null) {
            sessionID = clientHandshakeState.tlsSession.getSessionID();
            if (sessionID == null || sessionID.length > 32) {
                sessionID = TlsUtils.EMPTY_BYTES;
            }
        }
        boolean zIsFallback = tlsClient.isFallback();
        clientHandshakeState.offeredCipherSuites = tlsClient.getCipherSuites();
        if (sessionID.length > 0 && clientHandshakeState.sessionParameters != null && (!clientHandshakeState.sessionParameters.isExtendedMasterSecret() || !Arrays.contains(clientHandshakeState.offeredCipherSuites, clientHandshakeState.sessionParameters.getCipherSuite()) || 0 != clientHandshakeState.sessionParameters.getCompressionAlgorithm())) {
            sessionID = TlsUtils.EMPTY_BYTES;
        }
        clientHandshakeState.clientExtensions = TlsExtensionsUtils.ensureExtensionsInitialised(tlsClient.getClientExtensions());
        TlsExtensionsUtils.addExtendedMasterSecretExtension(clientHandshakeState.clientExtensions);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeVersion(clientVersion, byteArrayOutputStream);
        byteArrayOutputStream.write(securityParameters.getClientRandom());
        TlsUtils.writeOpaque8(sessionID, byteArrayOutputStream);
        TlsUtils.writeOpaque8(TlsUtils.EMPTY_BYTES, byteArrayOutputStream);
        boolean z = null == TlsUtils.getExtensionData(clientHandshakeState.clientExtensions, TlsProtocol.EXT_RenegotiationInfo);
        boolean z2 = !Arrays.contains(clientHandshakeState.offeredCipherSuites, 255);
        if (z && z2) {
            clientHandshakeState.offeredCipherSuites = Arrays.append(clientHandshakeState.offeredCipherSuites, 255);
        }
        if (zIsFallback && !Arrays.contains(clientHandshakeState.offeredCipherSuites, 22016)) {
            clientHandshakeState.offeredCipherSuites = Arrays.append(clientHandshakeState.offeredCipherSuites, 22016);
        }
        TlsUtils.writeUint16ArrayWithUint16Length(clientHandshakeState.offeredCipherSuites, byteArrayOutputStream);
        TlsUtils.writeUint8ArrayWithUint8Length(new short[]{0}, byteArrayOutputStream);
        TlsProtocol.writeExtensions(byteArrayOutputStream, clientHandshakeState.clientExtensions);
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] generateClientKeyExchange(ClientHandshakeState clientHandshakeState) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        clientHandshakeState.keyExchange.generateClientKeyExchange(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected void invalidateSession(ClientHandshakeState clientHandshakeState) {
        if (clientHandshakeState.sessionParameters != null) {
            clientHandshakeState.sessionParameters.clear();
            clientHandshakeState.sessionParameters = null;
        }
        if (clientHandshakeState.tlsSession != null) {
            clientHandshakeState.tlsSession.invalidate();
            clientHandshakeState.tlsSession = null;
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    protected void processCertificateRequest(ClientHandshakeState clientHandshakeState, byte[] bArr) throws IOException {
        if (clientHandshakeState.authentication == null) {
            throw new TlsFatalAlert((short) 40);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        clientHandshakeState.certificateRequest = CertificateRequest.parse(clientHandshakeState.clientContext, byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.keyExchange.validateCertificateRequest(clientHandshakeState.certificateRequest);
    }

    protected void processCertificateStatus(ClientHandshakeState clientHandshakeState, byte[] bArr) throws IOException {
        if (!clientHandshakeState.allowCertificateStatus) {
            throw new TlsFatalAlert((short) 10);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        clientHandshakeState.certificateStatus = CertificateStatus.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
    }

    protected byte[] processHelloVerifyRequest(ClientHandshakeState clientHandshakeState, byte[] bArr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ProtocolVersion version = TlsUtils.readVersion(byteArrayInputStream);
        byte[] opaque8 = TlsUtils.readOpaque8(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (!version.isEqualOrEarlierVersionOf(clientHandshakeState.clientContext.getClientVersion())) {
            throw new TlsFatalAlert((short) 47);
        }
        if (ProtocolVersion.DTLSv12.isEqualOrEarlierVersionOf(version) || opaque8.length <= 32) {
            return opaque8;
        }
        throw new TlsFatalAlert((short) 47);
    }

    protected void processNewSessionTicket(ClientHandshakeState clientHandshakeState, byte[] bArr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        NewSessionTicket newSessionTicket = NewSessionTicket.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.client.notifyNewSessionTicket(newSessionTicket);
    }

    protected Certificate processServerCertificate(ClientHandshakeState clientHandshakeState, byte[] bArr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        Certificate certificate = Certificate.parse(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        clientHandshakeState.keyExchange.processServerCertificate(certificate);
        clientHandshakeState.authentication = clientHandshakeState.client.getAuthentication();
        clientHandshakeState.authentication.notifyServerCertificate(certificate);
        return certificate;
    }

    /* JADX WARN: Type inference failed for: r1v34, types: [org.bouncycastle.crypto.tls.TlsClientContextImpl, org.bouncycastle.crypto.tls.TlsContext] */
    protected void processServerHello(ClientHandshakeState clientHandshakeState, byte[] bArr) throws IOException {
        SecurityParameters securityParameters = clientHandshakeState.clientContext.getSecurityParameters();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        reportServerVersion(clientHandshakeState, TlsUtils.readVersion(byteArrayInputStream));
        securityParameters.serverRandom = TlsUtils.readFully(32, byteArrayInputStream);
        clientHandshakeState.selectedSessionID = TlsUtils.readOpaque8(byteArrayInputStream);
        if (clientHandshakeState.selectedSessionID.length > 32) {
            throw new TlsFatalAlert((short) 47);
        }
        clientHandshakeState.client.notifySessionID(clientHandshakeState.selectedSessionID);
        clientHandshakeState.resumedSession = clientHandshakeState.selectedSessionID.length > 0 && clientHandshakeState.tlsSession != null && Arrays.areEqual(clientHandshakeState.selectedSessionID, clientHandshakeState.tlsSession.getSessionID());
        int uint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (!Arrays.contains(clientHandshakeState.offeredCipherSuites, uint16) || uint16 == 0 || CipherSuite.isSCSV(uint16) || !TlsUtils.isValidCipherSuiteForVersion(uint16, clientHandshakeState.clientContext.getServerVersion())) {
            throw new TlsFatalAlert((short) 47);
        }
        validateSelectedCipherSuite(uint16, (short) 47);
        clientHandshakeState.client.notifySelectedCipherSuite(uint16);
        short uint8 = TlsUtils.readUint8(byteArrayInputStream);
        if (0 != uint8) {
            throw new TlsFatalAlert((short) 47);
        }
        clientHandshakeState.client.notifySelectedCompressionMethod(uint8);
        clientHandshakeState.serverExtensions = TlsProtocol.readExtensions(byteArrayInputStream);
        securityParameters.extendedMasterSecret = TlsExtensionsUtils.hasExtendedMasterSecretExtension(clientHandshakeState.serverExtensions);
        if (!securityParameters.isExtendedMasterSecret() && (clientHandshakeState.resumedSession || clientHandshakeState.client.requiresExtendedMasterSecret())) {
            throw new TlsFatalAlert((short) 40);
        }
        if (clientHandshakeState.serverExtensions != null) {
            Enumeration enumerationKeys = clientHandshakeState.serverExtensions.keys();
            while (enumerationKeys.hasMoreElements()) {
                Integer num = (Integer) enumerationKeys.nextElement();
                if (!num.equals(TlsProtocol.EXT_RenegotiationInfo)) {
                    if (null == TlsUtils.getExtensionData(clientHandshakeState.clientExtensions, num)) {
                        throw new TlsFatalAlert((short) 110);
                    }
                    if (clientHandshakeState.resumedSession) {
                    }
                }
            }
        }
        byte[] extensionData = TlsUtils.getExtensionData(clientHandshakeState.serverExtensions, TlsProtocol.EXT_RenegotiationInfo);
        if (extensionData != null) {
            clientHandshakeState.secure_renegotiation = true;
            if (!Arrays.constantTimeAreEqual(extensionData, TlsProtocol.createRenegotiationInfo(TlsUtils.EMPTY_BYTES))) {
                throw new TlsFatalAlert((short) 40);
            }
        }
        clientHandshakeState.client.notifySecureRenegotiation(clientHandshakeState.secure_renegotiation);
        Hashtable hashtable = clientHandshakeState.clientExtensions;
        Hashtable serverExtensions = clientHandshakeState.serverExtensions;
        if (clientHandshakeState.resumedSession) {
            if (uint16 != clientHandshakeState.sessionParameters.getCipherSuite() || uint8 != clientHandshakeState.sessionParameters.getCompressionAlgorithm()) {
                throw new TlsFatalAlert((short) 47);
            }
            hashtable = null;
            serverExtensions = clientHandshakeState.sessionParameters.readServerExtensions();
        }
        securityParameters.cipherSuite = uint16;
        securityParameters.compressionAlgorithm = uint8;
        if (serverExtensions != null && !serverExtensions.isEmpty()) {
            boolean zHasEncryptThenMACExtension = TlsExtensionsUtils.hasEncryptThenMACExtension(serverExtensions);
            if (zHasEncryptThenMACExtension && !TlsUtils.isBlockCipherSuite(securityParameters.getCipherSuite())) {
                throw new TlsFatalAlert((short) 47);
            }
            securityParameters.encryptThenMAC = zHasEncryptThenMACExtension;
            securityParameters.maxFragmentLength = evaluateMaxFragmentLengthExtension(clientHandshakeState.resumedSession, hashtable, serverExtensions, (short) 47);
            securityParameters.truncatedHMac = TlsExtensionsUtils.hasTruncatedHMacExtension(serverExtensions);
            clientHandshakeState.allowCertificateStatus = !clientHandshakeState.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(serverExtensions, TlsExtensionsUtils.EXT_status_request, (short) 47);
            clientHandshakeState.expectSessionTicket = !clientHandshakeState.resumedSession && TlsUtils.hasExpectedEmptyExtensionData(serverExtensions, TlsProtocol.EXT_SessionTicket, (short) 47);
        }
        if (hashtable != null) {
            clientHandshakeState.client.processServerExtensions(serverExtensions);
        }
        securityParameters.prfAlgorithm = TlsProtocol.getPRFAlgorithm(clientHandshakeState.clientContext, securityParameters.getCipherSuite());
        securityParameters.verifyDataLength = 12;
    }

    protected void processServerKeyExchange(ClientHandshakeState clientHandshakeState, byte[] bArr) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        clientHandshakeState.keyExchange.processServerKeyExchange(byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
    }

    protected void processServerSupplementalData(ClientHandshakeState clientHandshakeState, byte[] bArr) throws IOException {
        clientHandshakeState.client.processServerSupplementalData(TlsProtocol.readSupplementalDataMessage(new ByteArrayInputStream(bArr)));
    }

    protected void reportServerVersion(ClientHandshakeState clientHandshakeState, ProtocolVersion protocolVersion) throws IOException {
        TlsClientContextImpl tlsClientContextImpl = clientHandshakeState.clientContext;
        ProtocolVersion serverVersion = tlsClientContextImpl.getServerVersion();
        if (null == serverVersion) {
            tlsClientContextImpl.setServerVersion(protocolVersion);
            clientHandshakeState.client.notifyServerVersion(protocolVersion);
        } else if (!serverVersion.equals(protocolVersion)) {
            throw new TlsFatalAlert((short) 47);
        }
    }

    protected static byte[] patchClientHelloWithCookie(byte[] bArr, byte[] bArr2) throws IOException {
        int uint8 = 34 + 1 + TlsUtils.readUint8(bArr, 34);
        int i = uint8 + 1;
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, uint8);
        TlsUtils.checkUint8(bArr2.length);
        TlsUtils.writeUint8(bArr2.length, bArr3, uint8);
        System.arraycopy(bArr2, 0, bArr3, i, bArr2.length);
        System.arraycopy(bArr, i, bArr3, i + bArr2.length, bArr.length - i);
        return bArr3;
    }
}
