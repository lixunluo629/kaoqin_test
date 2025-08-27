package org.springframework.expression.spel.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.asm.Label;
import org.springframework.asm.MethodVisitor;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.CompilablePropertyAccessor;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/PropertyOrFieldReference.class */
public class PropertyOrFieldReference extends SpelNodeImpl {
    private final boolean nullSafe;
    private String originalPrimitiveExitTypeDescriptor;
    private final String name;
    private volatile PropertyAccessor cachedReadAccessor;
    private volatile PropertyAccessor cachedWriteAccessor;

    public PropertyOrFieldReference(boolean nullSafe, String propertyOrFieldName, int pos) {
        super(pos, new SpelNodeImpl[0]);
        this.originalPrimitiveExitTypeDescriptor = null;
        this.nullSafe = nullSafe;
        this.name = propertyOrFieldName;
    }

    public boolean isNullSafe() {
        return this.nullSafe;
    }

    public String getName() {
        return this.name;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public ValueRef getValueRef(ExpressionState state) throws EvaluationException {
        return new AccessorLValue(this, state.getActiveContextObject(), state.getEvaluationContext(), state.getConfiguration().isAutoGrowNullReferences());
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public TypedValue getValueInternal(ExpressionState state) throws IllegalAccessException, InstantiationException, EvaluationException {
        TypedValue tv = getValueInternal(state.getActiveContextObject(), state.getEvaluationContext(), state.getConfiguration().isAutoGrowNullReferences());
        PropertyAccessor accessorToUse = this.cachedReadAccessor;
        if (accessorToUse instanceof CompilablePropertyAccessor) {
            CompilablePropertyAccessor accessor = (CompilablePropertyAccessor) accessorToUse;
            setExitTypeDescriptor(CodeFlow.toDescriptor(accessor.getPropertyType()));
        }
        return tv;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TypedValue getValueInternal(TypedValue contextObject, EvaluationContext evalContext, boolean isAutoGrowNullReferences) throws IllegalAccessException, InstantiationException, EvaluationException {
        TypedValue result = readProperty(contextObject, evalContext, this.name);
        if (result.getValue() == null && isAutoGrowNullReferences && nextChildIs(Indexer.class, PropertyOrFieldReference.class)) {
            TypeDescriptor resultDescriptor = result.getTypeDescriptor();
            if (List.class == resultDescriptor.getType()) {
                try {
                    if (isWritableProperty(this.name, contextObject, evalContext)) {
                        List<?> newList = (List) ArrayList.class.newInstance();
                        writeProperty(contextObject, evalContext, this.name, newList);
                        result = readProperty(contextObject, evalContext, this.name);
                    }
                } catch (IllegalAccessException ex) {
                    throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING, new Object[0]);
                } catch (InstantiationException ex2) {
                    throw new SpelEvaluationException(getStartPosition(), ex2, SpelMessage.UNABLE_TO_CREATE_LIST_FOR_INDEXING, new Object[0]);
                }
            } else if (Map.class == resultDescriptor.getType()) {
                try {
                    if (isWritableProperty(this.name, contextObject, evalContext)) {
                        Map<?, ?> newMap = (Map) HashMap.class.newInstance();
                        writeProperty(contextObject, evalContext, this.name, newMap);
                        result = readProperty(contextObject, evalContext, this.name);
                    }
                } catch (IllegalAccessException ex3) {
                    throw new SpelEvaluationException(getStartPosition(), ex3, SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING, new Object[0]);
                } catch (InstantiationException ex4) {
                    throw new SpelEvaluationException(getStartPosition(), ex4, SpelMessage.UNABLE_TO_CREATE_MAP_FOR_INDEXING, new Object[0]);
                }
            } else {
                try {
                    if (isWritableProperty(this.name, contextObject, evalContext)) {
                        Object newObject = result.getTypeDescriptor().getType().newInstance();
                        writeProperty(contextObject, evalContext, this.name, newObject);
                        result = readProperty(contextObject, evalContext, this.name);
                    }
                } catch (IllegalAccessException ex5) {
                    throw new SpelEvaluationException(getStartPosition(), ex5, SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, result.getTypeDescriptor().getType());
                } catch (InstantiationException ex6) {
                    throw new SpelEvaluationException(getStartPosition(), ex6, SpelMessage.UNABLE_TO_DYNAMICALLY_CREATE_OBJECT, result.getTypeDescriptor().getType());
                }
            }
        }
        return result;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl, org.springframework.expression.spel.SpelNode
    public void setValue(ExpressionState state, Object newValue) throws EvaluationException {
        writeProperty(state.getActiveContextObject(), state.getEvaluationContext(), this.name, newValue);
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl, org.springframework.expression.spel.SpelNode
    public boolean isWritable(ExpressionState state) throws EvaluationException {
        return isWritableProperty(this.name, state.getActiveContextObject(), state.getEvaluationContext());
    }

    @Override // org.springframework.expression.spel.SpelNode
    public String toStringAST() {
        return this.name;
    }

    private TypedValue readProperty(TypedValue contextObject, EvaluationContext evalContext, String name) throws EvaluationException {
        Object targetObject = contextObject.getValue();
        if (targetObject == null && this.nullSafe) {
            return TypedValue.NULL;
        }
        PropertyAccessor accessorToUse = this.cachedReadAccessor;
        if (accessorToUse != null) {
            if (evalContext.getPropertyAccessors().contains(accessorToUse)) {
                try {
                    return accessorToUse.read(evalContext, contextObject.getValue(), name);
                } catch (Exception e) {
                }
            }
            this.cachedReadAccessor = null;
        }
        List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
        if (accessorsToTry != null) {
            try {
                for (PropertyAccessor accessor : accessorsToTry) {
                    if (accessor.canRead(evalContext, contextObject.getValue(), name)) {
                        if (accessor instanceof ReflectivePropertyAccessor) {
                            accessor = ((ReflectivePropertyAccessor) accessor).createOptimalAccessor(evalContext, contextObject.getValue(), name);
                        }
                        this.cachedReadAccessor = accessor;
                        return accessor.read(evalContext, contextObject.getValue(), name);
                    }
                }
            } catch (Exception ex) {
                throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_DURING_PROPERTY_READ, name, ex.getMessage());
            }
        }
        if (contextObject.getValue() == null) {
            throw new SpelEvaluationException(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL, name);
        }
        throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE, name, FormatHelper.formatClassNameForMessage(getObjectClass(contextObject.getValue())));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeProperty(TypedValue contextObject, EvaluationContext evalContext, String name, Object newValue) throws EvaluationException {
        if (contextObject.getValue() == null && this.nullSafe) {
            return;
        }
        PropertyAccessor accessorToUse = this.cachedWriteAccessor;
        if (accessorToUse != null) {
            if (evalContext.getPropertyAccessors().contains(accessorToUse)) {
                try {
                    accessorToUse.write(evalContext, contextObject.getValue(), name, newValue);
                    return;
                } catch (Exception e) {
                }
            }
            this.cachedWriteAccessor = null;
        }
        List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
        if (accessorsToTry != null) {
            try {
                for (PropertyAccessor accessor : accessorsToTry) {
                    if (accessor.canWrite(evalContext, contextObject.getValue(), name)) {
                        this.cachedWriteAccessor = accessor;
                        accessor.write(evalContext, contextObject.getValue(), name, newValue);
                        return;
                    }
                }
            } catch (AccessException ex) {
                throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_PROPERTY_WRITE, name, ex.getMessage());
            }
        }
        if (contextObject.getValue() == null) {
            throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE_ON_NULL, name);
        }
        throw new SpelEvaluationException(getStartPosition(), SpelMessage.PROPERTY_OR_FIELD_NOT_WRITABLE, name, FormatHelper.formatClassNameForMessage(getObjectClass(contextObject.getValue())));
    }

    public boolean isWritableProperty(String name, TypedValue contextObject, EvaluationContext evalContext) throws EvaluationException {
        List<PropertyAccessor> accessorsToTry = getPropertyAccessorsToTry(contextObject.getValue(), evalContext.getPropertyAccessors());
        if (accessorsToTry != null) {
            for (PropertyAccessor accessor : accessorsToTry) {
                if (accessor.canWrite(evalContext, contextObject.getValue(), name)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private List<PropertyAccessor> getPropertyAccessorsToTry(Object contextObject, List<PropertyAccessor> propertyAccessors) {
        Class<?> targetType = contextObject != null ? contextObject.getClass() : null;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (PropertyAccessor resolver : propertyAccessors) {
            Class<?>[] targets = resolver.getSpecificTargetClasses();
            if (targets == null) {
                arrayList2.add(resolver);
            } else if (targetType != null) {
                int length = targets.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Class<?> clazz = targets[i];
                    if (clazz == targetType) {
                        arrayList.add(resolver);
                        break;
                    }
                    if (clazz.isAssignableFrom(targetType)) {
                        arrayList2.add(resolver);
                    }
                    i++;
                }
            }
        }
        List<PropertyAccessor> resolvers = new ArrayList<>();
        resolvers.addAll(arrayList);
        arrayList2.removeAll(arrayList);
        resolvers.addAll(arrayList2);
        return resolvers;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public boolean isCompilable() {
        PropertyAccessor accessorToUse = this.cachedReadAccessor;
        return (accessorToUse instanceof CompilablePropertyAccessor) && ((CompilablePropertyAccessor) accessorToUse).isCompilable();
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        PropertyAccessor accessorToUse = this.cachedReadAccessor;
        if (!(accessorToUse instanceof CompilablePropertyAccessor)) {
            throw new IllegalStateException("Property accessor is not compilable: " + accessorToUse);
        }
        Label skipIfNull = null;
        if (this.nullSafe) {
            mv.visitInsn(89);
            skipIfNull = new Label();
            Label continueLabel = new Label();
            mv.visitJumpInsn(199, continueLabel);
            CodeFlow.insertCheckCast(mv, this.exitTypeDescriptor);
            mv.visitJumpInsn(167, skipIfNull);
            mv.visitLabel(continueLabel);
        }
        ((CompilablePropertyAccessor) accessorToUse).generateCode(this.name, mv, cf);
        cf.pushDescriptor(this.exitTypeDescriptor);
        if (this.originalPrimitiveExitTypeDescriptor != null) {
            CodeFlow.insertBoxIfNecessary(mv, this.originalPrimitiveExitTypeDescriptor);
        }
        if (skipIfNull != null) {
            mv.visitLabel(skipIfNull);
        }
    }

    void setExitTypeDescriptor(String descriptor) {
        if (this.nullSafe && CodeFlow.isPrimitive(descriptor)) {
            this.originalPrimitiveExitTypeDescriptor = descriptor;
            this.exitTypeDescriptor = CodeFlow.toBoxedDescriptor(descriptor);
        } else {
            this.exitTypeDescriptor = descriptor;
        }
    }

    /* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/PropertyOrFieldReference$AccessorLValue.class */
    private static class AccessorLValue implements ValueRef {
        private final PropertyOrFieldReference ref;
        private final TypedValue contextObject;
        private final EvaluationContext evalContext;
        private final boolean autoGrowNullReferences;

        public AccessorLValue(PropertyOrFieldReference propertyOrFieldReference, TypedValue activeContextObject, EvaluationContext evalContext, boolean autoGrowNullReferences) {
            this.ref = propertyOrFieldReference;
            this.contextObject = activeContextObject;
            this.evalContext = evalContext;
            this.autoGrowNullReferences = autoGrowNullReferences;
        }

        @Override // org.springframework.expression.spel.ast.ValueRef
        public TypedValue getValue() throws IllegalAccessException, InstantiationException, EvaluationException {
            TypedValue value = this.ref.getValueInternal(this.contextObject, this.evalContext, this.autoGrowNullReferences);
            PropertyAccessor accessorToUse = this.ref.cachedReadAccessor;
            if (accessorToUse instanceof CompilablePropertyAccessor) {
                this.ref.setExitTypeDescriptor(CodeFlow.toDescriptor(((CompilablePropertyAccessor) accessorToUse).getPropertyType()));
            }
            return value;
        }

        @Override // org.springframework.expression.spel.ast.ValueRef
        public void setValue(Object newValue) throws EvaluationException {
            this.ref.writeProperty(this.contextObject, this.evalContext, this.ref.name, newValue);
        }

        @Override // org.springframework.expression.spel.ast.ValueRef
        public boolean isWritable() {
            return this.ref.isWritableProperty(this.ref.name, this.contextObject, this.evalContext);
        }
    }
}
