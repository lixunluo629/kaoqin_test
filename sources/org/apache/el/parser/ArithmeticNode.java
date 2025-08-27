package org.apache.el.parser;

import javax.el.ELException;
import org.apache.el.lang.EvaluationContext;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/parser/ArithmeticNode.class */
public abstract class ArithmeticNode extends SimpleNode {
    public ArithmeticNode(int i) {
        super(i);
    }

    @Override // org.apache.el.parser.SimpleNode, org.apache.el.parser.Node
    public Class<?> getType(EvaluationContext ctx) throws ELException {
        return Number.class;
    }
}
