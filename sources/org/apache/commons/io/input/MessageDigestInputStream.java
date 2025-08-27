package org.apache.commons.io.input;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.input.ObservableInputStream;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/MessageDigestInputStream.class */
public final class MessageDigestInputStream extends ObservableInputStream {
    private final MessageDigest messageDigest;

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/MessageDigestInputStream$Builder.class */
    public static class Builder extends ObservableInputStream.AbstractBuilder<Builder> {
        private MessageDigest messageDigest;

        @Override // org.apache.commons.io.function.IOSupplier
        public MessageDigestInputStream get() throws IOException {
            setObservers(Arrays.asList(new MessageDigestMaintainingObserver(this.messageDigest)));
            return new MessageDigestInputStream(this);
        }

        public Builder setMessageDigest(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
            return this;
        }

        public Builder setMessageDigest(String algorithm) throws NoSuchAlgorithmException {
            this.messageDigest = MessageDigest.getInstance(algorithm);
            return this;
        }
    }

    /* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/input/MessageDigestInputStream$MessageDigestMaintainingObserver.class */
    public static class MessageDigestMaintainingObserver extends ObservableInputStream.Observer {
        private final MessageDigest messageDigest;

        public MessageDigestMaintainingObserver(MessageDigest messageDigest) {
            this.messageDigest = (MessageDigest) Objects.requireNonNull(messageDigest, "messageDigest");
        }

        @Override // org.apache.commons.io.input.ObservableInputStream.Observer
        public void data(byte[] input, int offset, int length) throws IOException {
            this.messageDigest.update(input, offset, length);
        }

        @Override // org.apache.commons.io.input.ObservableInputStream.Observer
        public void data(int input) throws IOException {
            this.messageDigest.update((byte) input);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private MessageDigestInputStream(Builder builder) throws IOException {
        super(builder);
        this.messageDigest = (MessageDigest) Objects.requireNonNull(builder.messageDigest, "builder.messageDigest");
    }

    public MessageDigest getMessageDigest() {
        return this.messageDigest;
    }
}
