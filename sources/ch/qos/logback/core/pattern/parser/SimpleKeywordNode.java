package ch.qos.logback.core.pattern.parser;

import java.util.List;

/* JADX WARN: Classes with same name are omitted:
  logback-core-1.2.3.jar:ch/qos/logback/core/pattern/parser/SimpleKeywordNode.class
 */
/* loaded from: logback-core-1.2.3.jar.bak:ch/qos/logback/core/pattern/parser/SimpleKeywordNode.class */
public class SimpleKeywordNode extends FormattingNode {
    List<String> optionList;

    SimpleKeywordNode(Object value) {
        super(1, value);
    }

    protected SimpleKeywordNode(int type, Object value) {
        super(type, value);
    }

    public List<String> getOptions() {
        return this.optionList;
    }

    public void setOptions(List<String> optionList) {
        this.optionList = optionList;
    }

    @Override // ch.qos.logback.core.pattern.parser.FormattingNode, ch.qos.logback.core.pattern.parser.Node
    public boolean equals(Object o) {
        if (!super.equals(o) || !(o instanceof SimpleKeywordNode)) {
            return false;
        }
        SimpleKeywordNode r = (SimpleKeywordNode) o;
        return this.optionList != null ? this.optionList.equals(r.optionList) : r.optionList == null;
    }

    @Override // ch.qos.logback.core.pattern.parser.FormattingNode, ch.qos.logback.core.pattern.parser.Node
    public int hashCode() {
        return super.hashCode();
    }

    @Override // ch.qos.logback.core.pattern.parser.Node
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if (this.optionList == null) {
            buf.append("KeyWord(" + this.value + "," + this.formatInfo + ")");
        } else {
            buf.append("KeyWord(" + this.value + ", " + this.formatInfo + "," + this.optionList + ")");
        }
        buf.append(printNext());
        return buf.toString();
    }
}
