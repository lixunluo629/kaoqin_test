package org.apache.tomcat.util.digester;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/digester/RuleSetBase.class */
public abstract class RuleSetBase implements RuleSet {

    @Deprecated
    protected String namespaceURI = null;

    @Override // org.apache.tomcat.util.digester.RuleSet
    public abstract void addRuleInstances(Digester digester);

    @Override // org.apache.tomcat.util.digester.RuleSet
    @Deprecated
    public String getNamespaceURI() {
        return this.namespaceURI;
    }
}
