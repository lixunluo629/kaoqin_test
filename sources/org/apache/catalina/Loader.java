package org.apache.catalina;

import java.beans.PropertyChangeListener;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/Loader.class */
public interface Loader {
    void backgroundProcess();

    ClassLoader getClassLoader();

    Context getContext();

    void setContext(Context context);

    boolean getDelegate();

    void setDelegate(boolean z);

    boolean getReloadable();

    void setReloadable(boolean z);

    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    boolean modified();

    void removePropertyChangeListener(PropertyChangeListener propertyChangeListener);
}
