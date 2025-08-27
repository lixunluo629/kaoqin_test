package org.apache.catalina;

import java.util.EventObject;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/LifecycleEvent.class */
public final class LifecycleEvent extends EventObject {
    private static final long serialVersionUID = 1;
    private final Object data;
    private final String type;

    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.type = type;
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public Lifecycle getLifecycle() {
        return (Lifecycle) getSource();
    }

    public String getType() {
        return this.type;
    }
}
