package org.hibernate.validator.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/SafeHtml.class */
public @interface SafeHtml {

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/SafeHtml$List.class */
    public @interface List {
        SafeHtml[] value();
    }

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/SafeHtml$Tag.class */
    public @interface Tag {
        String name();

        String[] attributes() default {};
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/constraints/SafeHtml$WhiteListType.class */
    public enum WhiteListType {
        NONE,
        SIMPLE_TEXT,
        BASIC,
        BASIC_WITH_IMAGES,
        RELAXED
    }

    String message() default "{org.hibernate.validator.constraints.SafeHtml.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    WhiteListType whitelistType() default WhiteListType.RELAXED;

    String[] additionalTags() default {};

    Tag[] additionalTagsWithAttributes() default {};
}
