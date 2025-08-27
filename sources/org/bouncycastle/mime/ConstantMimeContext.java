package org.bouncycastle.mime;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/ConstantMimeContext.class */
public class ConstantMimeContext implements MimeContext, MimeMultipartContext {
    @Override // org.bouncycastle.mime.MimeContext
    public InputStream applyContext(Headers headers, InputStream inputStream) throws IOException {
        return inputStream;
    }

    @Override // org.bouncycastle.mime.MimeMultipartContext
    public MimeContext createContext(int i) throws IOException {
        return this;
    }
}
