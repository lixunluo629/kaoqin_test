package org.springframework.remoting.caucho;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.support.WebContentGenerator;
import org.springframework.web.util.NestedServletException;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/caucho/BurlapServiceExporter.class */
public class BurlapServiceExporter extends BurlapExporter implements HttpRequestHandler {
    @Override // org.springframework.web.HttpRequestHandler
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!WebContentGenerator.METHOD_POST.equals(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[]{WebContentGenerator.METHOD_POST}, "BurlapServiceExporter only supports POST requests");
        }
        try {
            invoke(request.getInputStream(), response.getOutputStream());
        } catch (Throwable ex) {
            throw new NestedServletException("Burlap skeleton invocation failed", ex);
        }
    }
}
