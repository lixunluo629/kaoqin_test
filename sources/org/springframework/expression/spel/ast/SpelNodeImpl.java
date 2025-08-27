package org.springframework.expression.spel.ast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;
import org.springframework.expression.common.ExpressionUtils;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/SpelNodeImpl.class */
public abstract class SpelNodeImpl implements SpelNode, Opcodes {
    private static final SpelNodeImpl[] NO_CHILDREN = new SpelNodeImpl[0];
    protected int pos;
    protected SpelNodeImpl[] children;
    private SpelNodeImpl parent;
    protected volatile String exitTypeDescriptor;

    public abstract TypedValue getValueInternal(ExpressionState expressionState) throws EvaluationException;

    public SpelNodeImpl(int pos, SpelNodeImpl... operands) {
        this.children = NO_CHILDREN;
        this.pos = pos;
        if (!ObjectUtils.isEmpty((Object[]) operands)) {
            this.children = operands;
            for (SpelNodeImpl operand : operands) {
                operand.parent = this;
            }
        }
    }

    @Deprecated
    protected SpelNodeImpl getPreviousChild() {
        SpelNodeImpl child;
        SpelNodeImpl result = null;
        if (this.parent != null) {
            SpelNodeImpl[] spelNodeImplArr = this.parent.children;
            int length = spelNodeImplArr.length;
            for (int i = 0; i < length && this != (child = spelNodeImplArr[i]); i++) {
                result = child;
            }
        }
        return result;
    }

    protected boolean nextChildIs(Class<?>... clazzes) {
        if (this.parent != null) {
            SpelNodeImpl[] peers = this.parent.children;
            int max = peers.length;
            for (int i = 0; i < max; i++) {
                if (this == peers[i]) {
                    if (i + 1 >= max) {
                        return false;
                    }
                    Class<?> clazz = peers[i + 1].getClass();
                    for (Class<?> desiredClazz : clazzes) {
                        if (clazz.equals(desiredClazz)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    @Override // org.springframework.expression.spel.SpelNode
    public final Object getValue(ExpressionState expressionState) throws EvaluationException {
        if (expressionState != null) {
            return getValueInternal(expressionState).getValue();
        }
        return getValue(new ExpressionState(new StandardEvaluationContext()));
    }

    @Override // org.springframework.expression.spel.SpelNode
    public final TypedValue getTypedValue(ExpressionState expressionState) throws EvaluationException {
        if (expressionState != null) {
            return getValueInternal(expressionState);
        }
        return getTypedValue(new ExpressionState(new StandardEvaluationContext()));
    }

    @Override // org.springframework.expression.spel.SpelNode
    public boolean isWritable(ExpressionState expressionState) throws EvaluationException {
        return false;
    }

    @Override // org.springframework.expression.spel.SpelNode
    public void setValue(ExpressionState expressionState, Object newValue) throws EvaluationException {
        throw new SpelEvaluationException(getStartPosition(), SpelMessage.SETVALUE_NOT_SUPPORTED, getClass());
    }

    @Override // org.springframework.expression.spel.SpelNode
    public SpelNode getChild(int index) {
        return this.children[index];
    }

    @Override // org.springframework.expression.spel.SpelNode
    public int getChildCount() {
        return this.children.length;
    }

    @Override // org.springframework.expression.spel.SpelNode
    public Class<?> getObjectClass(Object obj) {
        if (obj == null) {
            return null;
        }
        return obj instanceof Class ? (Class) obj : obj.getClass();
    }

    protected final <T> T getValue(ExpressionState expressionState, Class<T> cls) throws EvaluationException {
        return (T) ExpressionUtils.convertTypedValue(expressionState.getEvaluationContext(), getValueInternal(expressionState), cls);
    }

    @Override // org.springframework.expression.spel.SpelNode
    public int getStartPosition() {
        return this.pos >> 16;
    }

    @Override // org.springframework.expression.spel.SpelNode
    public int getEndPosition() {
        return this.pos & 65535;
    }

    protected ValueRef getValueRef(ExpressionState state) throws EvaluationException {
        throw new SpelEvaluationException(this.pos, SpelMessage.NOT_ASSIGNABLE, toStringAST());
    }

    public boolean isCompilable() {
        return false;
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        throw new IllegalStateException(getClass().getName() + " has no generateCode(..) method");
    }

    public String getExitDescriptor() {
        return this.exitTypeDescriptor;
    }

    protected static void generateCodeForArguments(MethodVisitor mv, CodeFlow cf, Member member, SpelNodeImpl[] arguments) {
        String[] paramDescriptors;
        boolean isVarargs;
        if (member instanceof Constructor) {
            Constructor<?> ctor = (Constructor) member;
            paramDescriptors = CodeFlow.toDescriptors(ctor.getParameterTypes());
            isVarargs = ctor.isVarArgs();
        } else {
            Method method = (Method) member;
            paramDescriptors = CodeFlow.toDescriptors(method.getParameterTypes());
            isVarargs = method.isVarArgs();
        }
        if (isVarargs) {
            int childCount = arguments.length;
            int p = 0;
            while (p < paramDescriptors.length - 1) {
                generateCodeForArgument(mv, cf, arguments[p], paramDescriptors[p]);
                p++;
            }
            SpelNodeImpl lastChild = childCount == 0 ? null : arguments[childCount - 1];
            String arrayType = paramDescriptors[paramDescriptors.length - 1];
            if (lastChild != null && arrayType.equals(lastChild.getExitDescriptor())) {
                generateCodeForArgument(mv, cf, lastChild, paramDescriptors[p]);
                return;
            }
            String arrayType2 = arrayType.substring(1);
            CodeFlow.insertNewArrayCode(mv, childCount - p, arrayType2);
            int arrayindex = 0;
            while (p < childCount) {
                SpelNodeImpl child = arguments[p];
                mv.visitInsn(89);
                int i = arrayindex;
                arrayindex++;
                CodeFlow.insertOptimalLoad(mv, i);
                generateCodeForArgument(mv, cf, child, arrayType2);
                CodeFlow.insertArrayStore(mv, arrayType2);
                p++;
            }
            return;
        }
        for (int i2 = 0; i2 < paramDescriptors.length; i2++) {
            generateCodeForArgument(mv, cf, arguments[i2], paramDescriptors[i2]);
        }
    }

    protected static void generateCodeForArgument(MethodVisitor mv, CodeFlow cf, SpelNodeImpl argument, String paramDesc) {
        cf.enterCompilationScope();
        argument.generateCode(mv, cf);
        String lastDesc = cf.lastDescriptor();
        boolean primitiveOnStack = CodeFlow.isPrimitive(lastDesc);
        if (primitiveOnStack && paramDesc.charAt(0) == 'L') {
            CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
        } else if (paramDesc.length() == 1 && !primitiveOnStack) {
            CodeFlow.insertUnboxInsns(mv, paramDesc.charAt(0), lastDesc);
        } else if (!paramDesc.equals(lastDesc)) {
            CodeFlow.insertCheckCast(mv, paramDesc);
        }
        cf.exitCompilationScope();
    }
}
