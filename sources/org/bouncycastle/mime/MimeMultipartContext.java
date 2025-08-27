package org.bouncycastle.mime;

import java.io.IOException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/MimeMultipartContext.class */
public interface MimeMultipartContext extends MimeContext {
    MimeContext createContext(int i) throws IOException;
}
