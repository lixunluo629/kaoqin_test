package org.springframework.data.projection;

import java.beans.PropertyDescriptor;
import java.util.List;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/ProjectionInformation.class */
public interface ProjectionInformation {
    Class<?> getType();

    List<PropertyDescriptor> getInputProperties();

    boolean isClosed();
}
