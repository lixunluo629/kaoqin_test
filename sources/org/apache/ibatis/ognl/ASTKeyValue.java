package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTKeyValue.class */
class ASTKeyValue extends SimpleNode {
    public ASTKeyValue(int id) {
        super(id);
    }

    public ASTKeyValue(OgnlParser p, int id) {
        super(p, id);
    }

    protected Node getKey() {
        return this._children[0];
    }

    protected Node getValue() {
        if (jjtGetNumChildren() > 1) {
            return this._children[1];
        }
        return null;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return null;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return getKey() + " -> " + getValue();
    }
}
