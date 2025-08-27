package org.apache.catalina;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/AsyncDispatcher.class */
public interface AsyncDispatcher {
    void dispatch(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException;
}
