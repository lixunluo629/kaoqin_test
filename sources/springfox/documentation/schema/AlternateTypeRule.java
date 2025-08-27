package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/schema/AlternateTypeRule.class */
public class AlternateTypeRule {
    private final ResolvedType original;
    private final ResolvedType alternate;

    public AlternateTypeRule(ResolvedType original, ResolvedType alternate) {
        this.original = original;
        this.alternate = alternate;
    }

    public ResolvedType alternateFor(ResolvedType type) {
        if (appliesTo(type)) {
            if (WildcardType.hasWildcards(this.original)) {
                return WildcardType.replaceWildcardsFrom(WildcardType.collectReplaceables(type, this.original), this.alternate);
            }
            return this.alternate;
        }
        return this.original;
    }

    public boolean appliesTo(ResolvedType type) {
        return (WildcardType.hasWildcards(this.original) && WildcardType.wildcardMatch(type, this.original)) || WildcardType.exactMatch(this.original, type);
    }
}
