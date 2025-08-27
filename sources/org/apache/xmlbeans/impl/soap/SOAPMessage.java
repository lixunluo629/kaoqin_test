package org.apache.xmlbeans.impl.soap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPMessage.class */
public abstract class SOAPMessage {
    public static final String CHARACTER_SET_ENCODING = "javax.xml.soap.character-set-encoding";
    public static final String WRITE_XML_DECLARATION = "javax.xml.soap.write-xml-declaration";

    public abstract String getContentDescription();

    public abstract void setContentDescription(String str);

    public abstract SOAPPart getSOAPPart();

    public abstract void removeAllAttachments();

    public abstract int countAttachments();

    public abstract Iterator getAttachments();

    public abstract Iterator getAttachments(MimeHeaders mimeHeaders);

    public abstract void addAttachmentPart(AttachmentPart attachmentPart);

    public abstract AttachmentPart createAttachmentPart();

    public abstract MimeHeaders getMimeHeaders();

    public abstract void saveChanges() throws SOAPException;

    public abstract boolean saveRequired();

    public abstract void writeTo(OutputStream outputStream) throws SOAPException, IOException;

    public abstract SOAPBody getSOAPBody() throws SOAPException;

    public abstract SOAPHeader getSOAPHeader() throws SOAPException;

    public abstract void setProperty(String str, Object obj) throws SOAPException;

    public abstract Object getProperty(String str) throws SOAPException;

    public AttachmentPart createAttachmentPart(Object content, String contentType) {
        AttachmentPart attachmentpart = createAttachmentPart();
        attachmentpart.setContent(content, contentType);
        return attachmentpart;
    }
}
