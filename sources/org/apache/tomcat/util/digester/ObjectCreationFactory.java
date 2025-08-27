package org.apache.tomcat.util.digester;

import org.xml.sax.Attributes;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/digester/ObjectCreationFactory.class */
public interface ObjectCreationFactory {
    Object createObject(Attributes attributes) throws Exception;

    Digester getDigester();

    void setDigester(Digester digester);
}
