package org.apache.xmlbeans.impl.soap;

import java.util.Iterator;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/AttachmentPart.class */
public abstract class AttachmentPart {
    public abstract int getSize() throws SOAPException;

    public abstract void clearContent();

    public abstract Object getContent() throws SOAPException;

    public abstract void setContent(Object obj, String str);

    public abstract void removeMimeHeader(String str);

    public abstract void removeAllMimeHeaders();

    public abstract String[] getMimeHeader(String str);

    public abstract void setMimeHeader(String str, String str2);

    public abstract void addMimeHeader(String str, String str2);

    public abstract Iterator getAllMimeHeaders();

    public abstract Iterator getMatchingMimeHeaders(String[] strArr);

    public abstract Iterator getNonMatchingMimeHeaders(String[] strArr);

    public String getContentId() {
        String[] as = getMimeHeader("Content-Id");
        if (as != null && as.length > 0) {
            return as[0];
        }
        return null;
    }

    public String getContentLocation() {
        String[] as = getMimeHeader("Content-Location");
        if (as != null && as.length > 0) {
            return as[0];
        }
        return null;
    }

    public String getContentType() {
        String[] as = getMimeHeader("Content-Type");
        if (as != null && as.length > 0) {
            return as[0];
        }
        return null;
    }

    public void setContentId(String contentId) {
        setMimeHeader("Content-Id", contentId);
    }

    public void setContentLocation(String contentLocation) {
        setMimeHeader("Content-Location", contentLocation);
    }

    public void setContentType(String contentType) {
        setMimeHeader("Content-Type", contentType);
    }
}
