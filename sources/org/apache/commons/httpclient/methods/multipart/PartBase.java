package org.apache.commons.httpclient.methods.multipart;

/* loaded from: commons-httpclient-3.1.jar:org/apache/commons/httpclient/methods/multipart/PartBase.class */
public abstract class PartBase extends Part {
    private String name;
    private String contentType;
    private String charSet;
    private String transferEncoding;

    public PartBase(String name, String contentType, String charSet, String transferEncoding) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        this.name = name;
        this.contentType = contentType;
        this.charSet = charSet;
        this.transferEncoding = transferEncoding;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    public String getName() {
        return this.name;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    public String getContentType() {
        return this.contentType;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    public String getCharSet() {
        return this.charSet;
    }

    @Override // org.apache.commons.httpclient.methods.multipart.Part
    public String getTransferEncoding() {
        return this.transferEncoding;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be null");
        }
        this.name = name;
    }

    public void setTransferEncoding(String transferEncoding) {
        this.transferEncoding = transferEncoding;
    }
}
