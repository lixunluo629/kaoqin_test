package org.springframework.expression.spel.ast;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.springframework.asm.MethodVisitor;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Operation;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.util.NumberUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/OpMultiply.class */
public class OpMultiply extends Operator {
    public OpMultiply(int pos, SpelNodeImpl... operands) {
        super("*", pos, operands);
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        Object leftOperand = getLeftOperand().getValueInternal(state).getValue();
        Object rightOperand = getRightOperand().getValueInternal(state).getValue();
        if ((leftOperand instanceof Number) && (rightOperand instanceof Number)) {
            Number leftNumber = (Number) leftOperand;
            Number rightNumber = (Number) rightOperand;
            if ((leftNumber instanceof BigDecimal) || (rightNumber instanceof BigDecimal)) {
                BigDecimal leftBigDecimal = (BigDecimal) NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
                BigDecimal rightBigDecimal = (BigDecimal) NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
                return new TypedValue(leftBigDecimal.multiply(rightBigDecimal));
            }
            if ((leftNumber instanceof Double) || (rightNumber instanceof Double)) {
                this.exitTypeDescriptor = "D";
                return new TypedValue(Double.valueOf(leftNumber.doubleValue() * rightNumber.doubleValue()));
            }
            if ((leftNumber instanceof Float) || (rightNumber instanceof Float)) {
                this.exitTypeDescriptor = "F";
                return new TypedValue(Float.valueOf(leftNumber.floatValue() * rightNumber.floatValue()));
            }
            if ((leftNumber instanceof BigInteger) || (rightNumber instanceof BigInteger)) {
                BigInteger leftBigInteger = (BigInteger) NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
                BigInteger rightBigInteger = (BigInteger) NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
                return new TypedValue(leftBigInteger.multiply(rightBigInteger));
            }
            if ((leftNumber instanceof Long) || (rightNumber instanceof Long)) {
                this.exitTypeDescriptor = "J";
                return new TypedValue(Long.valueOf(leftNumber.longValue() * rightNumber.longValue()));
            }
            if (CodeFlow.isIntegerForNumericOp(leftNumber) || CodeFlow.isIntegerForNumericOp(rightNumber)) {
                this.exitTypeDescriptor = "I";
                return new TypedValue(Integer.valueOf(leftNumber.intValue() * rightNumber.intValue()));
            }
            return new TypedValue(Double.valueOf(leftNumber.doubleValue() * rightNumber.doubleValue()));
        }
        if ((leftOperand instanceof String) && (rightOperand instanceof Integer)) {
            int repeats = ((Integer) rightOperand).intValue();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < repeats; i++) {
                result.append(leftOperand);
            }
            return new TypedValue(result.toString());
        }
        return state.operate(Operation.MULTIPLY, leftOperand, rightOperand);
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public boolean isCompilable() {
        if (getLeftOperand().isCompilable()) {
            return (this.children.length <= 1 || getRightOperand().isCompilable()) && this.exitTypeDescriptor != null;
        }
        return false;
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        getLeftOperand().generateCode(mv, cf);
        String leftDesc = getLeftOperand().exitTypeDescriptor;
        CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, leftDesc, this.exitTypeDescriptor.charAt(0));
        if (this.children.length > 1) {
            cf.enterCompilationScope();
            getRightOperand().generateCode(mv, cf);
            String rightDesc = getRightOperand().exitTypeDescriptor;
            cf.exitCompilationScope();
            CodeFlow.insertNumericUnboxOrPrimitiveTypeCoercion(mv, rightDesc, this.exitTypeDescriptor.charAt(0));
            switch (this.exitTypeDescriptor.charAt(0)) {
                case 'D':
                    mv.visitInsn(107);
                    break;
                case 'E':
                case 'G':
                case 'H':
                default:
                    throw new IllegalStateException("Unrecognized exit type descriptor: '" + this.exitTypeDescriptor + "'");
                case 'F':
                    mv.visitInsn(106);
                    break;
                case 'I':
                    mv.visitInsn(104);
                    break;
                case 'J':
                    mv.visitInsn(105);
                    break;
            }
        }
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
