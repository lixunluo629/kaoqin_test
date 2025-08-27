package org.aspectj.weaver;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/CompressingDataOutputStream.class */
public class CompressingDataOutputStream extends DataOutputStream {
    private ConstantPoolWriter constantPoolWriter;
    public boolean compressionEnabled;

    public CompressingDataOutputStream(ByteArrayOutputStream baos, ConstantPoolWriter constantPoolWriter) {
        super(baos);
        this.compressionEnabled = true;
        this.constantPoolWriter = constantPoolWriter;
    }

    public CompressingDataOutputStream(FileOutputStream fos) {
        super(fos);
        this.compressionEnabled = true;
    }

    public boolean canCompress() {
        return this.constantPoolWriter != null && this.compressionEnabled;
    }

    public int compressSignature(String signature) {
        if (this.constantPoolWriter == null) {
            throw new IllegalStateException();
        }
        return this.constantPoolWriter.writeUtf8(signature);
    }

    public int compressFilepath(String filepath) {
        if (this.constantPoolWriter == null) {
            throw new IllegalStateException();
        }
        return this.constantPoolWriter.writeUtf8(filepath);
    }

    public int compressName(String name) {
        if (this.constantPoolWriter == null) {
            throw new IllegalStateException();
        }
        return this.constantPoolWriter.writeUtf8(name);
    }

    public void writeCompressedName(String name) throws IOException {
        writeShort(compressName(name));
    }

    public void writeCompressedSignature(String signature) throws IOException {
        writeShort(compressSignature(signature));
    }

    public void writeCompressedPath(String path) throws IOException {
        writeShort(compressFilepath(path));
    }
}
