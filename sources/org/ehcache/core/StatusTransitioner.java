package org.ehcache.core;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.ehcache.StateTransitionException;
import org.ehcache.Status;
import org.ehcache.core.InternalStatus;
import org.ehcache.core.events.StateChangeListener;
import org.ehcache.core.spi.LifeCycled;
import org.slf4j.Logger;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/StatusTransitioner.class */
final class StatusTransitioner {
    private volatile Thread maintenanceLease;
    private final Logger logger;
    private final CopyOnWriteArrayList<LifeCycled> hooks = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<StateChangeListener> listeners = new CopyOnWriteArrayList<>();
    private final AtomicReference<InternalStatus.Transition> currentState = new AtomicReference<>(InternalStatus.initial());

    StatusTransitioner(Logger logger) {
        this.logger = logger;
    }

    Status currentStatus() {
        return this.currentState.get().get().toPublicStatus();
    }

    boolean isTransitioning() {
        return !this.currentState.get().done();
    }

    void checkAvailable() {
        Status status = currentStatus();
        if (status == Status.MAINTENANCE && Thread.currentThread() != this.maintenanceLease) {
            throw new IllegalStateException("State is " + status + ", yet you don't own it!");
        }
        if (status == Status.UNINITIALIZED) {
            throw new IllegalStateException("State is " + status);
        }
    }

    void checkMaintenance() {
        Status status = currentStatus();
        if (status == Status.MAINTENANCE && Thread.currentThread() != this.maintenanceLease) {
            throw new IllegalStateException("State is " + status + ", yet you don't own it!");
        }
        if (status != Status.MAINTENANCE) {
            throw new IllegalStateException("State is " + status);
        }
    }

    Transition init() {
        AtomicReference<InternalStatus.Transition> atomicReference;
        InternalStatus.Transition cs;
        InternalStatus.Transition st;
        this.logger.trace("Initializing");
        do {
            atomicReference = this.currentState;
            cs = this.currentState.get();
            st = cs.get().init();
        } while (!atomicReference.compareAndSet(cs, st));
        return new Transition(st, null, "Initialize");
    }

    Transition close() {
        AtomicReference<InternalStatus.Transition> atomicReference;
        InternalStatus.Transition cs;
        InternalStatus.Transition st;
        this.logger.trace("Closing");
        if (this.maintenanceLease != null && Thread.currentThread() != this.maintenanceLease) {
            throw new IllegalStateException("You don't own this MAINTENANCE lease");
        }
        do {
            atomicReference = this.currentState;
            cs = this.currentState.get();
            st = cs.get().close();
        } while (!atomicReference.compareAndSet(cs, st));
        return new Transition(st, null, "Close");
    }

    Transition maintenance() {
        AtomicReference<InternalStatus.Transition> atomicReference;
        InternalStatus.Transition cs;
        InternalStatus.Transition st;
        this.logger.trace("Entering Maintenance");
        do {
            atomicReference = this.currentState;
            cs = this.currentState.get();
            st = cs.get().maintenance();
        } while (!atomicReference.compareAndSet(cs, st));
        return new Transition(st, Thread.currentThread(), "Enter Maintenance");
    }

    Transition exitMaintenance() {
        AtomicReference<InternalStatus.Transition> atomicReference;
        InternalStatus.Transition cs;
        InternalStatus.Transition st;
        checkMaintenance();
        this.logger.trace("Exiting Maintenance");
        do {
            atomicReference = this.currentState;
            cs = this.currentState.get();
            st = cs.get().close();
        } while (!atomicReference.compareAndSet(cs, st));
        return new Transition(st, Thread.currentThread(), "Exit Maintenance");
    }

    void addHook(LifeCycled hook) {
        validateHookRegistration();
        this.hooks.add(hook);
    }

    void removeHook(LifeCycled hook) {
        validateHookRegistration();
        this.hooks.remove(hook);
    }

    private void validateHookRegistration() {
        if (currentStatus() != Status.UNINITIALIZED) {
            throw new IllegalStateException("Can't modify hooks when not in " + Status.UNINITIALIZED);
        }
    }

    void registerListener(StateChangeListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    void deregisterListener(StateChangeListener listener) {
        this.listeners.remove(listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runInitHooks() throws Exception {
        Deque<LifeCycled> initiated = new ArrayDeque<>();
        Iterator i$ = this.hooks.iterator();
        while (i$.hasNext()) {
            LifeCycled hook = i$.next();
            try {
                hook.init();
                initiated.add(hook);
            } catch (Exception initException) {
                while (!initiated.isEmpty()) {
                    try {
                        initiated.pop().close();
                    } catch (Exception closeException) {
                        this.logger.error("Failed to close() while shutting down because of .init() having thrown", (Throwable) closeException);
                    }
                }
                throw initException;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runCloseHooks() throws Exception {
        Deque<LifeCycled> initiated = new ArrayDeque<>();
        Iterator i$ = this.hooks.iterator();
        while (i$.hasNext()) {
            LifeCycled hook = i$.next();
            initiated.addFirst(hook);
        }
        Exception firstFailure = null;
        while (!initiated.isEmpty()) {
            try {
                initiated.pop().close();
            } catch (Exception closeException) {
                if (firstFailure == null) {
                    firstFailure = closeException;
                } else {
                    this.logger.error("A LifeCyclable has thrown already while closing down", (Throwable) closeException);
                }
            }
        }
        if (firstFailure != null) {
            throw firstFailure;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireTransitionEvent(Status previousStatus, Status newStatus) {
        Iterator i$ = this.listeners.iterator();
        while (i$.hasNext()) {
            StateChangeListener listener = i$.next();
            listener.stateTransition(previousStatus, newStatus);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/StatusTransitioner$Transition.class */
    final class Transition {
        private final InternalStatus.Transition st;
        private final Thread thread;
        private final String action;

        public Transition(InternalStatus.Transition st, Thread thread, String action) {
            this.st = st;
            this.thread = thread;
            this.action = action;
        }

        public void succeeded() {
            try {
                switch (this.st.to()) {
                    case AVAILABLE:
                        StatusTransitioner.this.runInitHooks();
                        break;
                    case UNINITIALIZED:
                        StatusTransitioner.this.maintenanceLease = null;
                        StatusTransitioner.this.runCloseHooks();
                        break;
                    case MAINTENANCE:
                        StatusTransitioner.this.maintenanceLease = this.thread;
                        break;
                    default:
                        throw new IllegalArgumentException("Didn't expect that enum value: " + this.st.to());
                }
                this.st.succeeded();
                try {
                    StatusTransitioner.this.fireTransitionEvent(this.st.from().toPublicStatus(), this.st.to().toPublicStatus());
                    StatusTransitioner.this.maintenanceLease = this.thread;
                    StatusTransitioner.this.logger.debug("{} successful.", this.action);
                } catch (Throwable th) {
                    StatusTransitioner.this.maintenanceLease = this.thread;
                    StatusTransitioner.this.logger.debug("{} successful.", this.action);
                    throw th;
                }
            } catch (Exception e) {
                this.st.failed();
                throw new StateTransitionException(e);
            }
        }

        public StateTransitionException failed(Throwable t) {
            if (this.st.done()) {
                if (t != null) {
                    throw ((AssertionError) new AssertionError("Throwable cannot be thrown if Transition is done.").initCause(t));
                }
                return null;
            }
            this.st.failed();
            if (t != null) {
                StatusTransitioner.this.logger.error("{} failed.", this.action);
                if (t instanceof StateTransitionException) {
                    return (StateTransitionException) t;
                }
                return new StateTransitionException(t);
            }
            return null;
        }
    }
}
