package org.apache.tomcat.util.descriptor.web;

import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;

/* compiled from: WebRuleSet.java */
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/descriptor/web/NameRule.class */
final class NameRule extends Rule {
    boolean isNameSet = false;

    @Override // org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        if (this.isNameSet) {
            throw new IllegalArgumentException(WebRuleSet.sm.getString("webRuleSet.nameCount"));
        }
        this.isNameSet = true;
    }

    @Override // org.apache.tomcat.util.digester.Rule
    public void body(String namespace, String name, String text) throws Exception {
        super.body(namespace, name, text);
        ((WebXml) this.digester.peek()).setName(text);
    }
}
