package org.ehcache.jsr107.internal;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.ehcache.jsr107.config.ConfigurationElementState;
import org.ehcache.jsr107.config.Jsr107CacheConfiguration;
import org.ehcache.jsr107.config.Jsr107Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.xml.CacheServiceConfigurationParser;
import org.ehcache.xml.exceptions.XmlConfigurationException;
import org.w3c.dom.Element;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/internal/Jsr107CacheConfigurationParser.class */
public class Jsr107CacheConfigurationParser implements CacheServiceConfigurationParser<Jsr107Service> {
    private static final URI NAMESPACE = URI.create("http://www.ehcache.org/v3/jsr107");
    private static final URL XML_SCHEMA = Jsr107CacheConfigurationParser.class.getResource("/ehcache-107ext.xsd");
    private static final String MANAGEMENT_ENABLED_ATTRIBUTE = "enable-management";
    private static final String STATISTICS_ENABLED_ATTRIBUTE = "enable-statistics";

    @Override // org.ehcache.xml.CacheServiceConfigurationParser
    public Source getXmlSchema() throws IOException {
        return new StreamSource(XML_SCHEMA.openStream());
    }

    @Override // org.ehcache.xml.CacheServiceConfigurationParser
    public URI getNamespace() {
        return NAMESPACE;
    }

    @Override // org.ehcache.xml.CacheServiceConfigurationParser
    public ServiceConfiguration<Jsr107Service> parseServiceConfiguration(Element fragment) {
        String localName = fragment.getLocalName();
        if ("mbeans".equals(localName)) {
            ConfigurationElementState managementEnabled = ConfigurationElementState.UNSPECIFIED;
            ConfigurationElementState statisticsEnabled = ConfigurationElementState.UNSPECIFIED;
            if (fragment.hasAttribute(MANAGEMENT_ENABLED_ATTRIBUTE)) {
                managementEnabled = Boolean.parseBoolean(fragment.getAttribute(MANAGEMENT_ENABLED_ATTRIBUTE)) ? ConfigurationElementState.ENABLED : ConfigurationElementState.DISABLED;
            }
            if (fragment.hasAttribute(STATISTICS_ENABLED_ATTRIBUTE)) {
                statisticsEnabled = Boolean.parseBoolean(fragment.getAttribute(STATISTICS_ENABLED_ATTRIBUTE)) ? ConfigurationElementState.ENABLED : ConfigurationElementState.DISABLED;
            }
            return new Jsr107CacheConfiguration(statisticsEnabled, managementEnabled);
        }
        Object[] objArr = new Object[2];
        objArr[0] = fragment.getTagName();
        objArr[1] = fragment.getParentNode() == null ? "null" : fragment.getParentNode().getLocalName();
        throw new XmlConfigurationException(String.format("XML configuration element <%s> in <%s> is not supported", objArr));
    }
}
