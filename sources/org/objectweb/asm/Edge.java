package org.objectweb.asm;

/* JADX WARN: Classes with same name are omitted:
  asm-4.2.jar:org/objectweb/asm/Edge.class
 */
/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/Edge.SCL.lombok */
final class Edge {
    static final int JUMP = 0;
    static final int EXCEPTION = Integer.MAX_VALUE;
    final int info;
    final Label successor;
    Edge nextEdge;

    Edge(int info, Label successor, Edge nextEdge) {
        this.info = info;
        this.successor = successor;
        this.nextEdge = nextEdge;
    }
}
