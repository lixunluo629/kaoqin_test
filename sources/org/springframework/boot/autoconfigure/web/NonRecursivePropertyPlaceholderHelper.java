package org.springframework.boot.autoconfigure.web;

import java.util.Set;
import org.springframework.util.PropertyPlaceholderHelper;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/NonRecursivePropertyPlaceholderHelper.class */
class NonRecursivePropertyPlaceholderHelper extends PropertyPlaceholderHelper {
    NonRecursivePropertyPlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {
        super(placeholderPrefix, placeholderSuffix);
    }

    @Override // org.springframework.util.PropertyPlaceholderHelper
    protected String parseStringValue(String strVal, PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver, Set<String> visitedPlaceholders) {
        return super.parseStringValue(strVal, new NonRecursivePlaceholderResolver(placeholderResolver), visitedPlaceholders);
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/NonRecursivePropertyPlaceholderHelper$NonRecursivePlaceholderResolver.class */
    private static class NonRecursivePlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
        private final PropertyPlaceholderHelper.PlaceholderResolver resolver;

        NonRecursivePlaceholderResolver(PropertyPlaceholderHelper.PlaceholderResolver resolver) {
            this.resolver = resolver;
        }

        @Override // org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver
        public String resolvePlaceholder(String placeholderName) {
            if (this.resolver instanceof NonRecursivePlaceholderResolver) {
                return null;
            }
            return this.resolver.resolvePlaceholder(placeholderName);
        }
    }
}
