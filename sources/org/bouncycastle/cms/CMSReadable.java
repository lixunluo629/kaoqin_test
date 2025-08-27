package org.bouncycastle.cms;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cms/CMSReadable.class */
interface CMSReadable {
    InputStream getInputStream() throws CMSException, IOException;
}
