package io.netty.handler.codec.http;

import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.handler.codec.DateFormatter;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.DefaultHeadersImpl;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.ValueConverter;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.PlatformDependent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/DefaultHttpHeaders.class */
public class DefaultHttpHeaders extends HttpHeaders {
    private static final int HIGHEST_INVALID_VALUE_CHAR_MASK = -16;
    private static final ByteProcessor HEADER_NAME_VALIDATOR = new ByteProcessor() { // from class: io.netty.handler.codec.http.DefaultHttpHeaders.1
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            DefaultHttpHeaders.validateHeaderNameElement(value);
            return true;
        }
    };
    static final DefaultHeaders.NameValidator<CharSequence> HttpNameValidator = new DefaultHeaders.NameValidator<CharSequence>() { // from class: io.netty.handler.codec.http.DefaultHttpHeaders.2
        @Override // io.netty.handler.codec.DefaultHeaders.NameValidator
        public void validateName(CharSequence name) {
            if (name == null || name.length() == 0) {
                throw new IllegalArgumentException("empty headers are not allowed [" + ((Object) name) + "]");
            }
            if (name instanceof AsciiString) {
                try {
                    ((AsciiString) name).forEachByte(DefaultHttpHeaders.HEADER_NAME_VALIDATOR);
                    return;
                } catch (Exception e) {
                    PlatformDependent.throwException(e);
                    return;
                }
            }
            for (int index = 0; index < name.length(); index++) {
                DefaultHttpHeaders.validateHeaderNameElement(name.charAt(index));
            }
        }
    };
    private final DefaultHeaders<CharSequence, CharSequence, ?> headers;

    public DefaultHttpHeaders() {
        this(true);
    }

    public DefaultHttpHeaders(boolean validate) {
        this(validate, nameValidator(validate));
    }

    protected DefaultHttpHeaders(boolean validate, DefaultHeaders.NameValidator<CharSequence> nameValidator) {
        this(new DefaultHeadersImpl(AsciiString.CASE_INSENSITIVE_HASHER, valueConverter(validate), nameValidator));
    }

    protected DefaultHttpHeaders(DefaultHeaders<CharSequence, CharSequence, ?> headers) {
        this.headers = headers;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            this.headers.add(((DefaultHttpHeaders) headers).headers);
            return this;
        }
        return super.add(headers);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            this.headers.set(((DefaultHttpHeaders) headers).headers);
            return this;
        }
        return super.set(headers);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(String name, Object value) {
        this.headers.addObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(CharSequence name, Object value) {
        this.headers.addObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(String name, Iterable<?> values) {
        this.headers.addObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, values);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(CharSequence name, Iterable<?> values) {
        this.headers.addObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, values);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders addInt(CharSequence name, int value) {
        this.headers.addInt(name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders addShort(CharSequence name, short value) {
        this.headers.addShort(name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders remove(String name) {
        this.headers.remove(name);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders remove(CharSequence name) {
        this.headers.remove(name);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(String name, Object value) {
        this.headers.setObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(CharSequence name, Object value) {
        this.headers.setObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(String name, Iterable<?> values) {
        this.headers.setObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, values);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(CharSequence name, Iterable<?> values) {
        this.headers.setObject((DefaultHeaders<CharSequence, CharSequence, ?>) name, values);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders setInt(CharSequence name, int value) {
        this.headers.setInt(name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders setShort(CharSequence name, short value) {
        this.headers.setShort(name, value);
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders clear() {
        this.headers.clear();
        return this;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public String get(String name) {
        return get((CharSequence) name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public String get(CharSequence name) {
        return HeadersUtils.getAsString(this.headers, name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Integer getInt(CharSequence name) {
        return this.headers.getInt(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public int getInt(CharSequence name, int defaultValue) {
        return this.headers.getInt(name, defaultValue);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Short getShort(CharSequence name) {
        return this.headers.getShort(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public short getShort(CharSequence name, short defaultValue) {
        return this.headers.getShort(name, defaultValue);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Long getTimeMillis(CharSequence name) {
        return this.headers.getTimeMillis(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public long getTimeMillis(CharSequence name, long defaultValue) {
        return this.headers.getTimeMillis(name, defaultValue);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<String> getAll(String name) {
        return getAll((CharSequence) name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<String> getAll(CharSequence name) {
        return HeadersUtils.getAllAsString(this.headers, name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<Map.Entry<String, String>> entries() {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        List<Map.Entry<String, String>> entriesConverted = new ArrayList<>(this.headers.size());
        Iterator<Map.Entry<String, String>> it = iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            entriesConverted.add(entry);
        }
        return entriesConverted;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders, java.lang.Iterable
    @Deprecated
    public Iterator<Map.Entry<String, String>> iterator() {
        return HeadersUtils.iteratorAsString(this.headers);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        return this.headers.iterator();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<String> valueStringIterator(CharSequence name) {
        final Iterator<CharSequence> itr = valueCharSequenceIterator(name);
        return new Iterator<String>() { // from class: io.netty.handler.codec.http.DefaultHttpHeaders.3
            @Override // java.util.Iterator
            public boolean hasNext() {
                return itr.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public String next() {
                return ((CharSequence) itr.next()).toString();
            }

            @Override // java.util.Iterator
            public void remove() {
                itr.remove();
            }
        };
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<CharSequence> valueCharSequenceIterator(CharSequence name) {
        return this.headers.valueIterator(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(String name) {
        return contains((CharSequence) name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(CharSequence name) {
        return this.headers.contains(name);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public int size() {
        return this.headers.size();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(String name, String value, boolean ignoreCase) {
        return contains((CharSequence) name, (CharSequence) value, ignoreCase);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
        return this.headers.contains(name, value, ignoreCase ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Set<String> names() {
        return HeadersUtils.namesAsString(this.headers);
    }

    public boolean equals(Object o) {
        return (o instanceof DefaultHttpHeaders) && this.headers.equals(((DefaultHttpHeaders) o).headers, AsciiString.CASE_SENSITIVE_HASHER);
    }

    public int hashCode() {
        return this.headers.hashCode(AsciiString.CASE_SENSITIVE_HASHER);
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders copy() {
        return new DefaultHttpHeaders((DefaultHeaders<CharSequence, CharSequence, ?>) this.headers.copy());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateHeaderNameElement(byte value) {
        switch (value) {
            case 0:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 32:
            case 44:
            case 58:
            case 59:
            case 61:
                throw new IllegalArgumentException("a header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + ((int) value));
            default:
                if (value < 0) {
                    throw new IllegalArgumentException("a header name cannot contain non-ASCII character: " + ((int) value));
                }
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateHeaderNameElement(char value) {
        switch (value) {
            case 0:
            case '\t':
            case '\n':
            case 11:
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case ';':
            case '=':
                throw new IllegalArgumentException("a header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: " + value);
            default:
                if (value > 127) {
                    throw new IllegalArgumentException("a header name cannot contain non-ASCII character: " + value);
                }
                return;
        }
    }

    static ValueConverter<CharSequence> valueConverter(boolean validate) {
        return validate ? HeaderValueConverterAndValidator.INSTANCE : HeaderValueConverter.INSTANCE;
    }

    static DefaultHeaders.NameValidator<CharSequence> nameValidator(boolean validate) {
        return validate ? HttpNameValidator : DefaultHeaders.NameValidator.NOT_NULL;
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/DefaultHttpHeaders$HeaderValueConverter.class */
    private static class HeaderValueConverter extends CharSequenceValueConverter {
        static final HeaderValueConverter INSTANCE = new HeaderValueConverter();

        private HeaderValueConverter() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.handler.codec.CharSequenceValueConverter, io.netty.handler.codec.ValueConverter
        public CharSequence convertObject(Object value) {
            if (value instanceof CharSequence) {
                return (CharSequence) value;
            }
            if (value instanceof Date) {
                return DateFormatter.format((Date) value);
            }
            if (value instanceof Calendar) {
                return DateFormatter.format(((Calendar) value).getTime());
            }
            return value.toString();
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/DefaultHttpHeaders$HeaderValueConverterAndValidator.class */
    private static final class HeaderValueConverterAndValidator extends HeaderValueConverter {
        static final HeaderValueConverterAndValidator INSTANCE = new HeaderValueConverterAndValidator();

        private HeaderValueConverterAndValidator() {
            super();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.netty.handler.codec.http.DefaultHttpHeaders.HeaderValueConverter, io.netty.handler.codec.CharSequenceValueConverter, io.netty.handler.codec.ValueConverter
        public CharSequence convertObject(Object value) {
            CharSequence seq = super.convertObject(value);
            int state = 0;
            for (int index = 0; index < seq.length(); index++) {
                state = validateValueChar(seq, state, seq.charAt(index));
            }
            if (state != 0) {
                throw new IllegalArgumentException("a header value must not end with '\\r' or '\\n':" + ((Object) seq));
            }
            return seq;
        }

        private static int validateValueChar(CharSequence seq, int state, char character) {
            if ((character & 65520) == 0) {
                switch (character) {
                    case 0:
                        throw new IllegalArgumentException("a header value contains a prohibited character '��': " + ((Object) seq));
                    case 11:
                        throw new IllegalArgumentException("a header value contains a prohibited character '\\v': " + ((Object) seq));
                    case '\f':
                        throw new IllegalArgumentException("a header value contains a prohibited character '\\f': " + ((Object) seq));
                }
            }
            switch (state) {
                case 0:
                    switch (character) {
                        case '\n':
                            return 2;
                        case '\r':
                            return 1;
                    }
                case 1:
                    switch (character) {
                        case '\n':
                            return 2;
                        default:
                            throw new IllegalArgumentException("only '\\n' is allowed after '\\r': " + ((Object) seq));
                    }
                case 2:
                    switch (character) {
                        case '\t':
                        case ' ':
                            return 0;
                        default:
                            throw new IllegalArgumentException("only ' ' and '\\t' are allowed after '\\n': " + ((Object) seq));
                    }
            }
            return state;
        }
    }
}
