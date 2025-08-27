package org.ehcache.impl.internal.sizeof.listeners;

import org.ehcache.sizeof.VisitorListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/sizeof/listeners/EhcacheVisitorListener.class */
public class EhcacheVisitorListener implements VisitorListener {
    private final long maxObjectGraphSize;
    private final long maxObjectSize;
    private long currentDepth;
    private long currentSize;

    /*  JADX ERROR: Failed to decode insn: 0x0007: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    /*  JADX ERROR: Failed to decode insn: 0x0035: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -2 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    @Override // org.ehcache.sizeof.VisitorListener
    public void visited(java.lang.Object r7, long r8) {
        /*
            r6 = this;
            r0 = r6
            r1 = r0
            long r1 = r1.currentDepth
            r2 = 1
            long r1 = r1 + r2
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.currentDepth = r1
            r0 = r6
            long r0 = r0.maxObjectGraphSize
            int r-1 = (r-1 > r0 ? 1 : (r-1 == r0 ? 0 : -1))
            if (r-1 <= 0) goto L2e
            org.ehcache.impl.internal.sizeof.listeners.exceptions.VisitorListenerException r-1 = new org.ehcache.impl.internal.sizeof.listeners.exceptions.VisitorListenerException
            r0 = r-1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r2 = r1
            r2.<init>()
            java.lang.String r2 = "Max Object Graph Size reached for the object : "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r7
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r-1
            r-1 = r6
            r0 = r-1
            long r0 = r0.currentSize
            r1 = r8
            long r0 = r0 + r1
            // decode failed: arraycopy: source index -2 out of bounds for object array[6]
            r-1.currentSize = r0
            r-1 = r6
            long r-1 = r-1.maxObjectSize
            int r-2 = (r-2 > r-1 ? 1 : (r-2 == r-1 ? 0 : -1))
            if (r-2 <= 0) goto L5c
            org.ehcache.impl.internal.sizeof.listeners.exceptions.VisitorListenerException r-2 = new org.ehcache.impl.internal.sizeof.listeners.exceptions.VisitorListenerException
            r-1 = r-2
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "Max Object Size reached for the object : "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r7
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r-1.<init>(r0)
            throw r-2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.sizeof.listeners.EhcacheVisitorListener.visited(java.lang.Object, long):void");
    }

    public EhcacheVisitorListener(long maxObjectGraphSize, long maxObjectSize) {
        this.maxObjectGraphSize = maxObjectGraphSize;
        this.maxObjectSize = maxObjectSize;
    }
}
