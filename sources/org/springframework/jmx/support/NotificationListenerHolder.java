package org.springframework.jmx.support;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/support/NotificationListenerHolder.class */
public class NotificationListenerHolder {
    private NotificationListener notificationListener;
    private NotificationFilter notificationFilter;
    private Object handback;
    protected Set<Object> mappedObjectNames;

    public void setNotificationListener(NotificationListener notificationListener) {
        this.notificationListener = notificationListener;
    }

    public NotificationListener getNotificationListener() {
        return this.notificationListener;
    }

    public void setNotificationFilter(NotificationFilter notificationFilter) {
        this.notificationFilter = notificationFilter;
    }

    public NotificationFilter getNotificationFilter() {
        return this.notificationFilter;
    }

    public void setHandback(Object handback) {
        this.handback = handback;
    }

    public Object getHandback() {
        return this.handback;
    }

    public void setMappedObjectName(Object mappedObjectName) {
        setMappedObjectNames(mappedObjectName != null ? new Object[]{mappedObjectName} : null);
    }

    public void setMappedObjectNames(Object[] mappedObjectNames) {
        this.mappedObjectNames = mappedObjectNames != null ? new LinkedHashSet(Arrays.asList(mappedObjectNames)) : null;
    }

    public ObjectName[] getResolvedObjectNames() throws MalformedObjectNameException {
        if (this.mappedObjectNames == null) {
            return null;
        }
        ObjectName[] resolved = new ObjectName[this.mappedObjectNames.size()];
        int i = 0;
        for (Object objectName : this.mappedObjectNames) {
            resolved[i] = ObjectNameManager.getInstance(objectName);
            i++;
        }
        return resolved;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NotificationListenerHolder)) {
            return false;
        }
        NotificationListenerHolder otherNlh = (NotificationListenerHolder) other;
        return ObjectUtils.nullSafeEquals(this.notificationListener, otherNlh.notificationListener) && ObjectUtils.nullSafeEquals(this.notificationFilter, otherNlh.notificationFilter) && ObjectUtils.nullSafeEquals(this.handback, otherNlh.handback) && ObjectUtils.nullSafeEquals(this.mappedObjectNames, otherNlh.mappedObjectNames);
    }

    public int hashCode() {
        int hashCode = ObjectUtils.nullSafeHashCode(this.notificationListener);
        return (29 * ((29 * ((29 * hashCode) + ObjectUtils.nullSafeHashCode(this.notificationFilter))) + ObjectUtils.nullSafeHashCode(this.handback))) + ObjectUtils.nullSafeHashCode(this.mappedObjectNames);
    }
}
