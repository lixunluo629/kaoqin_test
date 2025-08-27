package org.apache.ibatis.ognl;

import java.lang.reflect.Method;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTStaticMethod.class */
public class ASTStaticMethod extends SimpleNode implements NodeType {
    private String _className;
    private String _methodName;
    private Class _getterClass;

    public ASTStaticMethod(int id) {
        super(id);
    }

    public ASTStaticMethod(OgnlParser p, int id) {
        super(p, id);
    }

    void init(String className, String methodName) {
        this._className = className;
        this._methodName = methodName;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object[] args = OgnlRuntime.getObjectArrayPool().create(jjtGetNumChildren());
        Object root = context.getRoot();
        try {
            int icount = args.length;
            for (int i = 0; i < icount; i++) {
                args[i] = this._children[i].getValue(context, root);
            }
            Object objCallStaticMethod = OgnlRuntime.callStaticMethod(context, this._className, this._methodName, args);
            OgnlRuntime.getObjectArrayPool().recycle(args);
            return objCallStaticMethod;
        } catch (Throwable th) {
            OgnlRuntime.getObjectArrayPool().recycle(args);
            throw th;
        }
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getGetterClass() {
        return this._getterClass;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getSetterClass() {
        return this._getterClass;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        String result = ("@" + this._className + "@" + this._methodName) + "(";
        if (this._children != null && this._children.length > 0) {
            for (int i = 0; i < this._children.length; i++) {
                if (i > 0) {
                    result = result + ", ";
                }
                result = result + this._children[i];
            }
        }
        return result + ")";
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        String result = this._className + "#" + this._methodName + "(";
        try {
            Class clazz = OgnlRuntime.classForName(context, this._className);
            Method m = OgnlRuntime.getMethod(context, clazz, this._methodName, this._children, true);
            if (clazz == null || m == null) {
                throw new UnsupportedCompilationException("Unable to find class/method combo " + this._className + " / " + this._methodName);
            }
            if (!context.getMemberAccess().isAccessible(context, clazz, m, this._methodName)) {
                throw new UnsupportedCompilationException("Method is not accessible, check your jvm runtime security settings. For static class method " + this._className + " / " + this._methodName);
            }
            if (this._children != null && this._children.length > 0) {
                Class[] parms = m.getParameterTypes();
                for (int i = 0; i < this._children.length; i++) {
                    if (i > 0) {
                        result = result + ", ";
                    }
                    Class prevType = context.getCurrentType();
                    Object value = this._children[i].getValue(context, context.getRoot());
                    String parmString = this._children[i].toGetSourceString(context, context.getRoot());
                    if (parmString == null || parmString.trim().length() < 1) {
                        parmString = "null";
                    }
                    if (ASTConst.class.isInstance(this._children[i])) {
                        context.setCurrentType(prevType);
                    }
                    String parmString2 = ExpressionCompiler.getRootExpression(this._children[i], context.getRoot(), context) + parmString;
                    String cast = "";
                    if (ExpressionCompiler.shouldCast(this._children[i])) {
                        cast = (String) context.remove(ExpressionCompiler.PRE_CAST);
                    }
                    if (cast == null) {
                        cast = "";
                    }
                    if (!ASTConst.class.isInstance(this._children[i])) {
                        parmString2 = cast + parmString2;
                    }
                    Class valueClass = value != null ? value.getClass() : null;
                    if (NodeType.class.isAssignableFrom(this._children[i].getClass())) {
                        valueClass = ((NodeType) this._children[i]).getGetterClass();
                    }
                    if (valueClass != parms[i]) {
                        if (parms[i].isArray()) {
                            parmString2 = OgnlRuntime.getCompiler().createLocalReference(context, "(" + ExpressionCompiler.getCastString(parms[i]) + ")ognl.OgnlOps.toArray(" + parmString2 + ", " + parms[i].getComponentType().getName() + ".class, true)", parms[i]);
                        } else if (parms[i].isPrimitive()) {
                            Class wrapClass = OgnlRuntime.getPrimitiveWrapperClass(parms[i]);
                            parmString2 = OgnlRuntime.getCompiler().createLocalReference(context, "((" + wrapClass.getName() + ")ognl.OgnlOps.convertValue(" + parmString2 + "," + wrapClass.getName() + ".class, true))." + OgnlRuntime.getNumericValueGetter(wrapClass), parms[i]);
                        } else if (parms[i] != Object.class) {
                            parmString2 = OgnlRuntime.getCompiler().createLocalReference(context, "(" + parms[i].getName() + ")ognl.OgnlOps.convertValue(" + parmString2 + "," + parms[i].getName() + ".class)", parms[i]);
                        } else if ((NodeType.class.isInstance(this._children[i]) && ((NodeType) this._children[i]).getGetterClass() != null && Number.class.isAssignableFrom(((NodeType) this._children[i]).getGetterClass())) || valueClass.isPrimitive()) {
                            parmString2 = " ($w) " + parmString2;
                        } else if (valueClass.isPrimitive()) {
                            parmString2 = "($w) " + parmString2;
                        }
                    }
                    result = result + parmString2;
                }
            }
            String result2 = result + ")";
            try {
                Object contextObj = getValueBody(context, target);
                context.setCurrentObject(contextObj);
            } catch (Throwable th) {
            }
            if (m != null) {
                this._getterClass = m.getReturnType();
                context.setCurrentType(m.getReturnType());
                context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m, m.getDeclaringClass()));
            }
            return result2;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        return toGetSourceString(context, target);
    }
}
