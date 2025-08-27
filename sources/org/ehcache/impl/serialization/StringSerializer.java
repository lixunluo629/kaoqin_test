package org.ehcache.impl.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/serialization/StringSerializer.class */
public class StringSerializer implements Serializer<String> {
    public StringSerializer() {
    }

    public StringSerializer(ClassLoader classLoader) {
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public ByteBuffer serialize(String object) throws IOException {
        char c;
        ByteArrayOutputStream bout = new ByteArrayOutputStream(object.length());
        try {
            int length = object.length();
            int i = 0;
            while (i < length && (c = object.charAt(i)) != 0 && c <= 127) {
                bout.write(c);
                i++;
            }
            while (i < length) {
                char c2 = object.charAt(i);
                if (c2 == 0) {
                    bout.write(192);
                    bout.write(128);
                } else if (c2 < 128) {
                    bout.write(c2);
                } else if (c2 < 2048) {
                    bout.write(192 | ((c2 >>> 6) & 31));
                    bout.write(128 | (c2 & '?'));
                } else {
                    bout.write(224 | ((c2 >>> '\f') & 31));
                    bout.write(128 | ((c2 >>> 6) & 63));
                    bout.write(128 | (c2 & '?'));
                }
                i++;
            }
            try {
                bout.close();
                return ByteBuffer.wrap(bout.toByteArray());
            } catch (IOException ex) {
                throw new AssertionError(ex);
            }
        } catch (Throwable th) {
            try {
                bout.close();
                throw th;
            } catch (IOException ex2) {
                throw new AssertionError(ex2);
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.spi.serialization.Serializer
    public String read(ByteBuffer binary) throws ClassNotFoundException {
        int codepoint;
        StringBuilder sb = new StringBuilder(binary.remaining());
        int i = binary.position();
        int end = binary.limit();
        while (i < end) {
            byte a = binary.get(i);
            if ((a & 128) != 0) {
                break;
            }
            sb.append((char) a);
            i++;
        }
        while (i < end) {
            byte a2 = binary.get(i);
            if ((a2 & 128) == 0) {
                sb.append((char) a2);
            } else if ((a2 & 224) == 192) {
                i++;
                sb.append((char) (((a2 & 31) << 6) | (binary.get(i) & 63)));
            } else if ((a2 & 240) == 224) {
                int i2 = i + 1;
                int i3 = ((a2 & 15) << 12) | ((binary.get(i2) & 63) << 6);
                i = i2 + 1;
                sb.append((char) (i3 | (binary.get(i) & 63)));
            } else {
                if ((a2 & 248) == 240) {
                    int i4 = i + 1;
                    int i5 = ((a2 & 7) << 18) | ((binary.get(i4) & 63) << 12);
                    int i6 = i4 + 1;
                    int i7 = i5 | ((binary.get(i6) & 63) << 6);
                    i = i6 + 1;
                    codepoint = i7 | (binary.get(i) & 63);
                } else if ((a2 & 252) == 248) {
                    int i8 = i + 1;
                    int i9 = ((a2 & 3) << 24) | ((binary.get(i8) & 63) << 18);
                    int i10 = i8 + 1;
                    int i11 = i9 | ((binary.get(i10) & 63) << 12);
                    int i12 = i10 + 1;
                    int i13 = i11 | ((binary.get(i12) & 63) << 6);
                    i = i12 + 1;
                    codepoint = i13 | (binary.get(i) & 63);
                } else if ((a2 & 254) == 252) {
                    int i14 = i + 1;
                    int i15 = ((a2 & 1) << 30) | ((binary.get(i14) & 63) << 24);
                    int i16 = i14 + 1;
                    int i17 = i15 | ((binary.get(i16) & 63) << 18);
                    int i18 = i16 + 1;
                    int i19 = i17 | ((binary.get(i18) & 63) << 12);
                    int i20 = i18 + 1;
                    int i21 = i19 | ((binary.get(i20) & 63) << 6);
                    i = i20 + 1;
                    codepoint = i21 | (binary.get(i) & 63);
                } else {
                    throw new SerializerException("Unexpected encoding");
                }
                sb.appendCodePoint(codepoint);
            }
            i++;
        }
        return sb.toString();
    }

    @Override // org.ehcache.spi.serialization.Serializer
    public boolean equals(String object, ByteBuffer binary) throws ClassNotFoundException {
        int codepoint;
        if (binary.remaining() < object.length()) {
            return false;
        }
        int bEnd = binary.limit();
        int bi = binary.position();
        int sLength = object.length();
        int si = 0;
        while (bi < bEnd && si < sLength) {
            byte a = binary.get(bi);
            if ((a & 128) != 0) {
                break;
            }
            if (object.charAt(si) == ((char) a)) {
                bi++;
                si++;
            } else {
                return false;
            }
        }
        while (bi < bEnd && si < sLength) {
            byte a2 = binary.get(bi);
            if ((a2 & 128) == 0) {
                if (object.charAt(si) != ((char) a2)) {
                    return false;
                }
            } else if ((a2 & 224) == 192) {
                bi++;
                if (object.charAt(si) != ((char) (((a2 & 31) << 6) | (binary.get(bi) & 63)))) {
                    return false;
                }
            } else if ((a2 & 240) == 224) {
                char cCharAt = object.charAt(si);
                int bi2 = bi + 1;
                int i = ((a2 & 15) << 12) | ((binary.get(bi2) & 63) << 6);
                bi = bi2 + 1;
                if (cCharAt != ((char) (i | (binary.get(bi) & 63)))) {
                    return false;
                }
            } else {
                if ((a2 & 248) == 240) {
                    int bi3 = bi + 1;
                    int i2 = ((a2 & 7) << 18) | ((binary.get(bi3) & 63) << 12);
                    int bi4 = bi3 + 1;
                    int i3 = i2 | ((binary.get(bi4) & 63) << 6);
                    bi = bi4 + 1;
                    codepoint = i3 | (binary.get(bi) & 63);
                } else if ((a2 & 252) == 248) {
                    int bi5 = bi + 1;
                    int i4 = ((a2 & 3) << 24) | ((binary.get(bi5) & 63) << 18);
                    int bi6 = bi5 + 1;
                    int i5 = i4 | ((binary.get(bi6) & 63) << 12);
                    int bi7 = bi6 + 1;
                    int i6 = i5 | ((binary.get(bi7) & 63) << 6);
                    bi = bi7 + 1;
                    codepoint = i6 | (binary.get(bi) & 63);
                } else if ((a2 & 254) == 252) {
                    int bi8 = bi + 1;
                    int i7 = ((a2 & 1) << 30) | ((binary.get(bi8) & 63) << 24);
                    int bi9 = bi8 + 1;
                    int i8 = i7 | ((binary.get(bi9) & 63) << 18);
                    int bi10 = bi9 + 1;
                    int i9 = i8 | ((binary.get(bi10) & 63) << 12);
                    int bi11 = bi10 + 1;
                    int i10 = i9 | ((binary.get(bi11) & 63) << 6);
                    bi = bi11 + 1;
                    codepoint = i10 | (binary.get(bi) & 63);
                } else {
                    throw new SerializerException("Unrecognized encoding");
                }
                char[] chars = Character.toChars(codepoint);
                if (si + 1 == sLength || object.charAt(si) != chars[0]) {
                    return false;
                }
                si++;
                if (object.charAt(si) != chars[1]) {
                    return false;
                }
            }
            bi++;
            si++;
        }
        return bi == bEnd && si == sLength;
    }
}
