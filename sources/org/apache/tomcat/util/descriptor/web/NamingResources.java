package org.apache.tomcat.util.descriptor.web;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/descriptor/web/NamingResources.class */
public interface NamingResources {
    void addEnvironment(ContextEnvironment contextEnvironment);

    void removeEnvironment(String str);

    void addResource(ContextResource contextResource);

    void removeResource(String str);

    void addResourceLink(ContextResourceLink contextResourceLink);

    void removeResourceLink(String str);

    Object getContainer();
}
