package ch.qos.logback.core.pattern.parser;

import ch.qos.logback.core.pattern.FormatInfo;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/pattern/parser/FormattingNode.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/pattern/parser/FormattingNode.class */
public class FormattingNode extends Node {
    FormatInfo formatInfo;

    FormattingNode(int type) {
        super(type);
    }

    FormattingNode(int type, Object value) {
        super(type, value);
    }

    public FormatInfo getFormatInfo() {
        return this.formatInfo;
    }

    public void setFormatInfo(FormatInfo formatInfo) {
        this.formatInfo = formatInfo;
    }

    @Override // ch.qos.logback.core.pattern.parser.Node
    public boolean equals(Object o) {
        if (!super.equals(o) || !(o instanceof FormattingNode)) {
            return false;
        }
        FormattingNode r = (FormattingNode) o;
        return this.formatInfo != null ? this.formatInfo.equals(r.formatInfo) : r.formatInfo == null;
    }

    @Override // ch.qos.logback.core.pattern.parser.Node
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.formatInfo != null ? this.formatInfo.hashCode() : 0);
    }
}
