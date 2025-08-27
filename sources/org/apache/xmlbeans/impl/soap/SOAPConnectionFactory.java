package org.apache.xmlbeans.impl.soap;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPConnectionFactory.class */
public abstract class SOAPConnectionFactory {
    private static final String DEFAULT_SOAP_CONNECTION_FACTORY = "org.apache.axis.soap.SOAPConnectionFactoryImpl";
    private static final String SF_PROPERTY = "javax.xml.soap.SOAPConnectionFactory";

    public abstract SOAPConnection createConnection() throws SOAPException;

    public static SOAPConnectionFactory newInstance() throws UnsupportedOperationException, SOAPException {
        try {
            return (SOAPConnectionFactory) FactoryFinder.find(SF_PROPERTY, DEFAULT_SOAP_CONNECTION_FACTORY);
        } catch (Exception exception) {
            throw new SOAPException("Unable to create SOAP connection factory: " + exception.getMessage());
        }
    }
}
