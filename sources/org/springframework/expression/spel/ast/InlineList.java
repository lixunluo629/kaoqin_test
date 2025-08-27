package org.springframework.expression.spel.ast;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.MethodVisitor;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.CodeFlow;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.expression.spel.SpelNode;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/ast/InlineList.class */
public class InlineList extends SpelNodeImpl {
    private TypedValue constant;

    public InlineList(int pos, SpelNodeImpl... args) {
        super(pos, args);
        this.constant = null;
        checkIfConstant();
    }

    private void checkIfConstant() {
        boolean isConstant = true;
        int max = getChildCount();
        for (int c = 0; c < max; c++) {
            SpelNode child = getChild(c);
            if (!(child instanceof Literal)) {
                if (child instanceof InlineList) {
                    InlineList inlineList = (InlineList) child;
                    if (!inlineList.isConstant()) {
                        isConstant = false;
                    }
                } else {
                    isConstant = false;
                }
            }
        }
        if (isConstant) {
            List<Object> constantList = new ArrayList<>();
            int childcount = getChildCount();
            for (int c2 = 0; c2 < childcount; c2++) {
                SpelNode child2 = getChild(c2);
                if (child2 instanceof Literal) {
                    constantList.add(((Literal) child2).getLiteralValue().getValue());
                } else if (child2 instanceof InlineList) {
                    constantList.add(((InlineList) child2).getConstantValue());
                }
            }
            this.constant = new TypedValue(Collections.unmodifiableList(constantList));
        }
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public TypedValue getValueInternal(ExpressionState expressionState) throws EvaluationException {
        if (this.constant != null) {
            return this.constant;
        }
        List<Object> returnValue = new ArrayList<>();
        int childCount = getChildCount();
        for (int c = 0; c < childCount; c++) {
            returnValue.add(getChild(c).getValue(expressionState));
        }
        return new TypedValue(returnValue);
    }

    @Override // org.springframework.expression.spel.SpelNode
    public String toStringAST() {
        StringBuilder sb = new StringBuilder("{");
        int count = getChildCount();
        for (int c = 0; c < count; c++) {
            if (c > 0) {
                sb.append(",");
            }
            sb.append(getChild(c).toStringAST());
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean isConstant() {
        return this.constant != null;
    }

    public List<Object> getConstantValue() {
        return (List) this.constant.getValue();
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public boolean isCompilable() {
        return isConstant();
    }

    @Override // org.springframework.expression.spel.ast.SpelNodeImpl
    public void generateCode(MethodVisitor mv, CodeFlow codeflow) {
        final String constantFieldName = "inlineList$" + codeflow.nextFieldId();
        final String className = codeflow.getClassName();
        codeflow.registerNewField(new CodeFlow.FieldAdder() { // from class: org.springframework.expression.spel.ast.InlineList.1
            @Override // org.springframework.expression.spel.CodeFlow.FieldAdder
            public void generateField(ClassWriter cw, CodeFlow codeflow2) {
                cw.visitField(26, constantFieldName, "Ljava/util/List;", null, null);
            }
        });
        codeflow.registerNewClinit(new CodeFlow.ClinitAdder() { // from class: org.springframework.expression.spel.ast.InlineList.2
            @Override // org.springframework.expression.spel.CodeFlow.ClinitAdder
            public void generateCode(MethodVisitor mv2, CodeFlow codeflow2) {
                InlineList.this.generateClinitCode(className, constantFieldName, mv2, codeflow2, false);
            }
        });
        mv.visitFieldInsn(178, className, constantFieldName, "Ljava/util/List;");
        codeflow.pushDescriptor("Ljava/util/List");
    }

    void generateClinitCode(String clazzname, String constantFieldName, MethodVisitor mv, CodeFlow codeflow, boolean nested) {
        mv.visitTypeInsn(187, "java/util/ArrayList");
        mv.visitInsn(89);
        mv.visitMethodInsn(183, "java/util/ArrayList", "<init>", "()V", false);
        if (!nested) {
            mv.visitFieldInsn(179, clazzname, constantFieldName, "Ljava/util/List;");
        }
        int childCount = getChildCount();
        for (int c = 0; c < childCount; c++) {
            if (!nested) {
                mv.visitFieldInsn(178, clazzname, constantFieldName, "Ljava/util/List;");
            } else {
                mv.visitInsn(89);
            }
            if (this.children[c] instanceof InlineList) {
                ((InlineList) this.children[c]).generateClinitCode(clazzname, constantFieldName, mv, codeflow, true);
            } else {
                this.children[c].generateCode(mv, codeflow);
                if (CodeFlow.isPrimitive(codeflow.lastDescriptor())) {
                    CodeFlow.insertBoxIfNecessary(mv, codeflow.lastDescriptor().charAt(0));
                }
            }
            mv.visitMethodInsn(185, "java/util/List", BeanUtil.PREFIX_ADDER, "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(87);
        }
    }
}
