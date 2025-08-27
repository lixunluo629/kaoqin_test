package org.springframework.boot.autoconfigure;

import java.util.EventListener;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/AutoConfigurationImportListener.class */
public interface AutoConfigurationImportListener extends EventListener {
    void onAutoConfigurationImportEvent(AutoConfigurationImportEvent autoConfigurationImportEvent);
}
