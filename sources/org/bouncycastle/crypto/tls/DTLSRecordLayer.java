package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSRecordLayer.class */
class DTLSRecordLayer implements DatagramTransport {
    private static final int RECORD_HEADER_LENGTH = 13;
    private static final int MAX_FRAGMENT_LENGTH = 16384;
    private static final long TCP_MSL = 120000;
    private static final long RETRANSMIT_TIMEOUT = 240000;
    private final DatagramTransport transport;
    private final TlsContext context;
    private final TlsPeer peer;
    private volatile int plaintextLimit;
    private DTLSEpoch currentEpoch;
    private DTLSEpoch readEpoch;
    private DTLSEpoch writeEpoch;
    private final ByteQueue recordQueue = new ByteQueue();
    private volatile boolean closed = false;
    private volatile boolean failed = false;
    private volatile ProtocolVersion readVersion = null;
    private volatile ProtocolVersion writeVersion = null;
    private DTLSHandshakeRetransmit retransmit = null;
    private DTLSEpoch retransmitEpoch = null;
    private long retransmitExpiry = 0;
    private volatile boolean inHandshake = true;
    private DTLSEpoch pendingEpoch = null;

    DTLSRecordLayer(DatagramTransport datagramTransport, TlsContext tlsContext, TlsPeer tlsPeer, short s) {
        this.transport = datagramTransport;
        this.context = tlsContext;
        this.peer = tlsPeer;
        this.currentEpoch = new DTLSEpoch(0, new TlsNullCipher(tlsContext));
        this.readEpoch = this.currentEpoch;
        this.writeEpoch = this.currentEpoch;
        setPlaintextLimit(16384);
    }

    boolean isClosed() {
        return this.closed;
    }

    void setPlaintextLimit(int i) {
        this.plaintextLimit = i;
    }

    int getReadEpoch() {
        return this.readEpoch.getEpoch();
    }

    ProtocolVersion getReadVersion() {
        return this.readVersion;
    }

    void setReadVersion(ProtocolVersion protocolVersion) {
        this.readVersion = protocolVersion;
    }

    void setWriteVersion(ProtocolVersion protocolVersion) {
        this.writeVersion = protocolVersion;
    }

    void initPendingEpoch(TlsCipher tlsCipher) {
        if (this.pendingEpoch != null) {
            throw new IllegalStateException();
        }
        this.pendingEpoch = new DTLSEpoch(this.writeEpoch.getEpoch() + 1, tlsCipher);
    }

    void handshakeSuccessful(DTLSHandshakeRetransmit dTLSHandshakeRetransmit) {
        if (this.readEpoch == this.currentEpoch || this.writeEpoch == this.currentEpoch) {
            throw new IllegalStateException();
        }
        if (dTLSHandshakeRetransmit != null) {
            this.retransmit = dTLSHandshakeRetransmit;
            this.retransmitEpoch = this.currentEpoch;
            this.retransmitExpiry = System.currentTimeMillis() + RETRANSMIT_TIMEOUT;
        }
        this.inHandshake = false;
        this.currentEpoch = this.pendingEpoch;
        this.pendingEpoch = null;
    }

    void resetWriteEpoch() {
        if (this.retransmitEpoch != null) {
            this.writeEpoch = this.retransmitEpoch;
        } else {
            this.writeEpoch = this.currentEpoch;
        }
    }

    @Override // org.bouncycastle.crypto.tls.DatagramTransport
    public int getReceiveLimit() throws IOException {
        return Math.min(this.plaintextLimit, this.readEpoch.getCipher().getPlaintextLimit(this.transport.getReceiveLimit() - 13));
    }

    @Override // org.bouncycastle.crypto.tls.DatagramTransport
    public int getSendLimit() throws IOException {
        return Math.min(this.plaintextLimit, this.writeEpoch.getCipher().getPlaintextLimit(this.transport.getSendLimit() - 13));
    }

    @Override // org.bouncycastle.crypto.tls.DatagramTransport
    public int receive(byte[] bArr, int i, int i2, int i3) throws IOException {
        byte[] bArrDecodeCiphertext;
        byte[] bArr2 = null;
        while (true) {
            int iMin = Math.min(i2, getReceiveLimit()) + 13;
            if (bArr2 == null || bArr2.length < iMin) {
                bArr2 = new byte[iMin];
            }
            try {
                if (this.retransmit != null && System.currentTimeMillis() > this.retransmitExpiry) {
                    this.retransmit = null;
                    this.retransmitEpoch = null;
                }
                int iReceiveRecord = receiveRecord(bArr2, 0, iMin, i3);
                if (iReceiveRecord < 0) {
                    return iReceiveRecord;
                }
                if (iReceiveRecord >= 13 && iReceiveRecord == TlsUtils.readUint16(bArr2, 11) + 13) {
                    short uint8 = TlsUtils.readUint8(bArr2, 0);
                    switch (uint8) {
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                            int uint16 = TlsUtils.readUint16(bArr2, 3);
                            DTLSEpoch dTLSEpoch = null;
                            if (uint16 == this.readEpoch.getEpoch()) {
                                dTLSEpoch = this.readEpoch;
                            } else if (uint8 == 22 && this.retransmitEpoch != null && uint16 == this.retransmitEpoch.getEpoch()) {
                                dTLSEpoch = this.retransmitEpoch;
                            }
                            if (dTLSEpoch == null) {
                                break;
                            } else {
                                long uint48 = TlsUtils.readUint48(bArr2, 5);
                                if (dTLSEpoch.getReplayWindow().shouldDiscard(uint48)) {
                                    break;
                                } else {
                                    ProtocolVersion version = TlsUtils.readVersion(bArr2, 1);
                                    if (version.isDTLS() && (this.readVersion == null || this.readVersion.equals(version))) {
                                        bArrDecodeCiphertext = dTLSEpoch.getCipher().decodeCiphertext(getMacSequenceNumber(dTLSEpoch.getEpoch(), uint48), uint8, bArr2, 13, iReceiveRecord - 13);
                                        dTLSEpoch.getReplayWindow().reportAuthenticated(uint48);
                                        if (bArrDecodeCiphertext.length <= this.plaintextLimit) {
                                            if (this.readVersion == null) {
                                                this.readVersion = version;
                                            }
                                            switch (uint8) {
                                                case 20:
                                                    for (int i4 = 0; i4 < bArrDecodeCiphertext.length; i4++) {
                                                        if (TlsUtils.readUint8(bArrDecodeCiphertext, i4) == 1 && this.pendingEpoch != null) {
                                                            this.readEpoch = this.pendingEpoch;
                                                        }
                                                    }
                                                    continue;
                                                case 21:
                                                    if (bArrDecodeCiphertext.length == 2) {
                                                        short s = bArrDecodeCiphertext[0];
                                                        short s2 = bArrDecodeCiphertext[1];
                                                        this.peer.notifyAlertReceived(s, s2);
                                                        if (s == 2) {
                                                            failed();
                                                            throw new TlsFatalAlert(s2);
                                                        }
                                                        if (s2 == 0) {
                                                            closeTransport();
                                                        }
                                                    }
                                                    continue;
                                                case 22:
                                                    if (this.inHandshake) {
                                                        break;
                                                    } else {
                                                        if (this.retransmit != null) {
                                                            this.retransmit.receivedHandshakeRecord(uint16, bArrDecodeCiphertext, 0, bArrDecodeCiphertext.length);
                                                        }
                                                        break;
                                                    }
                                                case 23:
                                                    if (this.inHandshake) {
                                                        break;
                                                    } else {
                                                        break;
                                                    }
                                            }
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (IOException e) {
                throw e;
            }
        }
        if (!this.inHandshake && this.retransmit != null) {
            this.retransmit = null;
            this.retransmitEpoch = null;
        }
        System.arraycopy(bArrDecodeCiphertext, 0, bArr, i, bArrDecodeCiphertext.length);
        return bArrDecodeCiphertext.length;
    }

    @Override // org.bouncycastle.crypto.tls.DatagramTransport
    public void send(byte[] bArr, int i, int i2) throws IOException {
        short s = 23;
        if (this.inHandshake || this.writeEpoch == this.retransmitEpoch) {
            s = 22;
            if (TlsUtils.readUint8(bArr, i) == 20) {
                DTLSEpoch dTLSEpoch = null;
                if (this.inHandshake) {
                    dTLSEpoch = this.pendingEpoch;
                } else if (this.writeEpoch == this.retransmitEpoch) {
                    dTLSEpoch = this.currentEpoch;
                }
                if (dTLSEpoch == null) {
                    throw new IllegalStateException();
                }
                byte[] bArr2 = {1};
                sendRecord((short) 20, bArr2, 0, bArr2.length);
                this.writeEpoch = dTLSEpoch;
            }
        }
        sendRecord(s, bArr, i, i2);
    }

    @Override // org.bouncycastle.crypto.tls.TlsCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        if (this.inHandshake) {
            warn((short) 90, "User canceled handshake");
        }
        closeTransport();
    }

    void fail(short s) {
        if (this.closed) {
            return;
        }
        try {
            raiseAlert((short) 2, s, null, null);
        } catch (Exception e) {
        }
        this.failed = true;
        closeTransport();
    }

    void failed() {
        if (this.closed) {
            return;
        }
        this.failed = true;
        closeTransport();
    }

    void warn(short s, String str) throws IOException {
        raiseAlert((short) 1, s, str, null);
    }

    private void closeTransport() {
        if (this.closed) {
            return;
        }
        try {
            if (!this.failed) {
                warn((short) 0, null);
            }
            this.transport.close();
        } catch (Exception e) {
        }
        this.closed = true;
    }

    private void raiseAlert(short s, short s2, String str, Throwable th) throws IOException {
        this.peer.notifyAlertRaised(s, s2, str, th);
        sendRecord((short) 21, new byte[]{(byte) s, (byte) s2}, 0, 2);
    }

    private int receiveRecord(byte[] bArr, int i, int i2, int i3) throws IOException {
        int uint16;
        if (this.recordQueue.available() <= 0) {
            int iReceive = this.transport.receive(bArr, i, i2, i3);
            if (iReceive >= 13 && iReceive > (uint16 = 13 + TlsUtils.readUint16(bArr, i + 11))) {
                this.recordQueue.addData(bArr, i + uint16, iReceive - uint16);
                iReceive = uint16;
            }
            return iReceive;
        }
        int uint162 = 0;
        if (this.recordQueue.available() >= 13) {
            byte[] bArr2 = new byte[2];
            this.recordQueue.read(bArr2, 0, 2, 11);
            uint162 = TlsUtils.readUint16(bArr2, 0);
        }
        int iMin = Math.min(this.recordQueue.available(), 13 + uint162);
        this.recordQueue.removeData(bArr, i, iMin, 0);
        return iMin;
    }

    private void sendRecord(short s, byte[] bArr, int i, int i2) throws IOException {
        if (this.writeVersion == null) {
            return;
        }
        if (i2 > this.plaintextLimit) {
            throw new TlsFatalAlert((short) 80);
        }
        if (i2 < 1 && s != 23) {
            throw new TlsFatalAlert((short) 80);
        }
        int epoch = this.writeEpoch.getEpoch();
        long jAllocateSequenceNumber = this.writeEpoch.allocateSequenceNumber();
        byte[] bArrEncodePlaintext = this.writeEpoch.getCipher().encodePlaintext(getMacSequenceNumber(epoch, jAllocateSequenceNumber), s, bArr, i, i2);
        byte[] bArr2 = new byte[bArrEncodePlaintext.length + 13];
        TlsUtils.writeUint8(s, bArr2, 0);
        TlsUtils.writeVersion(this.writeVersion, bArr2, 1);
        TlsUtils.writeUint16(epoch, bArr2, 3);
        TlsUtils.writeUint48(jAllocateSequenceNumber, bArr2, 5);
        TlsUtils.writeUint16(bArrEncodePlaintext.length, bArr2, 11);
        System.arraycopy(bArrEncodePlaintext, 0, bArr2, 13, bArrEncodePlaintext.length);
        this.transport.send(bArr2, 0, bArr2.length);
    }

    private static long getMacSequenceNumber(int i, long j) {
        return ((i & 4294967295L) << 48) | j;
    }
}
