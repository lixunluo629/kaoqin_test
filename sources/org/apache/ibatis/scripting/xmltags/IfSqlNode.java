package org.apache.ibatis.scripting.xmltags;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/IfSqlNode.class */
public class IfSqlNode implements SqlNode {
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private final String test;
    private final SqlNode contents;

    public IfSqlNode(SqlNode contents, String test) {
        this.test = test;
        this.contents = contents;
    }

    @Override // org.apache.ibatis.scripting.xmltags.SqlNode
    public boolean apply(DynamicContext context) {
        if (this.evaluator.evaluateBoolean(this.test, context.getBindings())) {
            this.contents.apply(context);
            return true;
        }
        return false;
    }
}
