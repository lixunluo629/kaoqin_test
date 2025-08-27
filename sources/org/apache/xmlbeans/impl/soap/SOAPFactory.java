package org.apache.xmlbeans.impl.soap;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/soap/SOAPFactory.class */
public abstract class SOAPFactory {
    private static final String SF_PROPERTY = "javax.xml.soap.SOAPFactory";
    private static final String DEFAULT_SF = "org.apache.axis.soap.SOAPFactoryImpl";

    public abstract SOAPElement createElement(Name name) throws SOAPException;

    public abstract SOAPElement createElement(String str) throws SOAPException;

    public abstract SOAPElement createElement(String str, String str2, String str3) throws SOAPException;

    public abstract Detail createDetail() throws SOAPException;

    public abstract Name createName(String str, String str2, String str3) throws SOAPException;

    public abstract Name createName(String str) throws SOAPException;

    public static SOAPFactory newInstance() throws SOAPException {
        try {
            return (SOAPFactory) FactoryFinder.find(SF_PROPERTY, DEFAULT_SF);
        } catch (Exception exception) {
            throw new SOAPException("Unable to create SOAP Factory: " + exception.getMessage());
        }
    }
}
