package javax.validation;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorContext.class */
public interface ConstraintValidatorContext {

    /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorContext$ConstraintViolationBuilder.class */
    public interface ConstraintViolationBuilder {

        /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderCustomizableContext.class */
        public interface LeafNodeBuilderCustomizableContext {
            LeafNodeContextBuilder inIterable();

            ConstraintValidatorContext addConstraintViolation();
        }

        /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeBuilderDefinedContext.class */
        public interface LeafNodeBuilderDefinedContext {
            ConstraintValidatorContext addConstraintViolation();
        }

        /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorContext$ConstraintViolationBuilder$LeafNodeContextBuilder.class */
        public interface LeafNodeContextBuilder {
            LeafNodeBuilderDefinedContext atKey(Object obj);

            LeafNodeBuilderDefinedContext atIndex(Integer num);

            ConstraintValidatorContext addConstraintViolation();
        }

        /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderCustomizableContext.class */
        public interface NodeBuilderCustomizableContext {
            NodeContextBuilder inIterable();

            NodeBuilderCustomizableContext addNode(String str);

            NodeBuilderCustomizableContext addPropertyNode(String str);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ConstraintValidatorContext addConstraintViolation();
        }

        /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorContext$ConstraintViolationBuilder$NodeBuilderDefinedContext.class */
        public interface NodeBuilderDefinedContext {
            NodeBuilderCustomizableContext addNode(String str);

            NodeBuilderCustomizableContext addPropertyNode(String str);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ConstraintValidatorContext addConstraintViolation();
        }

        /* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintValidatorContext$ConstraintViolationBuilder$NodeContextBuilder.class */
        public interface NodeContextBuilder {
            NodeBuilderDefinedContext atKey(Object obj);

            NodeBuilderDefinedContext atIndex(Integer num);

            NodeBuilderCustomizableContext addNode(String str);

            NodeBuilderCustomizableContext addPropertyNode(String str);

            LeafNodeBuilderCustomizableContext addBeanNode();

            ConstraintValidatorContext addConstraintViolation();
        }

        NodeBuilderDefinedContext addNode(String str);

        NodeBuilderCustomizableContext addPropertyNode(String str);

        LeafNodeBuilderCustomizableContext addBeanNode();

        NodeBuilderDefinedContext addParameterNode(int i);

        ConstraintValidatorContext addConstraintViolation();
    }

    void disableDefaultConstraintViolation();

    String getDefaultConstraintMessageTemplate();

    ConstraintViolationBuilder buildConstraintViolationWithTemplate(String str);

    <T> T unwrap(Class<T> cls);
}
