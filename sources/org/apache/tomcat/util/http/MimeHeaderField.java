package org.apache.tomcat.util.http;

import org.apache.tomcat.util.buf.MessageBytes;

/* compiled from: MimeHeaders.java */
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/http/MimeHeaderField.class */
class MimeHeaderField {
    private final MessageBytes nameB = MessageBytes.newInstance();
    private final MessageBytes valueB = MessageBytes.newInstance();

    public void recycle() {
        this.nameB.recycle();
        this.valueB.recycle();
    }

    public MessageBytes getName() {
        return this.nameB;
    }

    public MessageBytes getValue() {
        return this.valueB;
    }
}
