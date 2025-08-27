package org.apache.ibatis.ognl;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ASTConst.class */
public class ASTConst extends SimpleNode implements NodeType {
    private Object value;
    private Class _getterClass;

    public ASTConst(int id) {
        super(id);
    }

    public ASTConst(OgnlParser p, int id) {
        super(p, id);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        return this.value;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public boolean isNodeConstant(OgnlContext context) throws OgnlException {
        return true;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getGetterClass() {
        if (this._getterClass == null) {
            return null;
        }
        return this._getterClass;
    }

    @Override // org.apache.ibatis.ognl.NodeType
    public Class getSetterClass() {
        return null;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode
    public String toString() {
        String result;
        if (this.value == null) {
            result = "null";
        } else if (this.value instanceof String) {
            result = '\"' + OgnlOps.getEscapeString(this.value.toString()) + '\"';
        } else if (this.value instanceof Character) {
            result = '\'' + OgnlOps.getEscapedChar(((Character) this.value).charValue()) + '\'';
        } else {
            result = this.value.toString();
            if (this.value instanceof Long) {
                result = result + StandardRoles.L;
            } else if (this.value instanceof BigDecimal) {
                result = result + "B";
            } else if (this.value instanceof BigInteger) {
                result = result + StandardRoles.H;
            } else if (this.value instanceof Node) {
                result = ":[ " + result + " ]";
            }
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toGetSourceString(OgnlContext context, Object target) {
        Object retval;
        if (this.value == null && this._parent != null && ExpressionNode.class.isInstance(this._parent)) {
            context.setCurrentType(null);
            return "null";
        }
        if (this.value == null) {
            context.setCurrentType(null);
            return "";
        }
        this._getterClass = this.value.getClass();
        Object obj = this.value;
        if (this._parent != null && ASTProperty.class.isInstance(this._parent)) {
            context.setCurrentObject(this.value);
            return this.value.toString();
        }
        if (this.value != null && Number.class.isAssignableFrom(this.value.getClass())) {
            context.setCurrentType(OgnlRuntime.getPrimitiveWrapperClass(this.value.getClass()));
            context.setCurrentObject(this.value);
            return this.value.toString();
        }
        if ((this._parent == null || this.value == null || !NumericExpression.class.isAssignableFrom(this._parent.getClass())) && String.class.isAssignableFrom(this.value.getClass())) {
            context.setCurrentType(String.class);
            Object retval2 = '\"' + OgnlOps.getEscapeString(this.value.toString()) + '\"';
            context.setCurrentObject(retval2.toString());
            return retval2.toString();
        }
        if (Character.class.isInstance(this.value)) {
            Character val = (Character) this.value;
            context.setCurrentType(Character.class);
            if (Character.isLetterOrDigit(val.charValue())) {
                retval = "'" + ((Character) this.value).charValue() + "'";
            } else {
                retval = "'" + OgnlOps.getEscapedChar(((Character) this.value).charValue()) + "'";
            }
            context.setCurrentObject(retval);
            return retval.toString();
        }
        if (Boolean.class.isAssignableFrom(this.value.getClass())) {
            this._getterClass = Boolean.TYPE;
            context.setCurrentType(Boolean.TYPE);
            context.setCurrentObject(this.value);
            return this.value.toString();
        }
        return this.value.toString();
    }

    @Override // org.apache.ibatis.ognl.SimpleNode, org.apache.ibatis.ognl.JavaSource
    public String toSetSourceString(OgnlContext context, Object target) {
        if (this._parent == null) {
            throw new UnsupportedCompilationException("Can't modify constant values.");
        }
        return toGetSourceString(context, target);
    }
}
