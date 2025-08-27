package org.apache.ibatis.scripting.xmltags;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/StaticTextSqlNode.class */
public class StaticTextSqlNode implements SqlNode {
    private final String text;

    public StaticTextSqlNode(String text) {
        this.text = text;
    }

    @Override // org.apache.ibatis.scripting.xmltags.SqlNode
    public boolean apply(DynamicContext context) {
        context.appendSql(this.text);
        return true;
    }
}
