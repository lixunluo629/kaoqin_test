package org.ehcache.jsr107.internal;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.ehcache.jsr107.config.ConfigurationElementState;
import org.ehcache.jsr107.config.Jsr107Configuration;
import org.ehcache.jsr107.config.Jsr107Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.ehcache.xml.CacheManagerServiceConfigurationParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/internal/Jsr107ServiceConfigurationParser.class */
public class Jsr107ServiceConfigurationParser implements CacheManagerServiceConfigurationParser<Jsr107Service> {
    private static final URI NAMESPACE = URI.create("http://www.ehcache.org/v3/jsr107");
    private static final URL XML_SCHEMA = Jsr107ServiceConfigurationParser.class.getResource("/ehcache-107ext.xsd");
    private static final String ENABLE_MANAGEMENT_ALL_ATTRIBUTE = "enable-management";
    private static final String JSR_107_COMPLIANT_ATOMICS_ATTRIBUTE = "jsr-107-compliant-atomics";
    private static final String ENABLE_STATISTICS_ALL_ATTRIBUTE = "enable-statistics";
    private static final String DEFAULT_TEMPLATE_ATTRIBUTE = "default-template";
    private static final String CACHE_NAME_ATTRIBUTE = "name";
    private static final String TEMPLATE_NAME_ATTRIBUTE = "template";

    @Override // org.ehcache.xml.CacheManagerServiceConfigurationParser
    public Source getXmlSchema() throws IOException {
        return new StreamSource(XML_SCHEMA.openStream());
    }

    @Override // org.ehcache.xml.CacheManagerServiceConfigurationParser
    public URI getNamespace() {
        return NAMESPACE;
    }

    @Override // org.ehcache.xml.CacheManagerServiceConfigurationParser
    public ServiceCreationConfiguration<Jsr107Service> parseServiceCreationConfiguration(Element fragment) {
        boolean jsr107CompliantAtomics = true;
        ConfigurationElementState enableManagementAll = ConfigurationElementState.UNSPECIFIED;
        ConfigurationElementState enableStatisticsAll = ConfigurationElementState.UNSPECIFIED;
        if (fragment.hasAttribute(JSR_107_COMPLIANT_ATOMICS_ATTRIBUTE)) {
            jsr107CompliantAtomics = Boolean.parseBoolean(fragment.getAttribute(JSR_107_COMPLIANT_ATOMICS_ATTRIBUTE));
        }
        if (fragment.hasAttribute(ENABLE_MANAGEMENT_ALL_ATTRIBUTE)) {
            enableManagementAll = Boolean.parseBoolean(fragment.getAttribute(ENABLE_MANAGEMENT_ALL_ATTRIBUTE)) ? ConfigurationElementState.ENABLED : ConfigurationElementState.DISABLED;
        }
        if (fragment.hasAttribute(ENABLE_STATISTICS_ALL_ATTRIBUTE)) {
            enableStatisticsAll = Boolean.parseBoolean(fragment.getAttribute(ENABLE_STATISTICS_ALL_ATTRIBUTE)) ? ConfigurationElementState.ENABLED : ConfigurationElementState.DISABLED;
        }
        String defaultTemplate = fragment.getAttribute(DEFAULT_TEMPLATE_ATTRIBUTE);
        HashMap<String, String> templates = new HashMap<>();
        NodeList childNodes = fragment.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == 1) {
                Element item = (Element) node;
                templates.put(item.getAttribute("name"), item.getAttribute(TEMPLATE_NAME_ATTRIBUTE));
            }
        }
        return new Jsr107Configuration(defaultTemplate, templates, jsr107CompliantAtomics, enableManagementAll, enableStatisticsAll);
    }
}
