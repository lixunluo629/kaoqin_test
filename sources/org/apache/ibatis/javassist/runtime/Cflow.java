package org.apache.ibatis.javassist.runtime;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/runtime/Cflow.class */
public class Cflow extends ThreadLocal {

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/runtime/Cflow$Depth.class */
    private static class Depth {
        private int depth = 0;

        Depth() {
        }

        int get() {
            return this.depth;
        }

        void inc() {
            this.depth++;
        }

        void dec() {
            this.depth--;
        }
    }

    @Override // java.lang.ThreadLocal
    protected synchronized Object initialValue() {
        return new Depth();
    }

    public void enter() {
        ((Depth) get()).inc();
    }

    public void exit() {
        ((Depth) get()).dec();
    }

    public int value() {
        return ((Depth) get()).get();
    }
}
