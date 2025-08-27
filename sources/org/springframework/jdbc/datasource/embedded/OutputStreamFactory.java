package org.springframework.jdbc.datasource.embedded;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/embedded/OutputStreamFactory.class */
public class OutputStreamFactory {
    public static OutputStream getNoopOutputStream() {
        return new OutputStream() { // from class: org.springframework.jdbc.datasource.embedded.OutputStreamFactory.1
            @Override // java.io.OutputStream
            public void write(int b) throws IOException {
            }
        };
    }
}
