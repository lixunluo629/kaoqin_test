package springfox.documentation.spi.service.contexts;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.List;
import springfox.documentation.service.SecurityReference;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/SecurityContextBuilder.class */
public class SecurityContextBuilder {
    private List<SecurityReference> securityReferences = Lists.newArrayList();
    private Predicate<String> pathSelector = Predicates.alwaysTrue();

    SecurityContextBuilder() {
    }

    public SecurityContextBuilder securityReferences(List<SecurityReference> securityReferences) {
        this.securityReferences = securityReferences;
        return this;
    }

    public SecurityContext build() {
        if (this.securityReferences == null) {
            this.securityReferences = Lists.newArrayList();
        }
        return new SecurityContext(this.securityReferences, this.pathSelector);
    }

    public SecurityContextBuilder forPaths(Predicate<String> selector) {
        this.pathSelector = selector;
        return this;
    }
}
