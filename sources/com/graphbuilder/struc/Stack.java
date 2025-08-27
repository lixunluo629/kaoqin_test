package com.graphbuilder.struc;

/* loaded from: curvesapi-1.04.jar:com/graphbuilder/struc/Stack.class */
public class Stack extends LinkedList {
    public Object peek() {
        if (this.head == null) {
            return null;
        }
        return this.head.userObject;
    }

    public Object pop() {
        return removeHead();
    }

    public void push(Object o) {
        addToHead(o);
    }
}
