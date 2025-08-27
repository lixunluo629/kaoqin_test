package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ExpressionNode.class */
public abstract class ExpressionNode extends SimpleNode {
    public ExpressionNode(int i) {
        super(i);
    }

    public ExpressionNode(OgnlParser p, int i) {
        super(p, i);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isNodeConstant(OgnlContext context) throws OgnlException {
        return false;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isConstant(OgnlContext context) throws OgnlException {
        boolean zIsConstant;
        boolean result = isNodeConstant(context);
        if (this._children != null && this._children.length > 0) {
            result = true;
            for (int i = 0; result && i < this._children.length; i++) {
                if (this._children[i] instanceof SimpleNode) {
                    zIsConstant = ((SimpleNode) this._children[i]).isConstant(context);
                } else {
                    zIsConstant = false;
                }
                result = zIsConstant;
            }
        }
        return result;
    }

    public String getExpressionOperator(int index) {
        throw new RuntimeException("unknown operator for " + OgnlParserTreeConstants.jjtNodeName[this._id]);
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        String result = this._parent == null ? "" : "(";
        if (this._children != null && this._children.length > 0) {
            for (int i = 0; i < this._children.length; i++) {
                if (i > 0) {
                    result = result + SymbolConstants.SPACE_SYMBOL + getExpressionOperator(i) + SymbolConstants.SPACE_SYMBOL;
                }
                result = result + this._children[i].toString();
            }
        }
        if (this._parent != null) {
            result = result + ")";
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        String result = (this._parent == null || NumericExpression.class.isAssignableFrom(this._parent.getClass())) ? "" : "(";
        if (this._children != null && this._children.length > 0) {
            for (int i = 0; i < this._children.length; i++) {
                if (i > 0) {
                    result = result + SymbolConstants.SPACE_SYMBOL + getExpressionOperator(i) + SymbolConstants.SPACE_SYMBOL;
                }
                String value = this._children[i].toGetSourceString(context, target);
                if ((ASTProperty.class.isInstance(this._children[i]) || ASTMethod.class.isInstance(this._children[i]) || ASTSequence.class.isInstance(this._children[i]) || ASTChain.class.isInstance(this._children[i])) && value != null && value.trim().length() > 0) {
                    String pre = null;
                    if (ASTMethod.class.isInstance(this._children[i])) {
                        pre = (String) context.get("_currentChain");
                    }
                    if (pre == null) {
                        pre = "";
                    }
                    String cast = (String) context.remove(ExpressionCompiler.PRE_CAST);
                    if (cast == null) {
                        cast = "";
                    }
                    value = cast + ExpressionCompiler.getRootExpression(this._children[i], context.getRoot(), context) + pre + value;
                }
                result = result + value;
            }
        }
        if (this._parent != null && !NumericExpression.class.isAssignableFrom(this._parent.getClass())) {
            result = result + ")";
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        String result = this._parent == null ? "" : "(";
        if (this._children != null && this._children.length > 0) {
            for (int i = 0; i < this._children.length; i++) {
                if (i > 0) {
                    result = result + SymbolConstants.SPACE_SYMBOL + getExpressionOperator(i) + SymbolConstants.SPACE_SYMBOL;
                }
                result = result + this._children[i].toSetSourceString(context, target);
            }
        }
        if (this._parent != null) {
            result = result + ")";
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isOperation(OgnlContext context) throws OgnlException {
        return true;
    }
}
