package springfox.documentation.builders;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.util.AntPathMatcher;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/PathSelectors.class */
public class PathSelectors {
    private PathSelectors() {
        throw new UnsupportedOperationException();
    }

    public static Predicate<String> any() {
        return Predicates.alwaysTrue();
    }

    public static Predicate<String> none() {
        return Predicates.alwaysFalse();
    }

    public static Predicate<String> regex(final String pathRegex) {
        return new Predicate<String>() { // from class: springfox.documentation.builders.PathSelectors.1
            @Override // com.google.common.base.Predicate
            public boolean apply(String input) {
                return input.matches(pathRegex);
            }
        };
    }

    public static Predicate<String> ant(final String antPattern) {
        return new Predicate<String>() { // from class: springfox.documentation.builders.PathSelectors.2
            @Override // com.google.common.base.Predicate
            public boolean apply(String input) {
                AntPathMatcher matcher = new AntPathMatcher();
                return matcher.match(antPattern, input);
            }
        };
    }
}
