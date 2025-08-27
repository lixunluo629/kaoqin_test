package io.netty.handler.codec.http.multipart;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.naming.factory.Constants;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/multipart/HttpPostMultipartRequestDecoder.class */
public class HttpPostMultipartRequestDecoder implements InterfaceHttpPostRequestDecoder {
    private final HttpDataFactory factory;
    private final HttpRequest request;
    private Charset charset;
    private boolean isLastChunk;
    private final List<InterfaceHttpData> bodyListHttpData;
    private final Map<String, List<InterfaceHttpData>> bodyMapHttpData;
    private ByteBuf undecodedChunk;
    private int bodyListHttpDataRank;
    private String multipartDataBoundary;
    private String multipartMixedBoundary;
    private HttpPostRequestDecoder.MultiPartStatus currentStatus;
    private Map<CharSequence, Attribute> currentFieldAttributes;
    private FileUpload currentFileUpload;
    private Attribute currentAttribute;
    private boolean destroyed;
    private int discardThreshold;
    private static final String FILENAME_ENCODED = HttpHeaderValues.FILENAME.toString() + '*';

    public HttpPostMultipartRequestDecoder(HttpRequest request) {
        this(new DefaultHttpDataFactory(16384L), request, HttpConstants.DEFAULT_CHARSET);
    }

    public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request) {
        this(factory, request, HttpConstants.DEFAULT_CHARSET);
    }

    public HttpPostMultipartRequestDecoder(HttpDataFactory factory, HttpRequest request, Charset charset) throws NumberFormatException {
        this.bodyListHttpData = new ArrayList();
        this.bodyMapHttpData = new TreeMap(CaseIgnoringComparator.INSTANCE);
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.NOTSTARTED;
        this.discardThreshold = Netty4ClientHttpRequestFactory.DEFAULT_MAX_RESPONSE_SIZE;
        this.request = (HttpRequest) ObjectUtil.checkNotNull(request, "request");
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
        this.factory = (HttpDataFactory) ObjectUtil.checkNotNull(factory, Constants.FACTORY);
        setMultipart(this.request.headers().get(HttpHeaderNames.CONTENT_TYPE));
        if (request instanceof HttpContent) {
            offer((HttpContent) request);
        } else {
            parseBody();
        }
    }

    private void setMultipart(String contentType) {
        String[] dataBoundary = HttpPostRequestDecoder.getMultipartDataBoundary(contentType);
        if (dataBoundary != null) {
            this.multipartDataBoundary = dataBoundary[0];
            if (dataBoundary.length > 1 && dataBoundary[1] != null) {
                this.charset = Charset.forName(dataBoundary[1]);
            }
        } else {
            this.multipartDataBoundary = null;
        }
        this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
    }

    private void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException(HttpPostMultipartRequestDecoder.class.getSimpleName() + " was destroyed already");
        }
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public boolean isMultipart() {
        checkDestroyed();
        return true;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void setDiscardThreshold(int discardThreshold) {
        this.discardThreshold = ObjectUtil.checkPositiveOrZero(discardThreshold, "discardThreshold");
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public int getDiscardThreshold() {
        return this.discardThreshold;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public List<InterfaceHttpData> getBodyHttpDatas() {
        checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        return this.bodyListHttpData;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public List<InterfaceHttpData> getBodyHttpDatas(String name) {
        checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        return this.bodyMapHttpData.get(name);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData getBodyHttpData(String name) {
        checkDestroyed();
        if (!this.isLastChunk) {
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        }
        List<InterfaceHttpData> list = this.bodyMapHttpData.get(name);
        if (list != null) {
            return list.get(0);
        }
        return null;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public HttpPostMultipartRequestDecoder offer(HttpContent content) throws NumberFormatException {
        ByteBuf byteBufWriteBytes;
        checkDestroyed();
        if (content instanceof LastHttpContent) {
            this.isLastChunk = true;
        }
        ByteBuf buf = content.content();
        if (this.undecodedChunk == null) {
            if (this.isLastChunk) {
                byteBufWriteBytes = buf.retainedSlice();
            } else {
                byteBufWriteBytes = buf.alloc().buffer(buf.readableBytes()).writeBytes(buf);
            }
            this.undecodedChunk = byteBufWriteBytes;
        } else {
            this.undecodedChunk.writeBytes(buf);
        }
        parseBody();
        if (this.undecodedChunk != null && this.undecodedChunk.writerIndex() > this.discardThreshold) {
            this.undecodedChunk.discardReadBytes();
        }
        return this;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public boolean hasNext() {
        checkDestroyed();
        if (this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.EPILOGUE || this.bodyListHttpDataRank < this.bodyListHttpData.size()) {
            return !this.bodyListHttpData.isEmpty() && this.bodyListHttpDataRank < this.bodyListHttpData.size();
        }
        throw new HttpPostRequestDecoder.EndOfDataDecoderException();
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData next() {
        checkDestroyed();
        if (hasNext()) {
            List<InterfaceHttpData> list = this.bodyListHttpData;
            int i = this.bodyListHttpDataRank;
            this.bodyListHttpDataRank = i + 1;
            return list.get(i);
        }
        return null;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public InterfaceHttpData currentPartialHttpData() {
        if (this.currentFileUpload != null) {
            return this.currentFileUpload;
        }
        return this.currentAttribute;
    }

    private void parseBody() throws NumberFormatException {
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE || this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
            if (this.isLastChunk) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.EPILOGUE;
                return;
            }
            return;
        }
        parseBodyMultipart();
    }

    protected void addHttpData(InterfaceHttpData data) {
        if (data == null) {
            return;
        }
        List<InterfaceHttpData> datas = this.bodyMapHttpData.get(data.getName());
        if (datas == null) {
            datas = new ArrayList(1);
            this.bodyMapHttpData.put(data.getName(), datas);
        }
        datas.add(data);
        this.bodyListHttpData.add(data);
    }

    private void parseBodyMultipart() throws NumberFormatException {
        if (this.undecodedChunk == null || this.undecodedChunk.readableBytes() == 0) {
            return;
        }
        InterfaceHttpData interfaceHttpDataDecodeMultipart = decodeMultipart(this.currentStatus);
        while (true) {
            InterfaceHttpData data = interfaceHttpDataDecodeMultipart;
            if (data != null) {
                addHttpData(data);
                if (this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE && this.currentStatus != HttpPostRequestDecoder.MultiPartStatus.EPILOGUE) {
                    interfaceHttpDataDecodeMultipart = decodeMultipart(this.currentStatus);
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private InterfaceHttpData decodeMultipart(HttpPostRequestDecoder.MultiPartStatus state) throws NumberFormatException {
        long size;
        long j;
        switch (state) {
            case NOTSTARTED:
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
            case PREAMBLE:
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Should not be called with the current getStatus");
            case HEADERDELIMITER:
                return findMultipartDelimiter(this.multipartDataBoundary, HttpPostRequestDecoder.MultiPartStatus.DISPOSITION, HttpPostRequestDecoder.MultiPartStatus.PREEPILOGUE);
            case DISPOSITION:
                return findMultipartDisposition();
            case FIELD:
                Charset localCharset = null;
                Attribute charsetAttribute = this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
                if (charsetAttribute != null) {
                    try {
                        localCharset = Charset.forName(charsetAttribute.getValue());
                    } catch (IOException e) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                    } catch (UnsupportedCharsetException e2) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
                    }
                }
                Attribute nameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.NAME);
                if (this.currentAttribute == null) {
                    Attribute lengthAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
                    if (lengthAttribute != null) {
                        try {
                            j = Long.parseLong(lengthAttribute.getValue());
                        } catch (IOException e3) {
                            throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
                        } catch (NumberFormatException e4) {
                            size = 0;
                        }
                    } else {
                        j = 0;
                    }
                    size = j;
                    try {
                        if (size > 0) {
                            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()), size);
                        } else {
                            this.currentAttribute = this.factory.createAttribute(this.request, cleanString(nameAttribute.getValue()));
                        }
                        if (localCharset != null) {
                            this.currentAttribute.setCharset(localCharset);
                        }
                    } catch (IOException e5) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e5);
                    } catch (IllegalArgumentException e6) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e6);
                    } catch (NullPointerException e7) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e7);
                    }
                }
                if (!loadDataMultipart(this.undecodedChunk, this.multipartDataBoundary, this.currentAttribute)) {
                    return null;
                }
                Attribute finalAttribute = this.currentAttribute;
                this.currentAttribute = null;
                this.currentFieldAttributes = null;
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
                return finalAttribute;
            case FILEUPLOAD:
                return getFileUpload(this.multipartDataBoundary);
            case MIXEDDELIMITER:
                return findMultipartDelimiter(this.multipartMixedBoundary, HttpPostRequestDecoder.MultiPartStatus.MIXEDDISPOSITION, HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
            case MIXEDDISPOSITION:
                return findMultipartDisposition();
            case MIXEDFILEUPLOAD:
                return getFileUpload(this.multipartMixedBoundary);
            case PREEPILOGUE:
                return null;
            case EPILOGUE:
                return null;
            default:
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("Shouldn't reach here.");
        }
    }

    private static void skipControlCharacters(ByteBuf undecodedChunk) {
        if (!undecodedChunk.hasArray()) {
            try {
                skipControlCharactersStandard(undecodedChunk);
                return;
            } catch (IndexOutOfBoundsException e1) {
                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e1);
            }
        }
        HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        while (sao.pos < sao.limit) {
            byte[] bArr = sao.bytes;
            int i = sao.pos;
            sao.pos = i + 1;
            char c = (char) (bArr[i] & 255);
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                sao.setReadPosition(1);
                return;
            }
        }
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException("Access out of bounds");
    }

    private static void skipControlCharactersStandard(ByteBuf undecodedChunk) {
        while (true) {
            char c = (char) undecodedChunk.readUnsignedByte();
            if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
                undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
                return;
            }
        }
    }

    private InterfaceHttpData findMultipartDelimiter(String delimiter, HttpPostRequestDecoder.MultiPartStatus dispositionStatus, HttpPostRequestDecoder.MultiPartStatus closeDelimiterStatus) {
        int readerIndex = this.undecodedChunk.readerIndex();
        try {
            skipControlCharacters(this.undecodedChunk);
            skipOneLine();
            try {
                String newline = readDelimiter(this.undecodedChunk, delimiter);
                if (newline.equals(delimiter)) {
                    this.currentStatus = dispositionStatus;
                    return decodeMultipart(dispositionStatus);
                }
                if (newline.equals(delimiter + ScriptUtils.DEFAULT_COMMENT_PREFIX)) {
                    this.currentStatus = closeDelimiterStatus;
                    if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER) {
                        this.currentFieldAttributes = null;
                        return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER);
                    }
                    return null;
                }
                this.undecodedChunk.readerIndex(readerIndex);
                throw new HttpPostRequestDecoder.ErrorDataDecoderException("No Multipart delimiter found");
            } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException e) {
                this.undecodedChunk.readerIndex(readerIndex);
                return null;
            }
        } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException e2) {
            this.undecodedChunk.readerIndex(readerIndex);
            return null;
        }
    }

    private InterfaceHttpData findMultipartDisposition() {
        boolean checkSecondArg;
        int readerIndex = this.undecodedChunk.readerIndex();
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
            this.currentFieldAttributes = new TreeMap(CaseIgnoringComparator.INSTANCE);
        }
        while (!skipOneLine()) {
            try {
                skipControlCharacters(this.undecodedChunk);
                String newline = readLine(this.undecodedChunk, this.charset);
                String[] contents = splitMultipartHeader(newline);
                if (HttpHeaderNames.CONTENT_DISPOSITION.contentEqualsIgnoreCase(contents[0])) {
                    if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
                        checkSecondArg = HttpHeaderValues.FORM_DATA.contentEqualsIgnoreCase(contents[1]);
                    } else {
                        checkSecondArg = HttpHeaderValues.ATTACHMENT.contentEqualsIgnoreCase(contents[1]) || HttpHeaderValues.FILE.contentEqualsIgnoreCase(contents[1]);
                    }
                    if (checkSecondArg) {
                        for (int i = 2; i < contents.length; i++) {
                            String[] values = contents[i].split(SymbolConstants.EQUAL_SYMBOL, 2);
                            try {
                                Attribute attribute = getContentDispositionAttribute(values);
                                this.currentFieldAttributes.put(attribute.getName(), attribute);
                            } catch (IllegalArgumentException e) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
                            } catch (NullPointerException e2) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
                            }
                        }
                    } else {
                        continue;
                    }
                } else if (HttpHeaderNames.CONTENT_TRANSFER_ENCODING.contentEqualsIgnoreCase(contents[0])) {
                    try {
                        this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_TRANSFER_ENCODING, this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_TRANSFER_ENCODING.toString(), cleanString(contents[1])));
                    } catch (IllegalArgumentException e3) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
                    } catch (NullPointerException e4) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e4);
                    }
                } else if (HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(contents[0])) {
                    try {
                        this.currentFieldAttributes.put(HttpHeaderNames.CONTENT_LENGTH, this.factory.createAttribute(this.request, HttpHeaderNames.CONTENT_LENGTH.toString(), cleanString(contents[1])));
                    } catch (IllegalArgumentException e5) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e5);
                    } catch (NullPointerException e6) {
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException(e6);
                    }
                } else if (!HttpHeaderNames.CONTENT_TYPE.contentEqualsIgnoreCase(contents[0])) {
                    continue;
                } else {
                    if (HttpHeaderValues.MULTIPART_MIXED.contentEqualsIgnoreCase(contents[1])) {
                        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
                            String values2 = StringUtil.substringAfter(contents[2], '=');
                            this.multipartMixedBoundary = ScriptUtils.DEFAULT_COMMENT_PREFIX + values2;
                            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
                            return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER);
                        }
                        throw new HttpPostRequestDecoder.ErrorDataDecoderException("Mixed Multipart found in a previous Mixed Multipart");
                    }
                    for (int i2 = 1; i2 < contents.length; i2++) {
                        String charsetHeader = HttpHeaderValues.CHARSET.toString();
                        if (contents[i2].regionMatches(true, 0, charsetHeader, 0, charsetHeader.length())) {
                            String values3 = StringUtil.substringAfter(contents[i2], '=');
                            try {
                                this.currentFieldAttributes.put(HttpHeaderValues.CHARSET, this.factory.createAttribute(this.request, charsetHeader, cleanString(values3)));
                            } catch (IllegalArgumentException e7) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e7);
                            } catch (NullPointerException e8) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e8);
                            }
                        } else {
                            try {
                                Attribute attribute2 = this.factory.createAttribute(this.request, cleanString(contents[0]), contents[i2]);
                                this.currentFieldAttributes.put(attribute2.getName(), attribute2);
                            } catch (IllegalArgumentException e9) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e9);
                            } catch (NullPointerException e10) {
                                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e10);
                            }
                        }
                    }
                }
            } catch (HttpPostRequestDecoder.NotEnoughDataDecoderException e11) {
                this.undecodedChunk.readerIndex(readerIndex);
                return null;
            }
        }
        Attribute filenameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
        if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.DISPOSITION) {
            if (filenameAttribute != null) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD;
                return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD);
            }
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.FIELD;
            return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.FIELD);
        }
        if (filenameAttribute != null) {
            this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD;
            return decodeMultipart(HttpPostRequestDecoder.MultiPartStatus.MIXEDFILEUPLOAD);
        }
        throw new HttpPostRequestDecoder.ErrorDataDecoderException("Filename not found");
    }

    private Attribute getContentDispositionAttribute(String... values) {
        String name = cleanString(values[0]);
        String value = values[1];
        if (HttpHeaderValues.FILENAME.contentEquals(name)) {
            int last = value.length() - 1;
            if (last > 0 && value.charAt(0) == '\"' && value.charAt(last) == '\"') {
                value = value.substring(1, last);
            }
        } else if (FILENAME_ENCODED.equals(name)) {
            try {
                name = HttpHeaderValues.FILENAME.toString();
                String[] split = cleanString(value).split("'", 3);
                value = QueryStringDecoder.decodeComponent(split[2], Charset.forName(split[0]));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            } catch (UnsupportedCharsetException e2) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
            }
        } else {
            value = cleanString(value);
        }
        return this.factory.createAttribute(this.request, name, value);
    }

    protected InterfaceHttpData getFileUpload(String delimiter) throws NumberFormatException {
        long size;
        long j;
        String contentType;
        Attribute encoding = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
        Charset localCharset = this.charset;
        HttpPostBodyUtil.TransferEncodingMechanism mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT7;
        if (encoding != null) {
            try {
                String code = encoding.getValue().toLowerCase();
                if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT7.value())) {
                    localCharset = CharsetUtil.US_ASCII;
                } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BIT8.value())) {
                    localCharset = CharsetUtil.ISO_8859_1;
                    mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BIT8;
                } else if (code.equals(HttpPostBodyUtil.TransferEncodingMechanism.BINARY.value())) {
                    mechanism = HttpPostBodyUtil.TransferEncodingMechanism.BINARY;
                } else {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException("TransferEncoding Unknown: " + code);
                }
            } catch (IOException e) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
            }
        }
        Attribute charsetAttribute = this.currentFieldAttributes.get(HttpHeaderValues.CHARSET);
        if (charsetAttribute != null) {
            try {
                localCharset = Charset.forName(charsetAttribute.getValue());
            } catch (IOException e2) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e2);
            } catch (UnsupportedCharsetException e3) {
                throw new HttpPostRequestDecoder.ErrorDataDecoderException(e3);
            }
        }
        if (this.currentFileUpload == null) {
            Attribute filenameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.FILENAME);
            Attribute nameAttribute = this.currentFieldAttributes.get(HttpHeaderValues.NAME);
            Attribute contentTypeAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_TYPE);
            Attribute lengthAttribute = this.currentFieldAttributes.get(HttpHeaderNames.CONTENT_LENGTH);
            if (lengthAttribute != null) {
                try {
                    j = Long.parseLong(lengthAttribute.getValue());
                } catch (IOException e4) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e4);
                } catch (NumberFormatException e5) {
                    size = 0;
                }
            } else {
                j = 0;
            }
            size = j;
            if (contentTypeAttribute != null) {
                try {
                    contentType = contentTypeAttribute.getValue();
                } catch (IOException e6) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e6);
                } catch (IllegalArgumentException e7) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e7);
                } catch (NullPointerException e8) {
                    throw new HttpPostRequestDecoder.ErrorDataDecoderException(e8);
                }
            } else {
                contentType = "application/octet-stream";
            }
            this.currentFileUpload = this.factory.createFileUpload(this.request, cleanString(nameAttribute.getValue()), cleanString(filenameAttribute.getValue()), contentType, mechanism.value(), localCharset, size);
        }
        if (loadDataMultipart(this.undecodedChunk, delimiter, this.currentFileUpload) && this.currentFileUpload.isCompleted()) {
            if (this.currentStatus == HttpPostRequestDecoder.MultiPartStatus.FILEUPLOAD) {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.HEADERDELIMITER;
                this.currentFieldAttributes = null;
            } else {
                this.currentStatus = HttpPostRequestDecoder.MultiPartStatus.MIXEDDELIMITER;
                cleanMixedAttributes();
            }
            FileUpload fileUpload = this.currentFileUpload;
            this.currentFileUpload = null;
            return fileUpload;
        }
        return null;
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void destroy() {
        cleanFiles();
        this.destroyed = true;
        if (this.undecodedChunk != null && this.undecodedChunk.refCnt() > 0) {
            this.undecodedChunk.release();
            this.undecodedChunk = null;
        }
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void cleanFiles() {
        checkDestroyed();
        this.factory.cleanRequestHttpData(this.request);
    }

    @Override // io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder
    public void removeHttpDataFromClean(InterfaceHttpData data) {
        checkDestroyed();
        this.factory.removeHttpDataFromClean(this.request, data);
    }

    private void cleanMixedAttributes() {
        this.currentFieldAttributes.remove(HttpHeaderValues.CHARSET);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_LENGTH);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TRANSFER_ENCODING);
        this.currentFieldAttributes.remove(HttpHeaderNames.CONTENT_TYPE);
        this.currentFieldAttributes.remove(HttpHeaderValues.FILENAME);
    }

    private static String readLineStandard(ByteBuf undecodedChunk, Charset charset) {
        int readerIndex = undecodedChunk.readerIndex();
        ByteBuf line = undecodedChunk.alloc().heapBuffer(64);
        while (undecodedChunk.isReadable()) {
            try {
                try {
                    byte nextByte = undecodedChunk.readByte();
                    if (nextByte == 13) {
                        if (undecodedChunk.getByte(undecodedChunk.readerIndex()) == 10) {
                            undecodedChunk.readByte();
                            String string = line.toString(charset);
                            line.release();
                            return string;
                        }
                        line.writeByte(13);
                    } else {
                        if (nextByte == 10) {
                            String string2 = line.toString(charset);
                            line.release();
                            return string2;
                        }
                        line.writeByte(nextByte);
                    }
                } catch (IndexOutOfBoundsException e) {
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
                }
            } finally {
                line.release();
            }
        }
        undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }

    private static String readLine(ByteBuf undecodedChunk, Charset charset) {
        if (!undecodedChunk.hasArray()) {
            return readLineStandard(undecodedChunk, charset);
        }
        HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        int readerIndex = undecodedChunk.readerIndex();
        ByteBuf line = undecodedChunk.alloc().heapBuffer(64);
        while (sao.pos < sao.limit) {
            try {
                try {
                    byte[] bArr = sao.bytes;
                    int i = sao.pos;
                    sao.pos = i + 1;
                    byte nextByte = bArr[i];
                    if (nextByte == 13) {
                        if (sao.pos < sao.limit) {
                            byte[] bArr2 = sao.bytes;
                            int i2 = sao.pos;
                            sao.pos = i2 + 1;
                            if (bArr2[i2] == 10) {
                                sao.setReadPosition(0);
                                String string = line.toString(charset);
                                line.release();
                                return string;
                            }
                            sao.pos--;
                            line.writeByte(13);
                        } else {
                            line.writeByte(nextByte);
                        }
                    } else {
                        if (nextByte == 10) {
                            sao.setReadPosition(0);
                            String string2 = line.toString(charset);
                            line.release();
                            return string2;
                        }
                        line.writeByte(nextByte);
                    }
                } catch (IndexOutOfBoundsException e) {
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
                }
            } finally {
                line.release();
            }
        }
        undecodedChunk.readerIndex(readerIndex);
        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
    }

    private static String readDelimiterStandard(ByteBuf undecodedChunk, String delimiter) {
        int readerIndex = undecodedChunk.readerIndex();
        try {
            StringBuilder sb = new StringBuilder(64);
            int delimiterPos = 0;
            int len = delimiter.length();
            while (undecodedChunk.isReadable() && delimiterPos < len) {
                byte nextByte = undecodedChunk.readByte();
                if (nextByte == delimiter.charAt(delimiterPos)) {
                    delimiterPos++;
                    sb.append((char) nextByte);
                } else {
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
            }
            if (undecodedChunk.isReadable()) {
                byte nextByte2 = undecodedChunk.readByte();
                if (nextByte2 == 13) {
                    if (undecodedChunk.readByte() == 10) {
                        return sb.toString();
                    }
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                if (nextByte2 == 10) {
                    return sb.toString();
                }
                if (nextByte2 == 45) {
                    sb.append('-');
                    if (undecodedChunk.readByte() == 45) {
                        sb.append('-');
                        if (undecodedChunk.isReadable()) {
                            byte nextByte3 = undecodedChunk.readByte();
                            if (nextByte3 == 13) {
                                if (undecodedChunk.readByte() == 10) {
                                    return sb.toString();
                                }
                                undecodedChunk.readerIndex(readerIndex);
                                throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                            }
                            if (nextByte3 == 10) {
                                return sb.toString();
                            }
                            undecodedChunk.readerIndex(undecodedChunk.readerIndex() - 1);
                            return sb.toString();
                        }
                        return sb.toString();
                    }
                }
            }
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        } catch (IndexOutOfBoundsException e) {
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
        }
    }

    private static String readDelimiter(ByteBuf undecodedChunk, String delimiter) {
        if (!undecodedChunk.hasArray()) {
            return readDelimiterStandard(undecodedChunk, delimiter);
        }
        HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        int readerIndex = undecodedChunk.readerIndex();
        int delimiterPos = 0;
        int len = delimiter.length();
        try {
            StringBuilder sb = new StringBuilder(64);
            while (sao.pos < sao.limit && delimiterPos < len) {
                byte[] bArr = sao.bytes;
                int i = sao.pos;
                sao.pos = i + 1;
                byte nextByte = bArr[i];
                if (nextByte == delimiter.charAt(delimiterPos)) {
                    delimiterPos++;
                    sb.append((char) nextByte);
                } else {
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
            }
            if (sao.pos < sao.limit) {
                byte[] bArr2 = sao.bytes;
                int i2 = sao.pos;
                sao.pos = i2 + 1;
                byte nextByte2 = bArr2[i2];
                if (nextByte2 == 13) {
                    if (sao.pos < sao.limit) {
                        byte[] bArr3 = sao.bytes;
                        int i3 = sao.pos;
                        sao.pos = i3 + 1;
                        if (bArr3[i3] == 10) {
                            sao.setReadPosition(0);
                            return sb.toString();
                        }
                        undecodedChunk.readerIndex(readerIndex);
                        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                    }
                    undecodedChunk.readerIndex(readerIndex);
                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                }
                if (nextByte2 == 10) {
                    sao.setReadPosition(0);
                    return sb.toString();
                }
                if (nextByte2 == 45) {
                    sb.append('-');
                    if (sao.pos < sao.limit) {
                        byte[] bArr4 = sao.bytes;
                        int i4 = sao.pos;
                        sao.pos = i4 + 1;
                        if (bArr4[i4] == 45) {
                            sb.append('-');
                            if (sao.pos < sao.limit) {
                                byte[] bArr5 = sao.bytes;
                                int i5 = sao.pos;
                                sao.pos = i5 + 1;
                                byte nextByte3 = bArr5[i5];
                                if (nextByte3 == 13) {
                                    if (sao.pos < sao.limit) {
                                        byte[] bArr6 = sao.bytes;
                                        int i6 = sao.pos;
                                        sao.pos = i6 + 1;
                                        if (bArr6[i6] == 10) {
                                            sao.setReadPosition(0);
                                            return sb.toString();
                                        }
                                        undecodedChunk.readerIndex(readerIndex);
                                        throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                                    }
                                    undecodedChunk.readerIndex(readerIndex);
                                    throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
                                }
                                if (nextByte3 == 10) {
                                    sao.setReadPosition(0);
                                    return sb.toString();
                                }
                                sao.setReadPosition(1);
                                return sb.toString();
                            }
                            sao.setReadPosition(0);
                            return sb.toString();
                        }
                    }
                }
            }
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException();
        } catch (IndexOutOfBoundsException e) {
            undecodedChunk.readerIndex(readerIndex);
            throw new HttpPostRequestDecoder.NotEnoughDataDecoderException(e);
        }
    }

    private static boolean loadDataMultipartStandard(ByteBuf undecodedChunk, String delimiter, HttpData httpData) {
        int startReaderIndex = undecodedChunk.readerIndex();
        int delimeterLength = delimiter.length();
        int index = 0;
        int lastPosition = startReaderIndex;
        byte prevByte = 10;
        boolean delimiterFound = false;
        while (true) {
            if (!undecodedChunk.isReadable()) {
                break;
            }
            byte nextByte = undecodedChunk.readByte();
            if (prevByte == 10 && nextByte == delimiter.codePointAt(index)) {
                index++;
                if (delimeterLength == index) {
                    delimiterFound = true;
                    break;
                }
            } else {
                lastPosition = undecodedChunk.readerIndex();
                if (nextByte == 10) {
                    index = 0;
                    lastPosition -= prevByte == 13 ? 2 : 1;
                }
                prevByte = nextByte;
            }
        }
        if (prevByte == 13) {
            lastPosition--;
        }
        ByteBuf content = undecodedChunk.retainedSlice(startReaderIndex, lastPosition - startReaderIndex);
        try {
            httpData.addContent(content, delimiterFound);
            undecodedChunk.readerIndex(lastPosition);
            return delimiterFound;
        } catch (IOException e) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
        }
    }

    private static boolean loadDataMultipart(ByteBuf undecodedChunk, String delimiter, HttpData httpData) {
        if (!undecodedChunk.hasArray()) {
            return loadDataMultipartStandard(undecodedChunk, delimiter, httpData);
        }
        HttpPostBodyUtil.SeekAheadOptimize sao = new HttpPostBodyUtil.SeekAheadOptimize(undecodedChunk);
        int startReaderIndex = undecodedChunk.readerIndex();
        int delimeterLength = delimiter.length();
        int index = 0;
        int lastRealPos = sao.pos;
        byte prevByte = 10;
        boolean delimiterFound = false;
        while (true) {
            if (sao.pos >= sao.limit) {
                break;
            }
            byte[] bArr = sao.bytes;
            int i = sao.pos;
            sao.pos = i + 1;
            byte nextByte = bArr[i];
            if (prevByte == 10 && nextByte == delimiter.codePointAt(index)) {
                index++;
                if (delimeterLength == index) {
                    delimiterFound = true;
                    break;
                }
            } else {
                lastRealPos = sao.pos;
                if (nextByte == 10) {
                    index = 0;
                    lastRealPos -= prevByte == 13 ? 2 : 1;
                }
                prevByte = nextByte;
            }
        }
        if (prevByte == 13) {
            lastRealPos--;
        }
        int lastPosition = sao.getReadPosition(lastRealPos);
        ByteBuf content = undecodedChunk.retainedSlice(startReaderIndex, lastPosition - startReaderIndex);
        try {
            httpData.addContent(content, delimiterFound);
            undecodedChunk.readerIndex(lastPosition);
            return delimiterFound;
        } catch (IOException e) {
            throw new HttpPostRequestDecoder.ErrorDataDecoderException(e);
        }
    }

    private static String cleanString(String field) {
        int size = field.length();
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            char nextChar = field.charAt(i);
            switch (nextChar) {
                case '\t':
                case ',':
                case ':':
                case ';':
                case '=':
                    sb.append(' ');
                    break;
                case '\"':
                    break;
                default:
                    sb.append(nextChar);
                    break;
            }
        }
        return sb.toString().trim();
    }

    private boolean skipOneLine() {
        if (!this.undecodedChunk.isReadable()) {
            return false;
        }
        byte nextByte = this.undecodedChunk.readByte();
        if (nextByte == 13) {
            if (!this.undecodedChunk.isReadable()) {
                this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
                return false;
            }
            if (this.undecodedChunk.readByte() == 10) {
                return true;
            }
            this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 2);
            return false;
        }
        if (nextByte == 10) {
            return true;
        }
        this.undecodedChunk.readerIndex(this.undecodedChunk.readerIndex() - 1);
        return false;
    }

    private static String[] splitMultipartHeader(String sb) {
        String[] values;
        char ch2;
        ArrayList<String> headers = new ArrayList<>(1);
        int nameStart = HttpPostBodyUtil.findNonWhitespace(sb, 0);
        int nameEnd = nameStart;
        while (nameEnd < sb.length() && (ch2 = sb.charAt(nameEnd)) != ':' && !Character.isWhitespace(ch2)) {
            nameEnd++;
        }
        int colonEnd = nameEnd;
        while (true) {
            if (colonEnd >= sb.length()) {
                break;
            }
            if (sb.charAt(colonEnd) != ':') {
                colonEnd++;
            } else {
                colonEnd++;
                break;
            }
        }
        int valueStart = HttpPostBodyUtil.findNonWhitespace(sb, colonEnd);
        int valueEnd = HttpPostBodyUtil.findEndOfString(sb);
        headers.add(sb.substring(nameStart, nameEnd));
        String svalue = valueStart >= valueEnd ? "" : sb.substring(valueStart, valueEnd);
        if (svalue.indexOf(59) >= 0) {
            values = splitMultipartHeaderValues(svalue);
        } else {
            values = svalue.split(",");
        }
        for (String value : values) {
            headers.add(value.trim());
        }
        String[] array = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            array[i] = headers.get(i);
        }
        return array;
    }

    private static String[] splitMultipartHeaderValues(String svalue) {
        List<String> values = InternalThreadLocalMap.get().arrayList(1);
        boolean inQuote = false;
        boolean escapeNext = false;
        int start = 0;
        for (int i = 0; i < svalue.length(); i++) {
            char c = svalue.charAt(i);
            if (inQuote) {
                if (escapeNext) {
                    escapeNext = false;
                } else if (c == '\\') {
                    escapeNext = true;
                } else if (c == '\"') {
                    inQuote = false;
                }
            } else if (c == '\"') {
                inQuote = true;
            } else if (c == ';') {
                values.add(svalue.substring(start, i));
                start = i + 1;
            }
        }
        values.add(svalue.substring(start));
        return (String[]) values.toArray(new String[0]);
    }
}
