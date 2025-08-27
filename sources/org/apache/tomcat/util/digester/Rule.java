package org.apache.tomcat.util.digester;

import org.xml.sax.Attributes;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/digester/Rule.class */
public abstract class Rule {
    protected Digester digester = null;
    protected String namespaceURI = null;

    public Digester getDigester() {
        return this.digester;
    }

    public void setDigester(Digester digester) {
        this.digester = digester;
    }

    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    public void begin(String namespace, String name, Attributes attributes) throws Exception {
    }

    public void body(String namespace, String name, String text) throws Exception {
    }

    public void end(String namespace, String name) throws Exception {
    }

    public void finish() throws Exception {
    }
}
