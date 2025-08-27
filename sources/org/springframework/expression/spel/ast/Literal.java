package org.springframework.expression.spel.ast;

import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.expression.spel.InternalParseException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.SpelParseException;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/Literal.class */
public abstract class Literal extends SpelNodeImpl {
    private final String originalValue;

    public abstract TypedValue getLiteralValue();

    public Literal(String originalValue, int pos) {
        super(pos, new SpelNodeImpl[0]);
        this.originalValue = originalValue;
    }

    public final String getOriginalValue() {
        return this.originalValue;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public final TypedValue getValueInternal(ExpressionState state) throws SpelEvaluationException {
        return getLiteralValue();
    }

    public String toString() {
        return getLiteralValue().getValue().toString();
    }

    @Override // org.springframework.expression.spel.SpelNode
    public String toStringAST() {
        return toString();
    }

    public static Literal getIntLiteral(String numberToken, int pos, int radix) throws NumberFormatException {
        try {
            int value = Integer.parseInt(numberToken, radix);
            return new IntLiteral(numberToken, pos, value);
        } catch (NumberFormatException ex) {
            throw new InternalParseException(new SpelParseException(pos >> 16, ex, SpelMessage.NOT_AN_INTEGER, numberToken));
        }
    }

    public static Literal getLongLiteral(String numberToken, int pos, int radix) throws NumberFormatException {
        try {
            long value = Long.parseLong(numberToken, radix);
            return new LongLiteral(numberToken, pos, value);
        } catch (NumberFormatException ex) {
            throw new InternalParseException(new SpelParseException(pos >> 16, ex, SpelMessage.NOT_A_LONG, numberToken));
        }
    }

    public static Literal getRealLiteral(String numberToken, int pos, boolean isFloat) throws NumberFormatException {
        try {
            if (isFloat) {
                float value = Float.parseFloat(numberToken);
                return new FloatLiteral(numberToken, pos, value);
            }
            double value2 = Double.parseDouble(numberToken);
            return new RealLiteral(numberToken, pos, value2);
        } catch (NumberFormatException ex) {
            throw new InternalParseException(new SpelParseException(pos >> 16, ex, SpelMessage.NOT_A_REAL, numberToken));
        }
    }
}
