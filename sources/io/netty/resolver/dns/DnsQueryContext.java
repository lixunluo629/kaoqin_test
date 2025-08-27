package io.netty.resolver.dns;

import io.netty.channel.AddressedEnvelope;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.dns.AbstractDnsOptPseudoRrRecord;
import io.netty.handler.codec.dns.DnsQuery;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/resolver/dns/DnsQueryContext.class */
abstract class DnsQueryContext implements FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DnsQueryContext.class);
    private final DnsNameResolver parent;
    private final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise;
    private final int id;
    private final DnsQuestion question;
    private final DnsRecord[] additionals;
    private final DnsRecord optResource;
    private final InetSocketAddress nameServerAddr;
    private final boolean recursionDesired;
    private volatile ScheduledFuture<?> timeoutFuture;

    protected abstract DnsQuery newQuery(int i);

    protected abstract Channel channel();

    protected abstract String protocol();

    DnsQueryContext(DnsNameResolver parent, InetSocketAddress nameServerAddr, DnsQuestion question, DnsRecord[] additionals, Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> promise) {
        this.parent = (DnsNameResolver) ObjectUtil.checkNotNull(parent, "parent");
        this.nameServerAddr = (InetSocketAddress) ObjectUtil.checkNotNull(nameServerAddr, "nameServerAddr");
        this.question = (DnsQuestion) ObjectUtil.checkNotNull(question, "question");
        this.additionals = (DnsRecord[]) ObjectUtil.checkNotNull(additionals, "additionals");
        this.promise = (Promise) ObjectUtil.checkNotNull(promise, "promise");
        this.recursionDesired = parent.isRecursionDesired();
        this.id = parent.queryContextManager.add(this);
        promise.addListener2((GenericFutureListener<? extends Future<? super AddressedEnvelope<DnsResponse, InetSocketAddress>>>) this);
        if (parent.isOptResourceEnabled()) {
            this.optResource = new AbstractDnsOptPseudoRrRecord(parent.maxPayloadSize(), 0, 0) { // from class: io.netty.resolver.dns.DnsQueryContext.1
            };
        } else {
            this.optResource = null;
        }
    }

    InetSocketAddress nameServerAddr() {
        return this.nameServerAddr;
    }

    DnsQuestion question() {
        return this.question;
    }

    DnsNameResolver parent() {
        return this.parent;
    }

    void query(boolean flush, ChannelPromise writePromise) {
        DnsQuestion question = question();
        InetSocketAddress nameServerAddr = nameServerAddr();
        DnsQuery query = newQuery(this.id);
        query.setRecursionDesired(this.recursionDesired);
        query.addRecord(DnsSection.QUESTION, (DnsRecord) question);
        for (DnsRecord record : this.additionals) {
            query.addRecord(DnsSection.ADDITIONAL, record);
        }
        if (this.optResource != null) {
            query.addRecord(DnsSection.ADDITIONAL, this.optResource);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("{} WRITE: {}, [{}: {}], {}", channel(), protocol(), Integer.valueOf(this.id), nameServerAddr, question);
        }
        sendQuery(query, flush, writePromise);
    }

    private void sendQuery(final DnsQuery query, boolean flush, final ChannelPromise writePromise) {
        if (this.parent.channelFuture.isDone()) {
            writeQuery(query, flush, writePromise);
        } else {
            this.parent.channelFuture.addListener2(new GenericFutureListener<Future<? super Channel>>() { // from class: io.netty.resolver.dns.DnsQueryContext.2
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(Future<? super Channel> future) {
                    if (future.isSuccess()) {
                        DnsQueryContext.this.writeQuery(query, true, writePromise);
                        return;
                    }
                    Throwable cause = future.cause();
                    DnsQueryContext.this.promise.tryFailure(cause);
                    writePromise.setFailure(cause);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeQuery(DnsQuery query, boolean flush, ChannelPromise writePromise) {
        final ChannelFuture writeFuture = flush ? channel().writeAndFlush(query, writePromise) : channel().write(query, writePromise);
        if (writeFuture.isDone()) {
            onQueryWriteCompletion(writeFuture);
        } else {
            writeFuture.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.resolver.dns.DnsQueryContext.3
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future) {
                    DnsQueryContext.this.onQueryWriteCompletion(writeFuture);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onQueryWriteCompletion(ChannelFuture writeFuture) {
        if (!writeFuture.isSuccess()) {
            tryFailure("failed to send a query via " + protocol(), writeFuture.cause(), false);
            return;
        }
        final long queryTimeoutMillis = this.parent.queryTimeoutMillis();
        if (queryTimeoutMillis > 0) {
            this.timeoutFuture = this.parent.f7ch.eventLoop().schedule(new Runnable() { // from class: io.netty.resolver.dns.DnsQueryContext.4
                @Override // java.lang.Runnable
                public void run() {
                    if (DnsQueryContext.this.promise.isDone()) {
                        return;
                    }
                    DnsQueryContext.this.tryFailure("query via " + DnsQueryContext.this.protocol() + " timed out after " + queryTimeoutMillis + " milliseconds", null, true);
                }
            }, queryTimeoutMillis, TimeUnit.MILLISECONDS);
        }
    }

    void finish(AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope) {
        DnsResponse res = envelope.content();
        if (res.count(DnsSection.QUESTION) != 1) {
            logger.warn("Received a DNS response with invalid number of questions: {}", envelope);
        } else if (!question().equals(res.recordAt(DnsSection.QUESTION))) {
            logger.warn("Received a mismatching DNS response: {}", envelope);
        } else if (trySuccess(envelope)) {
            return;
        }
        envelope.release();
    }

    private boolean trySuccess(AddressedEnvelope<? extends DnsResponse, InetSocketAddress> envelope) {
        return this.promise.trySuccess(envelope);
    }

    boolean tryFailure(String message, Throwable cause, boolean timeout) {
        DnsNameResolverException e;
        if (this.promise.isDone()) {
            return false;
        }
        InetSocketAddress nameServerAddr = nameServerAddr();
        StringBuilder buf = new StringBuilder(message.length() + 64);
        buf.append('[').append(nameServerAddr).append("] ").append(message).append(" (no stack trace available)");
        if (timeout) {
            e = new DnsNameResolverTimeoutException(nameServerAddr, question(), buf.toString());
        } else {
            e = new DnsNameResolverException(nameServerAddr, question(), buf.toString(), cause);
        }
        return this.promise.tryFailure(e);
    }

    @Override // io.netty.util.concurrent.GenericFutureListener
    public void operationComplete(Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future) {
        ScheduledFuture<?> timeoutFuture = this.timeoutFuture;
        if (timeoutFuture != null) {
            this.timeoutFuture = null;
            timeoutFuture.cancel(false);
        }
        this.parent.queryContextManager.remove(this.nameServerAddr, this.id);
    }
}
