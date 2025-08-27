package org.apache.ibatis.ognl;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.List;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTCtor.class */
public class ASTCtor extends SimpleNode {
    private String className;
    private boolean isArray;

    public ASTCtor(int id) {
        super(id);
    }

    public ASTCtor(OgnlParser p, int id) {
        super(p, id);
    }

    void setClassName(String className) {
        this.className = className;
    }

    Class getCreatedClass(OgnlContext context) throws ClassNotFoundException {
        return OgnlRuntime.classForName(context, this.className);
    }

    void setArray(boolean value) {
        this.isArray = value;
    }

    public boolean isArray() {
        return this.isArray;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object result;
        int size;
        Object root = context.getRoot();
        int count = jjtGetNumChildren();
        Object[] args = OgnlRuntime.getObjectArrayPool().create(count);
        for (int i = 0; i < count; i++) {
            try {
                args[i] = this._children[i].getValue(context, root);
            } catch (Throwable th) {
                OgnlRuntime.getObjectArrayPool().recycle(args);
                throw th;
            }
        }
        if (this.isArray) {
            if (args.length == 1) {
                try {
                    Class componentClass = OgnlRuntime.classForName(context, this.className);
                    List sourceList = null;
                    if (args[0] instanceof List) {
                        sourceList = (List) args[0];
                        size = sourceList.size();
                    } else {
                        size = (int) OgnlOps.longValue(args[0]);
                    }
                    result = Array.newInstance((Class<?>) componentClass, size);
                    if (sourceList != null) {
                        TypeConverter converter = context.getTypeConverter();
                        int icount = sourceList.size();
                        for (int i2 = 0; i2 < icount; i2++) {
                            Object o = sourceList.get(i2);
                            if (o == null || componentClass.isInstance(o)) {
                                Array.set(result, i2, o);
                            } else {
                                Array.set(result, i2, converter.convertValue(context, null, null, null, o, componentClass));
                            }
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    throw new OgnlException("array component class '" + this.className + "' not found", ex);
                }
            } else {
                throw new OgnlException("only expect array size or fixed initializer list");
            }
        } else {
            result = OgnlRuntime.callConstructor(context, this.className, args);
        }
        Object obj = result;
        OgnlRuntime.getObjectArrayPool().recycle(args);
        return obj;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        String result;
        String result2 = "new " + this.className;
        if (this.isArray) {
            if (this._children[0] instanceof ASTConst) {
                result = result2 + org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX + this._children[0] + "]";
            } else {
                result = result2 + "[] " + this._children[0];
            }
        } else {
            String result3 = result2 + "(";
            if (this._children != null && this._children.length > 0) {
                for (int i = 0; i < this._children.length; i++) {
                    if (i > 0) {
                        result3 = result3 + ", ";
                    }
                    result3 = result3 + this._children[i];
                }
            }
            result = result3 + ")";
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        RuntimeException runtimeExceptionCastToRuntime;
        String result;
        String literal;
        String result2 = "new " + this.className;
        try {
            Class clazz = OgnlRuntime.classForName(context, this.className);
            Object ctorValue = getValueBody(context, target);
            context.setCurrentObject(ctorValue);
            if (clazz != null && ctorValue != null) {
                context.setCurrentType(ctorValue.getClass());
                context.setCurrentAccessor(ctorValue.getClass());
            }
            if (this.isArray) {
                context.put("_ctorClass", clazz);
            }
            try {
                if (this.isArray) {
                    if (this._children[0] instanceof ASTConst) {
                        result = result2 + org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX + this._children[0].toGetSourceString(context, target) + "]";
                    } else if (ASTProperty.class.isInstance(this._children[0])) {
                        result = result2 + org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX + ExpressionCompiler.getRootExpression(this._children[0], target, context) + this._children[0].toGetSourceString(context, target) + "]";
                    } else if (ASTChain.class.isInstance(this._children[0])) {
                        result = result2 + org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX + this._children[0].toGetSourceString(context, target) + "]";
                    } else {
                        result = result2 + "[] " + this._children[0].toGetSourceString(context, target);
                    }
                } else {
                    String result3 = result2 + "(";
                    if (this._children != null && this._children.length > 0) {
                        Object[] values = new Object[this._children.length];
                        String[] expressions = new String[this._children.length];
                        Class[] types = new Class[this._children.length];
                        for (int i = 0; i < this._children.length; i++) {
                            Object objValue = this._children[i].getValue(context, context.getRoot());
                            String value = this._children[i].toGetSourceString(context, target);
                            if (!ASTRootVarRef.class.isInstance(this._children[i])) {
                                value = ExpressionCompiler.getRootExpression(this._children[i], target, context) + value;
                            }
                            String cast = "";
                            if (ExpressionCompiler.shouldCast(this._children[i])) {
                                cast = (String) context.remove(ExpressionCompiler.PRE_CAST);
                            }
                            if (cast == null) {
                                cast = "";
                            }
                            if (!ASTConst.class.isInstance(this._children[i])) {
                                value = cast + value;
                            }
                            values[i] = objValue;
                            expressions[i] = value;
                            types[i] = context.getCurrentType();
                        }
                        Constructor[] cons = clazz.getConstructors();
                        Constructor ctor = null;
                        Class[] ctorParamTypes = null;
                        for (int i2 = 0; i2 < cons.length; i2++) {
                            Class[] ctorTypes = cons[i2].getParameterTypes();
                            if (OgnlRuntime.areArgsCompatible(values, ctorTypes) && (ctor == null || OgnlRuntime.isMoreSpecific(ctorTypes, ctorParamTypes))) {
                                ctor = cons[i2];
                                ctorParamTypes = ctorTypes;
                            }
                        }
                        if (ctor == null) {
                            ctor = OgnlRuntime.getConvertedConstructorAndArgs(context, clazz, OgnlRuntime.getConstructors(clazz), values, new Object[values.length]);
                        }
                        if (ctor == null) {
                            throw new NoSuchMethodException("Unable to find constructor appropriate for arguments in class: " + clazz);
                        }
                        Class[] ctorParamTypes2 = ctor.getParameterTypes();
                        for (int i3 = 0; i3 < this._children.length; i3++) {
                            if (i3 > 0) {
                                result3 = result3 + ", ";
                            }
                            String value2 = expressions[i3];
                            if (types[i3].isPrimitive() && (literal = OgnlRuntime.getNumericLiteral(types[i3])) != null) {
                                value2 = value2 + literal;
                            }
                            if (ctorParamTypes2[i3] != types[i3]) {
                                if (values[i3] != null && !types[i3].isPrimitive() && !values[i3].getClass().isArray() && !ASTConst.class.isInstance(this._children[i3])) {
                                    value2 = "(" + OgnlRuntime.getCompiler().getInterfaceClass(values[i3].getClass()).getName() + ")" + value2;
                                } else if (!ASTConst.class.isInstance(this._children[i3]) || (ASTConst.class.isInstance(this._children[i3]) && !types[i3].isPrimitive())) {
                                    if (!types[i3].isArray() && types[i3].isPrimitive() && !ctorParamTypes2[i3].isPrimitive()) {
                                        value2 = "new " + ExpressionCompiler.getCastString(OgnlRuntime.getPrimitiveWrapperClass(types[i3])) + "(" + value2 + ")";
                                    } else {
                                        value2 = " ($w) " + value2;
                                    }
                                }
                            }
                            result3 = result3 + value2;
                        }
                    }
                    result = result3 + ")";
                }
                context.setCurrentType(ctorValue != null ? ctorValue.getClass() : clazz);
                context.setCurrentAccessor(clazz);
                context.setCurrentObject(ctorValue);
                context.remove("_ctorClass");
                return result;
            } finally {
            }
        } finally {
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        return "";
    }
}
