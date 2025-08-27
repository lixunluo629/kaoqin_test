package org.apache.poi.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: poi-3.17.jar:org/apache/poi/util/HexRead.class */
public class HexRead {
    public static byte[] readData(String filename) throws IOException {
        File file = new File(filename);
        InputStream stream = new FileInputStream(file);
        try {
            byte[] data = readData(stream, -1);
            stream.close();
            return data;
        } catch (Throwable th) {
            stream.close();
            throw th;
        }
    }

    public static byte[] readData(InputStream stream, String section) throws IOException {
        StringBuffer sectionText;
        boolean inSection;
        int c;
        try {
            sectionText = new StringBuffer();
            inSection = false;
            c = stream.read();
        } finally {
            stream.close();
        }
        while (c != -1) {
            switch (c) {
                case 10:
                case 13:
                    inSection = false;
                    sectionText = new StringBuffer();
                    continue;
                    c = stream.read();
                case 91:
                    inSection = true;
                    continue;
                    c = stream.read();
                case 93:
                    inSection = false;
                    if (!sectionText.toString().equals(section)) {
                        sectionText = new StringBuffer();
                        continue;
                        c = stream.read();
                    } else {
                        byte[] data = readData(stream, 91);
                        stream.close();
                        return data;
                    }
                default:
                    if (inSection) {
                        sectionText.append((char) c);
                        continue;
                    }
                    c = stream.read();
            }
            stream.close();
        }
        throw new IOException("Section '" + section + "' not found");
    }

    public static byte[] readData(String filename, String section) throws IOException {
        return readData(new FileInputStream(filename), section);
    }

    public static byte[] readData(InputStream stream, int eofChar) throws IOException {
        int characterCount = 0;
        byte b = 0;
        List<Byte> bytes = new ArrayList<>();
        while (true) {
            int count = stream.read();
            int digitValue = -1;
            if (48 <= count && count <= 57) {
                digitValue = count - 48;
            } else if (65 <= count && count <= 70) {
                digitValue = count - 55;
            } else if (97 <= count && count <= 102) {
                digitValue = count - 87;
            } else if (35 == count) {
                readToEOL(stream);
            } else if (-1 == count || eofChar == count) {
                break;
            }
            if (digitValue != -1) {
                b = (byte) (((byte) (b << 4)) + ((byte) digitValue));
                characterCount++;
                if (characterCount == 2) {
                    bytes.add(Byte.valueOf(b));
                    characterCount = 0;
                    b = 0;
                }
            }
        }
        Byte[] polished = (Byte[]) bytes.toArray(new Byte[bytes.size()]);
        byte[] rval = new byte[polished.length];
        for (int j = 0; j < polished.length; j++) {
            rval[j] = polished[j].byteValue();
        }
        return rval;
    }

    public static byte[] readFromString(String data) {
        try {
            return readData(new ByteArrayInputStream(data.getBytes(StringUtil.UTF8)), -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readToEOL(InputStream stream) throws IOException {
        int i = stream.read();
        while (true) {
            int c = i;
            if (c != -1 && c != 10 && c != 13) {
                i = stream.read();
            } else {
                return;
            }
        }
    }
}
