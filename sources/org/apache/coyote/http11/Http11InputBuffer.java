package org.apache.coyote.http11;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.apache.coyote.InputBuffer;
import org.apache.coyote.Request;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.apache.tomcat.util.net.ApplicationBufferHandler;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/Http11InputBuffer.class */
public class Http11InputBuffer implements InputBuffer, ApplicationBufferHandler {
    private static final Log log = LogFactory.getLog((Class<?>) Http11InputBuffer.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) Http11InputBuffer.class);
    private static final byte[] CLIENT_PREFACE_START = "PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1);
    private final Request request;
    private final MimeHeaders headers;
    private final boolean rejectIllegalHeaderName;
    private ByteBuffer byteBuffer;
    private int end;
    private SocketWrapperBase<?> wrapper;
    private int parsingRequestLinePhase;
    private boolean parsingRequestLineEol;
    private int parsingRequestLineStart;
    private int parsingRequestLineQPos;
    private final HttpParser httpParser;
    private final int headerBufferSize;
    private int socketReadBufferSize;
    private final HeaderParseData headerData = new HeaderParseData();
    private InputFilter[] filterLibrary = new InputFilter[0];
    private InputFilter[] activeFilters = new InputFilter[0];
    private int lastActiveFilter = -1;
    private boolean parsingHeader = true;
    private boolean parsingRequestLine = true;
    private HeaderParsePosition headerParsePos = HeaderParsePosition.HEADER_START;
    private boolean swallowInput = true;
    private InputBuffer inputStreamInputBuffer = new SocketInputBuffer();

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/Http11InputBuffer$HeaderParsePosition.class */
    private enum HeaderParsePosition {
        HEADER_START,
        HEADER_NAME,
        HEADER_VALUE_START,
        HEADER_VALUE,
        HEADER_MULTI_LINE,
        HEADER_SKIPLINE
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/Http11InputBuffer$HeaderParseStatus.class */
    private enum HeaderParseStatus {
        DONE,
        HAVE_MORE_HEADERS,
        NEED_MORE_DATA
    }

    public Http11InputBuffer(Request request, int headerBufferSize, boolean rejectIllegalHeaderName, HttpParser httpParser) {
        this.parsingRequestLinePhase = 0;
        this.parsingRequestLineEol = false;
        this.parsingRequestLineStart = 0;
        this.parsingRequestLineQPos = -1;
        this.request = request;
        this.headers = request.getMimeHeaders();
        this.headerBufferSize = headerBufferSize;
        this.rejectIllegalHeaderName = rejectIllegalHeaderName;
        this.httpParser = httpParser;
        this.parsingRequestLinePhase = 0;
        this.parsingRequestLineEol = false;
        this.parsingRequestLineStart = 0;
        this.parsingRequestLineQPos = -1;
    }

    void addFilter(InputFilter filter) {
        if (filter == null) {
            throw new NullPointerException(sm.getString("iib.filter.npe"));
        }
        InputFilter[] newFilterLibrary = new InputFilter[this.filterLibrary.length + 1];
        for (int i = 0; i < this.filterLibrary.length; i++) {
            newFilterLibrary[i] = this.filterLibrary[i];
        }
        newFilterLibrary[this.filterLibrary.length] = filter;
        this.filterLibrary = newFilterLibrary;
        this.activeFilters = new InputFilter[this.filterLibrary.length];
    }

    InputFilter[] getFilters() {
        return this.filterLibrary;
    }

    void addActiveFilter(InputFilter filter) {
        if (this.lastActiveFilter == -1) {
            filter.setBuffer(this.inputStreamInputBuffer);
        } else {
            for (int i = 0; i <= this.lastActiveFilter; i++) {
                if (this.activeFilters[i] == filter) {
                    return;
                }
            }
            filter.setBuffer(this.activeFilters[this.lastActiveFilter]);
        }
        InputFilter[] inputFilterArr = this.activeFilters;
        int i2 = this.lastActiveFilter + 1;
        this.lastActiveFilter = i2;
        inputFilterArr[i2] = filter;
        filter.setRequest(this.request);
    }

    void setSwallowInput(boolean swallowInput) {
        this.swallowInput = swallowInput;
    }

    @Override // org.apache.coyote.InputBuffer
    @Deprecated
    public int doRead(ByteChunk chunk) throws IOException {
        if (this.lastActiveFilter == -1) {
            return this.inputStreamInputBuffer.doRead(chunk);
        }
        return this.activeFilters[this.lastActiveFilter].doRead(chunk);
    }

    @Override // org.apache.coyote.InputBuffer
    public int doRead(ApplicationBufferHandler handler) throws IOException {
        if (this.lastActiveFilter == -1) {
            return this.inputStreamInputBuffer.doRead(handler);
        }
        return this.activeFilters[this.lastActiveFilter].doRead(handler);
    }

    void recycle() {
        this.wrapper = null;
        this.request.recycle();
        for (int i = 0; i <= this.lastActiveFilter; i++) {
            this.activeFilters[i].recycle();
        }
        this.byteBuffer.limit(0).position(0);
        this.lastActiveFilter = -1;
        this.parsingHeader = true;
        this.swallowInput = true;
        this.headerParsePos = HeaderParsePosition.HEADER_START;
        this.parsingRequestLine = true;
        this.parsingRequestLinePhase = 0;
        this.parsingRequestLineEol = false;
        this.parsingRequestLineStart = 0;
        this.parsingRequestLineQPos = -1;
        this.headerData.recycle();
    }

    void nextRequest() {
        this.request.recycle();
        if (this.byteBuffer.position() > 0) {
            if (this.byteBuffer.remaining() > 0) {
                this.byteBuffer.compact();
                this.byteBuffer.flip();
            } else {
                this.byteBuffer.position(0).limit(0);
            }
        }
        for (int i = 0; i <= this.lastActiveFilter; i++) {
            this.activeFilters[i].recycle();
        }
        this.lastActiveFilter = -1;
        this.parsingHeader = true;
        this.swallowInput = true;
        this.headerParsePos = HeaderParsePosition.HEADER_START;
        this.parsingRequestLine = true;
        this.parsingRequestLinePhase = 0;
        this.parsingRequestLineEol = false;
        this.parsingRequestLineStart = 0;
        this.parsingRequestLineQPos = -1;
        this.headerData.recycle();
    }

    boolean parseRequestLine(boolean keptAlive) throws IOException {
        if (!this.parsingRequestLine) {
            return true;
        }
        if (this.parsingRequestLinePhase < 2) {
            while (true) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit()) {
                    if (keptAlive) {
                        this.wrapper.setReadTimeout(this.wrapper.getEndpoint().getKeepAliveTimeout());
                    }
                    if (!fill(false)) {
                        this.parsingRequestLinePhase = 1;
                        return false;
                    }
                    this.wrapper.setReadTimeout(this.wrapper.getEndpoint().getConnectionTimeout());
                }
                if (!keptAlive && this.byteBuffer.position() == 0 && this.byteBuffer.limit() >= CLIENT_PREFACE_START.length - 1) {
                    boolean prefaceMatch = true;
                    for (int i = 0; i < CLIENT_PREFACE_START.length && prefaceMatch; i++) {
                        if (CLIENT_PREFACE_START[i] != this.byteBuffer.get(i)) {
                            prefaceMatch = false;
                        }
                    }
                    if (prefaceMatch) {
                        this.parsingRequestLinePhase = -1;
                        return false;
                    }
                }
                if (this.request.getStartTime() < 0) {
                    this.request.setStartTime(System.currentTimeMillis());
                }
                byte chr = this.byteBuffer.get();
                if (chr != 13 && chr != 10) {
                    this.byteBuffer.position(this.byteBuffer.position() - 1);
                    this.parsingRequestLineStart = this.byteBuffer.position();
                    this.parsingRequestLinePhase = 2;
                    if (log.isDebugEnabled()) {
                        log.debug("Received [" + new String(this.byteBuffer.array(), this.byteBuffer.position(), this.byteBuffer.remaining(), StandardCharsets.ISO_8859_1) + "]");
                    }
                }
            }
        }
        if (this.parsingRequestLinePhase == 2) {
            boolean space = false;
            while (!space) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                int pos = this.byteBuffer.position();
                byte chr2 = this.byteBuffer.get();
                if (chr2 == 32 || chr2 == 9) {
                    space = true;
                    this.request.method().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, pos - this.parsingRequestLineStart);
                } else if (!HttpParser.isToken(chr2)) {
                    this.byteBuffer.position(this.byteBuffer.position() - 1);
                    this.request.protocol().setString(Constants.HTTP_11);
                    throw new IllegalArgumentException(sm.getString("iib.invalidmethod"));
                }
            }
            this.parsingRequestLinePhase = 3;
        }
        if (this.parsingRequestLinePhase == 3) {
            boolean space2 = true;
            while (space2) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                byte chr3 = this.byteBuffer.get();
                if (chr3 != 32 && chr3 != 9) {
                    space2 = false;
                    this.byteBuffer.position(this.byteBuffer.position() - 1);
                }
            }
            this.parsingRequestLineStart = this.byteBuffer.position();
            this.parsingRequestLinePhase = 4;
        }
        if (this.parsingRequestLinePhase == 4) {
            int end = 0;
            boolean space3 = false;
            while (!space3) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                int pos2 = this.byteBuffer.position();
                byte chr4 = this.byteBuffer.get();
                if (chr4 == 32 || chr4 == 9) {
                    space3 = true;
                    end = pos2;
                } else if (chr4 == 13 || chr4 == 10) {
                    this.parsingRequestLineEol = true;
                    space3 = true;
                    end = pos2;
                } else if (chr4 == 63 && this.parsingRequestLineQPos == -1) {
                    this.parsingRequestLineQPos = pos2;
                } else {
                    if (this.parsingRequestLineQPos != -1 && !this.httpParser.isQueryRelaxed(chr4)) {
                        this.request.protocol().setString(Constants.HTTP_11);
                        throw new IllegalArgumentException(sm.getString("iib.invalidRequestTarget"));
                    }
                    if (this.httpParser.isNotRequestTargetRelaxed(chr4)) {
                        this.request.protocol().setString(Constants.HTTP_11);
                        throw new IllegalArgumentException(sm.getString("iib.invalidRequestTarget"));
                    }
                }
            }
            if (this.parsingRequestLineQPos >= 0) {
                this.request.queryString().setBytes(this.byteBuffer.array(), this.parsingRequestLineQPos + 1, (end - this.parsingRequestLineQPos) - 1);
                this.request.requestURI().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, this.parsingRequestLineQPos - this.parsingRequestLineStart);
            } else {
                this.request.requestURI().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, end - this.parsingRequestLineStart);
            }
            this.parsingRequestLinePhase = 5;
        }
        if (this.parsingRequestLinePhase == 5) {
            boolean space4 = true;
            while (space4) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                byte chr5 = this.byteBuffer.get();
                if (chr5 != 32 && chr5 != 9) {
                    space4 = false;
                    this.byteBuffer.position(this.byteBuffer.position() - 1);
                }
            }
            this.parsingRequestLineStart = this.byteBuffer.position();
            this.parsingRequestLinePhase = 6;
            this.end = 0;
        }
        if (this.parsingRequestLinePhase == 6) {
            while (!this.parsingRequestLineEol) {
                if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                    return false;
                }
                int pos3 = this.byteBuffer.position();
                byte chr6 = this.byteBuffer.get();
                if (chr6 == 13) {
                    this.end = pos3;
                } else if (chr6 == 10) {
                    if (this.end == 0) {
                        this.end = pos3;
                    }
                    this.parsingRequestLineEol = true;
                } else if (!HttpParser.isHttpProtocol(chr6)) {
                    throw new IllegalArgumentException(sm.getString("iib.invalidHttpProtocol"));
                }
            }
            if (this.end - this.parsingRequestLineStart > 0) {
                this.request.protocol().setBytes(this.byteBuffer.array(), this.parsingRequestLineStart, this.end - this.parsingRequestLineStart);
            } else {
                this.request.protocol().setString("");
            }
            this.parsingRequestLine = false;
            this.parsingRequestLinePhase = 0;
            this.parsingRequestLineEol = false;
            this.parsingRequestLineStart = 0;
            return true;
        }
        throw new IllegalStateException("Invalid request line parse phase:" + this.parsingRequestLinePhase);
    }

    boolean parseHeaders() throws IOException {
        HeaderParseStatus status;
        if (!this.parsingHeader) {
            throw new IllegalStateException(sm.getString("iib.parseheaders.ise.error"));
        }
        HeaderParseStatus headerParseStatus = HeaderParseStatus.HAVE_MORE_HEADERS;
        do {
            status = parseHeader();
            if (this.byteBuffer.position() > this.headerBufferSize || this.byteBuffer.capacity() - this.byteBuffer.position() < this.socketReadBufferSize) {
                throw new IllegalArgumentException(sm.getString("iib.requestheadertoolarge.error"));
            }
        } while (status == HeaderParseStatus.HAVE_MORE_HEADERS);
        if (status == HeaderParseStatus.DONE) {
            this.parsingHeader = false;
            this.end = this.byteBuffer.position();
            return true;
        }
        return false;
    }

    int getParsingRequestLinePhase() {
        return this.parsingRequestLinePhase;
    }

    void endRequest() throws IOException {
        if (this.swallowInput && this.lastActiveFilter != -1) {
            int extraBytes = (int) this.activeFilters[this.lastActiveFilter].end();
            this.byteBuffer.position(this.byteBuffer.position() - extraBytes);
        }
    }

    int available(boolean read) {
        int available = this.byteBuffer.remaining();
        if (available == 0 && this.lastActiveFilter >= 0) {
            for (int i = 0; available == 0 && i <= this.lastActiveFilter; i++) {
                available = this.activeFilters[i].available();
            }
        }
        if (available > 0 || !read) {
            return available;
        }
        try {
            if (this.wrapper.hasDataToRead()) {
                fill(false);
                available = this.byteBuffer.remaining();
            }
        } catch (IOException ioe) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("iib.available.readFail"), ioe);
            }
            available = 1;
        }
        return available;
    }

    boolean isFinished() {
        if (this.byteBuffer.limit() <= this.byteBuffer.position() && this.lastActiveFilter >= 0) {
            return this.activeFilters[this.lastActiveFilter].isFinished();
        }
        return false;
    }

    ByteBuffer getLeftover() {
        int available = this.byteBuffer.remaining();
        if (available > 0) {
            return ByteBuffer.wrap(this.byteBuffer.array(), this.byteBuffer.position(), available);
        }
        return null;
    }

    void init(SocketWrapperBase<?> socketWrapper) {
        this.wrapper = socketWrapper;
        this.wrapper.setAppReadBufHandler(this);
        int bufLength = this.headerBufferSize + this.wrapper.getSocketBufferHandler().getReadBuffer().capacity();
        if (this.byteBuffer == null || this.byteBuffer.capacity() < bufLength) {
            this.byteBuffer = ByteBuffer.allocate(bufLength);
            this.byteBuffer.position(0).limit(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean fill(boolean block) throws IOException {
        if (this.parsingHeader) {
            if (this.byteBuffer.limit() >= this.headerBufferSize) {
                if (this.parsingRequestLine) {
                    this.request.protocol().setString(Constants.HTTP_11);
                }
                throw new IllegalArgumentException(sm.getString("iib.requestheadertoolarge.error"));
            }
        } else {
            this.byteBuffer.limit(this.end).position(this.end);
        }
        this.byteBuffer.mark();
        if (this.byteBuffer.position() < this.byteBuffer.limit()) {
            this.byteBuffer.position(this.byteBuffer.limit());
        }
        this.byteBuffer.limit(this.byteBuffer.capacity());
        int nRead = this.wrapper.read(block, this.byteBuffer);
        this.byteBuffer.limit(this.byteBuffer.position()).reset();
        if (nRead > 0) {
            return true;
        }
        if (nRead == -1) {
            throw new EOFException(sm.getString("iib.eof.error"));
        }
        return false;
    }

    private HeaderParseStatus parseHeader() throws IOException {
        while (true) {
            if (this.headerParsePos != HeaderParsePosition.HEADER_START) {
                break;
            }
            if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                this.headerParsePos = HeaderParsePosition.HEADER_START;
                return HeaderParseStatus.NEED_MORE_DATA;
            }
            byte chr = this.byteBuffer.get();
            if (chr != 13) {
                if (chr == 10) {
                    return HeaderParseStatus.DONE;
                }
                this.byteBuffer.position(this.byteBuffer.position() - 1);
            }
        }
        if (this.headerParsePos == HeaderParsePosition.HEADER_START) {
            this.headerData.start = this.byteBuffer.position();
            this.headerParsePos = HeaderParsePosition.HEADER_NAME;
        }
        while (true) {
            if (this.headerParsePos != HeaderParsePosition.HEADER_NAME) {
                break;
            }
            if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                return HeaderParseStatus.NEED_MORE_DATA;
            }
            int pos = this.byteBuffer.position();
            byte chr2 = this.byteBuffer.get();
            if (chr2 == 58) {
                this.headerParsePos = HeaderParsePosition.HEADER_VALUE_START;
                this.headerData.headerValue = this.headers.addValue(this.byteBuffer.array(), this.headerData.start, pos - this.headerData.start);
                int pos2 = this.byteBuffer.position();
                this.headerData.start = pos2;
                this.headerData.realPos = pos2;
                this.headerData.lastSignificantChar = pos2;
                break;
            }
            if (!HttpParser.isToken(chr2)) {
                this.headerData.lastSignificantChar = pos;
                this.byteBuffer.position(this.byteBuffer.position() - 1);
                return skipLine();
            }
            if (chr2 >= 65 && chr2 <= 90) {
                this.byteBuffer.put(pos, (byte) (chr2 - (-32)));
            }
        }
        if (this.headerParsePos == HeaderParsePosition.HEADER_SKIPLINE) {
            return skipLine();
        }
        while (true) {
            if (this.headerParsePos != HeaderParsePosition.HEADER_VALUE_START && this.headerParsePos != HeaderParsePosition.HEADER_VALUE && this.headerParsePos != HeaderParsePosition.HEADER_MULTI_LINE) {
                break;
            }
            if (this.headerParsePos == HeaderParsePosition.HEADER_VALUE_START) {
                while (true) {
                    if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                        return HeaderParseStatus.NEED_MORE_DATA;
                    }
                    byte chr3 = this.byteBuffer.get();
                    if (chr3 != 32 && chr3 != 9) {
                        this.headerParsePos = HeaderParsePosition.HEADER_VALUE;
                        this.byteBuffer.position(this.byteBuffer.position() - 1);
                        break;
                    }
                }
            }
            if (this.headerParsePos == HeaderParsePosition.HEADER_VALUE) {
                boolean eol = false;
                while (!eol) {
                    if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                        return HeaderParseStatus.NEED_MORE_DATA;
                    }
                    byte chr4 = this.byteBuffer.get();
                    if (chr4 != 13) {
                        if (chr4 == 10) {
                            eol = true;
                        } else if (chr4 == 32 || chr4 == 9) {
                            this.byteBuffer.put(this.headerData.realPos, chr4);
                            this.headerData.realPos++;
                        } else {
                            this.byteBuffer.put(this.headerData.realPos, chr4);
                            this.headerData.realPos++;
                            this.headerData.lastSignificantChar = this.headerData.realPos;
                        }
                    }
                }
                this.headerData.realPos = this.headerData.lastSignificantChar;
                this.headerParsePos = HeaderParsePosition.HEADER_MULTI_LINE;
            }
            if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                return HeaderParseStatus.NEED_MORE_DATA;
            }
            byte chr5 = this.byteBuffer.get(this.byteBuffer.position());
            if (this.headerParsePos == HeaderParsePosition.HEADER_MULTI_LINE) {
                if (chr5 != 32 && chr5 != 9) {
                    this.headerParsePos = HeaderParsePosition.HEADER_START;
                    break;
                }
                this.byteBuffer.put(this.headerData.realPos, chr5);
                this.headerData.realPos++;
                this.headerParsePos = HeaderParsePosition.HEADER_VALUE_START;
            }
        }
        this.headerData.headerValue.setBytes(this.byteBuffer.array(), this.headerData.start, this.headerData.lastSignificantChar - this.headerData.start);
        this.headerData.recycle();
        return HeaderParseStatus.HAVE_MORE_HEADERS;
    }

    private HeaderParseStatus skipLine() throws IOException {
        this.headerParsePos = HeaderParsePosition.HEADER_SKIPLINE;
        boolean eol = false;
        while (!eol) {
            if (this.byteBuffer.position() >= this.byteBuffer.limit() && !fill(false)) {
                return HeaderParseStatus.NEED_MORE_DATA;
            }
            int pos = this.byteBuffer.position();
            byte chr = this.byteBuffer.get();
            if (chr != 13) {
                if (chr == 10) {
                    eol = true;
                } else {
                    this.headerData.lastSignificantChar = pos;
                }
            }
        }
        if (this.rejectIllegalHeaderName || log.isDebugEnabled()) {
            String message = sm.getString("iib.invalidheader", new String(this.byteBuffer.array(), this.headerData.start, (this.headerData.lastSignificantChar - this.headerData.start) + 1, StandardCharsets.ISO_8859_1));
            if (this.rejectIllegalHeaderName) {
                throw new IllegalArgumentException(message);
            }
            log.debug(message);
        }
        this.headerParsePos = HeaderParsePosition.HEADER_START;
        return HeaderParseStatus.HAVE_MORE_HEADERS;
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/Http11InputBuffer$HeaderParseData.class */
    private static class HeaderParseData {
        int start;
        int realPos;
        int lastSignificantChar;
        MessageBytes headerValue;

        private HeaderParseData() {
            this.start = 0;
            this.realPos = 0;
            this.lastSignificantChar = 0;
            this.headerValue = null;
        }

        public void recycle() {
            this.start = 0;
            this.realPos = 0;
            this.lastSignificantChar = 0;
            this.headerValue = null;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http11/Http11InputBuffer$SocketInputBuffer.class */
    private class SocketInputBuffer implements InputBuffer {
        private SocketInputBuffer() {
        }

        @Override // org.apache.coyote.InputBuffer
        @Deprecated
        public int doRead(ByteChunk chunk) throws IOException {
            if (Http11InputBuffer.this.byteBuffer.position() < Http11InputBuffer.this.byteBuffer.limit() || Http11InputBuffer.this.fill(true)) {
                int length = Http11InputBuffer.this.byteBuffer.remaining();
                chunk.setBytes(Http11InputBuffer.this.byteBuffer.array(), Http11InputBuffer.this.byteBuffer.position(), length);
                Http11InputBuffer.this.byteBuffer.position(Http11InputBuffer.this.byteBuffer.limit());
                return length;
            }
            return -1;
        }

        @Override // org.apache.coyote.InputBuffer
        public int doRead(ApplicationBufferHandler handler) throws IOException {
            if (Http11InputBuffer.this.byteBuffer.position() < Http11InputBuffer.this.byteBuffer.limit() || Http11InputBuffer.this.fill(true)) {
                int length = Http11InputBuffer.this.byteBuffer.remaining();
                handler.setByteBuffer(Http11InputBuffer.this.byteBuffer.duplicate());
                Http11InputBuffer.this.byteBuffer.position(Http11InputBuffer.this.byteBuffer.limit());
                return length;
            }
            return -1;
        }
    }

    @Override // org.apache.tomcat.util.net.ApplicationBufferHandler
    public void setByteBuffer(ByteBuffer buffer) {
        this.byteBuffer = buffer;
    }

    @Override // org.apache.tomcat.util.net.ApplicationBufferHandler
    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    @Override // org.apache.tomcat.util.net.ApplicationBufferHandler
    public void expand(int size) {
        if (this.byteBuffer.capacity() >= size) {
            this.byteBuffer.limit(size);
        }
        ByteBuffer temp = ByteBuffer.allocate(size);
        temp.put(this.byteBuffer);
        this.byteBuffer = temp;
        this.byteBuffer.mark();
    }
}
