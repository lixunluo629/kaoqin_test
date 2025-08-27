package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.error.Mark;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/nodes/CollectionNode.class */
public abstract class CollectionNode extends Node {
    private Boolean flowStyle;

    public CollectionNode(Tag tag, Mark startMark, Mark endMark, Boolean flowStyle) {
        super(tag, startMark, endMark);
        this.flowStyle = flowStyle;
    }

    public Boolean getFlowStyle() {
        return this.flowStyle;
    }

    public void setFlowStyle(Boolean flowStyle) {
        this.flowStyle = flowStyle;
    }

    public void setEndMark(Mark endMark) {
        this.endMark = endMark;
    }
}
