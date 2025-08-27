package org.apache.tomcat.util.digester;

import java.util.List;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/digester/Rules.class */
public interface Rules {
    Digester getDigester();

    void setDigester(Digester digester);

    @Deprecated
    String getNamespaceURI();

    @Deprecated
    void setNamespaceURI(String str);

    void add(String str, Rule rule);

    void clear();

    List<Rule> match(String str, String str2);

    List<Rule> rules();
}
