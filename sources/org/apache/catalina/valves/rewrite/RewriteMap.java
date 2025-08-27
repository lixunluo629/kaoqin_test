package org.apache.catalina.valves.rewrite;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/rewrite/RewriteMap.class */
public interface RewriteMap {
    String setParameters(String str);

    String lookup(String str);
}
