package org.apache.ibatis.ognl;

import java.lang.reflect.Array;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.OrderedReturn;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTChain.class */
public class ASTChain extends SimpleNode implements NodeType, OrderedReturn {
    private Class _getterClass;
    private Class _setterClass;
    private String _lastExpression;
    private String _coreExpression;

    public ASTChain(int id) {
        super(id);
    }

    public ASTChain(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.enhance.OrderedReturn
    public String getLastExpression() {
        return this._lastExpression;
    }

    @Override // org.apache.ibatis.ognl.enhance.OrderedReturn
    public String getCoreExpression() {
        return this._coreExpression;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.Node
    public void jjtClose() {
        flattenTree();
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException, NegativeArraySizeException {
        ASTProperty propertyNode;
        int indexType;
        Object result = source;
        int i = 0;
        int ilast = this._children.length - 1;
        while (i <= ilast) {
            boolean handled = false;
            if (i < ilast && (this._children[i] instanceof ASTProperty) && (indexType = (propertyNode = (ASTProperty) this._children[i]).getIndexedPropertyType(context, result)) != OgnlRuntime.INDEXED_PROPERTY_NONE && (this._children[i + 1] instanceof ASTProperty)) {
                ASTProperty indexNode = (ASTProperty) this._children[i + 1];
                if (indexNode.isIndexedAccess()) {
                    Object index = indexNode.getProperty(context, result);
                    if (index instanceof DynamicSubscript) {
                        if (indexType == OgnlRuntime.INDEXED_PROPERTY_INT) {
                            Object array = propertyNode.getValue(context, result);
                            int len = Array.getLength(array);
                            switch (((DynamicSubscript) index).getFlag()) {
                                case 0:
                                    index = new Integer(len > 0 ? 0 : -1);
                                    break;
                                case 1:
                                    index = new Integer(len > 0 ? len / 2 : -1);
                                    break;
                                case 2:
                                    index = new Integer(len > 0 ? len - 1 : -1);
                                    break;
                                case 3:
                                    result = Array.newInstance(array.getClass().getComponentType(), len);
                                    System.arraycopy(array, 0, result, 0, len);
                                    handled = true;
                                    i++;
                                    break;
                            }
                        } else if (indexType == OgnlRuntime.INDEXED_PROPERTY_OBJECT) {
                            throw new OgnlException("DynamicSubscript '" + indexNode + "' not allowed for object indexed property '" + propertyNode + "'");
                        }
                    }
                    if (!handled) {
                        result = OgnlRuntime.getIndexedProperty(context, result, propertyNode.getProperty(context, result).toString(), index);
                        handled = true;
                        i++;
                    }
                }
            }
            if (!handled) {
                result = this._children[i].getValue(context, result);
            }
            i++;
        }
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x018d A[PHI: r10 r11
  0x018d: PHI (r10v2 'handled' boolean) = 
  (r10v1 'handled' boolean)
  (r10v1 'handled' boolean)
  (r10v1 'handled' boolean)
  (r10v1 'handled' boolean)
  (r10v1 'handled' boolean)
  (r10v4 'handled' boolean)
  (r10v5 'handled' boolean)
 binds: [B:6:0x001a, B:8:0x0027, B:10:0x0044, B:12:0x0053, B:14:0x0069, B:48:0x0172, B:46:0x0155] A[DONT_GENERATE, DONT_INLINE]
  0x018d: PHI (r11v2 'i' int) = (r11v1 'i' int), (r11v1 'i' int), (r11v1 'i' int), (r11v1 'i' int), (r11v1 'i' int), (r11v5 'i' int), (r11v7 'i' int) binds: [B:6:0x001a, B:8:0x0027, B:10:0x0044, B:12:0x0053, B:14:0x0069, B:48:0x0172, B:46:0x0155] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01a1 A[SYNTHETIC] */
    @Override // org.apache.ibatis.ognl.SimpleNode
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void setValueBody(org.apache.ibatis.ognl.OgnlContext r7, java.lang.Object r8, java.lang.Object r9) throws org.apache.ibatis.ognl.OgnlException {
        /*
            Method dump skipped, instructions count: 449
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.ASTChain.setValueBody(org.apache.ibatis.ognl.OgnlContext, java.lang.Object, java.lang.Object):void");
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isSimpleNavigationChain(OgnlContext context) throws OgnlException {
        boolean zIsSimpleProperty;
        boolean result = false;
        if (this._children != null && this._children.length > 0) {
            result = true;
            for (int i = 0; result && i < this._children.length; i++) {
                if (this._children[i] instanceof SimpleNode) {
                    zIsSimpleProperty = ((SimpleNode) this._children[i]).isSimpleProperty(context);
                } else {
                    zIsSimpleProperty = false;
                }
                result = zIsSimpleProperty;
            }
        }
        return result;
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
        String result = "";
        if (this._children != null && this._children.length > 0) {
            for (int i = 0; i < this._children.length; i++) {
                if (i > 0 && (!(this._children[i] instanceof ASTProperty) || !((ASTProperty) this._children[i]).isIndexedAccess())) {
                    result = result + ".";
                }
                result = result + this._children[i].toString();
            }
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        String prevChain = (String) context.get("_currentChain");
        if (target != null) {
            context.setCurrentObject(target);
            context.setCurrentType(target.getClass());
        }
        String result = "";
        NodeType _lastType = null;
        boolean ordered = false;
        boolean constructor = false;
        try {
            if (this._children != null && this._children.length > 0) {
                for (int i = 0; i < this._children.length; i++) {
                    String value = this._children[i].toGetSourceString(context, context.getCurrentObject());
                    if (ASTCtor.class.isInstance(this._children[i])) {
                        constructor = true;
                    }
                    if (NodeType.class.isInstance(this._children[i]) && ((NodeType) this._children[i]).getGetterClass() != null) {
                        _lastType = (NodeType) this._children[i];
                    }
                    if (!ASTVarRef.class.isInstance(this._children[i]) && !constructor && ((!OrderedReturn.class.isInstance(this._children[i]) || ((OrderedReturn) this._children[i]).getLastExpression() == null) && (this._parent == null || !ASTSequence.class.isInstance(this._parent)))) {
                        value = OgnlRuntime.getCompiler().castExpression(context, this._children[i], value);
                    }
                    if (OrderedReturn.class.isInstance(this._children[i]) && ((OrderedReturn) this._children[i]).getLastExpression() != null) {
                        ordered = true;
                        OrderedReturn or = (OrderedReturn) this._children[i];
                        if (or.getCoreExpression() == null || or.getCoreExpression().trim().length() <= 0) {
                            result = "";
                        } else {
                            result = result + or.getCoreExpression();
                        }
                        this._lastExpression = or.getLastExpression();
                        if (context.get(ExpressionCompiler.PRE_CAST) != null) {
                            this._lastExpression = context.remove(ExpressionCompiler.PRE_CAST) + this._lastExpression;
                        }
                    } else if (ASTOr.class.isInstance(this._children[i]) || ASTAnd.class.isInstance(this._children[i]) || ASTCtor.class.isInstance(this._children[i]) || (ASTStaticField.class.isInstance(this._children[i]) && this._parent == null)) {
                        context.put("_noRoot", "true");
                        result = value;
                    } else {
                        result = result + value;
                    }
                    context.put("_currentChain", result);
                }
            }
            if (_lastType != null) {
                this._getterClass = _lastType.getGetterClass();
                this._setterClass = _lastType.getSetterClass();
            }
            if (ordered) {
                this._coreExpression = result;
            }
            context.put("_currentChain", prevChain);
            return result;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        String prevChain = (String) context.get("_currentChain");
        String prevChild = (String) context.get("_lastChild");
        if (prevChain != null) {
            throw new UnsupportedCompilationException("Can't compile nested chain expressions.");
        }
        if (target != null) {
            context.setCurrentObject(target);
            context.setCurrentType(target.getClass());
        }
        String result = "";
        NodeType _lastType = null;
        boolean constructor = false;
        try {
            if (this._children != null && this._children.length > 0) {
                if (ASTConst.class.isInstance(this._children[0])) {
                    throw new UnsupportedCompilationException("Can't modify constant values.");
                }
                for (int i = 0; i < this._children.length; i++) {
                    if (i == this._children.length - 1) {
                        context.put("_lastChild", "true");
                    }
                    String value = this._children[i].toSetSourceString(context, context.getCurrentObject());
                    if (ASTCtor.class.isInstance(this._children[i])) {
                        constructor = true;
                    }
                    if (NodeType.class.isInstance(this._children[i]) && ((NodeType) this._children[i]).getGetterClass() != null) {
                        _lastType = (NodeType) this._children[i];
                    }
                    if (!ASTVarRef.class.isInstance(this._children[i]) && !constructor && ((!OrderedReturn.class.isInstance(this._children[i]) || ((OrderedReturn) this._children[i]).getLastExpression() == null) && (this._parent == null || !ASTSequence.class.isInstance(this._parent)))) {
                        value = OgnlRuntime.getCompiler().castExpression(context, this._children[i], value);
                    }
                    if (ASTOr.class.isInstance(this._children[i]) || ASTAnd.class.isInstance(this._children[i]) || ASTCtor.class.isInstance(this._children[i]) || ASTStaticField.class.isInstance(this._children[i])) {
                        context.put("_noRoot", "true");
                        result = value;
                    } else {
                        result = result + value;
                    }
                    context.put("_currentChain", result);
                }
            }
            context.put("_lastChild", prevChild);
            context.put("_currentChain", prevChain);
            if (_lastType != null) {
                this._setterClass = _lastType.getSetterClass();
            }
            return result;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isChain(OgnlContext context) {
        return true;
    }
}
