package org.apache.el.parser;

import java.util.ArrayList;
import org.apache.el.lang.EvaluationContext;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/parser/AstMethodParameters.class */
public final class AstMethodParameters extends SimpleNode {
    public AstMethodParameters(int id) {
        super(id);
    }

    public Object[] getParameters(EvaluationContext ctx) {
        ArrayList<Object> params = new ArrayList<>();
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            params.add(jjtGetChild(i).getValue(ctx));
        }
        return params.toArray(new Object[params.size()]);
    }

    @Override // org.apache.el.parser.SimpleNode
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('(');
        if (this.children != null) {
            Node[] arr$ = this.children;
            for (Node n : arr$) {
                result.append(n.toString());
                result.append(',');
            }
        }
        result.append(')');
        return result.toString();
    }
}
