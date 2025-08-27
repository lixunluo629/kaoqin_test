package org.apache.el.parser;

import javax.el.ELException;
import org.apache.el.lang.EvaluationContext;

/* loaded from: tomcat-embed-el-8.5.43.jar:org/apache/el/parser/BooleanNode.class */
public abstract class BooleanNode extends SimpleNode {
    public BooleanNode(int i) {
        super(i);
    }

    @Override // org.apache.el.parser.SimpleNode, org.apache.el.parser.Node
    public Class<?> getType(EvaluationContext ctx) throws ELException {
        return Boolean.class;
    }
}
