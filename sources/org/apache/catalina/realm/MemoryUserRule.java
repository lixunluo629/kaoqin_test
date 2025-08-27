package org.apache.catalina.realm;

import com.mysql.jdbc.NonRegisteringDriver;
import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;

/* compiled from: MemoryRuleSet.java */
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/realm/MemoryUserRule.class */
final class MemoryUserRule extends Rule {
    @Override // org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        String username = attributes.getValue("username");
        if (username == null) {
            username = attributes.getValue("name");
        }
        String password = attributes.getValue(NonRegisteringDriver.PASSWORD_PROPERTY_KEY);
        String roles = attributes.getValue("roles");
        MemoryRealm realm = (MemoryRealm) this.digester.peek(this.digester.getCount() - 1);
        realm.addUser(username, password, roles);
    }
}
