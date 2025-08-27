package org.ehcache.core;

import org.ehcache.Status;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/InternalStatus.class */
enum InternalStatus {
    UNINITIALIZED { // from class: org.ehcache.core.InternalStatus.1
        @Override // org.ehcache.core.InternalStatus
        public Transition init() {
            return new Transition(AVAILABLE);
        }

        @Override // org.ehcache.core.InternalStatus
        public Transition maintenance() {
            return new Transition(MAINTENANCE);
        }

        @Override // org.ehcache.core.InternalStatus
        public Status toPublicStatus() {
            return Status.UNINITIALIZED;
        }
    },
    MAINTENANCE { // from class: org.ehcache.core.InternalStatus.2
        @Override // org.ehcache.core.InternalStatus
        public Transition close() {
            return new Transition(UNINITIALIZED);
        }

        @Override // org.ehcache.core.InternalStatus
        public Status toPublicStatus() {
            return Status.MAINTENANCE;
        }
    },
    AVAILABLE { // from class: org.ehcache.core.InternalStatus.3
        @Override // org.ehcache.core.InternalStatus
        public Transition close() {
            return new Transition(UNINITIALIZED);
        }

        @Override // org.ehcache.core.InternalStatus
        public Status toPublicStatus() {
            return Status.AVAILABLE;
        }
    };

    public abstract Status toPublicStatus();

    public Transition init() {
        throw new IllegalStateException("Init not supported from " + name());
    }

    public Transition close() {
        throw new IllegalStateException("Close not supported from " + name());
    }

    public Transition maintenance() {
        throw new IllegalStateException("Maintenance not supported from " + name());
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/InternalStatus$Transition.class */
    public class Transition {
        private final InternalStatus to;
        private final Thread owner;
        private volatile InternalStatus done;

        private Transition(InternalStatus to) {
            this.owner = Thread.currentThread();
            if (to == null) {
                throw new NullPointerException();
            }
            this.to = to;
        }

        /* JADX WARN: Finally extract failed */
        public InternalStatus get() {
            InternalStatus internalStatus;
            if (this.done != null) {
                return this.done;
            }
            if (this.owner == Thread.currentThread()) {
                return this.to.compareTo(from()) > 0 ? this.to : from();
            }
            synchronized (this) {
                boolean interrupted = false;
                while (this.done == null) {
                    try {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            interrupted = true;
                        }
                    } catch (Throwable th) {
                        if (interrupted) {
                            Thread.currentThread().interrupt();
                        }
                        throw th;
                    }
                }
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                internalStatus = this.done;
            }
            return internalStatus;
        }

        public synchronized void succeeded() {
            this.done = this.to;
            notifyAll();
        }

        public synchronized void failed() {
            this.done = this.to.compareTo(from()) > 0 ? from() : this.to;
            notifyAll();
        }

        public InternalStatus from() {
            return InternalStatus.this;
        }

        public InternalStatus to() {
            return this.to;
        }

        public boolean done() {
            return this.done != null;
        }
    }

    public static Transition initial() {
        Transition close = MAINTENANCE.close();
        close.succeeded();
        return close;
    }
}
