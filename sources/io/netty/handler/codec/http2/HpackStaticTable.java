package io.netty.handler.codec.http2;

import io.netty.handler.codec.UnsupportedValueConverter;
import io.netty.util.AsciiString;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.httpclient.cookie.RFC2109Spec;
import org.apache.naming.EjbRef;
import org.springframework.web.servlet.support.WebContentGenerator;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/HpackStaticTable.class */
final class HpackStaticTable {
    private static final List<HpackHeaderField> STATIC_TABLE = Arrays.asList(newEmptyHeaderField(":authority"), newHeaderField(":method", "GET"), newHeaderField(":method", WebContentGenerator.METHOD_POST), newHeaderField(":path", "/"), newHeaderField(":path", "/index.html"), newHeaderField(":scheme", "http"), newHeaderField(":scheme", "https"), newHeaderField(":status", "200"), newHeaderField(":status", "204"), newHeaderField(":status", "206"), newHeaderField(":status", "304"), newHeaderField(":status", "400"), newHeaderField(":status", "404"), newHeaderField(":status", "500"), newEmptyHeaderField("accept-charset"), newHeaderField("accept-encoding", "gzip, deflate"), newEmptyHeaderField("accept-language"), newEmptyHeaderField("accept-ranges"), newEmptyHeaderField("accept"), newEmptyHeaderField("access-control-allow-origin"), newEmptyHeaderField("age"), newEmptyHeaderField("allow"), newEmptyHeaderField("authorization"), newEmptyHeaderField("cache-control"), newEmptyHeaderField("content-disposition"), newEmptyHeaderField("content-encoding"), newEmptyHeaderField("content-language"), newEmptyHeaderField("content-length"), newEmptyHeaderField("content-location"), newEmptyHeaderField("content-range"), newEmptyHeaderField("content-type"), newEmptyHeaderField("cookie"), newEmptyHeaderField("date"), newEmptyHeaderField("etag"), newEmptyHeaderField("expect"), newEmptyHeaderField("expires"), newEmptyHeaderField("from"), newEmptyHeaderField("host"), newEmptyHeaderField("if-match"), newEmptyHeaderField("if-modified-since"), newEmptyHeaderField("if-none-match"), newEmptyHeaderField("if-range"), newEmptyHeaderField("if-unmodified-since"), newEmptyHeaderField("last-modified"), newEmptyHeaderField(EjbRef.LINK), newEmptyHeaderField("location"), newEmptyHeaderField("max-forwards"), newEmptyHeaderField("proxy-authenticate"), newEmptyHeaderField("proxy-authorization"), newEmptyHeaderField("range"), newEmptyHeaderField("referer"), newEmptyHeaderField("refresh"), newEmptyHeaderField("retry-after"), newEmptyHeaderField("server"), newEmptyHeaderField(RFC2109Spec.SET_COOKIE_KEY), newEmptyHeaderField("strict-transport-security"), newEmptyHeaderField("transfer-encoding"), newEmptyHeaderField("user-agent"), newEmptyHeaderField("vary"), newEmptyHeaderField("via"), newEmptyHeaderField("www-authenticate"));
    private static final CharSequenceMap<Integer> STATIC_INDEX_BY_NAME = createMap();
    static final int length = STATIC_TABLE.size();

    private static HpackHeaderField newEmptyHeaderField(String name) {
        return new HpackHeaderField(AsciiString.cached(name), AsciiString.EMPTY_STRING);
    }

    private static HpackHeaderField newHeaderField(String name, String value) {
        return new HpackHeaderField(AsciiString.cached(name), AsciiString.cached(value));
    }

    static HpackHeaderField getEntry(int index) {
        return STATIC_TABLE.get(index - 1);
    }

    static int getIndex(CharSequence name) {
        Integer index = STATIC_INDEX_BY_NAME.get(name);
        if (index == null) {
            return -1;
        }
        return index.intValue();
    }

    static int getIndexInsensitive(CharSequence name, CharSequence value) {
        int index = getIndex(name);
        if (index == -1) {
            return -1;
        }
        while (index <= length) {
            HpackHeaderField entry = getEntry(index);
            if (HpackUtil.equalsVariableTime(name, entry.name) && HpackUtil.equalsVariableTime(value, entry.value)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static CharSequenceMap<Integer> createMap() {
        int length2 = STATIC_TABLE.size();
        CharSequenceMap<Integer> ret = new CharSequenceMap<>(true, UnsupportedValueConverter.instance(), length2);
        for (int index = length2; index > 0; index--) {
            HpackHeaderField entry = getEntry(index);
            CharSequence name = entry.name;
            ret.set((CharSequenceMap<Integer>) name, (CharSequence) Integer.valueOf(index));
        }
        return ret;
    }

    private HpackStaticTable() {
    }
}
