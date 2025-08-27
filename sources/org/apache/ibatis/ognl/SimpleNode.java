package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintWriter;
import java.io.Serializable;
import org.apache.ibatis.ognl.enhance.ExpressionAccessor;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/SimpleNode.class */
public abstract class SimpleNode implements Node, Serializable {
    protected Node _parent;
    protected Node[] _children;
    protected int _id;
    protected OgnlParser _parser;
    private boolean _constantValueCalculated;
    private volatile boolean _hasConstantValue;
    private Object _constantValue;
    private ExpressionAccessor _accessor;

    protected abstract Object getValueBody(OgnlContext ognlContext, Object obj) throws OgnlException;

    public SimpleNode(int i) {
        this._id = i;
    }

    public SimpleNode(OgnlParser p, int i) {
        this(i);
        this._parser = p;
    }

    @Override // org.apache.ibatis.ognl.Node
    public void jjtOpen() {
    }

    @Override // org.apache.ibatis.ognl.Node
    public void jjtClose() {
    }

    @Override // org.apache.ibatis.ognl.Node
    public void jjtSetParent(Node n) {
        this._parent = n;
    }

    @Override // org.apache.ibatis.ognl.Node
    public Node jjtGetParent() {
        return this._parent;
    }

    @Override // org.apache.ibatis.ognl.Node
    public void jjtAddChild(Node n, int i) {
        if (this._children == null) {
            this._children = new Node[i + 1];
        } else if (i >= this._children.length) {
            Node[] c = new Node[i + 1];
            System.arraycopy(this._children, 0, c, 0, this._children.length);
            this._children = c;
        }
        this._children[i] = n;
    }

    @Override // org.apache.ibatis.ognl.Node
    public Node jjtGetChild(int i) {
        return this._children[i];
    }

    @Override // org.apache.ibatis.ognl.Node
    public int jjtGetNumChildren() {
        if (this._children == null) {
            return 0;
        }
        return this._children.length;
    }

    public String toString() {
        return OgnlParserTreeConstants.jjtNodeName[this._id];
    }

    public String toString(String prefix) {
        return prefix + OgnlParserTreeConstants.jjtNodeName[this._id] + SymbolConstants.SPACE_SYMBOL + toString();
    }

    @Override // org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        return toString();
    }

    @Override // org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        return toString();
    }

    public void dump(PrintWriter writer, String prefix) {
        writer.println(toString(prefix));
        if (this._children != null) {
            for (int i = 0; i < this._children.length; i++) {
                SimpleNode n = (SimpleNode) this._children[i];
                if (n != null) {
                    n.dump(writer, prefix + "  ");
                }
            }
        }
    }

    public int getIndexInParent() {
        int result = -1;
        if (this._parent != null) {
            int icount = this._parent.jjtGetNumChildren();
            int i = 0;
            while (true) {
                if (i >= icount) {
                    break;
                }
                if (this._parent.jjtGetChild(i) != this) {
                    i++;
                } else {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    public Node getNextSibling() {
        Node result = null;
        int i = getIndexInParent();
        if (i >= 0) {
            int icount = this._parent.jjtGetNumChildren();
            if (i < icount) {
                result = this._parent.jjtGetChild(i + 1);
            }
        }
        return result;
    }

    protected Object evaluateGetValueBody(OgnlContext context, Object source) throws OgnlException {
        context.setCurrentObject(source);
        context.setCurrentNode(this);
        if (!this._constantValueCalculated) {
            this._constantValueCalculated = true;
            boolean constant = isConstant(context);
            if (constant) {
                this._constantValue = getValueBody(context, source);
            }
            this._hasConstantValue = constant;
        }
        return this._hasConstantValue ? this._constantValue : getValueBody(context, source);
    }

    protected void evaluateSetValueBody(OgnlContext context, Object target, Object value) throws OgnlException {
        context.setCurrentObject(target);
        context.setCurrentNode(this);
        setValueBody(context, target, value);
    }

    @Override // org.apache.ibatis.ognl.Node
    public final Object getValue(OgnlContext context, Object source) throws OgnlException {
        Object result = null;
        if (context.getTraceEvaluations()) {
            EvaluationPool pool = OgnlRuntime.getEvaluationPool();
            Evaluation evaluation = pool.create(this, source);
            context.pushEvaluation(evaluation);
            try {
                try {
                    result = evaluateGetValueBody(context, source);
                    Evaluation eval = context.popEvaluation();
                    eval.setResult(result);
                    if (0 != 0) {
                        eval.setException(null);
                    }
                    if (0 == 0 && context.getRootEvaluation() == null && !context.getKeepLastEvaluation()) {
                        pool.recycleAll(eval);
                    }
                } catch (RuntimeException ex) {
                    throw ex;
                } catch (OgnlException ex2) {
                    throw ex2;
                }
            } catch (Throwable th) {
                Evaluation eval2 = context.popEvaluation();
                eval2.setResult(result);
                if (0 != 0) {
                    eval2.setException(null);
                }
                if (0 == 0 && context.getRootEvaluation() == null && !context.getKeepLastEvaluation()) {
                    pool.recycleAll(eval2);
                }
                throw th;
            }
        } else {
            result = evaluateGetValueBody(context, source);
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.Node
    public final void setValue(OgnlContext context, Object target, Object value) throws OgnlException {
        if (context.getTraceEvaluations()) {
            EvaluationPool pool = OgnlRuntime.getEvaluationPool();
            Evaluation evaluation = pool.create(this, target, true);
            context.pushEvaluation(evaluation);
            try {
                try {
                    evaluateSetValueBody(context, target, value);
                    Evaluation eval = context.popEvaluation();
                    if (0 != 0) {
                        eval.setException(null);
                    }
                    if (0 == 0 && context.getRootEvaluation() == null && !context.getKeepLastEvaluation()) {
                        pool.recycleAll(eval);
                        return;
                    }
                    return;
                } catch (RuntimeException ex) {
                    throw ex;
                } catch (OgnlException ex2) {
                    ex2.setEvaluation(evaluation);
                    throw ex2;
                }
            } catch (Throwable th) {
                Evaluation eval2 = context.popEvaluation();
                if (0 != 0) {
                    eval2.setException(null);
                }
                if (0 == 0 && context.getRootEvaluation() == null && !context.getKeepLastEvaluation()) {
                    pool.recycleAll(eval2);
                }
                throw th;
            }
        }
        evaluateSetValueBody(context, target, value);
    }

    protected void setValueBody(OgnlContext context, Object target, Object value) throws OgnlException {
        throw new InappropriateExpressionException(this);
    }

    public boolean isNodeConstant(OgnlContext context) throws OgnlException {
        return false;
    }

    public boolean isConstant(OgnlContext context) throws OgnlException {
        return isNodeConstant(context);
    }

    public boolean isNodeSimpleProperty(OgnlContext context) throws OgnlException {
        return false;
    }

    public boolean isSimpleProperty(OgnlContext context) throws OgnlException {
        return isNodeSimpleProperty(context);
    }

    public boolean isSimpleNavigationChain(OgnlContext context) throws OgnlException {
        return isSimpleProperty(context);
    }

    public boolean isEvalChain(OgnlContext context) throws OgnlException {
        if (this._children == null) {
            return false;
        }
        Node[] arr$ = this._children;
        for (Node child : arr$) {
            if ((child instanceof SimpleNode) && ((SimpleNode) child).isEvalChain(context)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSequence(OgnlContext context) throws OgnlException {
        if (this._children == null) {
            return false;
        }
        Node[] arr$ = this._children;
        for (Node child : arr$) {
            if ((child instanceof SimpleNode) && ((SimpleNode) child).isSequence(context)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOperation(OgnlContext context) throws OgnlException {
        if (this._children == null) {
            return false;
        }
        Node[] arr$ = this._children;
        for (Node child : arr$) {
            if ((child instanceof SimpleNode) && ((SimpleNode) child).isOperation(context)) {
                return true;
            }
        }
        return false;
    }

    public boolean isChain(OgnlContext context) throws OgnlException {
        if (this._children == null) {
            return false;
        }
        Node[] arr$ = this._children;
        for (Node child : arr$) {
            if ((child instanceof SimpleNode) && ((SimpleNode) child).isChain(context)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSimpleMethod(OgnlContext context) throws OgnlException {
        return false;
    }

    protected boolean lastChild(OgnlContext context) {
        return this._parent == null || context.get("_lastChild") != null;
    }

    protected void flattenTree() {
        boolean shouldFlatten = false;
        int newSize = 0;
        for (int i = 0; i < this._children.length; i++) {
            if (this._children[i].getClass() == getClass()) {
                shouldFlatten = true;
                newSize += this._children[i].jjtGetNumChildren();
            } else {
                newSize++;
            }
        }
        if (shouldFlatten) {
            Node[] newChildren = new Node[newSize];
            int j = 0;
            for (int i2 = 0; i2 < this._children.length; i2++) {
                Node c = this._children[i2];
                if (c.getClass() == getClass()) {
                    for (int k = 0; k < c.jjtGetNumChildren(); k++) {
                        int i3 = j;
                        j++;
                        newChildren[i3] = c.jjtGetChild(k);
                    }
                } else {
                    int i4 = j;
                    j++;
                    newChildren[i4] = c;
                }
            }
            if (j != newSize) {
                throw new Error("Assertion error: " + j + " != " + newSize);
            }
            this._children = newChildren;
        }
    }

    @Override // org.apache.ibatis.ognl.Node
    public ExpressionAccessor getAccessor() {
        return this._accessor;
    }

    @Override // org.apache.ibatis.ognl.Node
    public void setAccessor(ExpressionAccessor accessor) {
        this._accessor = accessor;
    }
}
