package org.apache.ibatis.ognl;

import java.lang.reflect.Method;
import java.util.List;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.OrderedReturn;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTMethod.class */
public class ASTMethod extends SimpleNode implements OrderedReturn, NodeType {
    private String _methodName;
    private String _lastExpression;
    private String _coreExpression;
    private Class _getterClass;

    public ASTMethod(int id) {
        super(id);
    }

    public ASTMethod(OgnlParser p, int id) {
        super(p, id);
    }

    public void setMethodName(String methodName) {
        this._methodName = methodName;
    }

    public String getMethodName() {
        return this._methodName;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object[] args = OgnlRuntime.getObjectArrayPool().create(jjtGetNumChildren());
        try {
            Object root = context.getRoot();
            int icount = args.length;
            for (int i = 0; i < icount; i++) {
                args[i] = this._children[i].getValue(context, root);
            }
            Object result = OgnlRuntime.callMethod(context, source, this._methodName, args);
            if (result == null) {
                NullHandler nh = OgnlRuntime.getNullHandler(OgnlRuntime.getTargetClass(source));
                result = nh.nullMethodResult(context, source, this._methodName, args);
            }
            Object obj = result;
            OgnlRuntime.getObjectArrayPool().recycle(args);
            return obj;
        } catch (Throwable th) {
            OgnlRuntime.getObjectArrayPool().recycle(args);
            throw th;
        }
    }

    @Override // org.apache.ibatis.ognl.enhance.OrderedReturn
    public String getLastExpression() {
        return this._lastExpression;
    }

    @Override // org.apache.ibatis.ognl.enhance.OrderedReturn
    public String getCoreExpression() {
        return this._coreExpression;
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
        String result = this._methodName + "(";
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
        RuntimeException runtimeExceptionCastToRuntime;
        if (target == null) {
            throw new UnsupportedCompilationException("Target object is null.");
        }
        try {
            Method m = OgnlRuntime.getMethod(context, context.getCurrentType() != null ? context.getCurrentType() : target.getClass(), this._methodName, this._children, false);
            Class[] argumentClasses = getChildrenClasses(context, this._children);
            if (m == null) {
                m = OgnlRuntime.getReadMethod(target.getClass(), this._methodName, argumentClasses);
            }
            if (m == null) {
                Method m2 = OgnlRuntime.getWriteMethod(target.getClass(), this._methodName, argumentClasses);
                if (m2 != null) {
                    context.setCurrentType(m2.getReturnType());
                    context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m2, m2.getDeclaringClass()));
                    this._coreExpression = toSetSourceString(context, target);
                    if (this._coreExpression == null || this._coreExpression.length() < 1) {
                        throw new UnsupportedCompilationException("can't find suitable getter method");
                    }
                    this._coreExpression += ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
                    this._lastExpression = "null";
                    return this._coreExpression;
                }
                return "";
            }
            this._getterClass = m.getReturnType();
            boolean varArgs = OgnlRuntime.isJdk15() && m.isVarArgs();
            if (varArgs) {
                throw new UnsupportedCompilationException("Javassist does not currently support varargs method calls");
            }
            String result = "." + m.getName() + "(";
            if (this._children != null && this._children.length > 0) {
                Class[] parms = m.getParameterTypes();
                String prevCast = (String) context.remove(ExpressionCompiler.PRE_CAST);
                for (int i = 0; i < this._children.length; i++) {
                    if (i > 0) {
                        result = result + ", ";
                    }
                    Class prevType = context.getCurrentType();
                    context.setCurrentObject(context.getRoot());
                    context.setCurrentType(context.getRoot() != null ? context.getRoot().getClass() : null);
                    context.setCurrentAccessor(null);
                    context.setPreviousType(null);
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
                    if ((!varArgs || (varArgs && i + 1 < parms.length)) && valueClass != parms[i]) {
                        if (parms[i].isArray()) {
                            parmString2 = OgnlRuntime.getCompiler().createLocalReference(context, "(" + ExpressionCompiler.getCastString(parms[i]) + ")ognl.OgnlOps#toArray(" + parmString2 + ", " + parms[i].getComponentType().getName() + ".class, true)", parms[i]);
                        } else if (parms[i].isPrimitive()) {
                            Class wrapClass = OgnlRuntime.getPrimitiveWrapperClass(parms[i]);
                            parmString2 = OgnlRuntime.getCompiler().createLocalReference(context, "((" + wrapClass.getName() + ")ognl.OgnlOps#convertValue(" + parmString2 + "," + wrapClass.getName() + ".class, true))." + OgnlRuntime.getNumericValueGetter(wrapClass), parms[i]);
                        } else if (parms[i] != Object.class) {
                            parmString2 = OgnlRuntime.getCompiler().createLocalReference(context, "(" + parms[i].getName() + ")ognl.OgnlOps#convertValue(" + parmString2 + "," + parms[i].getName() + ".class)", parms[i]);
                        } else if ((NodeType.class.isInstance(this._children[i]) && ((NodeType) this._children[i]).getGetterClass() != null && Number.class.isAssignableFrom(((NodeType) this._children[i]).getGetterClass())) || (valueClass != null && valueClass.isPrimitive())) {
                            parmString2 = " ($w) " + parmString2;
                        } else if (valueClass != null && valueClass.isPrimitive()) {
                            parmString2 = "($w) " + parmString2;
                        }
                    }
                    result = result + parmString2;
                }
                if (prevCast != null) {
                    context.put(ExpressionCompiler.PRE_CAST, prevCast);
                }
            }
            try {
                Object contextObj = getValueBody(context, target);
                context.setCurrentObject(contextObj);
                String result2 = result + ")";
                if (m.getReturnType() == Void.TYPE) {
                    this._coreExpression = result2 + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
                    this._lastExpression = "null";
                }
                context.setCurrentType(m.getReturnType());
                context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m, m.getDeclaringClass()));
                return result2;
            } finally {
            }
        } finally {
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        Method m = OgnlRuntime.getWriteMethod(context.getCurrentType() != null ? context.getCurrentType() : target.getClass(), this._methodName, getChildrenClasses(context, this._children));
        if (m == null) {
            throw new UnsupportedCompilationException("Unable to determine setter method generation for " + this._methodName);
        }
        String post = "";
        String result = "." + m.getName() + "(";
        if (m.getReturnType() != Void.TYPE && m.getReturnType().isPrimitive() && (this._parent == null || !ASTTest.class.isInstance(this._parent))) {
            Class wrapper = OgnlRuntime.getPrimitiveWrapperClass(m.getReturnType());
            ExpressionCompiler.addCastString(context, "new " + wrapper.getName() + "(");
            post = ")";
            this._getterClass = wrapper;
        }
        boolean varArgs = OgnlRuntime.isJdk15() && m.isVarArgs();
        if (varArgs) {
            throw new UnsupportedCompilationException("Javassist does not currently support varargs method calls");
        }
        try {
            if (this._children != null && this._children.length > 0) {
                Class[] parms = m.getParameterTypes();
                String prevCast = (String) context.remove(ExpressionCompiler.PRE_CAST);
                for (int i = 0; i < this._children.length; i++) {
                    if (i > 0) {
                        result = result + ", ";
                    }
                    Class prevType = context.getCurrentType();
                    context.setCurrentObject(context.getRoot());
                    context.setCurrentType(context.getRoot() != null ? context.getRoot().getClass() : null);
                    context.setCurrentAccessor(null);
                    context.setPreviousType(null);
                    Object value = this._children[i].getValue(context, context.getRoot());
                    String parmString = this._children[i].toSetSourceString(context, context.getRoot());
                    if (context.getCurrentType() == Void.TYPE || context.getCurrentType() == Void.TYPE) {
                        throw new UnsupportedCompilationException("Method argument can't be a void type.");
                    }
                    if (parmString == null || parmString.trim().length() < 1) {
                        if (ASTProperty.class.isInstance(this._children[i]) || ASTMethod.class.isInstance(this._children[i]) || ASTStaticMethod.class.isInstance(this._children[i]) || ASTChain.class.isInstance(this._children[i])) {
                            throw new UnsupportedCompilationException("ASTMethod setter child returned null from a sub property expression.");
                        }
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
                    String parmString3 = cast + parmString2;
                    Class valueClass = value != null ? value.getClass() : null;
                    if (NodeType.class.isAssignableFrom(this._children[i].getClass())) {
                        valueClass = ((NodeType) this._children[i]).getGetterClass();
                    }
                    if (valueClass != parms[i]) {
                        if (parms[i].isArray()) {
                            parmString3 = OgnlRuntime.getCompiler().createLocalReference(context, "(" + ExpressionCompiler.getCastString(parms[i]) + ")ognl.OgnlOps#toArray(" + parmString3 + ", " + parms[i].getComponentType().getName() + ".class)", parms[i]);
                        } else if (parms[i].isPrimitive()) {
                            Class wrapClass = OgnlRuntime.getPrimitiveWrapperClass(parms[i]);
                            parmString3 = OgnlRuntime.getCompiler().createLocalReference(context, "((" + wrapClass.getName() + ")ognl.OgnlOps#convertValue(" + parmString3 + "," + wrapClass.getName() + ".class, true))." + OgnlRuntime.getNumericValueGetter(wrapClass), parms[i]);
                        } else if (parms[i] != Object.class) {
                            parmString3 = OgnlRuntime.getCompiler().createLocalReference(context, "(" + parms[i].getName() + ")ognl.OgnlOps#convertValue(" + parmString3 + "," + parms[i].getName() + ".class)", parms[i]);
                        } else if ((NodeType.class.isInstance(this._children[i]) && ((NodeType) this._children[i]).getGetterClass() != null && Number.class.isAssignableFrom(((NodeType) this._children[i]).getGetterClass())) || (valueClass != null && valueClass.isPrimitive())) {
                            parmString3 = " ($w) " + parmString3;
                        } else if (valueClass != null && valueClass.isPrimitive()) {
                            parmString3 = "($w) " + parmString3;
                        }
                    }
                    result = result + parmString3;
                }
                if (prevCast != null) {
                    context.put(ExpressionCompiler.PRE_CAST, prevCast);
                }
            }
            try {
                Object contextObj = getValueBody(context, target);
                context.setCurrentObject(contextObj);
            } catch (Throwable th) {
            }
            context.setCurrentType(m.getReturnType());
            context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m, m.getDeclaringClass()));
            return result + ")" + post;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    private static Class getClassMatchingAllChildren(OgnlContext context, Node[] _children) {
        Class<?>[] cc = getChildrenClasses(context, _children);
        Class componentType = null;
        int j = 0;
        while (true) {
            if (j >= cc.length) {
                break;
            }
            Class<?> ic = cc[j];
            if (ic == null) {
                componentType = Object.class;
                break;
            }
            if (componentType == null) {
                componentType = ic;
            } else if (componentType.isAssignableFrom(ic)) {
                continue;
            } else if (ic.isAssignableFrom(componentType)) {
                componentType = ic;
            } else {
                while (true) {
                    Class pc = componentType.getSuperclass();
                    if (pc == null) {
                        break;
                    }
                    if (pc.isAssignableFrom(ic)) {
                        componentType = pc;
                        break;
                    }
                }
                if (!componentType.isAssignableFrom(ic)) {
                    componentType = Object.class;
                    break;
                }
            }
            j++;
        }
        if (componentType == null) {
            componentType = Object.class;
        }
        return componentType;
    }

    private static Class[] getChildrenClasses(OgnlContext context, Node[] _children) {
        if (_children == null) {
            return null;
        }
        Class[] argumentClasses = new Class[_children.length];
        for (int i = 0; i < _children.length; i++) {
            Node child = _children[i];
            if (child instanceof ASTList) {
                argumentClasses[i] = List.class;
            } else if (child instanceof NodeType) {
                argumentClasses[i] = ((NodeType) child).getGetterClass();
            } else if (child instanceof ASTCtor) {
                try {
                    argumentClasses[i] = ((ASTCtor) child).getCreatedClass(context);
                } catch (ClassNotFoundException nfe) {
                    throw OgnlOps.castToRuntime(nfe);
                }
            } else if (child instanceof ASTTest) {
                argumentClasses[i] = getClassMatchingAllChildren(context, ((ASTTest) child)._children);
            } else {
                throw new UnsupportedOperationException("Don't know how to handle child: " + child);
            }
        }
        return argumentClasses;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isSimpleMethod(OgnlContext context) {
        return true;
    }
}
