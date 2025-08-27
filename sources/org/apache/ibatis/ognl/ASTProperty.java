package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;
import org.springframework.hateoas.Link;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTProperty.class */
public class ASTProperty extends SimpleNode implements NodeType {
    private boolean _indexedAccess;
    private Class _getterClass;
    private Class _setterClass;

    public ASTProperty(int id) {
        super(id);
        this._indexedAccess = false;
    }

    public void setIndexedAccess(boolean value) {
        this._indexedAccess = value;
    }

    public boolean isIndexedAccess() {
        return this._indexedAccess;
    }

    public int getIndexedPropertyType(OgnlContext context, Object source) throws OgnlException {
        Class type = context.getCurrentType();
        Class prevType = context.getPreviousType();
        try {
            if (!isIndexedAccess()) {
                Object property = getProperty(context, source);
                if (property instanceof String) {
                    int indexedPropertyType = OgnlRuntime.getIndexedPropertyType(context, source == null ? null : OgnlRuntime.getCompiler().getInterfaceClass(source.getClass()), (String) property);
                    context.setCurrentObject(source);
                    context.setCurrentType(type);
                    context.setPreviousType(prevType);
                    return indexedPropertyType;
                }
            }
            int i = OgnlRuntime.INDEXED_PROPERTY_NONE;
            context.setCurrentObject(source);
            context.setCurrentType(type);
            context.setPreviousType(prevType);
            return i;
        } catch (Throwable th) {
            context.setCurrentObject(source);
            context.setCurrentType(type);
            context.setPreviousType(prevType);
            throw th;
        }
    }

    public Object getProperty(OgnlContext context, Object source) throws OgnlException {
        return this._children[0].getValue(context, context.getRoot());
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object property = getProperty(context, source);
        Object result = OgnlRuntime.getProperty(context, source, property);
        if (result == null) {
            result = OgnlRuntime.getNullHandler(OgnlRuntime.getTargetClass(source)).nullPropertyValue(context, source, property);
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected void setValueBody(OgnlContext context, Object target, Object value) throws OgnlException {
        OgnlRuntime.setProperty(context, target, getProperty(context, target), value);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isNodeSimpleProperty(OgnlContext context) throws OgnlException {
        return this._children != null && this._children.length == 1 && ((SimpleNode) this._children[0]).isConstant(context);
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getGetterClass() {
        return this._getterClass;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getSetterClass() {
        return this._setterClass;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        String result;
        if (isIndexedAccess()) {
            result = org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX + this._children[0] + "]";
        } else {
            result = ((ASTConst) this._children[0]).getValue().toString();
        }
        return result;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        String cast;
        if (context.getCurrentObject() == null) {
            throw new UnsupportedCompilationException("Current target is null.");
        }
        String result = "";
        Method m = null;
        try {
            if (isIndexedAccess()) {
                Object value = this._children[0].getValue(context, context.getRoot());
                if (value == null || DynamicSubscript.class.isAssignableFrom(value.getClass())) {
                    throw new UnsupportedCompilationException("Value passed as indexed property was null or not supported.");
                }
                String srcString = ExpressionCompiler.getRootExpression(this._children[0], context.getRoot(), context) + this._children[0].toGetSourceString(context, context.getRoot());
                if (ASTChain.class.isInstance(this._children[0]) && (cast = (String) context.remove(ExpressionCompiler.PRE_CAST)) != null) {
                    srcString = cast + srcString;
                }
                if (ASTConst.class.isInstance(this._children[0]) && String.class.isInstance(context.getCurrentObject())) {
                    srcString = SymbolConstants.QUOTES_SYMBOL + srcString + SymbolConstants.QUOTES_SYMBOL;
                }
                if (context.get("_indexedMethod") != null) {
                    Method m2 = (Method) context.remove("_indexedMethod");
                    this._getterClass = m2.getReturnType();
                    Object indexedValue = OgnlRuntime.callMethod(context, target, m2.getName(), new Object[]{value});
                    context.setCurrentType(this._getterClass);
                    context.setCurrentObject(indexedValue);
                    context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m2, m2.getDeclaringClass()));
                    return "." + m2.getName() + "(" + srcString + ")";
                }
                PropertyAccessor p = OgnlRuntime.getPropertyAccessor(target.getClass());
                Object currObj = context.getCurrentObject();
                Class currType = context.getCurrentType();
                Class prevType = context.getPreviousType();
                Object indexVal = p.getProperty(context, target, value);
                context.setCurrentObject(currObj);
                context.setCurrentType(currType);
                context.setPreviousType(prevType);
                if (ASTConst.class.isInstance(this._children[0]) && Number.class.isInstance(context.getCurrentObject())) {
                    context.setCurrentType(OgnlRuntime.getPrimitiveWrapperClass(context.getCurrentObject().getClass()));
                }
                String result2 = p.getSourceAccessor(context, target, srcString);
                this._getterClass = context.getCurrentType();
                context.setCurrentObject(indexVal);
                return result2;
            }
            String name = ((ASTConst) this._children[0]).getValue().toString();
            if (!Iterator.class.isAssignableFrom(context.getCurrentObject().getClass()) || (Iterator.class.isAssignableFrom(context.getCurrentObject().getClass()) && name.indexOf(Link.REL_NEXT) < 0)) {
                try {
                    try {
                        target = getValue(context, context.getCurrentObject());
                        context.setCurrentObject(target);
                    } catch (Throwable th) {
                        context.setCurrentObject(target);
                        throw th;
                    }
                } catch (NoSuchPropertyException e) {
                    try {
                        target = getValue(context, context.getRoot());
                    } catch (NoSuchPropertyException e2) {
                    }
                    context.setCurrentObject(target);
                }
            }
            IndexedPropertyDescriptor propertyDescriptor = OgnlRuntime.getPropertyDescriptor(context.getCurrentObject().getClass(), name);
            if (propertyDescriptor != null && propertyDescriptor.getReadMethod() != null && !context.getMemberAccess().isAccessible(context, context.getCurrentObject(), propertyDescriptor.getReadMethod(), name)) {
                throw new UnsupportedCompilationException("Member access forbidden for property " + name + " on class " + context.getCurrentObject().getClass());
            }
            if (getIndexedPropertyType(context, context.getCurrentObject()) > 0 && propertyDescriptor != null) {
                if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
                    m = propertyDescriptor.getIndexedReadMethod();
                } else if (propertyDescriptor instanceof ObjectIndexedPropertyDescriptor) {
                    m = ((ObjectIndexedPropertyDescriptor) propertyDescriptor).getIndexedReadMethod();
                } else {
                    throw new OgnlException("property '" + name + "' is not an indexed property");
                }
                if (this._parent == null) {
                    m = OgnlRuntime.getReadMethod(context.getCurrentObject().getClass(), name);
                    result = m.getName() + "()";
                    this._getterClass = m.getReturnType();
                } else {
                    context.put("_indexedMethod", m);
                }
            } else {
                PropertyAccessor pa = OgnlRuntime.getPropertyAccessor(context.getCurrentObject().getClass());
                if (context.getCurrentObject().getClass().isArray()) {
                    if (propertyDescriptor == null) {
                        PropertyDescriptor pd = OgnlRuntime.getProperty(context.getCurrentObject().getClass(), name);
                        if (pd != null && pd.getReadMethod() != null) {
                            m = pd.getReadMethod();
                            result = pd.getName();
                        } else {
                            this._getterClass = Integer.TYPE;
                            context.setCurrentAccessor(context.getCurrentObject().getClass());
                            context.setCurrentType(Integer.TYPE);
                            result = "." + name;
                        }
                    }
                } else if (propertyDescriptor != null && propertyDescriptor.getReadMethod() != null) {
                    m = propertyDescriptor.getReadMethod();
                    result = "." + m.getName() + "()";
                } else if (pa != null) {
                    Object currObj2 = context.getCurrentObject();
                    Class currType2 = context.getCurrentType();
                    Class prevType2 = context.getPreviousType();
                    String srcString2 = this._children[0].toGetSourceString(context, context.getRoot());
                    if (ASTConst.class.isInstance(this._children[0]) && String.class.isInstance(context.getCurrentObject())) {
                        srcString2 = SymbolConstants.QUOTES_SYMBOL + srcString2 + SymbolConstants.QUOTES_SYMBOL;
                    }
                    context.setCurrentObject(currObj2);
                    context.setCurrentType(currType2);
                    context.setPreviousType(prevType2);
                    result = pa.getSourceAccessor(context, context.getCurrentObject(), srcString2);
                    this._getterClass = context.getCurrentType();
                }
            }
            if (m != null) {
                this._getterClass = m.getReturnType();
                context.setCurrentType(m.getReturnType());
                context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m, m.getDeclaringClass()));
            }
            context.setCurrentObject(target);
            return result;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    Method getIndexedWriteMethod(PropertyDescriptor pd) {
        if (IndexedPropertyDescriptor.class.isInstance(pd)) {
            return ((IndexedPropertyDescriptor) pd).getIndexedWriteMethod();
        }
        if (ObjectIndexedPropertyDescriptor.class.isInstance(pd)) {
            return ((ObjectIndexedPropertyDescriptor) pd).getIndexedWriteMethod();
        }
        return null;
    }

    /* JADX WARN: Finally extract failed */
    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        String cast;
        String result = "";
        Method m = null;
        if (context.getCurrentObject() == null) {
            throw new UnsupportedCompilationException("Current target is null.");
        }
        try {
            if (isIndexedAccess()) {
                Object value = this._children[0].getValue(context, context.getRoot());
                if (value == null) {
                    throw new UnsupportedCompilationException("Value passed as indexed property is null, can't enhance statement to bytecode.");
                }
                String srcString = ExpressionCompiler.getRootExpression(this._children[0], context.getRoot(), context) + this._children[0].toGetSourceString(context, context.getRoot());
                if (ASTChain.class.isInstance(this._children[0]) && (cast = (String) context.remove(ExpressionCompiler.PRE_CAST)) != null) {
                    srcString = cast + srcString;
                }
                if (ASTConst.class.isInstance(this._children[0]) && String.class.isInstance(context.getCurrentObject())) {
                    srcString = SymbolConstants.QUOTES_SYMBOL + srcString + SymbolConstants.QUOTES_SYMBOL;
                }
                if (context.get("_indexedMethod") != null) {
                    Method m2 = (Method) context.remove("_indexedMethod");
                    PropertyDescriptor pd = (PropertyDescriptor) context.remove("_indexedDescriptor");
                    boolean lastChild = lastChild(context);
                    if (lastChild) {
                        m2 = getIndexedWriteMethod(pd);
                        if (m2 == null) {
                            throw new UnsupportedCompilationException("Indexed property has no corresponding write method.");
                        }
                    }
                    this._setterClass = m2.getParameterTypes()[0];
                    Object indexedValue = null;
                    if (!lastChild) {
                        indexedValue = OgnlRuntime.callMethod(context, target, m2.getName(), new Object[]{value});
                    }
                    context.setCurrentType(this._setterClass);
                    context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m2, m2.getDeclaringClass()));
                    if (!lastChild) {
                        context.setCurrentObject(indexedValue);
                        return "." + m2.getName() + "(" + srcString + ")";
                    }
                    return "." + m2.getName() + "(" + srcString + ", $3)";
                }
                PropertyAccessor p = OgnlRuntime.getPropertyAccessor(target.getClass());
                Object currObj = context.getCurrentObject();
                Class currType = context.getCurrentType();
                Class prevType = context.getPreviousType();
                Object indexVal = p.getProperty(context, target, value);
                context.setCurrentObject(currObj);
                context.setCurrentType(currType);
                context.setPreviousType(prevType);
                if (ASTConst.class.isInstance(this._children[0]) && Number.class.isInstance(context.getCurrentObject())) {
                    context.setCurrentType(OgnlRuntime.getPrimitiveWrapperClass(context.getCurrentObject().getClass()));
                }
                String result2 = lastChild(context) ? p.getSourceSetter(context, target, srcString) : p.getSourceAccessor(context, target, srcString);
                this._getterClass = context.getCurrentType();
                context.setCurrentObject(indexVal);
                return result2;
            }
            String name = ((ASTConst) this._children[0]).getValue().toString();
            if (!Iterator.class.isAssignableFrom(context.getCurrentObject().getClass()) || (Iterator.class.isAssignableFrom(context.getCurrentObject().getClass()) && name.indexOf(Link.REL_NEXT) < 0)) {
                try {
                    try {
                        target = getValue(context, context.getCurrentObject());
                        context.setCurrentObject(target);
                    } catch (Throwable th) {
                        context.setCurrentObject(target);
                        throw th;
                    }
                } catch (NoSuchPropertyException e) {
                    try {
                        target = getValue(context, context.getRoot());
                    } catch (NoSuchPropertyException e2) {
                    }
                    context.setCurrentObject(target);
                }
            }
            IndexedPropertyDescriptor propertyDescriptor = OgnlRuntime.getPropertyDescriptor(OgnlRuntime.getCompiler().getInterfaceClass(context.getCurrentObject().getClass()), name);
            if (propertyDescriptor != null) {
                Method pdMethod = lastChild(context) ? propertyDescriptor.getWriteMethod() : propertyDescriptor.getReadMethod();
                if (pdMethod != null && !context.getMemberAccess().isAccessible(context, context.getCurrentObject(), pdMethod, name)) {
                    throw new UnsupportedCompilationException("Member access forbidden for property " + name + " on class " + context.getCurrentObject().getClass());
                }
            }
            if (propertyDescriptor != null && getIndexedPropertyType(context, context.getCurrentObject()) > 0) {
                if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
                    IndexedPropertyDescriptor ipd = propertyDescriptor;
                    m = lastChild(context) ? ipd.getIndexedWriteMethod() : ipd.getIndexedReadMethod();
                } else if (propertyDescriptor instanceof ObjectIndexedPropertyDescriptor) {
                    ObjectIndexedPropertyDescriptor opd = (ObjectIndexedPropertyDescriptor) propertyDescriptor;
                    m = lastChild(context) ? opd.getIndexedWriteMethod() : opd.getIndexedReadMethod();
                } else {
                    throw new OgnlException("property '" + name + "' is not an indexed property");
                }
                if (this._parent == null) {
                    m = OgnlRuntime.getWriteMethod(context.getCurrentObject().getClass(), name);
                    Class parm = m.getParameterTypes()[0];
                    String cast2 = parm.isArray() ? ExpressionCompiler.getCastString(parm) : parm.getName();
                    result = m.getName() + "((" + cast2 + ")$3)";
                    this._setterClass = parm;
                } else {
                    context.put("_indexedMethod", m);
                    context.put("_indexedDescriptor", propertyDescriptor);
                }
            } else {
                PropertyAccessor pa = OgnlRuntime.getPropertyAccessor(context.getCurrentObject().getClass());
                if (target != null) {
                    this._setterClass = target.getClass();
                }
                if (this._parent != null && propertyDescriptor != null && pa == null) {
                    m = propertyDescriptor.getReadMethod();
                    result = m.getName() + "()";
                } else if (context.getCurrentObject().getClass().isArray()) {
                    result = "";
                } else if (pa != null) {
                    Object currObj2 = context.getCurrentObject();
                    String srcString2 = this._children[0].toGetSourceString(context, context.getRoot());
                    if (ASTConst.class.isInstance(this._children[0]) && String.class.isInstance(context.getCurrentObject())) {
                        srcString2 = SymbolConstants.QUOTES_SYMBOL + srcString2 + SymbolConstants.QUOTES_SYMBOL;
                    }
                    context.setCurrentObject(currObj2);
                    if (!lastChild(context)) {
                        result = pa.getSourceAccessor(context, context.getCurrentObject(), srcString2);
                    } else {
                        result = pa.getSourceSetter(context, context.getCurrentObject(), srcString2);
                    }
                    this._getterClass = context.getCurrentType();
                }
            }
            context.setCurrentObject(target);
            if (m != null) {
                context.setCurrentType(m.getReturnType());
                context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m, m.getDeclaringClass()));
            }
            return result;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
