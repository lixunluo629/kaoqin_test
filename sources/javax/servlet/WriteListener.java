package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/WriteListener.class */
public interface WriteListener extends EventListener {
    void onWritePossible() throws IOException;

    void onError(Throwable th);
}
