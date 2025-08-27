package org.apache.tomcat.util.digester;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/digester/RuleSet.class */
public interface RuleSet {
    @Deprecated
    String getNamespaceURI();

    void addRuleInstances(Digester digester);
}
