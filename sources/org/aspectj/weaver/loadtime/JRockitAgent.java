package org.aspectj.weaver.loadtime;

import com.bea.jvm.ClassLibrary;
import com.bea.jvm.JVMFactory;
import java.util.Stack;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/JRockitAgent.class */
public class JRockitAgent implements com.bea.jvm.ClassPreProcessor {
    private ClassPreProcessor preProcessor = new Aj();
    private static ThreadLocalStack stack = new ThreadLocalStack();

    public JRockitAgent() {
        ClassLibrary cl = JVMFactory.getJVM().getClassLibrary();
        cl.setClassPreProcessor(this);
    }

    public byte[] preProcess(ClassLoader loader, String className, byte[] bytes) {
        byte[] newBytes = bytes;
        if (stack.empty()) {
            stack.push(className);
            newBytes = this.preProcessor.preProcess(className, bytes, loader, null);
            stack.pop();
        }
        return newBytes;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/JRockitAgent$ThreadLocalStack.class */
    private static class ThreadLocalStack extends ThreadLocal {
        private ThreadLocalStack() {
        }

        public boolean empty() {
            Stack stack = (Stack) get();
            return stack.empty();
        }

        public Object peek() {
            Object obj = null;
            Stack stack = (Stack) get();
            if (!stack.empty()) {
                obj = stack.peek();
            }
            return obj;
        }

        public void push(Object obj) {
            Stack stack = (Stack) get();
            if (!stack.empty() && obj == stack.peek()) {
                throw new RuntimeException(obj.toString());
            }
            stack.push(obj);
        }

        public Object pop() {
            Stack stack = (Stack) get();
            return stack.pop();
        }

        @Override // java.lang.ThreadLocal
        protected Object initialValue() {
            return new Stack();
        }
    }
}
