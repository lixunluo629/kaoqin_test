package org.springframework.jmx.export.metadata;

import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/metadata/ManagedNotification.class */
public class ManagedNotification {
    private String[] notificationTypes;
    private String name;
    private String description;

    public void setNotificationType(String notificationType) {
        this.notificationTypes = StringUtils.commaDelimitedListToStringArray(notificationType);
    }

    public void setNotificationTypes(String... notificationTypes) {
        this.notificationTypes = notificationTypes;
    }

    public String[] getNotificationTypes() {
        return this.notificationTypes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
