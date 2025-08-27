package org.ehcache.jsr107.internal.tck;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerBuilder;
import javax.management.MBeanServerDelegate;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/internal/tck/Eh107MBeanServerBuilder.class */
public class Eh107MBeanServerBuilder extends MBeanServerBuilder {
    public MBeanServer newMBeanServer(String defaultDomain, MBeanServer outer, MBeanServerDelegate delegate) {
        MBeanServerDelegate decoratingDelegate = new Eh107MBeanServerDelegate(delegate);
        return super.newMBeanServer(defaultDomain, outer, decoratingDelegate);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/internal/tck/Eh107MBeanServerBuilder$Eh107MBeanServerDelegate.class */
    public static class Eh107MBeanServerDelegate extends MBeanServerDelegate {
        private final MBeanServerDelegate delegate;

        public Eh107MBeanServerDelegate(MBeanServerDelegate delegate) {
            this.delegate = delegate;
        }

        public String getSpecificationName() {
            return this.delegate.getSpecificationName();
        }

        public String getSpecificationVersion() {
            return this.delegate.getSpecificationVersion();
        }

        public String getSpecificationVendor() {
            return this.delegate.getSpecificationVendor();
        }

        public String getImplementationName() {
            return this.delegate.getImplementationName();
        }

        public String getImplementationVersion() {
            return this.delegate.getImplementationVersion();
        }

        public String getImplementationVendor() {
            return this.delegate.getImplementationVendor();
        }

        public MBeanNotificationInfo[] getNotificationInfo() {
            return this.delegate.getNotificationInfo();
        }

        public synchronized void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
            this.delegate.addNotificationListener(listener, filter, handback);
        }

        public synchronized void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws ListenerNotFoundException {
            this.delegate.removeNotificationListener(listener, filter, handback);
        }

        public synchronized void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
            this.delegate.removeNotificationListener(listener);
        }

        public void sendNotification(Notification notification) {
            this.delegate.sendNotification(notification);
        }

        public synchronized String getMBeanServerId() {
            return System.getProperty("org.jsr107.tck.management.agentId");
        }
    }
}
