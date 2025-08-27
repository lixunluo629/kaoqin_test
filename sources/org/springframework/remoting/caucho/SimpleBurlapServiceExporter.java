package org.springframework.remoting.caucho;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.lang.UsesSunHttpServer;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.support.WebContentGenerator;

@Deprecated
@UsesSunHttpServer
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/remoting/caucho/SimpleBurlapServiceExporter.class */
public class SimpleBurlapServiceExporter extends BurlapExporter implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        if (!WebContentGenerator.METHOD_POST.equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().set("Allow", WebContentGenerator.METHOD_POST);
            exchange.sendResponseHeaders(405, -1L);
            return;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        try {
            invoke(exchange.getRequestBody(), output);
        } catch (Throwable ex) {
            exchange.sendResponseHeaders(500, -1L);
            this.logger.error("Burlap skeleton invocation failed", ex);
        }
        exchange.sendResponseHeaders(200, output.size());
        FileCopyUtils.copy(output.toByteArray(), exchange.getResponseBody());
    }
}
