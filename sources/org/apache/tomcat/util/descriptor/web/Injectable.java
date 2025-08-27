package org.apache.tomcat.util.descriptor.web;

import java.util.List;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/descriptor/web/Injectable.class */
public interface Injectable {
    String getName();

    void addInjectionTarget(String str, String str2);

    List<InjectionTarget> getInjectionTargets();
}
