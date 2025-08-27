package org.apache.ibatis.scripting.xmltags;

import java.util.List;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/ChooseSqlNode.class */
public class ChooseSqlNode implements SqlNode {
    private final SqlNode defaultSqlNode;
    private final List<SqlNode> ifSqlNodes;

    public ChooseSqlNode(List<SqlNode> ifSqlNodes, SqlNode defaultSqlNode) {
        this.ifSqlNodes = ifSqlNodes;
        this.defaultSqlNode = defaultSqlNode;
    }

    @Override // org.apache.ibatis.scripting.xmltags.SqlNode
    public boolean apply(DynamicContext context) {
        for (SqlNode sqlNode : this.ifSqlNodes) {
            if (sqlNode.apply(context)) {
                return true;
            }
        }
        if (this.defaultSqlNode != null) {
            this.defaultSqlNode.apply(context);
            return true;
        }
        return false;
    }
}
