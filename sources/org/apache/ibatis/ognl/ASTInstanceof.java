package org.apache.ibatis.ognl;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTInstanceof.class */
public class ASTInstanceof extends SimpleNode implements NodeType {
    private String targetType;

    public ASTInstanceof(int id) {
        super(id);
    }

    public ASTInstanceof(OgnlParser p, int id) {
        super(p, id);
    }

    void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object value = this._children[0].getValue(context, source);
        return OgnlRuntime.isInstance(context, value, this.targetType) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        return this._children[0] + " instanceof " + this.targetType;
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
        String ret;
        try {
            if (ASTConst.class.isInstance(this._children[0])) {
                ret = ((Boolean) getValueBody(context, target)).toString();
            } else {
                ret = this._children[0].toGetSourceString(context, target) + " instanceof " + this.targetType;
            }
            context.setCurrentType(Boolean.TYPE);
            return ret;
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        return toGetSourceString(context, target);
    }
}
