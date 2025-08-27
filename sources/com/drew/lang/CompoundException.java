package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/CompoundException.class */
public class CompoundException extends Exception {
    private static final long serialVersionUID = -9207883813472069925L;

    @Nullable
    private final Throwable _innerException;

    public CompoundException(@Nullable String msg) {
        this(msg, null);
    }

    public CompoundException(@Nullable Throwable exception) {
        this(null, exception);
    }

    public CompoundException(@Nullable String msg, @Nullable Throwable innerException) {
        super(msg);
        this._innerException = innerException;
    }

    @Nullable
    public Throwable getInnerException() {
        return this._innerException;
    }

    @Override // java.lang.Throwable
    @NotNull
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(super.toString());
        if (this._innerException != null) {
            string.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            string.append("--- inner exception ---");
            string.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            string.append(this._innerException.toString());
        }
        return string.toString();
    }

    @Override // java.lang.Throwable
    public void printStackTrace(@NotNull PrintStream s) {
        super.printStackTrace(s);
        if (this._innerException != null) {
            s.println("--- inner exception ---");
            this._innerException.printStackTrace(s);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(@NotNull PrintWriter s) {
        super.printStackTrace(s);
        if (this._innerException != null) {
            s.println("--- inner exception ---");
            this._innerException.printStackTrace(s);
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        super.printStackTrace();
        if (this._innerException != null) {
            System.err.println("--- inner exception ---");
            this._innerException.printStackTrace();
        }
    }
}
