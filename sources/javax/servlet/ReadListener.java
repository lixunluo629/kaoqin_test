package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/ReadListener.class */
public interface ReadListener extends EventListener {
    void onDataAvailable() throws IOException;

    void onAllDataRead() throws IOException;

    void onError(Throwable th);
}
