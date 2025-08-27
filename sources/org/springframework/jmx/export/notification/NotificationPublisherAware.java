package org.springframework.jmx.export.notification;

import org.springframework.beans.factory.Aware;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/notification/NotificationPublisherAware.class */
public interface NotificationPublisherAware extends Aware {
    void setNotificationPublisher(NotificationPublisher notificationPublisher);
}
