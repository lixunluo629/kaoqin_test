package org.apache.commons.httpclient.methods.multipart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/multipart/ByteArrayPartSource.class */
public class ByteArrayPartSource implements PartSource {
    private String fileName;
    private byte[] bytes;

    public ByteArrayPartSource(String fileName, byte[] bytes) {
        this.fileName = fileName;
        this.bytes = bytes;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.PartSource
    public long getLength() {
        return this.bytes.length;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.PartSource
    public String getFileName() {
        return this.fileName;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.PartSource
    public InputStream createInputStream() throws IOException {
        return new ByteArrayInputStream(this.bytes);
    }
}
