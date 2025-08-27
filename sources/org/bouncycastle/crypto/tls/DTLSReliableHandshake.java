package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.util.Integers;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSReliableHandshake.class */
class DTLSReliableHandshake {
    private static final int MAX_RECEIVE_AHEAD = 16;
    private static final int MESSAGE_HEADER_LENGTH = 12;
    private DTLSRecordLayer recordLayer;
    private Hashtable currentInboundFlight = new Hashtable();
    private Hashtable previousInboundFlight = null;
    private Vector outboundFlight = new Vector();
    private boolean sending = true;
    private int message_seq = 0;
    private int next_receive_seq = 0;
    private TlsHandshakeHash handshakeHash = new DeferredHash();

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSReliableHandshake$Message.class */
    static class Message {
        private final int message_seq;
        private final short msg_type;
        private final byte[] body;

        private Message(int i, short s, byte[] bArr) {
            this.message_seq = i;
            this.msg_type = s;
            this.body = bArr;
        }

        public int getSeq() {
            return this.message_seq;
        }

        public short getType() {
            return this.msg_type;
        }

        public byte[] getBody() {
            return this.body;
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DTLSReliableHandshake$RecordLayerBuffer.class */
    static class RecordLayerBuffer extends ByteArrayOutputStream {
        RecordLayerBuffer(int i) {
            super(i);
        }

        void sendToRecordLayer(DTLSRecordLayer dTLSRecordLayer) throws IOException {
            dTLSRecordLayer.send(this.buf, 0, this.count);
            this.buf = null;
        }
    }

    DTLSReliableHandshake(TlsContext tlsContext, DTLSRecordLayer dTLSRecordLayer) {
        this.recordLayer = dTLSRecordLayer;
        this.handshakeHash.init(tlsContext);
    }

    void notifyHelloComplete() {
        this.handshakeHash = this.handshakeHash.notifyPRFDetermined();
    }

    TlsHandshakeHash getHandshakeHash() {
        return this.handshakeHash;
    }

    TlsHandshakeHash prepareToFinish() {
        TlsHandshakeHash tlsHandshakeHash = this.handshakeHash;
        this.handshakeHash = this.handshakeHash.stopTracking();
        return tlsHandshakeHash;
    }

    void sendMessage(short s, byte[] bArr) throws IOException {
        TlsUtils.checkUint24(bArr.length);
        if (!this.sending) {
            checkInboundFlight();
            this.sending = true;
            this.outboundFlight.removeAllElements();
        }
        int i = this.message_seq;
        this.message_seq = i + 1;
        Message message = new Message(i, s, bArr);
        this.outboundFlight.addElement(message);
        writeMessage(message);
        updateHandshakeMessagesDigest(message);
    }

    byte[] receiveMessageBody(short s) throws IOException {
        Message messageReceiveMessage = receiveMessage();
        if (messageReceiveMessage.getType() != s) {
            throw new TlsFatalAlert((short) 10);
        }
        return messageReceiveMessage.getBody();
    }

    Message receiveMessage() throws IOException {
        if (this.sending) {
            this.sending = false;
            prepareInboundFlight(new Hashtable());
        }
        byte[] bArr = null;
        int iBackOff = 1000;
        while (true) {
            if (this.recordLayer.isClosed()) {
                throw new TlsFatalAlert((short) 90);
            }
            Message pendingMessage = getPendingMessage();
            if (pendingMessage != null) {
                return pendingMessage;
            }
            int receiveLimit = this.recordLayer.getReceiveLimit();
            if (bArr == null || bArr.length < receiveLimit) {
                bArr = new byte[receiveLimit];
            }
            int iReceive = this.recordLayer.receive(bArr, 0, receiveLimit, iBackOff);
            if (iReceive < 0) {
                resendOutboundFlight();
                iBackOff = backOff(iBackOff);
            } else if (processRecord(16, this.recordLayer.getReadEpoch(), bArr, 0, iReceive)) {
                iBackOff = backOff(iBackOff);
            }
        }
    }

    void finish() {
        DTLSHandshakeRetransmit dTLSHandshakeRetransmit = null;
        if (this.sending) {
            prepareInboundFlight(null);
            if (this.previousInboundFlight != null) {
                dTLSHandshakeRetransmit = new DTLSHandshakeRetransmit() { // from class: org.bouncycastle.crypto.tls.DTLSReliableHandshake.1
                    @Override // org.bouncycastle.crypto.tls.DTLSHandshakeRetransmit
                    public void receivedHandshakeRecord(int i, byte[] bArr, int i2, int i3) throws IOException {
                        DTLSReliableHandshake.this.processRecord(0, i, bArr, i2, i3);
                    }
                };
            }
        } else {
            checkInboundFlight();
        }
        this.recordLayer.handshakeSuccessful(dTLSHandshakeRetransmit);
    }

    void resetHandshakeMessagesDigest() {
        this.handshakeHash.reset();
    }

    private int backOff(int i) {
        return Math.min(i * 2, 60000);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0011  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void checkInboundFlight() {
        /*
            r3 = this;
            r0 = r3
            java.util.Hashtable r0 = r0.currentInboundFlight
            java.util.Enumeration r0 = r0.keys()
            r4 = r0
        L8:
            r0 = r4
            boolean r0 = r0.hasMoreElements()
            if (r0 == 0) goto L29
            r0 = r4
            java.lang.Object r0 = r0.nextElement()
            java.lang.Integer r0 = (java.lang.Integer) r0
            r5 = r0
            r0 = r5
            int r0 = r0.intValue()
            r1 = r3
            int r1 = r1.next_receive_seq
            if (r0 < r1) goto L26
        L26:
            goto L8
        L29:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.tls.DTLSReliableHandshake.checkInboundFlight():void");
    }

    private Message getPendingMessage() throws IOException {
        byte[] bodyIfComplete;
        DTLSReassembler dTLSReassembler = (DTLSReassembler) this.currentInboundFlight.get(Integers.valueOf(this.next_receive_seq));
        if (dTLSReassembler == null || (bodyIfComplete = dTLSReassembler.getBodyIfComplete()) == null) {
            return null;
        }
        this.previousInboundFlight = null;
        int i = this.next_receive_seq;
        this.next_receive_seq = i + 1;
        return updateHandshakeMessagesDigest(new Message(i, dTLSReassembler.getMsgType(), bodyIfComplete));
    }

    private void prepareInboundFlight(Hashtable hashtable) {
        resetAll(this.currentInboundFlight);
        this.previousInboundFlight = this.currentInboundFlight;
        this.currentInboundFlight = hashtable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean processRecord(int i, int i2, byte[] bArr, int i3, int i4) throws IOException {
        int uint24;
        int uint242;
        DTLSReassembler dTLSReassembler;
        boolean z = false;
        while (i4 >= 12 && i4 >= (uint242 = (uint24 = TlsUtils.readUint24(bArr, i3 + 9)) + 12)) {
            int uint243 = TlsUtils.readUint24(bArr, i3 + 1);
            int uint244 = TlsUtils.readUint24(bArr, i3 + 6);
            if (uint244 + uint24 > uint243) {
                break;
            }
            short uint8 = TlsUtils.readUint8(bArr, i3 + 0);
            if (i2 != (uint8 == 20 ? 1 : 0)) {
                break;
            }
            int uint16 = TlsUtils.readUint16(bArr, i3 + 4);
            if (uint16 < this.next_receive_seq + i) {
                if (uint16 >= this.next_receive_seq) {
                    DTLSReassembler dTLSReassembler2 = (DTLSReassembler) this.currentInboundFlight.get(Integers.valueOf(uint16));
                    if (dTLSReassembler2 == null) {
                        dTLSReassembler2 = new DTLSReassembler(uint8, uint243);
                        this.currentInboundFlight.put(Integers.valueOf(uint16), dTLSReassembler2);
                    }
                    dTLSReassembler2.contributeFragment(uint8, uint243, bArr, i3 + 12, uint244, uint24);
                } else if (this.previousInboundFlight != null && (dTLSReassembler = (DTLSReassembler) this.previousInboundFlight.get(Integers.valueOf(uint16))) != null) {
                    dTLSReassembler.contributeFragment(uint8, uint243, bArr, i3 + 12, uint244, uint24);
                    z = true;
                }
            }
            i3 += uint242;
            i4 -= uint242;
        }
        boolean z2 = z && checkAll(this.previousInboundFlight);
        if (z2) {
            resendOutboundFlight();
            resetAll(this.previousInboundFlight);
        }
        return z2;
    }

    private void resendOutboundFlight() throws IOException {
        this.recordLayer.resetWriteEpoch();
        for (int i = 0; i < this.outboundFlight.size(); i++) {
            writeMessage((Message) this.outboundFlight.elementAt(i));
        }
    }

    private Message updateHandshakeMessagesDigest(Message message) throws IOException {
        if (message.getType() != 0) {
            byte[] body = message.getBody();
            byte[] bArr = new byte[12];
            TlsUtils.writeUint8(message.getType(), bArr, 0);
            TlsUtils.writeUint24(body.length, bArr, 1);
            TlsUtils.writeUint16(message.getSeq(), bArr, 4);
            TlsUtils.writeUint24(0, bArr, 6);
            TlsUtils.writeUint24(body.length, bArr, 9);
            this.handshakeHash.update(bArr, 0, bArr.length);
            this.handshakeHash.update(body, 0, body.length);
        }
        return message;
    }

    private void writeMessage(Message message) throws IOException {
        int sendLimit = this.recordLayer.getSendLimit() - 12;
        if (sendLimit < 1) {
            throw new TlsFatalAlert((short) 80);
        }
        int length = message.getBody().length;
        int i = 0;
        do {
            int iMin = Math.min(length - i, sendLimit);
            writeHandshakeFragment(message, i, iMin);
            i += iMin;
        } while (i < length);
    }

    private void writeHandshakeFragment(Message message, int i, int i2) throws IOException {
        RecordLayerBuffer recordLayerBuffer = new RecordLayerBuffer(12 + i2);
        TlsUtils.writeUint8(message.getType(), recordLayerBuffer);
        TlsUtils.writeUint24(message.getBody().length, recordLayerBuffer);
        TlsUtils.writeUint16(message.getSeq(), recordLayerBuffer);
        TlsUtils.writeUint24(i, recordLayerBuffer);
        TlsUtils.writeUint24(i2, recordLayerBuffer);
        recordLayerBuffer.write(message.getBody(), i, i2);
        recordLayerBuffer.sendToRecordLayer(this.recordLayer);
    }

    private static boolean checkAll(Hashtable hashtable) {
        Enumeration enumerationElements = hashtable.elements();
        while (enumerationElements.hasMoreElements()) {
            if (((DTLSReassembler) enumerationElements.nextElement()).getBodyIfComplete() == null) {
                return false;
            }
        }
        return true;
    }

    private static void resetAll(Hashtable hashtable) {
        Enumeration enumerationElements = hashtable.elements();
        while (enumerationElements.hasMoreElements()) {
            ((DTLSReassembler) enumerationElements.nextElement()).reset();
        }
    }
}
