package org.terracotta.offheapstore.util;

import java.lang.Comparable;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Stack;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/AATreeSet.class */
public class AATreeSet<T extends Comparable<? super T>> extends AbstractSet<T> implements SortedSet<T> {
    private boolean mutated;
    private T removed;
    private Node<T> root = TerminalNode.terminal();
    private int size = 0;
    private Node<T> item = TerminalNode.terminal();
    private Node<T> heir = TerminalNode.terminal();

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/AATreeSet$Node.class */
    public interface Node<E extends Comparable<? super E>> {
        void setLeft(Node<E> node);

        void setRight(Node<E> node);

        Node<E> getLeft();

        Node<E> getRight();

        int getLevel();

        void setLevel(int i);

        int decrementLevel();

        int incrementLevel();

        void swapPayload(Node<E> node);

        E getPayload();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean add(T o) {
        try {
            this.root = insert(this.root, o);
            if (this.mutated) {
                this.size++;
            }
            boolean z = this.mutated;
            this.mutated = false;
            return z;
        } catch (Throwable th) {
            this.mutated = false;
            throw th;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o) {
        try {
            this.root = remove(this.root, (Comparable) o);
            if (this.mutated) {
                this.size--;
            }
            boolean z = this.mutated;
            this.heir = TerminalNode.terminal();
            this.item = TerminalNode.terminal();
            this.mutated = false;
            this.removed = null;
            return z;
        } catch (Throwable th) {
            this.heir = TerminalNode.terminal();
            this.item = TerminalNode.terminal();
            this.mutated = false;
            this.removed = null;
            throw th;
        }
    }

    public T removeAndReturn(Object o) {
        try {
            this.root = remove(this.root, (Comparable) o);
            if (this.mutated) {
                this.size--;
            }
            T t = this.removed;
            this.heir = TerminalNode.terminal();
            this.item = TerminalNode.terminal();
            this.mutated = false;
            this.removed = null;
            return t;
        } catch (Throwable th) {
            this.heir = TerminalNode.terminal();
            this.item = TerminalNode.terminal();
            this.mutated = false;
            this.removed = null;
            throw th;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public void clear() {
        this.root = TerminalNode.terminal();
        this.size = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<T> iterator() {
        return new TreeIterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.root == TerminalNode.terminal();
    }

    @Override // java.util.SortedSet
    public Comparator<? super T> comparator() {
        return null;
    }

    @Override // java.util.SortedSet
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new SubSet(fromElement, toElement);
    }

    @Override // java.util.SortedSet
    public SortedSet<T> headSet(T toElement) {
        return new SubSet(null, toElement);
    }

    @Override // java.util.SortedSet
    public SortedSet<T> tailSet(T fromElement) {
        return new SubSet(fromElement, null);
    }

    @Override // java.util.SortedSet
    public T first() {
        Node<T> left = this.root;
        while (true) {
            Node<T> node = left;
            if (node.getLeft() != TerminalNode.terminal()) {
                left = node.getLeft();
            } else {
                return (T) node.getPayload();
            }
        }
    }

    @Override // java.util.SortedSet
    public T last() {
        Node<T> right = this.root;
        while (true) {
            Node<T> node = right;
            if (node.getRight() != TerminalNode.terminal()) {
                right = node.getRight();
            } else {
                return (T) node.getPayload();
            }
        }
    }

    public T find(Object obj) {
        return (T) find(this.root, (Comparable) obj).getPayload();
    }

    protected final Node<T> getRoot() {
        return this.root;
    }

    private Node<T> find(Node<T> top, T probe) {
        if (top == TerminalNode.terminal()) {
            return top;
        }
        int direction = top.getPayload().compareTo(probe);
        if (direction > 0) {
            return find(top.getLeft(), probe);
        }
        if (direction < 0) {
            return find(top.getRight(), probe);
        }
        return top;
    }

    private Node<T> insert(Node<T> top, T data) {
        if (top == TerminalNode.terminal()) {
            this.mutated = true;
            return createNode(data);
        }
        int direction = top.getPayload().compareTo(data);
        if (direction > 0) {
            top.setLeft(insert(top.getLeft(), data));
        } else if (direction < 0) {
            top.setRight(insert(top.getRight(), data));
        } else {
            return top;
        }
        return split(skew(top));
    }

    private Node<T> createNode(T data) {
        if (data instanceof Node) {
            return (Node) data;
        }
        return new TreeNode(data);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v40, types: [org.terracotta.offheapstore.util.AATreeSet$Node] */
    private Node<T> remove(Node<T> node, T t) {
        if (node != TerminalNode.terminal()) {
            int iCompareTo = node.getPayload().compareTo(t);
            this.heir = node;
            if (iCompareTo > 0) {
                node.setLeft(remove(node.getLeft(), t));
            } else {
                this.item = node;
                node.setRight(remove(node.getRight(), t));
            }
            if (node == this.heir) {
                if (this.item != TerminalNode.terminal() && this.item.getPayload().compareTo(t) == 0) {
                    this.mutated = true;
                    this.item.swapPayload(node);
                    this.removed = (T) node.getPayload();
                    node = node.getRight();
                }
            } else if (node.getLeft().getLevel() < node.getLevel() - 1 || node.getRight().getLevel() < node.getLevel() - 1) {
                if (node.getRight().getLevel() > node.decrementLevel()) {
                    node.getRight().setLevel(node.getLevel());
                }
                Node nodeSkew = skew(node);
                nodeSkew.setRight(skew(nodeSkew.getRight()));
                nodeSkew.getRight().setRight(skew(nodeSkew.getRight().getRight()));
                node = split(nodeSkew);
                node.setRight(split(node.getRight()));
            }
        }
        return node;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7, types: [org.terracotta.offheapstore.util.AATreeSet$Node] */
    private static <T extends Comparable<? super T>> Node<T> skew(Node<T> top) {
        if (top.getLeft().getLevel() == top.getLevel() && top.getLevel() != 0) {
            ?? left = top.getLeft();
            top.setLeft(left.getRight());
            left.setRight(top);
            top = left;
        }
        return top;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v8, types: [org.terracotta.offheapstore.util.AATreeSet$Node] */
    private static <T extends Comparable<? super T>> Node<T> split(Node<T> top) {
        if (top.getRight().getRight().getLevel() == top.getLevel() && top.getLevel() != 0) {
            ?? right = top.getRight();
            top.setRight(right.getLeft());
            right.setLeft(top);
            top = right;
            top.incrementLevel();
        }
        return top;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/AATreeSet$AbstractTreeNode.class */
    public static abstract class AbstractTreeNode<E extends Comparable<? super E>> implements Node<E> {
        private Node<E> left;
        private Node<E> right;
        private int level;

        public AbstractTreeNode() {
            this(1);
        }

        private AbstractTreeNode(int level) {
            this.left = TerminalNode.terminal();
            this.right = TerminalNode.terminal();
            this.level = level;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public void setLeft(Node<E> node) {
            this.left = node;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public void setRight(Node<E> node) {
            this.right = node;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public Node<E> getLeft() {
            return this.left;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public Node<E> getRight() {
            return this.right;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public int getLevel() {
            return this.level;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public void setLevel(int value) {
            this.level = value;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public int decrementLevel() {
            int i = this.level - 1;
            this.level = i;
            return i;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public int incrementLevel() {
            int i = this.level + 1;
            this.level = i;
            return i;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/AATreeSet$TreeNode.class */
    private static final class TreeNode<E extends Comparable<? super E>> extends AbstractTreeNode<E> {
        private E payload;

        public TreeNode(E payload) {
            this.payload = payload;
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public void swapPayload(Node<E> node) {
            if (node instanceof TreeNode) {
                TreeNode<E> treeNode = (TreeNode) node;
                E temp = treeNode.payload;
                treeNode.payload = this.payload;
                this.payload = temp;
                return;
            }
            throw new IllegalArgumentException();
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public E getPayload() {
            return this.payload;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/AATreeSet$TerminalNode.class */
    private static final class TerminalNode extends AbstractTreeNode {
        private static final Node<?> TERMINAL = new TerminalNode();

        public static <T extends Comparable<? super T>> Node<T> terminal() {
            return (Node<T>) TERMINAL;
        }

        private TerminalNode() {
            super(0);
            super.setLeft(this);
            super.setRight(this);
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.AbstractTreeNode, org.terracotta.offheapstore.util.AATreeSet.Node
        public void setLeft(Node right) {
            if (right != TERMINAL) {
                throw new AssertionError();
            }
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.AbstractTreeNode, org.terracotta.offheapstore.util.AATreeSet.Node
        public void setRight(Node left) {
            if (left != TERMINAL) {
                throw new AssertionError();
            }
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.AbstractTreeNode, org.terracotta.offheapstore.util.AATreeSet.Node
        public void setLevel(int value) {
            throw new AssertionError();
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.AbstractTreeNode, org.terracotta.offheapstore.util.AATreeSet.Node
        public int decrementLevel() {
            throw new AssertionError();
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.AbstractTreeNode, org.terracotta.offheapstore.util.AATreeSet.Node
        public int incrementLevel() {
            throw new AssertionError();
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public void swapPayload(Node payload) {
            throw new AssertionError();
        }

        @Override // org.terracotta.offheapstore.util.AATreeSet.Node
        public Comparable getPayload() {
            return null;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/AATreeSet$SubSet.class */
    class SubSet extends AbstractSet<T> implements SortedSet<T> {
        private final T start;
        private final T end;

        SubSet(T start, T end) {
            this.start = start;
            this.end = end;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(T o) {
            if (inRange(o)) {
                return AATreeSet.this.add((AATreeSet) o);
            }
            throw new IllegalArgumentException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (inRange((Comparable) o)) {
                return remove(o);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<T> iterator() {
            if (this.end == null) {
                return new SubTreeIterator(this.start);
            }
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return !iterator().hasNext();
        }

        @Override // java.util.SortedSet
        public Comparator<? super T> comparator() {
            return null;
        }

        @Override // java.util.SortedSet
        public SortedSet<T> subSet(T fromElement, T toElement) {
            if (inRangeInclusive(fromElement) && inRangeInclusive(toElement)) {
                return new SubSet(fromElement, toElement);
            }
            throw new IllegalArgumentException();
        }

        @Override // java.util.SortedSet
        public SortedSet<T> headSet(T toElement) {
            if (inRangeInclusive(toElement)) {
                return new SubSet(this.start, toElement);
            }
            throw new IllegalArgumentException();
        }

        @Override // java.util.SortedSet
        public SortedSet<T> tailSet(T fromElement) {
            if (inRangeInclusive(fromElement)) {
                return new SubSet(fromElement, this.end);
            }
            throw new IllegalArgumentException();
        }

        @Override // java.util.SortedSet
        public T first() {
            if (this.start == null) {
                return (T) AATreeSet.this.first();
            }
            throw new UnsupportedOperationException();
        }

        @Override // java.util.SortedSet
        public T last() {
            if (this.end == null) {
                return (T) AATreeSet.this.last();
            }
            throw new UnsupportedOperationException();
        }

        private boolean inRange(T value) {
            return (this.start == null || this.start.compareTo(value) <= 0) && (this.end == null || this.end.compareTo(value) > 0);
        }

        private boolean inRangeInclusive(T value) {
            return (this.start == null || this.start.compareTo(value) <= 0) && (this.end == null || this.end.compareTo(value) >= 0);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/AATreeSet$TreeIterator.class */
    class TreeIterator implements Iterator<T> {
        private final Stack<Node<T>> path = new Stack<>();
        private Node<T> next;

        TreeIterator() {
            this.path.push(TerminalNode.terminal());
            Node<T> left = AATreeSet.this.root;
            while (true) {
                Node<T> leftMost = left;
                if (leftMost.getLeft() != TerminalNode.terminal()) {
                    this.path.push(leftMost);
                    left = leftMost.getLeft();
                } else {
                    this.next = leftMost;
                    return;
                }
            }
        }

        TreeIterator(T start) {
            this.path.push(TerminalNode.terminal());
            Node<T> left = AATreeSet.this.root;
            while (true) {
                Node<T> current = left;
                int direction = current.getPayload().compareTo(start);
                if (direction > 0) {
                    if (current.getLeft() == TerminalNode.terminal()) {
                        this.next = current;
                        return;
                    } else {
                        this.path.push(current);
                        left = current.getLeft();
                    }
                } else if (direction < 0) {
                    if (current.getRight() == TerminalNode.terminal()) {
                        this.next = this.path.pop();
                        return;
                    }
                    left = current.getRight();
                } else {
                    this.next = current;
                    return;
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != TerminalNode.terminal();
        }

        @Override // java.util.Iterator
        public T next() {
            Node<T> node = this.next;
            advance();
            return (T) node.getPayload();
        }

        private void advance() {
            Node right = this.next.getRight();
            if (right != TerminalNode.terminal()) {
                while (right.getLeft() != TerminalNode.terminal()) {
                    this.path.push(right);
                    right = right.getLeft();
                }
                this.next = right;
                return;
            }
            this.next = this.path.pop();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/AATreeSet$SubTreeIterator.class */
    class SubTreeIterator extends TreeIterator {
        public SubTreeIterator(T start) {
            super(start);
        }
    }
}
