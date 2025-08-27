package io.netty.resolver.dns;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/UnixResolverOptions.class */
final class UnixResolverOptions {
    private final int ndots;
    private final int timeout;
    private final int attempts;

    UnixResolverOptions(int ndots, int timeout, int attempts) {
        this.ndots = ndots;
        this.timeout = timeout;
        this.attempts = attempts;
    }

    static Builder newBuilder() {
        return new Builder();
    }

    int ndots() {
        return this.ndots;
    }

    int timeout() {
        return this.timeout;
    }

    int attempts() {
        return this.attempts;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/UnixResolverOptions$Builder.class */
    static final class Builder {
        private int ndots;
        private int timeout;
        private int attempts;

        private Builder() {
            this.ndots = 1;
            this.timeout = 5;
            this.attempts = 16;
        }

        void setNdots(int ndots) {
            this.ndots = ndots;
        }

        void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        void setAttempts(int attempts) {
            this.attempts = attempts;
        }

        UnixResolverOptions build() {
            return new UnixResolverOptions(this.ndots, this.timeout, this.attempts);
        }
    }
}
