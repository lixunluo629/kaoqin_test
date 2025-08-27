package springfox.documentation.spi.service;

import java.util.List;
import springfox.documentation.RequestHandler;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/RequestHandlerProvider.class */
public interface RequestHandlerProvider {
    List<RequestHandler> requestHandlers();
}
