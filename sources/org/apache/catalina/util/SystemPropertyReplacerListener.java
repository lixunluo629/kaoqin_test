package org.apache.catalina.util;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.juli.logging.LogConfigurationException;
import org.apache.tomcat.util.digester.Digester;

@Deprecated
/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/util/SystemPropertyReplacerListener.class */
public class SystemPropertyReplacerListener implements LifecycleListener {
    @Override // org.apache.catalina.LifecycleListener
    public void lifecycleEvent(LifecycleEvent event) throws LogConfigurationException {
        if (Lifecycle.BEFORE_INIT_EVENT.equals(event.getType())) {
            Digester.replaceSystemProperties();
        }
    }
}
