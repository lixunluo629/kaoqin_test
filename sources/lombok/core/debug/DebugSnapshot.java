package lombok.core.debug;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: lombok-1.16.22.jar:lombok/core/debug/DebugSnapshot.SCL.lombok */
public class DebugSnapshot implements Comparable<DebugSnapshot> {
    private static AtomicLong counter = new AtomicLong();
    private final long bits;
    private final List<StackTraceElement> trace;
    private final String threadName;
    private final String message;
    private final Object[] params;
    private final WeakReference<CompilationUnitDeclaration> owner;
    private final long id = counter.getAndIncrement();
    private final long when = System.currentTimeMillis();

    public DebugSnapshot(CompilationUnitDeclaration owner, int stackHiding, String message, Object... params) {
        this.bits = owner.bits;
        if (stackHiding < 0) {
            this.trace = null;
        } else {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            this.trace = new ArrayList(Math.max(0, (stackTrace.length - stackHiding) - 1));
            for (int i = 1 + stackHiding; i < stackTrace.length; i++) {
                this.trace.add(stackTrace[i]);
            }
        }
        this.threadName = Thread.currentThread().getName();
        this.message = message;
        this.params = params == null ? new Object[0] : params;
        this.owner = new WeakReference<>(owner);
    }

    private String ownerName() {
        CompilationUnitDeclaration node = this.owner.get();
        if (node == null) {
            return "--GCed--";
        }
        char[] tn = node.getMainTypeName();
        char[] fs = node.getFileName();
        if (tn == null || tn.length == 0) {
            return (fs == null || fs.length == 0) ? "--UNKNOWN--" : new String(fs);
        }
        return new String(tn);
    }

    public String shortToString() {
        StringBuilder out = new StringBuilder();
        Object[] objArr = new Object[4];
        objArr[0] = Long.valueOf(this.when);
        objArr[1] = this.threadName;
        objArr[2] = ownerName();
        objArr[3] = Boolean.valueOf(0 != (this.bits & 16));
        out.append(String.format("WHEN: %14d THREAD: %s AST: %s HAMB: %b -- ", objArr));
        if (this.message != null) {
            out.append(SymbolConstants.SPACE_SYMBOL).append(String.format(this.message, this.params));
        }
        return out.toString();
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(shortToString()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        if (this.trace == null) {
            out.append("    Stack Omitted");
        } else {
            for (StackTraceElement elem : this.trace) {
                out.append("    ").append(elem.toString()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        return out.toString();
    }

    @Override // java.lang.Comparable
    public int compareTo(DebugSnapshot o) {
        return Long.valueOf(this.id).compareTo(Long.valueOf(o.id));
    }
}
