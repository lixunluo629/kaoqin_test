package org.apache.ibatis.ognl;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTList.class */
public class ASTList extends SimpleNode implements NodeType {
    public ASTList(int id) {
        super(id);
    }

    public ASTList(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        List answer = new ArrayList(jjtGetNumChildren());
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            answer.add(this._children[i].getValue(context, source));
        }
        return answer;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getGetterClass() {
        return null;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getSetterClass() {
        return null;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        String result = "{ ";
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            if (i > 0) {
                result = result + ", ";
            }
            result = result + this._children[i].toString();
        }
        return result + " }";
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        String result = "";
        boolean array = false;
        if (this._parent != null && ASTCtor.class.isInstance(this._parent) && ((ASTCtor) this._parent).isArray()) {
            array = true;
        }
        context.setCurrentType(List.class);
        context.setCurrentAccessor(List.class);
        if (!array) {
            if (jjtGetNumChildren() < 1) {
                return "java.util.Arrays.asList( new Object[0])";
            }
            result = result + "java.util.Arrays.asList( new Object[] ";
        }
        String result2 = result + "{ ";
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            try {
                if (i > 0) {
                    result2 = result2 + ", ";
                }
                Class prevType = context.getCurrentType();
                Object objValue = this._children[i].getValue(context, context.getRoot());
                String value = this._children[i].toGetSourceString(context, target);
                if (ASTConst.class.isInstance(this._children[i])) {
                    context.setCurrentType(prevType);
                }
                String value2 = ExpressionCompiler.getRootExpression(this._children[i], target, context) + value;
                String cast = "";
                if (ExpressionCompiler.shouldCast(this._children[i])) {
                    cast = (String) context.remove(ExpressionCompiler.PRE_CAST);
                }
                if (cast == null) {
                    cast = "";
                }
                if (!ASTConst.class.isInstance(this._children[i])) {
                    value2 = cast + value2;
                }
                Class ctorClass = (Class) context.get("_ctorClass");
                if (array && ctorClass != null && !ctorClass.isPrimitive()) {
                    Class valueClass = value2 != null ? value2.getClass() : null;
                    if (NodeType.class.isAssignableFrom(this._children[i].getClass())) {
                        valueClass = ((NodeType) this._children[i]).getGetterClass();
                    }
                    if (valueClass != null && ctorClass.isArray()) {
                        value2 = OgnlRuntime.getCompiler().createLocalReference(context, "(" + ExpressionCompiler.getCastString(ctorClass) + ")ognl.OgnlOps.toArray(" + value2 + ", " + ctorClass.getComponentType().getName() + ".class, true)", ctorClass);
                    } else if (ctorClass.isPrimitive()) {
                        Class wrapClass = OgnlRuntime.getPrimitiveWrapperClass(ctorClass);
                        value2 = OgnlRuntime.getCompiler().createLocalReference(context, "((" + wrapClass.getName() + ")ognl.OgnlOps.convertValue(" + value2 + "," + wrapClass.getName() + ".class, true))." + OgnlRuntime.getNumericValueGetter(wrapClass), ctorClass);
                    } else if (ctorClass != Object.class) {
                        value2 = OgnlRuntime.getCompiler().createLocalReference(context, "(" + ctorClass.getName() + ")ognl.OgnlOps.convertValue(" + value2 + "," + ctorClass.getName() + ".class)", ctorClass);
                    } else if ((NodeType.class.isInstance(this._children[i]) && ((NodeType) this._children[i]).getGetterClass() != null && Number.class.isAssignableFrom(((NodeType) this._children[i]).getGetterClass())) || valueClass.isPrimitive()) {
                        value2 = " ($w) (" + value2 + ")";
                    } else if (valueClass.isPrimitive()) {
                        value2 = "($w) (" + value2 + ")";
                    }
                } else if (ctorClass == null || !ctorClass.isPrimitive()) {
                    value2 = " ($w) (" + value2 + ")";
                }
                if (objValue == null || value2.length() <= 0) {
                    value2 = "null";
                }
                result2 = result2 + value2;
            } catch (Throwable t) {
                throw OgnlOps.castToRuntime(t);
            }
        }
        context.setCurrentType(List.class);
        context.setCurrentAccessor(List.class);
        String result3 = result2 + "}";
        if (!array) {
            result3 = result3 + ")";
        }
        return result3;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Can't generate setter for ASTList.");
    }
}
