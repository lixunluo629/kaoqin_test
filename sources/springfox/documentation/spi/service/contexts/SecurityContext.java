package springfox.documentation.spi.service.contexts;

import com.google.common.base.Predicate;
import java.util.List;
import springfox.documentation.service.SecurityReference;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/SecurityContext.class */
public class SecurityContext {
    private final List<SecurityReference> securityReferences;
    private final Predicate<String> selector;

    public SecurityContext(List<SecurityReference> securityReferences, Predicate<String> selector) {
        this.securityReferences = securityReferences;
        this.selector = selector;
    }

    public List<SecurityReference> securityForPath(String path) {
        if (this.selector.apply(path)) {
            return this.securityReferences;
        }
        return null;
    }

    public List<SecurityReference> getSecurityReferences() {
        return this.securityReferences;
    }

    public static SecurityContextBuilder builder() {
        return new SecurityContextBuilder();
    }
}
