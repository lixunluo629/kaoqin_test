package org.apache.log4j;

import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

/* loaded from: log4j-over-slf4j-1.7.26.jar:org/apache/log4j/Appender.class */
public interface Appender {
    void addFilter(Filter filter);

    Filter getFilter();

    void clearFilters();

    void close();

    void doAppend(LoggingEvent loggingEvent);

    String getName();

    void setErrorHandler(ErrorHandler errorHandler);

    ErrorHandler getErrorHandler();

    void setLayout(Layout layout);

    Layout getLayout();

    void setName(String str);

    boolean requiresLayout();
}
