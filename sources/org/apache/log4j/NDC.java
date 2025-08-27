package org.apache.log4j;

import java.util.Stack;

/* loaded from: log4j-over-slf4j-1.7.26.jar:org/apache/log4j/NDC.class */
public class NDC {
    public static final String PREFIX = "NDC";

    public static void clear() throws IllegalArgumentException {
        int depth = getDepth();
        for (int i = 0; i < depth; i++) {
            String key = PREFIX + i;
            org.slf4j.MDC.remove(key);
        }
    }

    public static Stack cloneStack() {
        return null;
    }

    public static void inherit(Stack stack) {
    }

    public static String get() {
        return null;
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x001a */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int getDepth() throws java.lang.IllegalArgumentException {
        /*
            r0 = 0
            r3 = r0
        L2:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "NDC"
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r3
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = org.slf4j.MDC.get(r0)
            r4 = r0
            r0 = r4
            if (r0 == 0) goto L23
            int r3 = r3 + 1
            goto L2
        L23:
            r0 = r3
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.log4j.NDC.getDepth():int");
    }

    public static String pop() throws IllegalArgumentException {
        int next = getDepth();
        if (next == 0) {
            return "";
        }
        int last = next - 1;
        String key = PREFIX + last;
        String val = org.slf4j.MDC.get(key);
        org.slf4j.MDC.remove(key);
        return val;
    }

    public static String peek() throws IllegalArgumentException {
        int next = getDepth();
        if (next == 0) {
            return "";
        }
        int last = next - 1;
        String key = PREFIX + last;
        String val = org.slf4j.MDC.get(key);
        return val;
    }

    public static void push(String message) throws IllegalArgumentException {
        int next = getDepth();
        org.slf4j.MDC.put(PREFIX + next, message);
    }

    public static void remove() throws IllegalArgumentException {
        clear();
    }

    public static void setMaxDepth(int maxDepth) {
    }
}
