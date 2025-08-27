package org.apache.catalina.core;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/StandardEngineValve.class */
final class StandardEngineValve extends ValveBase {
    private static final StringManager sm = StringManager.getManager(Constants.Package);

    public StandardEngineValve() {
        super(true);
    }

    @Override // org.apache.catalina.Valve
    public final void invoke(Request request, Response response) throws ServletException, IOException {
        Host host = request.getHost();
        if (host == null) {
            response.sendError(400, sm.getString("standardEngine.noHost", request.getServerName()));
            return;
        }
        if (request.isAsyncSupported()) {
            request.setAsyncSupported(host.getPipeline().isAsyncSupported());
        }
        host.getPipeline().getFirst().invoke(request, response);
    }
}
