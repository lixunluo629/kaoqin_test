package org.apache.catalina.core;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/JniLifecycleListener.class */
public class JniLifecycleListener implements LifecycleListener {
    private static final Log log = LogFactory.getLog((Class<?>) JniLifecycleListener.class);
    private String libraryName = "";
    private String libraryPath = "";

    @Override // org.apache.catalina.LifecycleListener
    public void lifecycleEvent(LifecycleEvent event) {
        if (Lifecycle.BEFORE_START_EVENT.equals(event.getType())) {
            if (!this.libraryName.isEmpty()) {
                System.loadLibrary(this.libraryName);
                log.info("Loaded native library " + this.libraryName);
            } else {
                if (!this.libraryPath.isEmpty()) {
                    System.load(this.libraryPath);
                    log.info("Loaded native library from " + this.libraryPath);
                    return;
                }
                throw new IllegalArgumentException("Either libraryName or libraryPath must be set");
            }
        }
    }

    public void setLibraryName(String libraryName) {
        if (!this.libraryPath.isEmpty()) {
            throw new IllegalArgumentException("Either libraryName or libraryPath may be set, not both.");
        }
        this.libraryName = libraryName;
    }

    public String getLibraryName() {
        return this.libraryName;
    }

    public void setLibraryPath(String libraryPath) {
        if (!this.libraryName.isEmpty()) {
            throw new IllegalArgumentException("Either libraryName or libraryPath may be set, not both.");
        }
        this.libraryPath = libraryPath;
    }

    public String getLibraryPath() {
        return this.libraryPath;
    }
}
