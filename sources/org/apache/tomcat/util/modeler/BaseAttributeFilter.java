package org.apache.tomcat.util.modeler;

import java.util.HashSet;
import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/modeler/BaseAttributeFilter.class */
public class BaseAttributeFilter implements NotificationFilter {
    private static final long serialVersionUID = 1;
    private HashSet<String> names = new HashSet<>();

    public BaseAttributeFilter(String name) {
        if (name != null) {
            addAttribute(name);
        }
    }

    public void addAttribute(String name) {
        synchronized (this.names) {
            this.names.add(name);
        }
    }

    public void clear() {
        synchronized (this.names) {
            this.names.clear();
        }
    }

    public String[] getNames() {
        String[] strArr;
        synchronized (this.names) {
            strArr = (String[]) this.names.toArray(new String[this.names.size()]);
        }
        return strArr;
    }

    public boolean isNotificationEnabled(Notification notification) {
        if (notification == null || !(notification instanceof AttributeChangeNotification)) {
            return false;
        }
        AttributeChangeNotification acn = (AttributeChangeNotification) notification;
        if (!"jmx.attribute.change".equals(acn.getType())) {
            return false;
        }
        synchronized (this.names) {
            if (this.names.size() < 1) {
                return true;
            }
            return this.names.contains(acn.getAttributeName());
        }
    }

    public void removeAttribute(String name) {
        synchronized (this.names) {
            this.names.remove(name);
        }
    }
}
