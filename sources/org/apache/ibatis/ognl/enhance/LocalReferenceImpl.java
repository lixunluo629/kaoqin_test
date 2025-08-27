package org.apache.ibatis.ognl.enhance;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/enhance/LocalReferenceImpl.class */
public class LocalReferenceImpl implements LocalReference {
    String _name;
    Class _type;
    String _expression;

    public LocalReferenceImpl(String name, String expression, Class type) {
        this._name = name;
        this._type = type;
        this._expression = expression;
    }

    @Override // org.apache.ibatis.ognl.enhance.LocalReference
    public String getName() {
        return this._name;
    }

    @Override // org.apache.ibatis.ognl.enhance.LocalReference
    public String getExpression() {
        return this._expression;
    }

    @Override // org.apache.ibatis.ognl.enhance.LocalReference
    public Class getType() {
        return this._type;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocalReferenceImpl that = (LocalReferenceImpl) o;
        if (this._expression != null) {
            if (!this._expression.equals(that._expression)) {
                return false;
            }
        } else if (that._expression != null) {
            return false;
        }
        if (this._name != null) {
            if (!this._name.equals(that._name)) {
                return false;
            }
        } else if (that._name != null) {
            return false;
        }
        return this._type != null ? this._type.equals(that._type) : that._type == null;
    }

    public int hashCode() {
        int result = this._name != null ? this._name.hashCode() : 0;
        return (31 * ((31 * result) + (this._type != null ? this._type.hashCode() : 0))) + (this._expression != null ? this._expression.hashCode() : 0);
    }

    public String toString() {
        return "LocalReferenceImpl[_name='" + this._name + "'\n, _type=" + this._type + "\n, _expression='" + this._expression + "'\n]";
    }
}
