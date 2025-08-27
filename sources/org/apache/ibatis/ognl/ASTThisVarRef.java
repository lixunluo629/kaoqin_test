package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTThisVarRef.class */
public class ASTThisVarRef extends ASTVarRef {
    public ASTThisVarRef(int id) {
        super(id);
    }

    public ASTThisVarRef(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return context.getCurrentObject();
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode
    protected void setValueBody(OgnlContext context, Object target, Object value) throws OgnlException {
        context.setCurrentObject(value);
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return "#this";
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Unable to compile this references.");
    }

    @Override // org.apache.ibatis.ognl.ASTVarRef, org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        throw new UnsupportedCompilationException("Unable to compile this references.");
    }
}
