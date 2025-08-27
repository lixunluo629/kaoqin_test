package org.apache.catalina;

import java.util.Set;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/Pipeline.class */
public interface Pipeline {
    Valve getBasic();

    void setBasic(Valve valve);

    void addValve(Valve valve);

    Valve[] getValves();

    void removeValve(Valve valve);

    Valve getFirst();

    boolean isAsyncSupported();

    Container getContainer();

    void setContainer(Container container);

    void findNonAsyncValves(Set<String> set);
}
