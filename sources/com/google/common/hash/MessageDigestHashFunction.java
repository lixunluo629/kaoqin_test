package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/* loaded from: guava-18.0.jar:com/google/common/hash/MessageDigestHashFunction.class */
final class MessageDigestHashFunction extends AbstractStreamingHashFunction implements Serializable {
    private final MessageDigest prototype;
    private final int bytes;
    private final boolean supportsClone;
    private final String toString;

    MessageDigestHashFunction(String algorithmName, String toString) {
        this.prototype = getMessageDigest(algorithmName);
        this.bytes = this.prototype.getDigestLength();
        this.toString = (String) Preconditions.checkNotNull(toString);
        this.supportsClone = supportsClone();
    }

    MessageDigestHashFunction(String algorithmName, int bytes, String toString) {
        this.toString = (String) Preconditions.checkNotNull(toString);
        this.prototype = getMessageDigest(algorithmName);
        int maxLength = this.prototype.getDigestLength();
        Preconditions.checkArgument(bytes >= 4 && bytes <= maxLength, "bytes (%s) must be >= 4 and < %s", Integer.valueOf(bytes), Integer.valueOf(maxLength));
        this.bytes = bytes;
        this.supportsClone = supportsClone();
    }

    private boolean supportsClone() throws CloneNotSupportedException {
        try {
            this.prototype.clone();
            return true;
        } catch (CloneNotSupportedException e) {
            return false;
        }
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return this.bytes * 8;
    }

    public String toString() {
        return this.toString;
    }

    private static MessageDigest getMessageDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        if (this.supportsClone) {
            try {
                return new MessageDigestHasher((MessageDigest) this.prototype.clone(), this.bytes);
            } catch (CloneNotSupportedException e) {
            }
        }
        return new MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/MessageDigestHashFunction$SerializedForm.class */
    private static final class SerializedForm implements Serializable {
        private final String algorithmName;
        private final int bytes;
        private final String toString;
        private static final long serialVersionUID = 0;

        private SerializedForm(String algorithmName, int bytes, String toString) {
            this.algorithmName = algorithmName;
            this.bytes = bytes;
            this.toString = toString;
        }

        private Object readResolve() {
            return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
        }
    }

    Object writeReplace() {
        return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString);
    }

    /* loaded from: guava-18.0.jar:com/google/common/hash/MessageDigestHashFunction$MessageDigestHasher.class */
    private static final class MessageDigestHasher extends AbstractByteHasher {
        private final MessageDigest digest;
        private final int bytes;
        private boolean done;

        private MessageDigestHasher(MessageDigest digest, int bytes) {
            this.digest = digest;
            this.bytes = bytes;
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte b) {
            checkNotDone();
            this.digest.update(b);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte[] b) {
            checkNotDone();
            this.digest.update(b);
        }

        @Override // com.google.common.hash.AbstractByteHasher
        protected void update(byte[] b, int off, int len) {
            checkNotDone();
            this.digest.update(b, off, len);
        }

        private void checkNotDone() {
            Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
        }

        @Override // com.google.common.hash.Hasher
        public HashCode hash() {
            checkNotDone();
            this.done = true;
            return this.bytes == this.digest.getDigestLength() ? HashCode.fromBytesNoCopy(this.digest.digest()) : HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
        }
    }
}
