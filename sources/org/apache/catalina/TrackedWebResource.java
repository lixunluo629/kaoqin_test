package org.apache.catalina;

import java.io.Closeable;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/TrackedWebResource.class */
public interface TrackedWebResource extends Closeable {
    Exception getCreatedBy();

    String getName();
}
