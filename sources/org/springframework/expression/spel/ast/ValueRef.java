package org.springframework.expression.spel.ast;

import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/ValueRef.class */
public interface ValueRef {
    TypedValue getValue();

    void setValue(Object obj);

    boolean isWritable();

    /* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/ValueRef$NullValueRef.class */
    public static class NullValueRef implements ValueRef {
        static final NullValueRef INSTANCE = new NullValueRef();

        @Override // org.springframework.expression.spel.ast.ValueRef
        public TypedValue getValue() {
            return TypedValue.NULL;
        }

        @Override // org.springframework.expression.spel.ast.ValueRef
        public void setValue(Object newValue) {
            throw new SpelEvaluationException(0, SpelMessage.NOT_ASSIGNABLE, "null");
        }

        @Override // org.springframework.expression.spel.ast.ValueRef
        public boolean isWritable() {
            return false;
        }
    }

    /* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/ValueRef$TypedValueHolderValueRef.class */
    public static class TypedValueHolderValueRef implements ValueRef {
        private final TypedValue typedValue;
        private final SpelNodeImpl node;

        public TypedValueHolderValueRef(TypedValue typedValue, SpelNodeImpl node) {
            this.typedValue = typedValue;
            this.node = node;
        }

        @Override // org.springframework.expression.spel.ast.ValueRef
        public TypedValue getValue() {
            return this.typedValue;
        }

        @Override // org.springframework.expression.spel.ast.ValueRef
        public void setValue(Object newValue) {
            throw new SpelEvaluationException(this.node.getStartPosition(), SpelMessage.NOT_ASSIGNABLE, this.node.toStringAST());
        }

        @Override // org.springframework.expression.spel.ast.ValueRef
        public boolean isWritable() {
            return false;
        }
    }
}
