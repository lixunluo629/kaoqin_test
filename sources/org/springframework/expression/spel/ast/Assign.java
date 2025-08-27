package org.springframework.expression.spel.ast;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.ExpressionState;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/Assign.class */
public class Assign extends SpelNodeImpl {
    public Assign(int pos, SpelNodeImpl... operands) {
        super(pos, operands);
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        TypedValue newValue = this.children[1].getValueInternal(state);
        getChild(0).setValue(state, newValue.getValue());
        return newValue;
    }

    @Override // org.springframework.expression.spel.SpelNode
    public String toStringAST() {
        return getChild(0).toStringAST() + SymbolConstants.EQUAL_SYMBOL + getChild(1).toStringAST();
    }
}
