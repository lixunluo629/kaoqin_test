package org.aspectj.weaver;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.aspectj.weaver.AjAttribute;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/VersionedDataInputStream.class */
public class VersionedDataInputStream extends DataInputStream {
    private AjAttribute.WeaverVersionInfo version;
    private ConstantPoolReader constantPoolReader;

    public VersionedDataInputStream(InputStream is, ConstantPoolReader constantPoolReader) {
        super(is);
        this.version = new AjAttribute.WeaverVersionInfo();
        this.constantPoolReader = constantPoolReader;
    }

    public int getMajorVersion() {
        return this.version.getMajorVersion();
    }

    public int getMinorVersion() {
        return this.version.getMinorVersion();
    }

    public long getBuildstamp() {
        return this.version.getBuildstamp();
    }

    public void setVersion(AjAttribute.WeaverVersionInfo version) {
        this.version = version;
    }

    public String readUtf8(int cpIndex) {
        if (this.constantPoolReader == null) {
            throw new IllegalStateException();
        }
        if (cpIndex < 0) {
            throw new IllegalStateException(cpIndex + "");
        }
        return this.constantPoolReader.readUtf8(cpIndex);
    }

    public boolean canDecompress() {
        return this.constantPoolReader != null;
    }

    public boolean isAtLeast169() {
        return getMajorVersion() >= 7;
    }

    public String readPath() throws IOException {
        return readUtf8(readShort());
    }

    public String readSignature() throws IOException {
        return readUtf8(readShort());
    }

    public UnresolvedType readSignatureAsUnresolvedType() throws IOException {
        return UnresolvedType.forSignature(readUtf8(readShort()));
    }

    public String toString() {
        return "VersionedDataInputStream: version=" + this.version + " constantPoolReader?" + (this.constantPoolReader != null);
    }
}
