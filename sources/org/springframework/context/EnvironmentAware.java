package org.springframework.context;

import org.springframework.beans.factory.Aware;
import org.springframework.core.env.Environment;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/EnvironmentAware.class */
public interface EnvironmentAware extends Aware {
    void setEnvironment(Environment environment);
}
