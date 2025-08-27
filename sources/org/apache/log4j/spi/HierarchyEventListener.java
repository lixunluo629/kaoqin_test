package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;

/* loaded from: log4j-over-slf4j-1.7.26.jar:org/apache/log4j/spi/HierarchyEventListener.class */
public interface HierarchyEventListener {
    void addAppenderEvent(Category category, Appender appender);

    void removeAppenderEvent(Category category, Appender appender);
}
