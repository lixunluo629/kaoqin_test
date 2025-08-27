package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: poi-3.17.jar:org/apache/poi/util/FixedField.class */
public interface FixedField {
    void readFromBytes(byte[] bArr) throws ArrayIndexOutOfBoundsException;

    void readFromStream(InputStream inputStream) throws IOException;

    void writeToBytes(byte[] bArr) throws ArrayIndexOutOfBoundsException;

    String toString();
}
