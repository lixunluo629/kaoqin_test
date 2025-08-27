package springfox.documentation.spi.schema;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import java.util.List;
import springfox.documentation.schema.AlternateTypeRule;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/schema/AlternateTypeProvider.class */
public class AlternateTypeProvider {
    private List<AlternateTypeRule> rules = Lists.newArrayList();

    public AlternateTypeProvider(List<AlternateTypeRule> alternateTypeRules) {
        this.rules.addAll(alternateTypeRules);
    }

    public ResolvedType alternateFor(ResolvedType type) {
        Optional<AlternateTypeRule> matchingRule = FluentIterable.from(this.rules).firstMatch(thatAppliesTo(type));
        if (matchingRule.isPresent()) {
            return matchingRule.get().alternateFor(type);
        }
        return type;
    }

    public void addRule(AlternateTypeRule rule) {
        this.rules.add(rule);
    }

    private Predicate<AlternateTypeRule> thatAppliesTo(final ResolvedType type) {
        return new Predicate<AlternateTypeRule>() { // from class: springfox.documentation.spi.schema.AlternateTypeProvider.1
            @Override // com.google.common.base.Predicate
            public boolean apply(AlternateTypeRule input) {
                return input.appliesTo(type);
            }
        };
    }
}
