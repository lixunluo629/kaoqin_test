package org.springframework.expression.spel.ast;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.springframework.asm.MethodVisitor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/StringLiteral.class */
public class StringLiteral extends Literal {
    private final TypedValue value;

    public StringLiteral(String payload, int pos, String value) {
        super(payload, pos);
        this.value = new TypedValue(value.substring(1, value.length() - 1).replaceAll("''", "'").replaceAll("\"\"", SymbolConstants.QUOTES_SYMBOL));
        this.exitTypeDescriptor = "Ljava/lang/String";
    }

    @Override // org.springframework.expression.spel.ast.Literal
    public TypedValue getLiteralValue() {
        return this.value;
    }

    @Override // org.springframework.expression.spel.ast.Literal
    public String toString() {
        return "'" + getLiteralValue().getValue() + "'";
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public boolean isCompilable() {
        return true;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        mv.visitLdcInsn(this.value.getValue());
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
