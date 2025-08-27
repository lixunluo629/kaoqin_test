package org.bouncycastle.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.io.Streams;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/ServerNameList.class */
public class ServerNameList {
    protected Vector serverNameList;

    public ServerNameList(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("'serverNameList' must not be null");
        }
        this.serverNameList = vector;
    }

    public Vector getServerNameList() {
        return this.serverNameList;
    }

    public void encode(OutputStream outputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        short[] sArrCheckNameType = new short[0];
        for (int i = 0; i < this.serverNameList.size(); i++) {
            ServerName serverName = (ServerName) this.serverNameList.elementAt(i);
            sArrCheckNameType = checkNameType(sArrCheckNameType, serverName.getNameType());
            if (sArrCheckNameType == null) {
                throw new TlsFatalAlert((short) 80);
            }
            serverName.encode(byteArrayOutputStream);
        }
        TlsUtils.checkUint16(byteArrayOutputStream.size());
        TlsUtils.writeUint16(byteArrayOutputStream.size(), outputStream);
        Streams.writeBufTo(byteArrayOutputStream, outputStream);
    }

    public static ServerNameList parse(InputStream inputStream) throws IOException {
        int uint16 = TlsUtils.readUint16(inputStream);
        if (uint16 < 1) {
            throw new TlsFatalAlert((short) 50);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TlsUtils.readFully(uint16, inputStream));
        short[] sArrCheckNameType = new short[0];
        Vector vector = new Vector();
        while (byteArrayInputStream.available() > 0) {
            ServerName serverName = ServerName.parse(byteArrayInputStream);
            sArrCheckNameType = checkNameType(sArrCheckNameType, serverName.getNameType());
            if (sArrCheckNameType == null) {
                throw new TlsFatalAlert((short) 47);
            }
            vector.addElement(serverName);
        }
        return new ServerNameList(vector);
    }

    private static short[] checkNameType(short[] sArr, short s) {
        if (!NameType.isValid(s) || Arrays.contains(sArr, s)) {
            return null;
        }
        return Arrays.append(sArr, s);
    }
}
