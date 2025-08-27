package org.hibernate.validator.internal.metadata.aggregated;

import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.ElementKind;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import org.hibernate.validator.HibernateValidatorPermission;
import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.PropertyDescriptorImpl;
import org.hibernate.validator.internal.metadata.facets.Cascadable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
import org.hibernate.validator.internal.util.privilegedactions.SetAccessibility;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/PropertyMetaData.class */
public class PropertyMetaData extends AbstractConstraintMetaData implements Cascadable {
    private static final Log log = LoggerFactory.make();
    private final Member cascadingMember;
    private final Type cascadableType;
    private final ElementType elementType;
    private final GroupConversionHelper groupConversionHelper;
    private final Set<MetaConstraint<?>> typeArgumentsConstraints;

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public /* bridge */ /* synthetic */ ElementDescriptor asDescriptor(boolean z, List list) {
        return asDescriptor(z, (List<Class<?>>) list);
    }

    private PropertyMetaData(String propertyName, Type type, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> groupConversions, Member cascadingMember, UnwrapMode unwrapMode) {
        super(propertyName, type, constraints, ElementKind.PROPERTY, cascadingMember != null, (cascadingMember == null && constraints.isEmpty() && typeArgumentsConstraints.isEmpty()) ? false : true, unwrapMode);
        if (cascadingMember != null) {
            this.cascadingMember = getAccessible(cascadingMember);
            this.cascadableType = ReflectionHelper.typeOf(cascadingMember);
            this.elementType = cascadingMember instanceof Field ? ElementType.FIELD : ElementType.METHOD;
        } else {
            this.cascadingMember = null;
            this.cascadableType = null;
            this.elementType = ElementType.TYPE;
        }
        this.typeArgumentsConstraints = Collections.unmodifiableSet(typeArgumentsConstraints);
        this.groupConversionHelper = new GroupConversionHelper(groupConversions);
        this.groupConversionHelper.validateGroupConversions(isCascading(), toString());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Member getAccessible(Member member) {
        Member member2;
        if (((AccessibleObject) member).isAccessible()) {
            return member;
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkPermission(HibernateValidatorPermission.ACCESS_PRIVATE_MEMBERS);
        }
        Class<?> clazz = member.getDeclaringClass();
        if (member instanceof Field) {
            member2 = (Member) run(GetDeclaredField.action(clazz, member.getName()));
        } else {
            member2 = (Member) run(GetDeclaredMethod.action(clazz, member.getName(), new Class[0]));
        }
        run(SetAccessibility.action(member2));
        return member2;
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public ElementType getElementType() {
        return this.elementType;
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Class<?> convertGroup(Class<?> from) {
        return this.groupConversionHelper.convertGroup(from);
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Set<GroupConversionDescriptor> getGroupConversionDescriptors() {
        return this.groupConversionHelper.asDescriptors();
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
        return this.typeArgumentsConstraints;
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.ConstraintMetaData
    public PropertyDescriptorImpl asDescriptor(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence) {
        return new PropertyDescriptorImpl(getType(), getName(), asDescriptors(getConstraints()), isCascading(), defaultGroupSequenceRedefined, defaultGroupSequence, getGroupConversionDescriptors());
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Object getValue(Object parent) {
        if (this.elementType == ElementType.METHOD) {
            return ReflectionHelper.getValue((Method) this.cascadingMember, parent);
        }
        return ReflectionHelper.getValue((Field) this.cascadingMember, parent);
    }

    @Override // org.hibernate.validator.internal.metadata.facets.Cascadable
    public Type getCascadableType() {
        return this.cascadableType;
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.AbstractConstraintMetaData
    public String toString() {
        return "PropertyMetaData [type=" + getType() + ", propertyName=" + getName() + ", cascadingMember=[" + this.cascadingMember + "]]";
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.AbstractConstraintMetaData
    public int hashCode() {
        return super.hashCode();
    }

    @Override // org.hibernate.validator.internal.metadata.aggregated.AbstractConstraintMetaData
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/aggregated/PropertyMetaData$Builder.class */
    public static class Builder extends MetaDataBuilder {
        private static final EnumSet<ConstrainedElement.ConstrainedElementKind> SUPPORTED_ELEMENT_KINDS = EnumSet.of(ConstrainedElement.ConstrainedElementKind.TYPE, ConstrainedElement.ConstrainedElementKind.FIELD, ConstrainedElement.ConstrainedElementKind.METHOD);
        private final String propertyName;
        private final Type propertyType;
        private Member cascadingMember;
        private final Set<MetaConstraint<?>> typeArgumentsConstraints;
        private UnwrapMode unwrapMode;
        private boolean unwrapModeExplicitlyConfigured;

        public Builder(Class<?> beanClass, ConstrainedField constrainedField, ConstraintHelper constraintHelper) {
            super(beanClass, constraintHelper);
            this.typeArgumentsConstraints = CollectionHelper.newHashSet();
            this.unwrapMode = UnwrapMode.AUTOMATIC;
            this.unwrapModeExplicitlyConfigured = false;
            this.propertyName = ReflectionHelper.getPropertyName(constrainedField.getLocation().getMember());
            this.propertyType = ReflectionHelper.typeOf(constrainedField.getLocation().getMember());
            add(constrainedField);
        }

        public Builder(Class<?> beanClass, ConstrainedType constrainedType, ConstraintHelper constraintHelper) {
            super(beanClass, constraintHelper);
            this.typeArgumentsConstraints = CollectionHelper.newHashSet();
            this.unwrapMode = UnwrapMode.AUTOMATIC;
            this.unwrapModeExplicitlyConfigured = false;
            this.propertyName = null;
            this.propertyType = null;
            add(constrainedType);
        }

        public Builder(Class<?> beanClass, ConstrainedExecutable constrainedMethod, ConstraintHelper constraintHelper) {
            super(beanClass, constraintHelper);
            this.typeArgumentsConstraints = CollectionHelper.newHashSet();
            this.unwrapMode = UnwrapMode.AUTOMATIC;
            this.unwrapModeExplicitlyConfigured = false;
            this.propertyName = ReflectionHelper.getPropertyName(constrainedMethod.getLocation().getMember());
            this.propertyType = ReflectionHelper.typeOf(constrainedMethod.getLocation().getMember());
            add(constrainedMethod);
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public boolean accepts(ConstrainedElement constrainedElement) {
            if (!SUPPORTED_ELEMENT_KINDS.contains(constrainedElement.getKind())) {
                return false;
            }
            if (constrainedElement.getKind() == ConstrainedElement.ConstrainedElementKind.METHOD && !((ConstrainedExecutable) constrainedElement).isGetterMethod()) {
                return false;
            }
            return equals(ReflectionHelper.getPropertyName(constrainedElement.getLocation().getMember()), this.propertyName);
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public final void add(ConstrainedElement constrainedElement) {
            super.add(constrainedElement);
            UnwrapMode newUnwrapMode = constrainedElement.unwrapMode();
            if (this.unwrapModeExplicitlyConfigured) {
                if (!UnwrapMode.AUTOMATIC.equals(newUnwrapMode) && !newUnwrapMode.equals(this.unwrapMode)) {
                    throw PropertyMetaData.log.getInconsistentValueUnwrappingConfigurationBetweenFieldAndItsGetterException(this.propertyName, getBeanClass().getName());
                }
            } else if (!UnwrapMode.AUTOMATIC.equals(newUnwrapMode)) {
                this.unwrapMode = constrainedElement.unwrapMode();
                this.unwrapModeExplicitlyConfigured = true;
            }
            if (constrainedElement.getKind() == ConstrainedElement.ConstrainedElementKind.FIELD) {
                this.typeArgumentsConstraints.addAll(((ConstrainedField) constrainedElement).getTypeArgumentsConstraints());
            } else if (constrainedElement.getKind() == ConstrainedElement.ConstrainedElementKind.METHOD) {
                this.typeArgumentsConstraints.addAll(((ConstrainedExecutable) constrainedElement).getTypeArgumentsConstraints());
            }
            if (constrainedElement.isCascading() && this.cascadingMember == null) {
                this.cascadingMember = constrainedElement.getLocation().getMember();
            }
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public UnwrapMode unwrapMode() {
            return this.unwrapMode;
        }

        @Override // org.hibernate.validator.internal.metadata.aggregated.MetaDataBuilder
        public PropertyMetaData build() {
            return new PropertyMetaData(this.propertyName, this.propertyType, adaptOriginsAndImplicitGroups(getConstraints()), this.typeArgumentsConstraints, getGroupConversions(), this.cascadingMember, unwrapMode());
        }

        private boolean equals(String s1, String s2) {
            return (s1 != null && s1.equals(s2)) || (s1 == null && s2 == null);
        }
    }
}
