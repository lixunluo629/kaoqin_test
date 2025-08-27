package org.bouncycastle.mime;

import ch.qos.logback.classic.net.SyslogAppender;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.netty.handler.codec.http.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.bouncycastle.util.Iterable;
import org.bouncycastle.util.Strings;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/Headers.class */
public class Headers implements Iterable<String> {
    private final Map<String, List> headers;
    private final List<String> headersAsPresented;
    private final String contentTransferEncoding;
    private String boundary;
    private boolean multipart;
    private String contentType;
    private Map<String, String> contentTypeParameters;

    /* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/mime/Headers$KV.class */
    private class KV {
        public final String key;
        public final String value;

        public KV(String str, String str2) {
            this.key = str;
            this.value = str2;
        }

        public KV(KV kv) {
            this.key = kv.key;
            this.value = kv.value;
        }
    }

    private static List<String> parseHeaders(InputStream inputStream) throws IOException {
        ArrayList arrayList = new ArrayList();
        LineReader lineReader = new LineReader(inputStream);
        while (true) {
            String line = lineReader.readLine();
            if (line == null || line.length() == 0) {
                break;
            }
            arrayList.add(line);
        }
        return arrayList;
    }

    public Headers(InputStream inputStream, String str) throws IOException {
        this(parseHeaders(inputStream), str);
    }

    public Headers(List<String> list, String str) {
        this.headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.headersAsPresented = list;
        String str2 = "";
        for (String str3 : list) {
            if (str3.startsWith(SymbolConstants.SPACE_SYMBOL) || str3.startsWith(SyslogAppender.DEFAULT_STACKTRACE_PATTERN)) {
                str2 = str2 + str3.trim();
            } else {
                if (str2.length() != 0) {
                    put(str2.substring(0, str2.indexOf(58)).trim(), str2.substring(str2.indexOf(58) + 1).trim());
                }
                str2 = str3;
            }
        }
        if (str2.trim().length() != 0) {
            put(str2.substring(0, str2.indexOf(58)).trim(), str2.substring(str2.indexOf(58) + 1).trim());
        }
        String str4 = getValues("Content-Type") == null ? "text/plain" : getValues("Content-Type")[0];
        int iIndexOf = str4.indexOf(59);
        if (iIndexOf < 0) {
            this.contentType = str4;
            this.contentTypeParameters = Collections.EMPTY_MAP;
        } else {
            this.contentType = str4.substring(0, iIndexOf);
            this.contentTypeParameters = createContentTypeParameters(str4.substring(iIndexOf + 1).trim());
        }
        this.contentTransferEncoding = getValues(HttpHeaders.Names.CONTENT_TRANSFER_ENCODING) == null ? str : getValues(HttpHeaders.Names.CONTENT_TRANSFER_ENCODING)[0];
        if (this.contentType.indexOf("multipart") < 0) {
            this.boundary = null;
            this.multipart = false;
        } else {
            this.multipart = true;
            String str5 = this.contentTypeParameters.get(HttpHeaders.Values.BOUNDARY);
            this.boundary = str5.substring(1, str5.length() - 1);
        }
    }

    public Map<String, String> getContentTypeAttributes() {
        return this.contentTypeParameters;
    }

    private Map<String, String> createContentTypeParameters(String str) {
        String[] strArrSplit = str.split(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; i != strArrSplit.length; i++) {
            String str2 = strArrSplit[i];
            int iIndexOf = str2.indexOf(61);
            if (iIndexOf < 0) {
                throw new IllegalArgumentException("malformed Content-Type header");
            }
            linkedHashMap.put(str2.substring(0, iIndexOf).trim(), str2.substring(iIndexOf + 1).trim());
        }
        return Collections.unmodifiableMap(linkedHashMap);
    }

    public boolean isMultipart() {
        return this.multipart;
    }

    public String getBoundary() {
        return this.boundary;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getContentTransferEncoding() {
        return this.contentTransferEncoding;
    }

    private void put(String str, String str2) {
        synchronized (this) {
            KV kv = new KV(str, str2);
            List arrayList = this.headers.get(str);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.headers.put(str, arrayList);
            }
            arrayList.add(kv);
        }
    }

    public Iterator<String> getNames() {
        return this.headers.keySet().iterator();
    }

    public String[] getValues(String str) {
        synchronized (this) {
            List list = this.headers.get(str);
            if (list == null) {
                return null;
            }
            String[] strArr = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                strArr[i] = ((KV) list.get(i)).value;
            }
            return strArr;
        }
    }

    public boolean isEmpty() {
        boolean zIsEmpty;
        synchronized (this) {
            zIsEmpty = this.headers.isEmpty();
        }
        return zIsEmpty;
    }

    public boolean containsKey(String str) {
        return this.headers.containsKey(str);
    }

    @Override // org.bouncycastle.util.Iterable, java.lang.Iterable
    public Iterator<String> iterator() {
        return this.headers.keySet().iterator();
    }

    public void dumpHeaders(OutputStream outputStream) throws IOException {
        Iterator<String> it = this.headersAsPresented.iterator();
        while (it.hasNext()) {
            outputStream.write(Strings.toUTF8ByteArray(it.next().toString()));
            outputStream.write(13);
            outputStream.write(10);
        }
    }
}
