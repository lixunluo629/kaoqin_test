package com.fasterxml.jackson.core;

import java.io.IOException;

/* loaded from: jackson-core-2.11.2.jar:com/fasterxml/jackson/core/TreeCodec.class */
public abstract class TreeCodec {
    public abstract <T extends TreeNode> T readTree(JsonParser jsonParser) throws IOException;

    public abstract void writeTree(JsonGenerator jsonGenerator, TreeNode treeNode) throws IOException;

    public abstract TreeNode createArrayNode();

    public abstract TreeNode createObjectNode();

    public abstract JsonParser treeAsTokens(TreeNode treeNode);

    public TreeNode missingNode() {
        return null;
    }

    public TreeNode nullNode() {
        return null;
    }
}
