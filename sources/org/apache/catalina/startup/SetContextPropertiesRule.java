package org.apache.catalina.startup;

import org.apache.commons.httpclient.cookie.Cookie2;
import org.apache.tomcat.util.IntrospectionUtils;
import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/startup/SetContextPropertiesRule.class */
public class SetContextPropertiesRule extends Rule {
    @Override // org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String nameX, Attributes attributes) throws Exception {
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getLocalName(i);
            if ("".equals(name)) {
                name = attributes.getQName(i);
            }
            if (!Cookie2.PATH.equals(name) && !"docBase".equals(name)) {
                String value = attributes.getValue(i);
                if (!this.digester.isFakeAttribute(this.digester.peek(), name) && !IntrospectionUtils.setProperty(this.digester.peek(), name, value) && this.digester.getRulesValidation()) {
                    this.digester.getLogger().warn("[SetContextPropertiesRule]{" + this.digester.getMatch() + "} Setting property '" + name + "' to '" + value + "' did not find a matching property.");
                }
            }
        }
    }
}
