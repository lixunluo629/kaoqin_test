package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/CertificateStatusRequest.class */
public class CertificateStatusRequest {
    protected short statusType;
    protected Object request;

    public CertificateStatusRequest(short s, Object obj) {
        if (!isCorrectType(s, obj)) {
            throw new IllegalArgumentException("'request' is not an instance of the correct type");
        }
        this.statusType = s;
        this.request = obj;
    }

    public short getStatusType() {
        return this.statusType;
    }

    public Object getRequest() {
        return this.request;
    }

    public OCSPStatusRequest getOCSPStatusRequest() {
        if (isCorrectType((short) 1, this.request)) {
            return (OCSPStatusRequest) this.request;
        }
        throw new IllegalStateException("'request' is not an OCSPStatusRequest");
    }

    public void encode(OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.statusType, outputStream);
        switch (this.statusType) {
            case 1:
                ((OCSPStatusRequest) this.request).encode(outputStream);
                return;
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public static CertificateStatusRequest parse(InputStream inputStream) throws IOException {
        short uint8 = TlsUtils.readUint8(inputStream);
        switch (uint8) {
            case 1:
                return new CertificateStatusRequest(uint8, OCSPStatusRequest.parse(inputStream));
            default:
                throw new TlsFatalAlert((short) 50);
        }
    }

    protected static boolean isCorrectType(short s, Object obj) {
        switch (s) {
            case 1:
                return obj instanceof OCSPStatusRequest;
            default:
                throw new IllegalArgumentException("'statusType' is an unsupported CertificateStatusType");
        }
    }
}
