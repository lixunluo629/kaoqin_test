package springfox.documentation.schema.property;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedConstructor;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.fasterxml.classmate.members.ResolvedParameterizedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/FactoryMethodProvider.class */
public class FactoryMethodProvider {
    private MemberResolver memberResolver;

    @Autowired
    public FactoryMethodProvider(TypeResolver resolver) {
        this.memberResolver = new MemberResolver(resolver);
    }

    public Optional<? extends ResolvedParameterizedMember> in(ResolvedType resolvedType, Predicate<ResolvedParameterizedMember> predicate) {
        return FluentIterable.from(Iterables.concat(constructors(resolvedType), delegatedFactoryMethods(resolvedType))).firstMatch(predicate);
    }

    static Predicate<ResolvedParameterizedMember> factoryMethodOf(final AnnotatedParameter parameter) {
        return new Predicate<ResolvedParameterizedMember>() { // from class: springfox.documentation.schema.property.FactoryMethodProvider.1
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedParameterizedMember input) {
                return input.getRawMember().equals(parameter.getOwner().getMember());
            }
        };
    }

    public Collection<ResolvedConstructor> constructors(ResolvedType resolvedType) throws NegativeArraySizeException {
        ResolvedTypeWithMembers typeWithMembers = this.memberResolver.resolve(resolvedType, null, null);
        return Lists.newArrayList(typeWithMembers.getConstructors());
    }

    public Collection<ResolvedMethod> delegatedFactoryMethods(ResolvedType resolvedType) throws NegativeArraySizeException {
        ResolvedTypeWithMembers typeWithMembers = this.memberResolver.resolve(resolvedType, null, null);
        return Lists.newArrayList(typeWithMembers.getStaticMethods());
    }
}
