package org.apache.catalina.core;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/StandardContextValve.class */
final class StandardContextValve extends ValveBase {
    private static final StringManager sm = StringManager.getManager((Class<?>) StandardContextValve.class);

    public StandardContextValve() {
        super(true);
    }

    @Override // org.apache.catalina.Valve
    public final void invoke(Request request, Response response) throws ServletException, IOException {
        MessageBytes requestPathMB = request.getRequestPathMB();
        if (requestPathMB.startsWithIgnoreCase("/META-INF/", 0) || requestPathMB.equalsIgnoreCase("/META-INF") || requestPathMB.startsWithIgnoreCase("/WEB-INF/", 0) || requestPathMB.equalsIgnoreCase("/WEB-INF")) {
            response.sendError(404);
            return;
        }
        Wrapper wrapper = request.getWrapper();
        if (wrapper == null || wrapper.isUnavailable()) {
            response.sendError(404);
            return;
        }
        try {
            response.sendAcknowledgement();
            if (request.isAsyncSupported()) {
                request.setAsyncSupported(wrapper.getPipeline().isAsyncSupported());
            }
            wrapper.getPipeline().getFirst().invoke(request, response);
        } catch (IOException ioe) {
            this.container.getLogger().error(sm.getString("standardContextValve.acknowledgeException"), ioe);
            request.setAttribute("javax.servlet.error.exception", ioe);
            response.sendError(500);
        }
    }
}
