package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/tokens/Token.class */
public abstract class Token {
    private final Mark startMark;
    private final Mark endMark;

    /* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/tokens/Token$ID.class */
    public enum ID {
        Alias,
        Anchor,
        BlockEnd,
        BlockEntry,
        BlockMappingStart,
        BlockSequenceStart,
        Directive,
        DocumentEnd,
        DocumentStart,
        FlowEntry,
        FlowMappingEnd,
        FlowMappingStart,
        FlowSequenceEnd,
        FlowSequenceStart,
        Key,
        Scalar,
        StreamEnd,
        StreamStart,
        Tag,
        Value,
        Whitespace,
        Comment,
        Error
    }

    public abstract ID getTokenId();

    public Token(Mark startMark, Mark endMark) {
        if (startMark == null || endMark == null) {
            throw new YAMLException("Token requires marks.");
        }
        this.startMark = startMark;
        this.endMark = endMark;
    }

    public String toString() {
        return "<" + getClass().getName() + "(" + getArguments() + ")>";
    }

    public Mark getStartMark() {
        return this.startMark;
    }

    public Mark getEndMark() {
        return this.endMark;
    }

    protected String getArguments() {
        return "";
    }

    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
