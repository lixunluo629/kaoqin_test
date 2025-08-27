package org.apache.catalina;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Request;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/Authenticator.class */
public interface Authenticator {
    boolean authenticate(Request request, HttpServletResponse httpServletResponse) throws IOException;

    void login(String str, String str2, Request request) throws ServletException;

    void logout(Request request);
}
