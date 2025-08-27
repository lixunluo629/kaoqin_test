package org.springframework.boot.context.embedded.undertow;

import io.undertow.UndertowMessages;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.server.handlers.resource.URLResource;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/JarResourceManager.class */
class JarResourceManager implements ResourceManager {
    private final String jarPath;

    JarResourceManager(File jarFile) {
        this(jarFile.getAbsolutePath());
    }

    JarResourceManager(String jarPath) {
        this.jarPath = jarPath;
    }

    public Resource getResource(String path) throws IOException {
        URL url = new URL("jar:file:" + this.jarPath + "!" + (path.startsWith("/") ? path : "/" + path));
        URLResource resource = new URLResource(url, path);
        if (resource.getContentLength().longValue() < 0) {
            return null;
        }
        return resource;
    }

    public boolean isResourceChangeListenerSupported() {
        return false;
    }

    public void registerResourceChangeListener(ResourceChangeListener listener) {
        throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
    }

    public void removeResourceChangeListener(ResourceChangeListener listener) {
        throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
    }

    public void close() throws IOException {
    }
}
