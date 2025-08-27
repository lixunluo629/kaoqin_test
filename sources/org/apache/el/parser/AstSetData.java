package org.apache.el.parser;

import java.util.HashSet;
import java.util.Set;
import javax.el.ELException;
import org.apache.el.lang.EvaluationContext;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/parser/AstSetData.class */
public class AstSetData extends SimpleNode {
    public AstSetData(int id) {
        super(id);
    }

    @Override // org.apache.el.parser.SimpleNode, org.apache.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Set<Object> result = new HashSet<>();
        if (this.children != null) {
            Node[] arr$ = this.children;
            for (Node child : arr$) {
                result.add(child.getValue(ctx));
            }
        }
        return result;
    }

    @Override // org.apache.el.parser.SimpleNode, org.apache.el.parser.Node
    public Class<?> getType(EvaluationContext ctx) throws ELException {
        return Set.class;
    }
}
