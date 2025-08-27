package org.bouncycastle.x509.util;

import java.util.Collection;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/x509/util/StreamParser.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/x509/util/StreamParser.class */
public interface StreamParser {
    Object read() throws StreamParsingException;

    Collection readAll() throws StreamParsingException;
}
