package org.apache.xmlbeans.impl.soap;

import java.util.Iterator;
import javax.xml.transform.Source;
import org.w3c.dom.Document;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPPart.class */
public abstract class SOAPPart implements Document {
    public abstract SOAPEnvelope getEnvelope() throws SOAPException;

    public abstract void removeMimeHeader(String str);

    public abstract void removeAllMimeHeaders();

    public abstract String[] getMimeHeader(String str);

    public abstract void setMimeHeader(String str, String str2);

    public abstract void addMimeHeader(String str, String str2);

    public abstract Iterator getAllMimeHeaders();

    public abstract Iterator getMatchingMimeHeaders(String[] strArr);

    public abstract Iterator getNonMatchingMimeHeaders(String[] strArr);

    public abstract void setContent(Source source) throws SOAPException;

    public abstract Source getContent() throws SOAPException;

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

    public void setContentId(String contentId) {
        setMimeHeader("Content-Id", contentId);
    }

    public void setContentLocation(String contentLocation) {
        setMimeHeader("Content-Location", contentLocation);
    }
}
