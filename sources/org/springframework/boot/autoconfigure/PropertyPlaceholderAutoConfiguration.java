package org.springframework.boot.autoconfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
@Deprecated
@AutoConfigureOrder(Integer.MIN_VALUE)
@Import({Selector.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/PropertyPlaceholderAutoConfiguration.class */
public class PropertyPlaceholderAutoConfiguration {
    private static final String[] REPLACEMENT = {"org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration"};

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/PropertyPlaceholderAutoConfiguration$Selector.class */
    static class Selector implements ImportSelector {
        Selector() {
        }

        @Override // org.springframework.context.annotation.ImportSelector
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return PropertyPlaceholderAutoConfiguration.REPLACEMENT;
        }
    }
}
