package org.bouncycastle.mime;

import java.io.IOException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/MimeParser.class */
public interface MimeParser {
    void parse(MimeParserListener mimeParserListener) throws IOException;
}
