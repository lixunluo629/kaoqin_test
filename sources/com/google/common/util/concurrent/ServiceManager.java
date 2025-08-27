package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenerCallQueue;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Service;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;
import redis.clients.jedis.Protocol;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ServiceManager.class */
public final class ServiceManager {
    private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
    private static final ListenerCallQueue.Callback<Listener> HEALTHY_CALLBACK = new ListenerCallQueue.Callback<Listener>("healthy()") { // from class: com.google.common.util.concurrent.ServiceManager.1
        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.util.concurrent.ListenerCallQueue.Callback
        public void call(Listener listener) {
            listener.healthy();
        }
    };
    private static final ListenerCallQueue.Callback<Listener> STOPPED_CALLBACK = new ListenerCallQueue.Callback<Listener>("stopped()") { // from class: com.google.common.util.concurrent.ServiceManager.2
        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.util.concurrent.ListenerCallQueue.Callback
        public void call(Listener listener) {
            listener.stopped();
        }
    };
    private final ServiceManagerState state;
    private final ImmutableList<Service> services;

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ServiceManager$Listener.class */
    public static abstract class Listener {
        public void healthy() {
        }

        public void stopped() {
        }

        public void failure(Service service) {
        }
    }

    public ServiceManager(Iterable<? extends Service> services) {
        ImmutableList<Service> copy = ImmutableList.copyOf(services);
        if (copy.isEmpty()) {
            logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", (Throwable) new EmptyServiceManagerWarning());
            copy = ImmutableList.of(new NoOpService());
        }
        this.state = new ServiceManagerState(copy);
        this.services = copy;
        WeakReference<ServiceManagerState> stateReference = new WeakReference<>(this.state);
        Iterator i$ = copy.iterator();
        while (i$.hasNext()) {
            Service service = (Service) i$.next();
            service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
            Preconditions.checkArgument(service.state() == Service.State.NEW, "Can only manage NEW services, %s", service);
        }
        this.state.markReady();
    }

    public void addListener(Listener listener, Executor executor) {
        this.state.addListener(listener, executor);
    }

    public void addListener(Listener listener) {
        this.state.addListener(listener, MoreExecutors.directExecutor());
    }

    public ServiceManager startAsync() {
        Iterator i$ = this.services.iterator();
        while (i$.hasNext()) {
            Service service = (Service) i$.next();
            Service.State state = service.state();
            Preconditions.checkState(state == Service.State.NEW, "Service %s is %s, cannot start it.", service, state);
        }
        Iterator i$2 = this.services.iterator();
        while (i$2.hasNext()) {
            Service service2 = (Service) i$2.next();
            try {
                this.state.tryStartTiming(service2);
                service2.startAsync();
            } catch (IllegalStateException e) {
                Logger logger2 = logger;
                Level level = Level.WARNING;
                String strValueOf = String.valueOf(String.valueOf(service2));
                logger2.log(level, new StringBuilder(24 + strValueOf.length()).append("Unable to start Service ").append(strValueOf).toString(), (Throwable) e);
            }
        }
        return this;
    }

    public void awaitHealthy() {
        this.state.awaitHealthy();
    }

    public void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
        this.state.awaitHealthy(timeout, unit);
    }

    public ServiceManager stopAsync() {
        Iterator i$ = this.services.iterator();
        while (i$.hasNext()) {
            Service service = (Service) i$.next();
            service.stopAsync();
        }
        return this;
    }

    public void awaitStopped() {
        this.state.awaitStopped();
    }

    public void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
        this.state.awaitStopped(timeout, unit);
    }

    public boolean isHealthy() {
        Iterator i$ = this.services.iterator();
        while (i$.hasNext()) {
            Service service = (Service) i$.next();
            if (!service.isRunning()) {
                return false;
            }
        }
        return true;
    }

    public ImmutableMultimap<Service.State, Service> servicesByState() {
        return this.state.servicesByState();
    }

    public ImmutableMap<Service, Long> startupTimes() {
        return this.state.startupTimes();
    }

    public String toString() {
        return MoreObjects.toStringHelper((Class<?>) ServiceManager.class).add("services", Collections2.filter(this.services, Predicates.not(Predicates.instanceOf(NoOpService.class)))).toString();
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ServiceManager$ServiceManagerState.class */
    private static final class ServiceManagerState {

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        boolean ready;

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        boolean transitioned;
        final int numberOfServices;
        final Monitor monitor = new Monitor();

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        final SetMultimap<Service.State, Service> servicesByState = Multimaps.newSetMultimap(new EnumMap(Service.State.class), new Supplier<Set<Service>>() { // from class: com.google.common.util.concurrent.ServiceManager.ServiceManagerState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.Supplier
            public Set<Service> get() {
                return Sets.newLinkedHashSet();
            }
        });

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        final Multiset<Service.State> states = this.servicesByState.keys();

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
        final Monitor.Guard awaitHealthGuard = new Monitor.Guard(this.monitor) { // from class: com.google.common.util.concurrent.ServiceManager.ServiceManagerState.2
            @Override // com.google.common.util.concurrent.Monitor.Guard
            public boolean isSatisfied() {
                return ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManagerState.this.numberOfServices || ServiceManagerState.this.states.contains(Service.State.STOPPING) || ServiceManagerState.this.states.contains(Service.State.TERMINATED) || ServiceManagerState.this.states.contains(Service.State.FAILED);
            }
        };
        final Monitor.Guard stoppedGuard = new Monitor.Guard(this.monitor) { // from class: com.google.common.util.concurrent.ServiceManager.ServiceManagerState.3
            @Override // com.google.common.util.concurrent.Monitor.Guard
            public boolean isSatisfied() {
                return ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManagerState.this.numberOfServices;
            }
        };

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        final List<ListenerCallQueue<Listener>> listeners = Collections.synchronizedList(new ArrayList());

        ServiceManagerState(ImmutableCollection<Service> services) {
            this.numberOfServices = services.size();
            this.servicesByState.putAll(Service.State.NEW, services);
        }

        void tryStartTiming(Service service) {
            this.monitor.enter();
            try {
                Stopwatch stopwatch = this.startupTimers.get(service);
                if (stopwatch == null) {
                    this.startupTimers.put(service, Stopwatch.createStarted());
                }
            } finally {
                this.monitor.leave();
            }
        }

        void markReady() {
            this.monitor.enter();
            try {
                if (!this.transitioned) {
                    this.ready = true;
                    return;
                }
                List<Service> servicesInBadStates = Lists.newArrayList();
                Iterator i$ = servicesByState().values().iterator();
                while (i$.hasNext()) {
                    Service service = (Service) i$.next();
                    if (service.state() != Service.State.NEW) {
                        servicesInBadStates.add(service);
                    }
                }
                String strValueOf = String.valueOf(String.valueOf("Services started transitioning asynchronously before the ServiceManager was constructed: "));
                String strValueOf2 = String.valueOf(String.valueOf(servicesInBadStates));
                throw new IllegalArgumentException(new StringBuilder(0 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(strValueOf2).toString());
            } finally {
                this.monitor.leave();
            }
        }

        void addListener(Listener listener, Executor executor) {
            Preconditions.checkNotNull(listener, "listener");
            Preconditions.checkNotNull(executor, "executor");
            this.monitor.enter();
            try {
                if (!this.stoppedGuard.isSatisfied()) {
                    this.listeners.add(new ListenerCallQueue<>(listener, executor));
                }
            } finally {
                this.monitor.leave();
            }
        }

        void awaitHealthy() {
            this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
            try {
                checkHealthy();
                this.monitor.leave();
            } catch (Throwable th) {
                this.monitor.leave();
                throw th;
            }
        }

        void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
            this.monitor.enter();
            try {
                if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
                    String strValueOf = String.valueOf(String.valueOf("Timeout waiting for the services to become healthy. The following services have not started: "));
                    String strValueOf2 = String.valueOf(String.valueOf(Multimaps.filterKeys((SetMultimap) this.servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING)))));
                    throw new TimeoutException(new StringBuilder(0 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(strValueOf2).toString());
                }
                checkHealthy();
                this.monitor.leave();
            } catch (Throwable th) {
                this.monitor.leave();
                throw th;
            }
        }

        void awaitStopped() {
            this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
            this.monitor.leave();
        }

        void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
            this.monitor.enter();
            try {
                if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit)) {
                    String strValueOf = String.valueOf(String.valueOf("Timeout waiting for the services to stop. The following services have not stopped: "));
                    String strValueOf2 = String.valueOf(String.valueOf(Multimaps.filterKeys((SetMultimap) this.servicesByState, Predicates.not(Predicates.in(ImmutableSet.of(Service.State.TERMINATED, Service.State.FAILED))))));
                    throw new TimeoutException(new StringBuilder(0 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(strValueOf2).toString());
                }
            } finally {
                this.monitor.leave();
            }
        }

        ImmutableMultimap<Service.State, Service> servicesByState() {
            ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
            this.monitor.enter();
            try {
                for (Map.Entry<Service.State, Service> entry : this.servicesByState.entries()) {
                    if (!(entry.getValue() instanceof NoOpService)) {
                        builder.put((ImmutableSetMultimap.Builder<Service.State, Service>) entry.getKey(), (Service.State) entry.getValue());
                    }
                }
                return builder.build();
            } finally {
                this.monitor.leave();
            }
        }

        ImmutableMap<Service, Long> startupTimes() {
            this.monitor.enter();
            try {
                List<Map.Entry<Service, Long>> loadTimes = Lists.newArrayListWithCapacity(this.startupTimers.size());
                for (Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
                    Service service = entry.getKey();
                    Stopwatch stopWatch = entry.getValue();
                    if (!stopWatch.isRunning() && !(service instanceof NoOpService)) {
                        loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopWatch.elapsed(TimeUnit.MILLISECONDS))));
                    }
                }
                Collections.sort(loadTimes, Ordering.natural().onResultOf(new Function<Map.Entry<Service, Long>, Long>() { // from class: com.google.common.util.concurrent.ServiceManager.ServiceManagerState.4
                    @Override // com.google.common.base.Function
                    public Long apply(Map.Entry<Service, Long> input) {
                        return input.getValue();
                    }
                }));
                ImmutableMap.Builder<Service, Long> builder = ImmutableMap.builder();
                Iterator i$ = loadTimes.iterator();
                while (i$.hasNext()) {
                    builder.put(i$.next());
                }
                return builder.build();
            } finally {
                this.monitor.leave();
            }
        }

        void transitionService(Service service, Service.State from, Service.State to) {
            Preconditions.checkNotNull(service);
            Preconditions.checkArgument(from != to);
            this.monitor.enter();
            try {
                this.transitioned = true;
                if (this.ready) {
                    Preconditions.checkState(this.servicesByState.remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
                    Preconditions.checkState(this.servicesByState.put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
                    Stopwatch stopwatch = this.startupTimers.get(service);
                    if (stopwatch == null) {
                        stopwatch = Stopwatch.createStarted();
                        this.startupTimers.put(service, stopwatch);
                    }
                    if (to.compareTo(Service.State.RUNNING) >= 0 && stopwatch.isRunning()) {
                        stopwatch.stop();
                        if (!(service instanceof NoOpService)) {
                            ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[]{service, stopwatch});
                        }
                    }
                    if (to == Service.State.FAILED) {
                        fireFailedListeners(service);
                    }
                    if (this.states.count(Service.State.RUNNING) == this.numberOfServices) {
                        fireHealthyListeners();
                    } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
                        fireStoppedListeners();
                    }
                    this.monitor.leave();
                    executeListeners();
                }
            } finally {
                this.monitor.leave();
                executeListeners();
            }
        }

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        void fireStoppedListeners() {
            ServiceManager.STOPPED_CALLBACK.enqueueOn(this.listeners);
        }

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        void fireHealthyListeners() {
            ServiceManager.HEALTHY_CALLBACK.enqueueOn(this.listeners);
        }

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        void fireFailedListeners(final Service service) {
            String strValueOf = String.valueOf(String.valueOf(service));
            new ListenerCallQueue.Callback<Listener>(new StringBuilder(18 + strValueOf.length()).append("failed({service=").append(strValueOf).append("})").toString()) { // from class: com.google.common.util.concurrent.ServiceManager.ServiceManagerState.5
                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.google.common.util.concurrent.ListenerCallQueue.Callback
                public void call(Listener listener) {
                    listener.failure(service);
                }
            }.enqueueOn(this.listeners);
        }

        void executeListeners() {
            Preconditions.checkState(!this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
            for (int i = 0; i < this.listeners.size(); i++) {
                this.listeners.get(i).execute();
            }
        }

        @GuardedBy(Protocol.SENTINEL_MONITOR)
        void checkHealthy() {
            if (this.states.count(Service.State.RUNNING) != this.numberOfServices) {
                String strValueOf = String.valueOf(String.valueOf(Multimaps.filterKeys((SetMultimap) this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING)))));
                IllegalStateException exception = new IllegalStateException(new StringBuilder(79 + strValueOf.length()).append("Expected to be healthy after starting. The following services are not running: ").append(strValueOf).toString());
                throw exception;
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ServiceManager$ServiceListener.class */
    private static final class ServiceListener extends Service.Listener {
        final Service service;
        final WeakReference<ServiceManagerState> state;

        ServiceListener(Service service, WeakReference<ServiceManagerState> state) {
            this.service = service;
            this.state = state;
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void starting() {
            ServiceManagerState state = this.state.get();
            if (state != null) {
                state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
                if (!(this.service instanceof NoOpService)) {
                    ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
                }
            }
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void running() {
            ServiceManagerState state = this.state.get();
            if (state != null) {
                state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
            }
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void stopping(Service.State from) {
            ServiceManagerState state = this.state.get();
            if (state != null) {
                state.transitionService(this.service, from, Service.State.STOPPING);
            }
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void terminated(Service.State from) {
            ServiceManagerState state = this.state.get();
            if (state != null) {
                if (!(this.service instanceof NoOpService)) {
                    ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[]{this.service, from});
                }
                state.transitionService(this.service, from, Service.State.TERMINATED);
            }
        }

        @Override // com.google.common.util.concurrent.Service.Listener
        public void failed(Service.State from, Throwable failure) {
            ServiceManagerState state = this.state.get();
            if (state != null) {
                if (!(this.service instanceof NoOpService)) {
                    Logger logger = ServiceManager.logger;
                    Level level = Level.SEVERE;
                    String strValueOf = String.valueOf(String.valueOf(this.service));
                    String strValueOf2 = String.valueOf(String.valueOf(from));
                    logger.log(level, new StringBuilder(34 + strValueOf.length() + strValueOf2.length()).append("Service ").append(strValueOf).append(" has failed in the ").append(strValueOf2).append(" state.").toString(), failure);
                }
                state.transitionService(this.service, from, Service.State.FAILED);
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ServiceManager$NoOpService.class */
    private static final class NoOpService extends AbstractService {
        private NoOpService() {
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected void doStart() {
            notifyStarted();
        }

        @Override // com.google.common.util.concurrent.AbstractService
        protected void doStop() {
            notifyStopped();
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ServiceManager$EmptyServiceManagerWarning.class */
    private static final class EmptyServiceManagerWarning extends Throwable {
        private EmptyServiceManagerWarning() {
        }
    }
}
