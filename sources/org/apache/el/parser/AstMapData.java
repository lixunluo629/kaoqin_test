package org.apache.el.parser;

import java.util.HashMap;
import java.util.Map;
import javax.el.ELException;
import org.apache.el.lang.EvaluationContext;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/parser/AstMapData.class */
public class AstMapData extends SimpleNode {
    public AstMapData(int id) {
        super(id);
    }

    @Override // org.apache.el.parser.SimpleNode, org.apache.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Map<Object, Object> result = new HashMap<>();
        if (this.children != null) {
            Node[] arr$ = this.children;
            for (Node child : arr$) {
                AstMapEntry mapEntry = (AstMapEntry) child;
                Object key = mapEntry.children[0].getValue(ctx);
                Object value = mapEntry.children[1].getValue(ctx);
                result.put(key, value);
            }
        }
        return result;
    }

    @Override // org.apache.el.parser.SimpleNode, org.apache.el.parser.Node
    public Class<?> getType(EvaluationContext ctx) throws ELException {
        return Map.class;
    }
}
