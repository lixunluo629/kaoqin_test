package org.apache.ibatis.scripting.xmltags;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/VarDeclSqlNode.class */
public class VarDeclSqlNode implements SqlNode {
    private final String name;
    private final String expression;

    public VarDeclSqlNode(String var, String exp) {
        this.name = var;
        this.expression = exp;
    }

    @Override // org.apache.ibatis.scripting.xmltags.SqlNode
    public boolean apply(DynamicContext context) {
        Object value = OgnlCache.getValue(this.expression, context.getBindings());
        context.bind(this.name, value);
        return true;
    }
}
