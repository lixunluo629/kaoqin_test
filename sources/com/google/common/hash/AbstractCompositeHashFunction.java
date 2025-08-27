package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.charset.Charset;

/* loaded from: guava-18.0.jar:com/google/common/hash/AbstractCompositeHashFunction.class */
abstract class AbstractCompositeHashFunction extends AbstractStreamingHashFunction {
    final HashFunction[] functions;
    private static final long serialVersionUID = 0;

    abstract HashCode makeHash(Hasher[] hasherArr);

    AbstractCompositeHashFunction(HashFunction... functions) {
        for (HashFunction function : functions) {
            Preconditions.checkNotNull(function);
        }
        this.functions = functions;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        final Hasher[] hashers = new Hasher[this.functions.length];
        for (int i = 0; i < hashers.length; i++) {
            hashers[i] = this.functions[i].newHasher();
        }
        return new Hasher() { // from class: com.google.common.hash.AbstractCompositeHashFunction.1
            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putByte(byte b) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putByte(b);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putBytes(byte[] bytes) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putBytes(bytes);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putBytes(byte[] bytes, int off, int len) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putBytes(bytes, off, len);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putShort(short s) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putShort(s);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putInt(int i2) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putInt(i2);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putLong(long l) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putLong(l);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putFloat(float f) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putFloat(f);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putDouble(double d) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putDouble(d);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putBoolean(boolean b) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putBoolean(b);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putChar(char c) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putChar(c);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putUnencodedChars(CharSequence chars) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putUnencodedChars(chars);
                }
                return this;
            }

            @Override // com.google.common.hash.PrimitiveSink
            public Hasher putString(CharSequence chars, Charset charset) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putString(chars, charset);
                }
                return this;
            }

            @Override // com.google.common.hash.Hasher
            public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
                Hasher[] arr$ = hashers;
                for (Hasher hasher : arr$) {
                    hasher.putObject(instance, funnel);
                }
                return this;
            }

            @Override // com.google.common.hash.Hasher
            public HashCode hash() {
                return AbstractCompositeHashFunction.this.makeHash(hashers);
            }
        };
    }
}
