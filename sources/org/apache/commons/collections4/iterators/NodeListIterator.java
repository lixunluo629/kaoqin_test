package org.apache.commons.collections4.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/iterators/NodeListIterator.class */
public class NodeListIterator implements Iterator<Node> {
    private final NodeList nodeList;
    private int index = 0;

    public NodeListIterator(Node node) {
        if (node == null) {
            throw new NullPointerException("Node must not be null.");
        }
        this.nodeList = node.getChildNodes();
    }

    public NodeListIterator(NodeList nodeList) {
        if (nodeList == null) {
            throw new NullPointerException("NodeList must not be null.");
        }
        this.nodeList = nodeList;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.nodeList != null && this.index < this.nodeList.getLength();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public Node next() {
        if (this.nodeList != null && this.index < this.nodeList.getLength()) {
            NodeList nodeList = this.nodeList;
            int i = this.index;
            this.index = i + 1;
            return nodeList.item(i);
        }
        throw new NoSuchElementException("underlying nodeList has no more elements");
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("remove() method not supported for a NodeListIterator.");
    }
}
