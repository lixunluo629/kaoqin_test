package org.bouncycastle.cms;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/CMSProcessable.class */
public interface CMSProcessable {
    void write(OutputStream outputStream) throws CMSException, IOException;

    Object getContent();
}
