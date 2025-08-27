package org.apache.xmlbeans;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlDocumentProperties.class */
public abstract class XmlDocumentProperties {
    public static final Object SOURCE_NAME = new Object();
    public static final Object ENCODING = new Object();
    public static final Object VERSION = new Object();
    public static final Object STANDALONE = new Object();
    public static final Object DOCTYPE_NAME = new Object();
    public static final Object DOCTYPE_PUBLIC_ID = new Object();
    public static final Object DOCTYPE_SYSTEM_ID = new Object();
    public static final Object MESSAGE_DIGEST = new Object();

    public abstract Object put(Object obj, Object obj2);

    public abstract Object get(Object obj);

    public abstract Object remove(Object obj);

    public void setSourceName(String sourceName) {
        put(SOURCE_NAME, sourceName);
    }

    public String getSourceName() {
        return (String) get(SOURCE_NAME);
    }

    public void setEncoding(String encoding) {
        put(ENCODING, encoding);
    }

    public String getEncoding() {
        return (String) get(ENCODING);
    }

    public void setVersion(String version) {
        put(VERSION, version);
    }

    public String getVersion() {
        return (String) get(VERSION);
    }

    public void setStandalone(boolean standalone) {
        put(STANDALONE, standalone ? "true" : null);
    }

    public boolean getStandalone() {
        return get(STANDALONE) != null;
    }

    public void setDoctypeName(String doctypename) {
        put(DOCTYPE_NAME, doctypename);
    }

    public String getDoctypeName() {
        return (String) get(DOCTYPE_NAME);
    }

    public void setDoctypePublicId(String publicid) {
        put(DOCTYPE_PUBLIC_ID, publicid);
    }

    public String getDoctypePublicId() {
        return (String) get(DOCTYPE_PUBLIC_ID);
    }

    public void setDoctypeSystemId(String systemid) {
        put(DOCTYPE_SYSTEM_ID, systemid);
    }

    public String getDoctypeSystemId() {
        return (String) get(DOCTYPE_SYSTEM_ID);
    }

    public void setMessageDigest(byte[] digest) {
        put(MESSAGE_DIGEST, digest);
    }

    public byte[] getMessageDigest() {
        return (byte[]) get(MESSAGE_DIGEST);
    }
}
