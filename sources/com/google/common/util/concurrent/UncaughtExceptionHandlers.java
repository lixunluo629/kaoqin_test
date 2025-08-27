package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import java.lang.Thread;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/UncaughtExceptionHandlers.class */
public final class UncaughtExceptionHandlers {
    private UncaughtExceptionHandlers() {
    }

    public static Thread.UncaughtExceptionHandler systemExit() {
        return new Exiter(Runtime.getRuntime());
    }

    @VisibleForTesting
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/UncaughtExceptionHandlers$Exiter.class */
    static final class Exiter implements Thread.UncaughtExceptionHandler {
        private static final Logger logger = Logger.getLogger(Exiter.class.getName());
        private final Runtime runtime;

        Exiter(Runtime runtime) {
            this.runtime = runtime;
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread t, Throwable e) {
            try {
                try {
                    logger.log(Level.SEVERE, String.format("Caught an exception in %s.  Shutting down.", t), e);
                    this.runtime.exit(1);
                } catch (Throwable errorInLogging) {
                    System.err.println(e.getMessage());
                    System.err.println(errorInLogging.getMessage());
                    this.runtime.exit(1);
                }
            } catch (Throwable th) {
                this.runtime.exit(1);
                throw th;
            }
        }
    }
}
