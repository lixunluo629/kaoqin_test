package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.io.InputStream;
import org.apache.tomcat.util.net.jsse.PEMFile;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.util.encoders.Base64;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/provider/PEMUtil.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/PEMUtil.class */
public class PEMUtil {
    private final String _header1;
    private final String _header2;
    private final String _footer1;
    private final String _footer2;

    PEMUtil(String str) {
        this._header1 = PEMFile.Part.BEGIN_BOUNDARY + str + "-----";
        this._header2 = "-----BEGIN X509 " + str + "-----";
        this._footer1 = PEMFile.Part.END_BOUNDARY + str + "-----";
        this._footer2 = "-----END X509 " + str + "-----";
    }

    private String readLine(InputStream inputStream) throws IOException {
        int i;
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            i = inputStream.read();
            if (i == 13 || i == 10 || i < 0) {
                if (i < 0 || stringBuffer.length() != 0) {
                    break;
                }
            } else if (i != 13) {
                stringBuffer.append((char) i);
            }
        }
        if (i < 0) {
            return null;
        }
        return stringBuffer.toString();
    }

    ASN1Sequence readPEMObject(InputStream inputStream) throws IOException {
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        do {
            line = readLine(inputStream);
            if (line == null || line.startsWith(this._header1)) {
                break;
            }
        } while (!line.startsWith(this._header2));
        while (true) {
            String line2 = readLine(inputStream);
            if (line2 == null || line2.startsWith(this._footer1) || line2.startsWith(this._footer2)) {
                break;
            }
            stringBuffer.append(line2);
        }
        if (stringBuffer.length() == 0) {
            return null;
        }
        DERObject object = new ASN1InputStream(Base64.decode(stringBuffer.toString())).readObject();
        if (object instanceof ASN1Sequence) {
            return (ASN1Sequence) object;
        }
        throw new IOException("malformed PEM data encountered");
    }
}
