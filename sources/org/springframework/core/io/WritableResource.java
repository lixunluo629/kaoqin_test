package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/io/WritableResource.class */
public interface WritableResource extends Resource {
    boolean isWritable();

    OutputStream getOutputStream() throws IOException;
}
