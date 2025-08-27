package org.springframework.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.util.Assert;

@Deprecated
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ControlFlowFactory.class */
public abstract class ControlFlowFactory {
    public static ControlFlow createControlFlow() {
        return new Jdk14ControlFlow();
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/ControlFlowFactory$Jdk14ControlFlow.class */
    static class Jdk14ControlFlow implements ControlFlow {
        private StackTraceElement[] stack = new Throwable().getStackTrace();

        @Override // org.springframework.core.ControlFlow
        public boolean under(Class<?> clazz) {
            Assert.notNull(clazz, "Class must not be null");
            String className = clazz.getName();
            for (StackTraceElement element : this.stack) {
                if (element.getClassName().equals(className)) {
                    return true;
                }
            }
            return false;
        }

        @Override // org.springframework.core.ControlFlow
        public boolean under(Class<?> clazz, String methodName) {
            Assert.notNull(clazz, "Class must not be null");
            Assert.notNull(methodName, "Method name must not be null");
            String className = clazz.getName();
            for (StackTraceElement element : this.stack) {
                if (element.getClassName().equals(className) && element.getMethodName().equals(methodName)) {
                    return true;
                }
            }
            return false;
        }

        @Override // org.springframework.core.ControlFlow
        public boolean underToken(String token) {
            if (token == null) {
                return false;
            }
            StringWriter sw = new StringWriter();
            new Throwable().printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            return stackTrace.contains(token);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Jdk14ControlFlow: ");
            for (int i = 0; i < this.stack.length; i++) {
                if (i > 0) {
                    sb.append("\n\t@");
                }
                sb.append(this.stack[i]);
            }
            return sb.toString();
        }
    }
}
