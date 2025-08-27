package org.ehcache.xml.exceptions;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/exceptions/XmlConfigurationException.class */
public class XmlConfigurationException extends RuntimeException {
    private static final long serialVersionUID = 4797841652996371653L;

    public XmlConfigurationException(String message) {
        super(message);
    }

    public XmlConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public XmlConfigurationException(Throwable cause) {
        super(cause);
    }
}
