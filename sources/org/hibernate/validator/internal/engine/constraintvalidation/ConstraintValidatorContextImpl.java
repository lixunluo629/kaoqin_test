package org.hibernate.validator.internal.engine.constraintvalidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ElementKind;
import javax.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.spi.time.TimeProvider;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl.class */
public class ConstraintValidatorContextImpl implements HibernateConstraintValidatorContext {
    private static final Log log = LoggerFactory.make();
    private final List<String> methodParameterNames;
    private final TimeProvider timeProvider;
    private final PathImpl basePath;
    private final ConstraintDescriptor<?> constraintDescriptor;
    private boolean defaultDisabled;
    private Object dynamicPayload;
    private final Map<String, Object> expressionVariables = CollectionHelper.newHashMap();
    private final List<ConstraintViolationCreationContext> constraintViolationCreationContexts = CollectionHelper.newArrayList(3);

    public ConstraintValidatorContextImpl(List<String> methodParameterNames, TimeProvider timeProvider, PathImpl propertyPath, ConstraintDescriptor<?> constraintDescriptor) {
        this.methodParameterNames = methodParameterNames;
        this.timeProvider = timeProvider;
        this.basePath = propertyPath;
        this.constraintDescriptor = constraintDescriptor;
    }

    @Override // javax.validation.ConstraintValidatorContext
    public final void disableDefaultConstraintViolation() {
        this.defaultDisabled = true;
    }

    @Override // javax.validation.ConstraintValidatorContext
    public final String getDefaultConstraintMessageTemplate() {
        return (String) this.constraintDescriptor.getAttributes().get(ConstraintHelper.MESSAGE);
    }

    @Override // javax.validation.ConstraintValidatorContext
    public final ConstraintValidatorContext.ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate) {
        return new ConstraintViolationBuilderImpl(this.methodParameterNames, messageTemplate, PathImpl.createCopy(this.basePath));
    }

    @Override // javax.validation.ConstraintValidatorContext
    public <T> T unwrap(Class<T> type) {
        if (type.isAssignableFrom(HibernateConstraintValidatorContext.class)) {
            return type.cast(this);
        }
        throw log.getTypeNotSupportedForUnwrappingException(type);
    }

    @Override // org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
    public HibernateConstraintValidatorContext addExpressionVariable(String name, Object value) {
        Contracts.assertNotNull(name, "null is not a valid value");
        this.expressionVariables.put(name, value);
        return this;
    }

    @Override // org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
    public TimeProvider getTimeProvider() {
        return this.timeProvider;
    }

    @Override // org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
    public HibernateConstraintValidatorContext withDynamicPayload(Object violationContext) {
        this.dynamicPayload = violationContext;
        return this;
    }

    public final ConstraintDescriptor<?> getConstraintDescriptor() {
        return this.constraintDescriptor;
    }

    public final List<ConstraintViolationCreationContext> getConstraintViolationCreationContexts() {
        if (this.defaultDisabled && this.constraintViolationCreationContexts.size() == 0) {
            throw log.getAtLeastOneCustomMessageMustBeCreatedException();
        }
        List<ConstraintViolationCreationContext> returnedConstraintViolationCreationContexts = new ArrayList<>(this.constraintViolationCreationContexts);
        if (!this.defaultDisabled) {
            Map<String, Object> parameterMapCopy = CollectionHelper.newHashMap();
            parameterMapCopy.putAll(this.expressionVariables);
            returnedConstraintViolationCreationContexts.add(new ConstraintViolationCreationContext(getDefaultConstraintMessageTemplate(), this.basePath, parameterMapCopy, this.dynamicPayload));
        }
        return returnedConstraintViolationCreationContexts;
    }

    public List<String> getMethodParameterNames() {
        return this.methodParameterNames;
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl$NodeBuilderBase.class */
    private abstract class NodeBuilderBase {
        protected final String messageTemplate;
        protected PathImpl propertyPath;

        protected NodeBuilderBase(String template, PathImpl path) {
            this.messageTemplate = template;
            this.propertyPath = path;
        }

        public ConstraintValidatorContext addConstraintViolation() {
            Map<String, Object> parameterMapCopy = CollectionHelper.newHashMap();
            parameterMapCopy.putAll(ConstraintValidatorContextImpl.this.expressionVariables);
            ConstraintValidatorContextImpl.this.constraintViolationCreationContexts.add(new ConstraintViolationCreationContext(this.messageTemplate, this.propertyPath, parameterMapCopy, ConstraintValidatorContextImpl.this.dynamicPayload));
            return ConstraintValidatorContextImpl.this;
        }
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl$ConstraintViolationBuilderImpl.class */
    private class ConstraintViolationBuilderImpl extends NodeBuilderBase implements ConstraintValidatorContext.ConstraintViolationBuilder {
        private final List<String> methodParameterNames;

        private ConstraintViolationBuilderImpl(List<String> methodParameterNames, String template, PathImpl path) {
            super(template, path);
            this.methodParameterNames = methodParameterNames;
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder
        public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext addNode(String name) {
            dropLeafNodeIfRequired();
            this.propertyPath.addPropertyNode(name);
            return new NodeBuilder(this.messageTemplate, this.propertyPath);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder
        public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addPropertyNode(String name) {
            dropLeafNodeIfRequired();
            return new DeferredNodeBuilder(this.messageTemplate, this.propertyPath, name);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder
        public ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode() {
            return new DeferredNodeBuilder(this.messageTemplate, this.propertyPath, null);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder
        public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext addParameterNode(int index) {
            if (this.propertyPath.getLeafNode().getKind() != ElementKind.CROSS_PARAMETER) {
                throw ConstraintValidatorContextImpl.log.getParameterNodeAddedForNonCrossParameterConstraintException(this.propertyPath);
            }
            dropLeafNodeIfRequired();
            this.propertyPath.addParameterNode(this.methodParameterNames.get(index), index);
            return new NodeBuilder(this.messageTemplate, this.propertyPath);
        }

        private void dropLeafNodeIfRequired() {
            if (this.propertyPath.getLeafNode().getKind() == ElementKind.BEAN || this.propertyPath.getLeafNode().getKind() == ElementKind.CROSS_PARAMETER) {
                this.propertyPath = this.propertyPath.getPathWithoutLeafNode();
            }
        }
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl$NodeBuilder.class */
    private class NodeBuilder extends NodeBuilderBase implements ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext, ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderDefinedContext {
        private NodeBuilder(String template, PathImpl path) {
            super(template, path);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext
        @Deprecated
        public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addNode(String name) {
            return addPropertyNode(name);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext
        public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addPropertyNode(String name) {
            return new DeferredNodeBuilder(this.messageTemplate, this.propertyPath, name);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext
        public ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode() {
            return new DeferredNodeBuilder(this.messageTemplate, this.propertyPath, null);
        }
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/constraintvalidation/ConstraintValidatorContextImpl$DeferredNodeBuilder.class */
    private class DeferredNodeBuilder extends NodeBuilderBase implements ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext, ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext, ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder, ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeContextBuilder {
        private final String leafNodeName;

        private DeferredNodeBuilder(String template, PathImpl path, String nodeName) {
            super(template, path);
            this.leafNodeName = nodeName;
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext
        public DeferredNodeBuilder inIterable() {
            this.propertyPath.makeLeafNodeIterable();
            return this;
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeContextBuilder
        public NodeBuilder atKey(Object key) {
            this.propertyPath.setLeafNodeMapKey(key);
            addLeafNode();
            return new NodeBuilder(this.messageTemplate, this.propertyPath);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeContextBuilder
        public NodeBuilder atIndex(Integer index) {
            this.propertyPath.setLeafNodeIndex(index);
            addLeafNode();
            return new NodeBuilder(this.messageTemplate, this.propertyPath);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext, javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder
        @Deprecated
        public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addNode(String name) {
            return addPropertyNode(name);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext, javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder
        public ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext addPropertyNode(String name) {
            addLeafNode();
            return ConstraintValidatorContextImpl.this.new DeferredNodeBuilder(this.messageTemplate, this.propertyPath, name);
        }

        @Override // javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext, javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder
        public ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext addBeanNode() {
            addLeafNode();
            return ConstraintValidatorContextImpl.this.new DeferredNodeBuilder(this.messageTemplate, this.propertyPath, null);
        }

        @Override // org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl.NodeBuilderBase, javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext, javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeBuilderCustomizableContext, javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeContextBuilder, javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.LeafNodeContextBuilder
        public ConstraintValidatorContext addConstraintViolation() {
            addLeafNode();
            return super.addConstraintViolation();
        }

        private void addLeafNode() {
            if (this.leafNodeName == null) {
                this.propertyPath.addBeanNode();
            } else {
                this.propertyPath.addPropertyNode(this.leafNodeName);
            }
        }
    }
}
