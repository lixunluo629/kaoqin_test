package org.springframework.expression.spel.ast;

import org.springframework.asm.MethodVisitor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/IntLiteral.class */
public class IntLiteral extends Literal {
    private final TypedValue value;

    public IntLiteral(String payload, int pos, int value) {
        super(payload, pos);
        this.value = new TypedValue(Integer.valueOf(value));
        this.exitTypeDescriptor = "I";
    }

    @Override // org.springframework.expression.spel.ast.Literal
    public TypedValue getLiteralValue() {
        return this.value;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public boolean isCompilable() {
        return true;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        int intValue = ((Integer) this.value.getValue()).intValue();
        if (intValue == -1) {
            mv.visitInsn(2);
        } else if (intValue >= 0 && intValue < 6) {
            mv.visitInsn(3 + intValue);
        } else {
            mv.visitLdcInsn(Integer.valueOf(intValue));
        }
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
