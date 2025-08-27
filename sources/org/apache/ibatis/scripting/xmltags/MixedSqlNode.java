package org.apache.ibatis.scripting.xmltags;

import java.util.List;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/MixedSqlNode.class */
public class MixedSqlNode implements SqlNode {
    private final List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override // org.apache.ibatis.scripting.xmltags.SqlNode
    public boolean apply(DynamicContext context) {
        for (SqlNode sqlNode : this.contents) {
            sqlNode.apply(context);
        }
        return true;
    }
}
