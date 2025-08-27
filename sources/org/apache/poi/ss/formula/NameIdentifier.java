package org.apache.poi.ss.formula;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/formula/NameIdentifier.class */
public class NameIdentifier {
    private final String _name;
    private final boolean _isQuoted;

    public NameIdentifier(String name, boolean isQuoted) {
        this._name = name;
        this._isQuoted = isQuoted;
    }

    public String getName() {
        return this._name;
    }

    public boolean isQuoted() {
        return this._isQuoted;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName());
        sb.append(" [");
        if (this._isQuoted) {
            sb.append("'").append(this._name).append("'");
        } else {
            sb.append(this._name);
        }
        sb.append("]");
        return sb.toString();
    }
}
