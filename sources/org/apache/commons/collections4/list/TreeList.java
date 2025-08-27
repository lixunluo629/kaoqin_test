package org.apache.commons.collections4.list;

import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.OrderedIterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/list/TreeList.class */
public class TreeList<E> extends AbstractList<E> {
    private AVLNode<E> root;
    private int size;

    public TreeList() {
    }

    public TreeList(Collection<? extends E> coll) {
        if (!coll.isEmpty()) {
            this.root = new AVLNode<>(coll);
            this.size = coll.size();
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int index) {
        checkInterval(index, 0, size() - 1);
        return this.root.get(index).getValue();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(int fromIndex) {
        checkInterval(fromIndex, 0, size());
        return new TreeListIterator(this, fromIndex);
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object object) {
        if (this.root == null) {
            return -1;
        }
        return this.root.indexOf(object, ((AVLNode) this.root).relativePosition);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object object) {
        return indexOf(object) >= 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] array = new Object[size()];
        if (this.root != null) {
            this.root.toArray(array, ((AVLNode) this.root).relativePosition);
        }
        return array;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, E obj) {
        this.modCount++;
        checkInterval(index, 0, size());
        if (this.root == null) {
            this.root = new AVLNode<>(index, obj, null, null);
        } else {
            this.root = this.root.insert(index, obj);
        }
        this.size++;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        this.modCount += c.size();
        AVLNode<E> cTree = new AVLNode<>(c);
        this.root = this.root == null ? cTree : this.root.addAll(cTree, this.size);
        this.size += c.size();
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i, E e) {
        checkInterval(i, 0, size() - 1);
        AVLNode<E> aVLNode = this.root.get(i);
        E e2 = (E) ((AVLNode) aVLNode).value;
        aVLNode.setValue(e);
        return e2;
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int index) {
        this.modCount++;
        checkInterval(index, 0, size() - 1);
        E result = get(index);
        this.root = this.root.remove(index);
        this.size--;
        return result;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.modCount++;
        this.root = null;
        this.size = 0;
    }

    private void checkInterval(int index, int startIndex, int endIndex) {
        if (index < startIndex || index > endIndex) {
            throw new IndexOutOfBoundsException("Invalid index:" + index + ", size=" + size());
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/list/TreeList$AVLNode.class */
    static class AVLNode<E> {
        private AVLNode<E> left;
        private boolean leftIsPrevious;
        private AVLNode<E> right;
        private boolean rightIsNext;
        private int height;
        private int relativePosition;
        private E value;

        private AVLNode(int relativePosition, E obj, AVLNode<E> rightFollower, AVLNode<E> leftFollower) {
            this.relativePosition = relativePosition;
            this.value = obj;
            this.rightIsNext = true;
            this.leftIsPrevious = true;
            this.right = rightFollower;
            this.left = leftFollower;
        }

        private AVLNode(Collection<? extends E> coll) {
            this(coll.iterator(), 0, coll.size() - 1, 0, null, null);
        }

        private AVLNode(Iterator<? extends E> iterator, int start, int end, int absolutePositionOfParent, AVLNode<E> prev, AVLNode<E> next) {
            int mid = start + ((end - start) / 2);
            if (start < mid) {
                this.left = new AVLNode<>(iterator, start, mid - 1, mid, prev, this);
            } else {
                this.leftIsPrevious = true;
                this.left = prev;
            }
            this.value = iterator.next();
            this.relativePosition = mid - absolutePositionOfParent;
            if (mid < end) {
                this.right = new AVLNode<>(iterator, mid + 1, end, mid, this, next);
            } else {
                this.rightIsNext = true;
                this.right = next;
            }
            recalcHeight();
        }

        E getValue() {
            return this.value;
        }

        void setValue(E obj) {
            this.value = obj;
        }

        AVLNode<E> get(int index) {
            int indexRelativeToMe = index - this.relativePosition;
            if (indexRelativeToMe == 0) {
                return this;
            }
            AVLNode<E> nextNode = indexRelativeToMe < 0 ? getLeftSubTree() : getRightSubTree();
            if (nextNode == null) {
                return null;
            }
            return nextNode.get(indexRelativeToMe);
        }

        int indexOf(Object object, int index) {
            int result;
            if (getLeftSubTree() != null && (result = this.left.indexOf(object, index + this.left.relativePosition)) != -1) {
                return result;
            }
            if (this.value != null ? this.value.equals(object) : this.value == object) {
                return index;
            }
            if (getRightSubTree() != null) {
                return this.right.indexOf(object, index + this.right.relativePosition);
            }
            return -1;
        }

        void toArray(Object[] array, int index) {
            array[index] = this.value;
            if (getLeftSubTree() != null) {
                this.left.toArray(array, index + this.left.relativePosition);
            }
            if (getRightSubTree() != null) {
                this.right.toArray(array, index + this.right.relativePosition);
            }
        }

        AVLNode<E> next() {
            if (this.rightIsNext || this.right == null) {
                return this.right;
            }
            return this.right.min();
        }

        AVLNode<E> previous() {
            if (this.leftIsPrevious || this.left == null) {
                return this.left;
            }
            return this.left.max();
        }

        AVLNode<E> insert(int index, E obj) {
            int indexRelativeToMe = index - this.relativePosition;
            if (indexRelativeToMe <= 0) {
                return insertOnLeft(indexRelativeToMe, obj);
            }
            return insertOnRight(indexRelativeToMe, obj);
        }

        private AVLNode<E> insertOnLeft(int indexRelativeToMe, E obj) {
            if (getLeftSubTree() == null) {
                setLeft(new AVLNode<>(-1, obj, this, this.left), null);
            } else {
                setLeft(this.left.insert(indexRelativeToMe, obj), null);
            }
            if (this.relativePosition >= 0) {
                this.relativePosition++;
            }
            AVLNode<E> ret = balance();
            recalcHeight();
            return ret;
        }

        private AVLNode<E> insertOnRight(int indexRelativeToMe, E obj) {
            if (getRightSubTree() == null) {
                setRight(new AVLNode<>(1, obj, this.right, this), null);
            } else {
                setRight(this.right.insert(indexRelativeToMe, obj), null);
            }
            if (this.relativePosition < 0) {
                this.relativePosition--;
            }
            AVLNode<E> ret = balance();
            recalcHeight();
            return ret;
        }

        private AVLNode<E> getLeftSubTree() {
            if (this.leftIsPrevious) {
                return null;
            }
            return this.left;
        }

        private AVLNode<E> getRightSubTree() {
            if (this.rightIsNext) {
                return null;
            }
            return this.right;
        }

        private AVLNode<E> max() {
            return getRightSubTree() == null ? this : this.right.max();
        }

        private AVLNode<E> min() {
            return getLeftSubTree() == null ? this : this.left.min();
        }

        AVLNode<E> remove(int index) {
            int indexRelativeToMe = index - this.relativePosition;
            if (indexRelativeToMe == 0) {
                return removeSelf();
            }
            if (indexRelativeToMe > 0) {
                setRight(this.right.remove(indexRelativeToMe), this.right.right);
                if (this.relativePosition < 0) {
                    this.relativePosition++;
                }
            } else {
                setLeft(this.left.remove(indexRelativeToMe), this.left.left);
                if (this.relativePosition > 0) {
                    this.relativePosition--;
                }
            }
            recalcHeight();
            return balance();
        }

        private AVLNode<E> removeMax() {
            if (getRightSubTree() == null) {
                return removeSelf();
            }
            setRight(this.right.removeMax(), this.right.right);
            if (this.relativePosition < 0) {
                this.relativePosition++;
            }
            recalcHeight();
            return balance();
        }

        private AVLNode<E> removeMin() {
            if (getLeftSubTree() == null) {
                return removeSelf();
            }
            setLeft(this.left.removeMin(), this.left.left);
            if (this.relativePosition > 0) {
                this.relativePosition--;
            }
            recalcHeight();
            return balance();
        }

        private AVLNode<E> removeSelf() {
            if (getRightSubTree() == null && getLeftSubTree() == null) {
                return null;
            }
            if (getRightSubTree() == null) {
                if (this.relativePosition > 0) {
                    this.left.relativePosition += this.relativePosition + (this.relativePosition > 0 ? 0 : 1);
                }
                this.left.max().setRight(null, this.right);
                return this.left;
            }
            if (getLeftSubTree() == null) {
                this.right.relativePosition += this.relativePosition - (this.relativePosition < 0 ? 0 : 1);
                this.right.min().setLeft(null, this.left);
                return this.right;
            }
            if (heightRightMinusLeft() > 0) {
                AVLNode<E> rightMin = this.right.min();
                this.value = rightMin.value;
                if (this.leftIsPrevious) {
                    this.left = rightMin.left;
                }
                this.right = this.right.removeMin();
                if (this.relativePosition < 0) {
                    this.relativePosition++;
                }
            } else {
                AVLNode<E> leftMax = this.left.max();
                this.value = leftMax.value;
                if (this.rightIsNext) {
                    this.right = leftMax.right;
                }
                AVLNode<E> leftPrevious = this.left.left;
                this.left = this.left.removeMax();
                if (this.left == null) {
                    this.left = leftPrevious;
                    this.leftIsPrevious = true;
                }
                if (this.relativePosition > 0) {
                    this.relativePosition--;
                }
            }
            recalcHeight();
            return this;
        }

        private AVLNode<E> balance() {
            switch (heightRightMinusLeft()) {
                case -2:
                    if (this.left.heightRightMinusLeft() > 0) {
                        setLeft(this.left.rotateLeft(), null);
                    }
                    return rotateRight();
                case -1:
                case 0:
                case 1:
                    return this;
                case 2:
                    if (this.right.heightRightMinusLeft() < 0) {
                        setRight(this.right.rotateRight(), null);
                    }
                    return rotateLeft();
                default:
                    throw new RuntimeException("tree inconsistent!");
            }
        }

        private int getOffset(AVLNode<E> node) {
            if (node == null) {
                return 0;
            }
            return node.relativePosition;
        }

        private int setOffset(AVLNode<E> node, int newOffest) {
            if (node == null) {
                return 0;
            }
            int oldOffset = getOffset(node);
            node.relativePosition = newOffest;
            return oldOffset;
        }

        private void recalcHeight() {
            this.height = Math.max(getLeftSubTree() == null ? -1 : getLeftSubTree().height, getRightSubTree() == null ? -1 : getRightSubTree().height) + 1;
        }

        private int getHeight(AVLNode<E> node) {
            if (node == null) {
                return -1;
            }
            return node.height;
        }

        private int heightRightMinusLeft() {
            return getHeight(getRightSubTree()) - getHeight(getLeftSubTree());
        }

        private AVLNode<E> rotateLeft() {
            AVLNode<E> newTop = this.right;
            AVLNode<E> movedNode = getRightSubTree().getLeftSubTree();
            int newTopPosition = this.relativePosition + getOffset(newTop);
            int myNewPosition = -newTop.relativePosition;
            int movedPosition = getOffset(newTop) + getOffset(movedNode);
            setRight(movedNode, newTop);
            newTop.setLeft(this, null);
            setOffset(newTop, newTopPosition);
            setOffset(this, myNewPosition);
            setOffset(movedNode, movedPosition);
            return newTop;
        }

        private AVLNode<E> rotateRight() {
            AVLNode<E> newTop = this.left;
            AVLNode<E> movedNode = getLeftSubTree().getRightSubTree();
            int newTopPosition = this.relativePosition + getOffset(newTop);
            int myNewPosition = -newTop.relativePosition;
            int movedPosition = getOffset(newTop) + getOffset(movedNode);
            setLeft(movedNode, newTop);
            newTop.setRight(this, null);
            setOffset(newTop, newTopPosition);
            setOffset(this, myNewPosition);
            setOffset(movedNode, movedPosition);
            return newTop;
        }

        private void setLeft(AVLNode<E> node, AVLNode<E> previous) {
            this.leftIsPrevious = node == null;
            this.left = this.leftIsPrevious ? previous : node;
            recalcHeight();
        }

        private void setRight(AVLNode<E> node, AVLNode<E> next) {
            this.rightIsNext = node == null;
            this.right = this.rightIsNext ? next : node;
            recalcHeight();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public AVLNode<E> addAll(AVLNode<E> otherTree, int currentSize) {
            AVLNode<E> maxNode = max();
            AVLNode<E> otherTreeMin = otherTree.min();
            if (otherTree.height > this.height) {
                AVLNode<E> leftSubTree = removeMax();
                Deque<AVLNode<E>> sAncestors = new ArrayDeque<>();
                AVLNode<E> s = otherTree;
                int sAbsolutePosition = s.relativePosition + currentSize;
                int sParentAbsolutePosition = 0;
                while (s != null && s.height > getHeight(leftSubTree)) {
                    sParentAbsolutePosition = sAbsolutePosition;
                    sAncestors.push(s);
                    s = s.left;
                    if (s != null) {
                        sAbsolutePosition += s.relativePosition;
                    }
                }
                maxNode.setLeft(leftSubTree, null);
                maxNode.setRight(s, otherTreeMin);
                if (leftSubTree != null) {
                    leftSubTree.max().setRight(null, maxNode);
                    leftSubTree.relativePosition -= currentSize - 1;
                }
                if (s != null) {
                    s.min().setLeft(null, maxNode);
                    s.relativePosition = (sAbsolutePosition - currentSize) + 1;
                }
                maxNode.relativePosition = (currentSize - 1) - sParentAbsolutePosition;
                otherTree.relativePosition += currentSize;
                AVLNode<E> aVLNodeBalance = maxNode;
                while (true) {
                    AVLNode<E> s2 = aVLNodeBalance;
                    if (!sAncestors.isEmpty()) {
                        AVLNode<E> sAncestor = sAncestors.pop();
                        sAncestor.setLeft(s2, null);
                        aVLNodeBalance = sAncestor.balance();
                    } else {
                        return s2;
                    }
                }
            } else {
                AVLNode<E> otherTree2 = otherTree.removeMin();
                Deque<AVLNode<E>> sAncestors2 = new ArrayDeque<>();
                AVLNode<E> s3 = this;
                int sAbsolutePosition2 = s3.relativePosition;
                int sParentAbsolutePosition2 = 0;
                while (s3 != null && s3.height > getHeight(otherTree2)) {
                    sParentAbsolutePosition2 = sAbsolutePosition2;
                    sAncestors2.push(s3);
                    s3 = s3.right;
                    if (s3 != null) {
                        sAbsolutePosition2 += s3.relativePosition;
                    }
                }
                otherTreeMin.setRight(otherTree2, null);
                otherTreeMin.setLeft(s3, maxNode);
                if (otherTree2 != null) {
                    otherTree2.min().setLeft(null, otherTreeMin);
                    otherTree2.relativePosition++;
                }
                if (s3 != null) {
                    s3.max().setRight(null, otherTreeMin);
                    s3.relativePosition = sAbsolutePosition2 - currentSize;
                }
                otherTreeMin.relativePosition = currentSize - sParentAbsolutePosition2;
                AVLNode<E> aVLNodeBalance2 = otherTreeMin;
                while (true) {
                    AVLNode<E> s4 = aVLNodeBalance2;
                    if (!sAncestors2.isEmpty()) {
                        AVLNode<E> sAncestor2 = sAncestors2.pop();
                        sAncestor2.setRight(s4, null);
                        aVLNodeBalance2 = sAncestor2.balance();
                    } else {
                        return s4;
                    }
                }
            }
        }

        public String toString() {
            return "AVLNode(" + this.relativePosition + ',' + (this.left != null) + ',' + this.value + ',' + (getRightSubTree() != null) + ", faedelung " + this.rightIsNext + " )";
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/list/TreeList$TreeListIterator.class */
    static class TreeListIterator<E> implements ListIterator<E>, OrderedIterator<E> {
        private final TreeList<E> parent;
        private AVLNode<E> next;
        private int nextIndex;
        private AVLNode<E> current;
        private int currentIndex;
        private int expectedModCount;

        protected TreeListIterator(TreeList<E> parent, int fromIndex) throws IndexOutOfBoundsException {
            this.parent = parent;
            this.expectedModCount = parent.modCount;
            this.next = ((TreeList) parent).root == null ? null : ((TreeList) parent).root.get(fromIndex);
            this.nextIndex = fromIndex;
            this.currentIndex = -1;
        }

        protected void checkModCount() {
            if (this.parent.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.nextIndex < this.parent.size();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public E next() {
            checkModCount();
            if (!hasNext()) {
                throw new NoSuchElementException("No element at index " + this.nextIndex + ".");
            }
            if (this.next == null) {
                this.next = ((TreeList) this.parent).root.get(this.nextIndex);
            }
            E value = this.next.getValue();
            this.current = this.next;
            int i = this.nextIndex;
            this.nextIndex = i + 1;
            this.currentIndex = i;
            this.next = this.next.next();
            return value;
        }

        @Override // java.util.ListIterator, org.apache.commons.collections4.OrderedIterator
        public boolean hasPrevious() {
            return this.nextIndex > 0;
        }

        @Override // java.util.ListIterator, org.apache.commons.collections4.OrderedIterator
        public E previous() {
            checkModCount();
            if (!hasPrevious()) {
                throw new NoSuchElementException("Already at start of list.");
            }
            if (this.next == null) {
                this.next = ((TreeList) this.parent).root.get(this.nextIndex - 1);
            } else {
                this.next = this.next.previous();
            }
            E value = this.next.getValue();
            this.current = this.next;
            int i = this.nextIndex - 1;
            this.nextIndex = i;
            this.currentIndex = i;
            return value;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.nextIndex;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return nextIndex() - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            checkModCount();
            if (this.currentIndex == -1) {
                throw new IllegalStateException();
            }
            this.parent.remove(this.currentIndex);
            if (this.nextIndex != this.currentIndex) {
                this.nextIndex--;
            }
            this.next = null;
            this.current = null;
            this.currentIndex = -1;
            this.expectedModCount++;
        }

        @Override // java.util.ListIterator
        public void set(E obj) {
            checkModCount();
            if (this.current == null) {
                throw new IllegalStateException();
            }
            this.current.setValue(obj);
        }

        @Override // java.util.ListIterator
        public void add(E obj) {
            checkModCount();
            this.parent.add(this.nextIndex, obj);
            this.current = null;
            this.currentIndex = -1;
            this.nextIndex++;
            this.expectedModCount++;
        }
    }
}
