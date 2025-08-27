package org.apache.ibatis.ognl;

import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTNotIn.class */
class ASTNotIn extends SimpleNode implements NodeType {
    public ASTNotIn(int id) {
        super(id);
    }

    public ASTNotIn(OgnlParser p, int id) {
        super(p, id);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object v1 = this._children[0].getValue(context, source);
        Object v2 = this._children[1].getValue(context, source);
        return OgnlOps.in(v1, v2) ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return this._children[0] + " not in " + this._children[1];
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getGetterClass() {
        return Boolean.TYPE;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getSetterClass() {
        return null;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        try {
            String result = "(! ognl.OgnlOps.in( ($w) " + OgnlRuntime.getChildSource(context, target, this._children[0]) + ", ($w) " + OgnlRuntime.getChildSource(context, target, this._children[1]);
            String result2 = result + ") )";
            context.setCurrentType(Boolean.TYPE);
            return result2;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new UnsupportedCompilationException("evaluation resulted in null expression.");
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
