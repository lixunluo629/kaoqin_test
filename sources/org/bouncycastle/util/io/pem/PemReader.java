package org.bouncycastle.util.io.pem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import org.bouncycastle.util.encoders.Base64;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/io/pem/PemReader.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/io/pem/PemReader.class */
public class PemReader extends BufferedReader {
    private static final String BEGIN = "-----BEGIN ";
    private static final String END = "-----END ";

    public PemReader(Reader reader) {
        super(reader);
    }

    public PemObject readPemObject() throws IOException {
        String line = readLine();
        if (line == null || !line.startsWith("-----BEGIN ")) {
            return null;
        }
        String strSubstring = line.substring("-----BEGIN ".length());
        int iIndexOf = strSubstring.indexOf(45);
        String strSubstring2 = strSubstring.substring(0, iIndexOf);
        if (iIndexOf > 0) {
            return loadObject(strSubstring2);
        }
        return null;
    }

    private PemObject loadObject(String str) throws IOException {
        String line;
        String str2 = "-----END " + str;
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList arrayList = new ArrayList();
        while (true) {
            line = readLine();
            if (line == null) {
                break;
            }
            if (line.indexOf(":") >= 0) {
                int iIndexOf = line.indexOf(58);
                arrayList.add(new PemHeader(line.substring(0, iIndexOf), line.substring(iIndexOf + 1).trim()));
            } else {
                if (line.indexOf(str2) != -1) {
                    break;
                }
                stringBuffer.append(line.trim());
            }
        }
        if (line == null) {
            throw new IOException(str2 + " not found");
        }
        return new PemObject(str, arrayList, Base64.decode(stringBuffer.toString()));
    }
}
