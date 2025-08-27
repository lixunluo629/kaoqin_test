package org.apache.el.parser;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/parser/AstLambdaParameters.class */
public class AstLambdaParameters extends SimpleNode {
    public AstLambdaParameters(int id) {
        super(id);
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
        result.append(")->");
        return result.toString();
    }
}
