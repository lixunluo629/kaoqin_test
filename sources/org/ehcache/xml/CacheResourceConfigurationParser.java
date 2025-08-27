package org.ehcache.xml;

import java.io.IOException;
import java.net.URI;
import javax.xml.transform.Source;
import org.ehcache.config.ResourcePool;
import org.w3c.dom.Element;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/CacheResourceConfigurationParser.class */
public interface CacheResourceConfigurationParser {
    Source getXmlSchema() throws IOException;

    URI getNamespace();

    ResourcePool parseResourceConfiguration(Element element);
}
