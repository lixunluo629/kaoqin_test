package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.StreamByteDistributor;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayDeque;
import java.util.Deque;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/UniformStreamByteDistributor.class */
public final class UniformStreamByteDistributor implements StreamByteDistributor {
    private final Http2Connection.PropertyKey stateKey;
    private final Deque<State> queue = new ArrayDeque(4);
    private int minAllocationChunk = 1024;
    private long totalStreamableBytes;

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$202(io.netty.handler.codec.http2.UniformStreamByteDistributor r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.totalStreamableBytes = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.UniformStreamByteDistributor.access$202(io.netty.handler.codec.http2.UniformStreamByteDistributor, long):long");
    }

    public UniformStreamByteDistributor(Http2Connection connection) {
        this.stateKey = connection.newKey();
        Http2Stream connectionStream = connection.connectionStream();
        connectionStream.setProperty(this.stateKey, new State(connectionStream));
        connection.addListener(new Http2ConnectionAdapter() { // from class: io.netty.handler.codec.http2.UniformStreamByteDistributor.1
            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamAdded(Http2Stream stream) {
                stream.setProperty(UniformStreamByteDistributor.this.stateKey, UniformStreamByteDistributor.this.new State(stream));
            }

            @Override // io.netty.handler.codec.http2.Http2ConnectionAdapter, io.netty.handler.codec.http2.Http2Connection.Listener
            public void onStreamClosed(Http2Stream stream) {
                UniformStreamByteDistributor.this.state(stream).close();
            }
        });
    }

    public void minAllocationChunk(int minAllocationChunk) {
        ObjectUtil.checkPositive(minAllocationChunk, "minAllocationChunk");
        this.minAllocationChunk = minAllocationChunk;
    }

    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    public void updateStreamableBytes(StreamByteDistributor.StreamState streamState) {
        state(streamState.stream()).updateStreamableBytes(Http2CodecUtil.streamableBytes(streamState), streamState.hasFrame(), streamState.windowSize());
    }

    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    public void updateDependencyTree(int childStreamId, int parentStreamId, short weight, boolean exclusive) {
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0097 A[EDGE_INSN: B:27:0x0097->B:21:0x0097 BREAK  A[LOOP:0: B:10:0x0037->B:28:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:28:? A[LOOP:0: B:10:0x0037->B:28:?, LOOP_END, SYNTHETIC] */
    @Override // io.netty.handler.codec.http2.StreamByteDistributor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean distribute(int r6, io.netty.handler.codec.http2.StreamByteDistributor.Writer r7) throws io.netty.handler.codec.http2.Http2Exception {
        /*
            r5 = this;
            r0 = r5
            java.util.Deque<io.netty.handler.codec.http2.UniformStreamByteDistributor$State> r0 = r0.queue
            int r0 = r0.size()
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L1d
            r0 = r5
            long r0 = r0.totalStreamableBytes
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L1b
            r0 = 1
            goto L1c
        L1b:
            r0 = 0
        L1c:
            return r0
        L1d:
            r0 = r5
            int r0 = r0.minAllocationChunk
            r1 = r6
            r2 = r8
            int r1 = r1 / r2
            int r0 = java.lang.Math.max(r0, r1)
            r9 = r0
            r0 = r5
            java.util.Deque<io.netty.handler.codec.http2.UniformStreamByteDistributor$State> r0 = r0.queue
            java.lang.Object r0 = r0.pollFirst()
            io.netty.handler.codec.http2.UniformStreamByteDistributor$State r0 = (io.netty.handler.codec.http2.UniformStreamByteDistributor.State) r0
            r10 = r0
        L37:
            r0 = r10
            r1 = 0
            r0.enqueued = r1
            r0 = r10
            boolean r0 = r0.windowNegative
            if (r0 == 0) goto L48
            goto L85
        L48:
            r0 = r6
            if (r0 != 0) goto L68
            r0 = r10
            int r0 = r0.streamableBytes
            if (r0 <= 0) goto L68
            r0 = r5
            java.util.Deque<io.netty.handler.codec.http2.UniformStreamByteDistributor$State> r0 = r0.queue
            r1 = r10
            r0.addFirst(r1)
            r0 = r10
            r1 = 1
            r0.enqueued = r1
            goto L97
        L68:
            r0 = r9
            r1 = r6
            r2 = r10
            int r2 = r2.streamableBytes
            int r1 = java.lang.Math.min(r1, r2)
            int r0 = java.lang.Math.min(r0, r1)
            r11 = r0
            r0 = r6
            r1 = r11
            int r0 = r0 - r1
            r6 = r0
            r0 = r10
            r1 = r11
            r2 = r7
            r0.write(r1, r2)
        L85:
            r0 = r5
            java.util.Deque<io.netty.handler.codec.http2.UniformStreamByteDistributor$State> r0 = r0.queue
            java.lang.Object r0 = r0.pollFirst()
            io.netty.handler.codec.http2.UniformStreamByteDistributor$State r0 = (io.netty.handler.codec.http2.UniformStreamByteDistributor.State) r0
            r1 = r0
            r10 = r1
            if (r0 != 0) goto L37
        L97:
            r0 = r5
            long r0 = r0.totalStreamableBytes
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto La4
            r0 = 1
            goto La5
        La4:
            r0 = 0
        La5:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.http2.UniformStreamByteDistributor.distribute(int, io.netty.handler.codec.http2.StreamByteDistributor$Writer):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public State state(Http2Stream stream) {
        return (State) ((Http2Stream) ObjectUtil.checkNotNull(stream, "stream")).getProperty(this.stateKey);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/UniformStreamByteDistributor$State.class */
    private final class State {
        final Http2Stream stream;
        int streamableBytes;
        boolean windowNegative;
        boolean enqueued;
        boolean writing;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !UniformStreamByteDistributor.class.desiredAssertionStatus();
        }

        State(Http2Stream stream) {
            this.stream = stream;
        }

        /* JADX WARN: Failed to check method for inline after forced processio.netty.handler.codec.http2.UniformStreamByteDistributor.access$202(io.netty.handler.codec.http2.UniformStreamByteDistributor, long):long */
        void updateStreamableBytes(int newStreamableBytes, boolean hasFrame, int windowSize) {
            if (!$assertionsDisabled && !hasFrame && newStreamableBytes != 0) {
                throw new AssertionError("hasFrame: " + hasFrame + " newStreamableBytes: " + newStreamableBytes);
            }
            int delta = newStreamableBytes - this.streamableBytes;
            if (delta != 0) {
                this.streamableBytes = newStreamableBytes;
                UniformStreamByteDistributor.access$202(UniformStreamByteDistributor.this, UniformStreamByteDistributor.this.totalStreamableBytes + delta);
            }
            this.windowNegative = windowSize < 0;
            if (hasFrame) {
                if (windowSize > 0 || (windowSize == 0 && !this.writing)) {
                    addToQueue();
                }
            }
        }

        void write(int numBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
            this.writing = true;
            try {
                try {
                    writer.write(this.stream, numBytes);
                    this.writing = false;
                } catch (Throwable t) {
                    throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "byte distribution write error", new Object[0]);
                }
            } catch (Throwable th) {
                this.writing = false;
                throw th;
            }
        }

        void addToQueue() {
            if (!this.enqueued) {
                this.enqueued = true;
                UniformStreamByteDistributor.this.queue.addLast(this);
            }
        }

        void removeFromQueue() {
            if (this.enqueued) {
                this.enqueued = false;
                UniformStreamByteDistributor.this.queue.remove(this);
            }
        }

        void close() {
            removeFromQueue();
            updateStreamableBytes(0, false, 0);
        }
    }
}
