package springfox.documentation.schema.property.field;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/field/FieldProvider.class */
public class FieldProvider {
    private final TypeResolver typeResolver;

    @Autowired
    public FieldProvider(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    public Iterable<ResolvedField> in(ResolvedType resolvedType) throws NegativeArraySizeException {
        MemberResolver memberResolver = new MemberResolver(this.typeResolver);
        if (resolvedType.getErasedType() == Object.class) {
            return Lists.newArrayList();
        }
        ResolvedTypeWithMembers resolvedMemberWithMembers = memberResolver.resolve(resolvedType, null, null);
        return Lists.newArrayList(resolvedMemberWithMembers.getMemberFields());
    }
}
