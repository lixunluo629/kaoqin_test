package org.springframework.asm;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/asm/Edge.class */
class Edge {
    static final int NORMAL = 0;
    static final int EXCEPTION = Integer.MAX_VALUE;
    int info;
    Label successor;
    Edge next;

    Edge() {
    }
}
