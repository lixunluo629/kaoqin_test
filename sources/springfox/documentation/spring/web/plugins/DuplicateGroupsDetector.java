package springfox.documentation.spring.web.plugins;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import springfox.documentation.spi.service.DocumentationPlugin;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/plugins/DuplicateGroupsDetector.class */
class DuplicateGroupsDetector {
    private DuplicateGroupsDetector() {
        throw new UnsupportedOperationException();
    }

    public static void ensureNoDuplicateGroups(List<DocumentationPlugin> allPlugins) throws IllegalStateException {
        Multimap<String, DocumentationPlugin> plugins = Multimaps.index(allPlugins, byGroupName());
        Iterable<String> duplicateGroups = FluentIterable.from(plugins.asMap().entrySet()).filter(duplicates()).transform(toGroupNames());
        if (Iterables.size(duplicateGroups) > 0) {
            throw new IllegalStateException(String.format("Multiple Dockets with the same group name are not supported. The following duplicate groups were discovered. %s", Joiner.on(',').join(duplicateGroups)));
        }
    }

    private static Function<? super Map.Entry<String, Collection<DocumentationPlugin>>, String> toGroupNames() {
        return new Function<Map.Entry<String, Collection<DocumentationPlugin>>, String>() { // from class: springfox.documentation.spring.web.plugins.DuplicateGroupsDetector.1
            @Override // com.google.common.base.Function
            public String apply(Map.Entry<String, Collection<DocumentationPlugin>> input) {
                return input.getKey();
            }
        };
    }

    private static Predicate<? super Map.Entry<String, Collection<DocumentationPlugin>>> duplicates() {
        return new Predicate<Map.Entry<String, Collection<DocumentationPlugin>>>() { // from class: springfox.documentation.spring.web.plugins.DuplicateGroupsDetector.2
            @Override // com.google.common.base.Predicate
            public boolean apply(Map.Entry<String, Collection<DocumentationPlugin>> input) {
                return input.getValue().size() > 1;
            }
        };
    }

    private static Function<? super DocumentationPlugin, String> byGroupName() {
        return new Function<DocumentationPlugin, String>() { // from class: springfox.documentation.spring.web.plugins.DuplicateGroupsDetector.3
            @Override // com.google.common.base.Function
            public String apply(DocumentationPlugin input) {
                return (String) Optional.fromNullable(input.getGroupName()).or((Optional) "default");
            }
        };
    }
}
