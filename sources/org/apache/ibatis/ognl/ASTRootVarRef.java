package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.ExpressionCompiler;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTRootVarRef.class */
public class ASTRootVarRef extends ASTVarRef {
    public ASTRootVarRef(int id) {
        super(id);
    }

    public ASTRootVarRef(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return context.getRoot();
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode
    protected void setValueBody(OgnlContext context, Object target, Object value) throws OgnlException {
        context.setRoot(value);
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return "#root";
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        if (target != null) {
            this._getterClass = target.getClass();
        }
        if (this._getterClass != null) {
            context.setCurrentType(this._getterClass);
        }
        if (this._parent == null) {
            return "";
        }
        if (this._getterClass != null && this._getterClass.isArray()) {
            return "";
        }
        return ExpressionCompiler.getRootExpression(this, target, context);
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        if (this._parent == null) {
            return "";
        }
        if (this._getterClass != null && this._getterClass.isArray()) {
            return "";
        }
        return "$3";
    }
}
