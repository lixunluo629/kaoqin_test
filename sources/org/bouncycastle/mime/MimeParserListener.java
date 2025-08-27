package org.bouncycastle.mime;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/MimeParserListener.class */
public interface MimeParserListener {
    MimeContext createContext(MimeParserContext mimeParserContext, Headers headers);

    void object(MimeParserContext mimeParserContext, Headers headers, InputStream inputStream) throws IOException;
}
