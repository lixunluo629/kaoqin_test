package org.aspectj.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/PartialOrder.class */
public class PartialOrder {

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/PartialOrder$PartialComparable.class */
    public interface PartialComparable {
        int compareTo(Object obj);

        int fallbackCompareTo(Object obj);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/PartialOrder$SortObject.class */
    private static class SortObject<T extends PartialComparable> {
        T object;
        List<SortObject<T>> smallerObjects = new LinkedList();
        List<SortObject<T>> biggerObjects = new LinkedList();

        public SortObject(T o) {
            this.object = o;
        }

        boolean hasNoSmallerObjects() {
            return this.smallerObjects.size() == 0;
        }

        boolean removeSmallerObject(SortObject<T> o) {
            this.smallerObjects.remove(o);
            return hasNoSmallerObjects();
        }

        void addDirectedLinks(SortObject<T> other) {
            int cmp = this.object.compareTo(other.object);
            if (cmp == 0) {
                return;
            }
            if (cmp > 0) {
                this.smallerObjects.add(other);
                other.biggerObjects.add(this);
            } else {
                this.biggerObjects.add(other);
                other.smallerObjects.add(this);
            }
        }

        public String toString() {
            return this.object.toString();
        }
    }

    private static <T extends PartialComparable> void addNewPartialComparable(List<SortObject<T>> graph, T o) {
        SortObject<T> so = new SortObject<>(o);
        for (SortObject<T> other : graph) {
            so.addDirectedLinks(other);
        }
        graph.add(so);
    }

    private static <T extends PartialComparable> void removeFromGraph(List<SortObject<T>> graph, SortObject<T> o) {
        Iterator<SortObject<T>> i = graph.iterator();
        while (i.hasNext()) {
            SortObject<T> other = i.next();
            if (o == other) {
                i.remove();
            }
            other.removeSmallerObject(o);
        }
    }

    public static <T extends PartialComparable> List<T> sort(List<T> objects) {
        if (objects.size() < 2) {
            return objects;
        }
        List<SortObject<T>> sortList = new LinkedList<>();
        Iterator<T> i = objects.iterator();
        while (i.hasNext()) {
            addNewPartialComparable(sortList, i.next());
        }
        int N = objects.size();
        for (int index = 0; index < N; index++) {
            SortObject<T> leastWithNoSmallers = null;
            for (SortObject<T> so : sortList) {
                if (so.hasNoSmallerObjects() && (leastWithNoSmallers == null || so.object.fallbackCompareTo(leastWithNoSmallers.object) < 0)) {
                    leastWithNoSmallers = so;
                }
            }
            if (leastWithNoSmallers == null) {
                return null;
            }
            removeFromGraph(sortList, leastWithNoSmallers);
            objects.set(index, leastWithNoSmallers.object);
        }
        return objects;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/util/PartialOrder$Token.class */
    static class Token implements PartialComparable {
        private String s;

        Token(String s) {
            this.s = s;
        }

        @Override // org.aspectj.util.PartialOrder.PartialComparable
        public int compareTo(Object other) {
            Token t = (Token) other;
            int cmp = this.s.charAt(0) - t.s.charAt(0);
            if (cmp == 1) {
                return 1;
            }
            if (cmp == -1) {
                return -1;
            }
            return 0;
        }

        @Override // org.aspectj.util.PartialOrder.PartialComparable
        public int fallbackCompareTo(Object other) {
            return -this.s.compareTo(((Token) other).s);
        }

        public String toString() {
            return this.s;
        }
    }

    public static void main(String[] args) {
        List<Token> l = new ArrayList<>();
        l.add(new Token("a1"));
        l.add(new Token("c2"));
        l.add(new Token("b3"));
        l.add(new Token("f4"));
        l.add(new Token("e5"));
        l.add(new Token("d6"));
        l.add(new Token("c7"));
        l.add(new Token("b8"));
        l.add(new Token(CompressorStreamFactory.Z));
        l.add(new Token("x"));
        l.add(new Token("f9"));
        l.add(new Token("e10"));
        l.add(new Token("a11"));
        l.add(new Token("d12"));
        l.add(new Token("b13"));
        l.add(new Token("c14"));
        System.out.println(l);
        sort(l);
        System.out.println(l);
    }
}
