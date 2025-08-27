package org.apache.xmlbeans.impl.soap;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/MessageFactory.class */
public abstract class MessageFactory {
    private static final String DEFAULT_MESSAGE_FACTORY = "org.apache.axis.soap.MessageFactoryImpl";
    private static final String MESSAGE_FACTORY_PROPERTY = "javax.xml.soap.MessageFactory";

    public abstract SOAPMessage createMessage() throws SOAPException;

    public abstract SOAPMessage createMessage(MimeHeaders mimeHeaders, InputStream inputStream) throws SOAPException, IOException;

    public static MessageFactory newInstance() throws SOAPException {
        try {
            return (MessageFactory) FactoryFinder.find(MESSAGE_FACTORY_PROPERTY, DEFAULT_MESSAGE_FACTORY);
        } catch (Exception exception) {
            throw new SOAPException("Unable to create message factory for SOAP: " + exception.getMessage());
        }
    }
}
