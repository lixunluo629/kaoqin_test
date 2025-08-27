package org.apache.ibatis.scripting.xmltags;

import java.util.Arrays;
import java.util.List;
import org.apache.ibatis.session.Configuration;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/xmltags/SetSqlNode.class */
public class SetSqlNode extends TrimSqlNode {
    private static List<String> suffixList = Arrays.asList(",");

    public SetSqlNode(Configuration configuration, SqlNode contents) {
        super(configuration, contents, "SET", (List<String>) null, (String) null, suffixList);
    }
}
