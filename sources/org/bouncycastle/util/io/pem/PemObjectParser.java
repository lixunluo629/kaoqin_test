package org.bouncycastle.util.io.pem;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/io/pem/PemObjectParser.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/io/pem/PemObjectParser.class */
public interface PemObjectParser {
    Object parseObject(PemObject pemObject) throws IOException;
}
