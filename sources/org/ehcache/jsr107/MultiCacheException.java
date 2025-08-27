package org.ehcache.jsr107;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.cache.CacheException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/MultiCacheException.class */
class MultiCacheException extends CacheException {
    private static final long serialVersionUID = -6839700789356356261L;
    private final List<Throwable> throwables = new ArrayList();

    MultiCacheException() {
    }

    MultiCacheException(Throwable t) {
        addThrowable(t);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void addThrowable(Throwable th) {
        if (th == 0) {
            throw new NullPointerException();
        }
        if (th == this) {
            throw new IllegalArgumentException("cannot add to self");
        }
        if (th instanceof MultiCacheException) {
            for (Throwable t2 : ((MultiCacheException) th).getThrowables()) {
                this.throwables.add(t2);
            }
            return;
        }
        this.throwables.add(th);
    }

    private List<Throwable> getThrowables() {
        return Collections.unmodifiableList(this.throwables);
    }

    public String getMessage() {
        List<Throwable> ts = getThrowables();
        if (ts.isEmpty()) {
            return super.getMessage();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ts.size(); i++) {
            sb.append("[Exception ").append(i).append("] ").append(ts.get(i).getMessage()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    MultiCacheException addFirstThrowable(Throwable th) {
        if (th == 0) {
            throw new NullPointerException();
        }
        if (th == this) {
            throw new IllegalArgumentException("cannot add to self");
        }
        if (th instanceof MultiCacheException) {
            MultiCacheException mce = (MultiCacheException) th;
            this.throwables.addAll(0, mce.getThrowables());
        }
        this.throwables.add(0, th);
        return this;
    }

    public Throwable initCause(Throwable cause) {
        throw new UnsupportedOperationException();
    }

    public Throwable getCause() {
        return null;
    }

    public void printStackTrace() {
        super.printStackTrace();
        for (int i = 0; i < this.throwables.size(); i++) {
            System.err.print("  [Exception " + i + "] ");
            this.throwables.get(i).printStackTrace();
        }
    }

    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        for (int i = 0; i < this.throwables.size(); i++) {
            ps.print("  [Exception " + i + "] ");
            this.throwables.get(i).printStackTrace(ps);
        }
    }

    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        for (int i = 0; i < this.throwables.size(); i++) {
            pw.print("  [Exception " + i + "] ");
            this.throwables.get(i).printStackTrace(pw);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.CacheException */
    /* JADX WARN: Multi-variable type inference failed */
    void throwIfNotEmpty() throws CacheException {
        if (!this.throwables.isEmpty()) {
            if (this.throwables.size() == 1) {
                CacheException cacheException = (Throwable) this.throwables.get(0);
                if (cacheException instanceof CacheException) {
                    throw cacheException;
                }
            }
            throw this;
        }
    }
}
