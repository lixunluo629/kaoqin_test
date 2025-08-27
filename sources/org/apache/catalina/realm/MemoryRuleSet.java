package org.apache.catalina.realm;

import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/realm/MemoryRuleSet.class */
public class MemoryRuleSet extends RuleSetBase {
    protected final String prefix;

    public MemoryRuleSet() {
        this("tomcat-users/");
    }

    public MemoryRuleSet(String prefix) {
        this.prefix = prefix;
    }

    @Override // org.apache.tomcat.util.digester.RuleSetBase, org.apache.tomcat.util.digester.RuleSet
    public void addRuleInstances(Digester digester) {
        digester.addRule(this.prefix + "user", new MemoryUserRule());
    }
}
