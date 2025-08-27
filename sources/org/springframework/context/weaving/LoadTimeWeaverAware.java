package org.springframework.context.weaving;

import org.springframework.beans.factory.Aware;
import org.springframework.instrument.classloading.LoadTimeWeaver;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/weaving/LoadTimeWeaverAware.class */
public interface LoadTimeWeaverAware extends Aware {
    void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver);
}
