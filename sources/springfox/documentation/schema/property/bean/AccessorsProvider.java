package springfox.documentation.schema.property.bean;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedMethod;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/bean/AccessorsProvider.class */
public class AccessorsProvider {
    private TypeResolver typeResolver;

    @Autowired
    public AccessorsProvider(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    private Predicate<ResolvedMethod> onlyGettersAndSetters() {
        return new Predicate<ResolvedMethod>() { // from class: springfox.documentation.schema.property.bean.AccessorsProvider.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.base.Predicate
            public boolean apply(ResolvedMethod input) {
                return Accessors.maybeAGetter((Method) input.getRawMember()) || Accessors.maybeASetter((Method) input.getRawMember());
            }
        };
    }

    public ImmutableList<ResolvedMethod> in(ResolvedType resolvedType) throws NegativeArraySizeException {
        MemberResolver resolver = new MemberResolver(this.typeResolver);
        resolver.setIncludeLangObject(false);
        if (resolvedType.getErasedType() == Object.class) {
            return ImmutableList.of();
        }
        ResolvedTypeWithMembers typeWithMembers = resolver.resolve(resolvedType, null, null);
        return FluentIterable.from(Lists.newArrayList(typeWithMembers.getMemberMethods())).filter(onlyGettersAndSetters()).toList();
    }
}
