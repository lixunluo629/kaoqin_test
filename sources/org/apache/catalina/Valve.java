package org.apache.catalina;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/Valve.class */
public interface Valve {
    Valve getNext();

    void setNext(Valve valve);

    void backgroundProcess();

    void invoke(Request request, Response response) throws ServletException, IOException;

    boolean isAsyncSupported();
}
