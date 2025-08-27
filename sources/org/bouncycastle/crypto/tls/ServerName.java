package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.httpclient.auth.NTLM;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/ServerName.class */
public class ServerName {
    protected short nameType;
    protected Object name;

    public ServerName(short s, Object obj) {
        if (!isCorrectType(s, obj)) {
            throw new IllegalArgumentException("'name' is not an instance of the correct type");
        }
        this.nameType = s;
        this.name = obj;
    }

    public short getNameType() {
        return this.nameType;
    }

    public Object getName() {
        return this.name;
    }

    public String getHostName() {
        if (isCorrectType((short) 0, this.name)) {
            return (String) this.name;
        }
        throw new IllegalStateException("'name' is not a HostName string");
    }

    public void encode(OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.nameType, outputStream);
        switch (this.nameType) {
            case 0:
                byte[] bytes = ((String) this.name).getBytes(NTLM.DEFAULT_CHARSET);
                if (bytes.length < 1) {
                    throw new TlsFatalAlert((short) 80);
                }
                TlsUtils.writeOpaque16(bytes, outputStream);
                return;
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    public static ServerName parse(InputStream inputStream) throws IOException {
        short uint8 = TlsUtils.readUint8(inputStream);
        switch (uint8) {
            case 0:
                byte[] opaque16 = TlsUtils.readOpaque16(inputStream);
                if (opaque16.length < 1) {
                    throw new TlsFatalAlert((short) 50);
                }
                return new ServerName(uint8, new String(opaque16, NTLM.DEFAULT_CHARSET));
            default:
                throw new TlsFatalAlert((short) 50);
        }
    }

    protected static boolean isCorrectType(short s, Object obj) {
        switch (s) {
            case 0:
                return obj instanceof String;
            default:
                throw new IllegalArgumentException("'nameType' is an unsupported NameType");
        }
    }
}
