package org.springframework.boot.context.embedded.undertow;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.attribute.RequestHeaderAttribute;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import java.lang.reflect.Field;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.embedded.Compression;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
import org.springframework.boot.context.embedded.PortInUseException;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.xnio.channels.BoundChannel;
import redis.clients.jedis.Protocol;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainer.class */
public class UndertowEmbeddedServletContainer implements EmbeddedServletContainer {
    private static final Log logger = LogFactory.getLog(UndertowEmbeddedServletContainer.class);
    private final Object monitor;
    private final Undertow.Builder builder;
    private final DeploymentManager manager;
    private final String contextPath;
    private final boolean useForwardHeaders;
    private final boolean autoStart;
    private final Compression compression;
    private final String serverHeader;
    private Undertow undertow;
    private volatile boolean started;

    public UndertowEmbeddedServletContainer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean autoStart, Compression compression) {
        this(builder, manager, contextPath, false, autoStart, compression);
    }

    public UndertowEmbeddedServletContainer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean useForwardHeaders, boolean autoStart, Compression compression) {
        this(builder, manager, contextPath, useForwardHeaders, autoStart, compression, null);
    }

    public UndertowEmbeddedServletContainer(Undertow.Builder builder, DeploymentManager manager, String contextPath, boolean useForwardHeaders, boolean autoStart, Compression compression, String serverHeader) {
        this.monitor = new Object();
        this.started = false;
        this.builder = builder;
        this.manager = manager;
        this.contextPath = contextPath;
        this.useForwardHeaders = useForwardHeaders;
        this.autoStart = autoStart;
        this.compression = compression;
        this.serverHeader = serverHeader;
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public void start() throws EmbeddedServletContainerException {
        synchronized (this.monitor) {
            if (this.started) {
                return;
            }
            try {
                if (this.autoStart) {
                    if (this.undertow == null) {
                        this.undertow = createUndertowServer();
                    }
                    this.undertow.start();
                    this.started = true;
                    logger.info("Undertow started on port(s) " + getPortsDescription());
                }
            } catch (Exception ex) {
                try {
                    if (findBindException(ex) != null) {
                        List<Port> failedPorts = getConfiguredPorts();
                        List<Port> actualPorts = getActualPorts();
                        failedPorts.removeAll(actualPorts);
                        if (failedPorts.size() == 1) {
                            throw new PortInUseException(failedPorts.iterator().next().getNumber());
                        }
                    }
                    throw new EmbeddedServletContainerException("Unable to start embedded Undertow", ex);
                } catch (Throwable th) {
                    stopSilently();
                    throw th;
                }
            }
        }
    }

    private void stopSilently() {
        try {
            if (this.manager != null) {
                this.manager.stop();
            }
            if (this.undertow != null) {
                this.undertow.stop();
            }
        } catch (Exception e) {
        }
    }

    private BindException findBindException(Exception ex) {
        Throwable cause = ex;
        while (true) {
            Throwable candidate = cause;
            if (candidate != null) {
                if (candidate instanceof BindException) {
                    return (BindException) candidate;
                }
                cause = candidate.getCause();
            } else {
                return null;
            }
        }
    }

    private Undertow createUndertowServer() throws ServletException {
        HttpHandler httpHandler = getContextHandler(this.manager.start());
        if (this.useForwardHeaders) {
            httpHandler = Handlers.proxyPeerAddress(httpHandler);
        }
        if (StringUtils.hasText(this.serverHeader)) {
            httpHandler = Handlers.header(httpHandler, "Server", this.serverHeader);
        }
        this.builder.setHandler(httpHandler);
        return this.builder.build();
    }

    private HttpHandler getContextHandler(HttpHandler httpHandler) {
        HttpHandler contextHandler = configurationCompressionIfNecessary(httpHandler);
        if (StringUtils.isEmpty(this.contextPath)) {
            return contextHandler;
        }
        return Handlers.path().addPrefixPath(this.contextPath, contextHandler);
    }

    private HttpHandler configurationCompressionIfNecessary(HttpHandler httpHandler) {
        if (this.compression == null || !this.compression.getEnabled()) {
            return httpHandler;
        }
        ContentEncodingRepository repository = new ContentEncodingRepository();
        repository.addEncodingHandler("gzip", new GzipEncodingProvider(), 50, Predicates.and(getCompressionPredicates(this.compression)));
        return new EncodingHandler(repository).setNext(httpHandler);
    }

    private Predicate[] getCompressionPredicates(Compression compression) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(new MaxSizePredicate(compression.getMinResponseSize()));
        predicates.add(new CompressibleMimeTypePredicate(compression.getMimeTypes()));
        if (compression.getExcludedUserAgents() != null) {
            for (String agent : compression.getExcludedUserAgents()) {
                RequestHeaderAttribute agentHeader = new RequestHeaderAttribute(new HttpString("User-Agent"));
                predicates.add(Predicates.not(Predicates.regex(agentHeader, agent)));
            }
        }
        return (Predicate[]) predicates.toArray(new Predicate[predicates.size()]);
    }

    private String getPortsDescription() {
        List<Port> ports = getActualPorts();
        if (!ports.isEmpty()) {
            return StringUtils.collectionToDelimitedString(ports, SymbolConstants.SPACE_SYMBOL);
        }
        return "unknown";
    }

    private List<Port> getActualPorts() {
        List<Port> ports = new ArrayList<>();
        try {
            if (!this.autoStart) {
                ports.add(new Port(-1, "unknown"));
            } else {
                for (BoundChannel channel : extractChannels()) {
                    ports.add(getPortFromChannel(channel));
                }
            }
        } catch (Exception e) {
        }
        return ports;
    }

    private List<BoundChannel> extractChannels() {
        Field channelsField = ReflectionUtils.findField(Undertow.class, Protocol.PUBSUB_CHANNELS);
        ReflectionUtils.makeAccessible(channelsField);
        return (List) ReflectionUtils.getField(channelsField, this.undertow);
    }

    private Port getPortFromChannel(BoundChannel channel) {
        SocketAddress socketAddress = channel.getLocalAddress();
        if (socketAddress instanceof InetSocketAddress) {
            Field field = ReflectionUtils.findField(channel.getClass(), "ssl");
            String protocol = field != null ? "https" : "http";
            return new Port(((InetSocketAddress) socketAddress).getPort(), protocol);
        }
        return null;
    }

    private List<Port> getConfiguredPorts() {
        List<Port> ports = new ArrayList<>();
        for (Object listener : extractListeners()) {
            try {
                ports.add(getPortFromListener(listener));
            } catch (Exception e) {
            }
        }
        return ports;
    }

    private List<Object> extractListeners() {
        Field listenersField = ReflectionUtils.findField(Undertow.class, "listeners");
        ReflectionUtils.makeAccessible(listenersField);
        return (List) ReflectionUtils.getField(listenersField, this.undertow);
    }

    private Port getPortFromListener(Object listener) {
        Field typeField = ReflectionUtils.findField(listener.getClass(), "type");
        ReflectionUtils.makeAccessible(typeField);
        String protocol = ReflectionUtils.getField(typeField, listener).toString();
        Field portField = ReflectionUtils.findField(listener.getClass(), "port");
        ReflectionUtils.makeAccessible(portField);
        int port = ((Integer) ReflectionUtils.getField(portField, listener)).intValue();
        return new Port(port, protocol);
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public void stop() throws EmbeddedServletContainerException {
        synchronized (this.monitor) {
            if (this.started) {
                this.started = false;
                try {
                    this.manager.stop();
                    this.manager.undeploy();
                    this.undertow.stop();
                } catch (Exception ex) {
                    throw new EmbeddedServletContainerException("Unable to stop undertow", ex);
                }
            }
        }
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedServletContainer
    public int getPort() {
        List<Port> ports = getActualPorts();
        if (ports.isEmpty()) {
            return 0;
        }
        return ports.get(0).getNumber();
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainer$Port.class */
    private static final class Port {
        private final int number;
        private final String protocol;

        private Port(int number, String protocol) {
            this.number = number;
            this.protocol = protocol;
        }

        public int getNumber() {
            return this.number;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Port other = (Port) obj;
            if (this.number != other.number) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return this.number;
        }

        public String toString() {
            return this.number + " (" + this.protocol + ")";
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainer$CompressibleMimeTypePredicate.class */
    private static class CompressibleMimeTypePredicate implements Predicate {
        private final List<MimeType> mimeTypes;

        CompressibleMimeTypePredicate(String[] mimeTypes) {
            this.mimeTypes = new ArrayList(mimeTypes.length);
            for (String mimeTypeString : mimeTypes) {
                this.mimeTypes.add(MimeTypeUtils.parseMimeType(mimeTypeString));
            }
        }

        public boolean resolve(HttpServerExchange value) {
            String contentType = value.getResponseHeaders().getFirst("Content-Type");
            if (contentType != null) {
                for (MimeType mimeType : this.mimeTypes) {
                    if (mimeType.isCompatibleWith(MimeTypeUtils.parseMimeType(contentType))) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/undertow/UndertowEmbeddedServletContainer$MaxSizePredicate.class */
    private static class MaxSizePredicate implements Predicate {
        private final Predicate maxContentSize;

        MaxSizePredicate(int size) {
            this.maxContentSize = Predicates.maxContentSize(size);
        }

        public boolean resolve(HttpServerExchange value) {
            if (value.getResponseHeaders().contains(Headers.CONTENT_LENGTH)) {
                return this.maxContentSize.resolve(value);
            }
            return true;
        }
    }
}
