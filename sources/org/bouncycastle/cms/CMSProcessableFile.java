package org.bouncycastle.cms;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/CMSProcessableFile.class */
public class CMSProcessableFile implements CMSTypedData, CMSReadable {
    private static final int DEFAULT_BUF_SIZE = 32768;
    private final ASN1ObjectIdentifier type;
    private final File file;
    private final byte[] buf;

    public CMSProcessableFile(File file) {
        this(file, 32768);
    }

    public CMSProcessableFile(File file, int i) {
        this(CMSObjectIdentifiers.data, file, i);
    }

    public CMSProcessableFile(ASN1ObjectIdentifier aSN1ObjectIdentifier, File file, int i) {
        this.type = aSN1ObjectIdentifier;
        this.file = file;
        this.buf = new byte[i];
    }

    @Override // org.bouncycastle.cms.CMSReadable
    public InputStream getInputStream() throws CMSException, IOException {
        return new BufferedInputStream(new FileInputStream(this.file), 32768);
    }

    @Override // org.bouncycastle.cms.CMSProcessable
    public void write(OutputStream outputStream) throws CMSException, IOException {
        FileInputStream fileInputStream = new FileInputStream(this.file);
        while (true) {
            int i = fileInputStream.read(this.buf, 0, this.buf.length);
            if (i <= 0) {
                fileInputStream.close();
                return;
            }
            outputStream.write(this.buf, 0, i);
        }
    }

    @Override // org.bouncycastle.cms.CMSProcessable
    public Object getContent() {
        return this.file;
    }

    @Override // org.bouncycastle.cms.CMSTypedData
    public ASN1ObjectIdentifier getContentType() {
        return this.type;
    }
}
