package org.springframework.boot.autoconfigure;

import org.springframework.core.type.AnnotationMetadata;

@Deprecated
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/EnableAutoConfigurationImportSelector.class */
public class EnableAutoConfigurationImportSelector extends AutoConfigurationImportSelector {
    @Override // org.springframework.boot.autoconfigure.AutoConfigurationImportSelector
    protected boolean isEnabled(AnnotationMetadata metadata) {
        if (getClass().equals(EnableAutoConfigurationImportSelector.class)) {
            return ((Boolean) getEnvironment().getProperty(EnableAutoConfiguration.ENABLED_OVERRIDE_PROPERTY, Boolean.class, true)).booleanValue();
        }
        return true;
    }
}
