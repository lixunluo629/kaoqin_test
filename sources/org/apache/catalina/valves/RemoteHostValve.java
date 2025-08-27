package org.apache.catalina.valves;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/RemoteHostValve.class */
public final class RemoteHostValve extends RequestFilterValve {
    private static final Log log = LogFactory.getLog((Class<?>) RemoteHostValve.class);

    @Override // org.apache.catalina.valves.RequestFilterValve, org.apache.catalina.Valve
    public void invoke(Request request, Response response) throws ServletException, IOException {
        String property;
        if (getAddConnectorPort()) {
            property = request.getRequest().getRemoteHost() + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR + request.getConnector().getPort();
        } else {
            property = request.getRequest().getRemoteHost();
        }
        process(property, request, response);
    }

    @Override // org.apache.catalina.valves.RequestFilterValve
    protected Log getLog() {
        return log;
    }
}
