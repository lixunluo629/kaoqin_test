package lombok.core.debug;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;

/* loaded from: lombok-1.16.22.jar:lombok/core/debug/DebugSnapshotStore.SCL.lombok */
public class DebugSnapshotStore {
    public static final DebugSnapshotStore INSTANCE = new DebugSnapshotStore();
    public static final boolean GLOBAL_DSS_DISABLE_SWITCH = true;
    private final Map<CompilationUnitDeclaration, List<DebugSnapshot>> map = new WeakHashMap();

    /*  JADX ERROR: NullPointerException in pass: BlockProcessor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.nodes.BlockNode.getPredecessors()" because "to" is null
        	at jadx.core.dex.visitors.blocks.BlockSplitter.removeConnection(BlockSplitter.java:166)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.removeExcHandler(BlockExceptionHandler.java:324)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.lambda$prepareTryBlocks$1(BlockExceptionHandler.java:207)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.prepareTryBlocks(BlockExceptionHandler.java:207)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.process(BlockExceptionHandler.java:60)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.independentBlockTreeMod(BlockProcessor.java:372)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:56)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:49)
        */
    /* JADX WARN: Unreachable blocks removed: 15, instructions: 47 */
    public void snapshot(org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration r8, java.lang.String r9, java.lang.Object... r10) {
        /*
            r7 = this;
            return
            lombok.core.debug.DebugSnapshot r0 = new lombok.core.debug.DebugSnapshot
            r1 = r0
            r2 = r8
            r3 = 1
            r4 = r9
            r5 = r10
            r1.<init>(r2, r3, r4, r5)
            r11 = r0
            r0 = r7
            java.util.Map<org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.util.List<lombok.core.debug.DebugSnapshot>> r0 = r0.map
            r1 = r0
            r13 = r1
            monitor-enter(r0)
            r0 = r7
            java.util.Map<org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.util.List<lombok.core.debug.DebugSnapshot>> r0 = r0.map     // Catch: java.lang.Throwable -> L67
            r1 = r8
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> L67
            java.util.List r0 = (java.util.List) r0     // Catch: java.lang.Throwable -> L67
            r12 = r0
            r0 = r12
            if (r0 != 0) goto L4d
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L67
            r1 = r0
            r1.<init>()     // Catch: java.lang.Throwable -> L67
            r12 = r0
            r0 = r7
            java.util.Map<org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.util.List<lombok.core.debug.DebugSnapshot>> r0 = r0.map     // Catch: java.lang.Throwable -> L67
            r1 = r8
            r2 = r12
            java.lang.Object r0 = r0.put(r1, r2)     // Catch: java.lang.Throwable -> L67
            r0 = r12
            r1 = r11
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L67
            goto L61
        L4d:
            r0 = r12
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L67
            if (r0 != 0) goto L61
            r0 = r12
            r1 = r11
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L67
        L61:
            r0 = r13
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L67
            goto L6b
        L67:
            r1 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L67
            throw r0     // Catch: java.lang.Throwable -> L67
        L6b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: lombok.core.debug.DebugSnapshotStore.snapshot(org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.lang.String, java.lang.Object[]):void");
    }

    /*  JADX ERROR: NullPointerException in pass: BlockProcessor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.nodes.BlockNode.getPredecessors()" because "to" is null
        	at jadx.core.dex.visitors.blocks.BlockSplitter.removeConnection(BlockSplitter.java:166)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.removeExcHandler(BlockExceptionHandler.java:324)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.lambda$prepareTryBlocks$1(BlockExceptionHandler.java:207)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.prepareTryBlocks(BlockExceptionHandler.java:207)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.process(BlockExceptionHandler.java:60)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.independentBlockTreeMod(BlockProcessor.java:372)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:56)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:49)
        */
    /* JADX WARN: Unreachable blocks removed: 15, instructions: 47 */
    public void log(org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration r8, java.lang.String r9, java.lang.Object... r10) {
        /*
            r7 = this;
            return
            lombok.core.debug.DebugSnapshot r0 = new lombok.core.debug.DebugSnapshot
            r1 = r0
            r2 = r8
            r3 = -1
            r4 = r9
            r5 = r10
            r1.<init>(r2, r3, r4, r5)
            r11 = r0
            r0 = r7
            java.util.Map<org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.util.List<lombok.core.debug.DebugSnapshot>> r0 = r0.map
            r1 = r0
            r13 = r1
            monitor-enter(r0)
            r0 = r7
            java.util.Map<org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.util.List<lombok.core.debug.DebugSnapshot>> r0 = r0.map     // Catch: java.lang.Throwable -> L67
            r1 = r8
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> L67
            java.util.List r0 = (java.util.List) r0     // Catch: java.lang.Throwable -> L67
            r12 = r0
            r0 = r12
            if (r0 != 0) goto L4d
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L67
            r1 = r0
            r1.<init>()     // Catch: java.lang.Throwable -> L67
            r12 = r0
            r0 = r7
            java.util.Map<org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.util.List<lombok.core.debug.DebugSnapshot>> r0 = r0.map     // Catch: java.lang.Throwable -> L67
            r1 = r8
            r2 = r12
            java.lang.Object r0 = r0.put(r1, r2)     // Catch: java.lang.Throwable -> L67
            r0 = r12
            r1 = r11
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L67
            goto L61
        L4d:
            r0 = r12
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> L67
            if (r0 != 0) goto L61
            r0 = r12
            r1 = r11
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L67
        L61:
            r0 = r13
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L67
            goto L6b
        L67:
            r1 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L67
            throw r0     // Catch: java.lang.Throwable -> L67
        L6b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: lombok.core.debug.DebugSnapshotStore.log(org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.lang.String, java.lang.Object[]):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Found unreachable blocks
        	at jadx.core.dex.visitors.blocks.DominatorTree.sortBlocks(DominatorTree.java:34)
        	at jadx.core.dex.visitors.blocks.DominatorTree.compute(DominatorTree.java:24)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.computeDominators(BlockProcessor.java:252)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:55)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:49)
        */
    /* JADX WARN: Unreachable blocks removed: 19, instructions: 58 */
    public java.lang.String print(org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration r11, java.lang.String r12, java.lang.Object... r13) {
        /*
            Method dump skipped, instructions count: 367
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: lombok.core.debug.DebugSnapshotStore.print(org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration, java.lang.String, java.lang.Object[]):java.lang.String");
    }
}
