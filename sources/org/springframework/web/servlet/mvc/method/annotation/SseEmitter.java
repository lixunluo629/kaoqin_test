package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.bouncycastle.i18n.TextBundle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/SseEmitter.class */
public class SseEmitter extends ResponseBodyEmitter {
    static final MediaType TEXT_PLAIN = new MediaType(TextBundle.TEXT_ENTRY, "plain", Charset.forName("UTF-8"));
    static final MediaType UTF8_TEXT_EVENTSTREAM = new MediaType(TextBundle.TEXT_ENTRY, "event-stream", Charset.forName("UTF-8"));

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/SseEmitter$SseEventBuilder.class */
    public interface SseEventBuilder {
        SseEventBuilder comment(String str);

        SseEventBuilder name(String str);

        SseEventBuilder id(String str);

        SseEventBuilder reconnectTime(long j);

        SseEventBuilder data(Object obj);

        SseEventBuilder data(Object obj, MediaType mediaType);

        Set<ResponseBodyEmitter.DataWithMediaType> build();
    }

    public SseEmitter() {
    }

    public SseEmitter(Long timeout) {
        super(timeout);
    }

    @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter
    protected void extendResponse(ServerHttpResponse outputMessage) {
        super.extendResponse(outputMessage);
        HttpHeaders headers = outputMessage.getHeaders();
        if (headers.getContentType() == null) {
            headers.setContentType(UTF8_TEXT_EVENTSTREAM);
        }
    }

    @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter
    public void send(Object object) throws IOException {
        send(object, null);
    }

    @Override // org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter
    public void send(Object object, MediaType mediaType) throws IOException {
        if (object != null) {
            send(event().data(object, mediaType));
        }
    }

    public void send(SseEventBuilder builder) throws IOException {
        Set<ResponseBodyEmitter.DataWithMediaType> dataToSend = builder.build();
        synchronized (this) {
            for (ResponseBodyEmitter.DataWithMediaType entry : dataToSend) {
                super.send(entry.getData(), entry.getMediaType());
            }
        }
    }

    public static SseEventBuilder event() {
        return new SseEventBuilderImpl();
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/SseEmitter$SseEventBuilderImpl.class */
    private static class SseEventBuilderImpl implements SseEventBuilder {
        private final Set<ResponseBodyEmitter.DataWithMediaType> dataToSend;
        private StringBuilder sb;

        private SseEventBuilderImpl() {
            this.dataToSend = new LinkedHashSet(4);
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
        public SseEventBuilder comment(String comment) {
            append(":").append(comment != null ? comment : "").append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            return this;
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
        public SseEventBuilder name(String name) {
            append("event:").append(name != null ? name : "").append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            return this;
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
        public SseEventBuilder id(String id) {
            append("id:").append(id != null ? id : "").append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            return this;
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
        public SseEventBuilder reconnectTime(long reconnectTimeMillis) {
            append("retry:").append(String.valueOf(reconnectTimeMillis)).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            return this;
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
        public SseEventBuilder data(Object object) {
            return data(object, null);
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
        public SseEventBuilder data(Object object, MediaType mediaType) {
            append("data:");
            saveAppendedText();
            this.dataToSend.add(new ResponseBodyEmitter.DataWithMediaType(object, mediaType));
            append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            return this;
        }

        SseEventBuilderImpl append(String text) {
            if (this.sb == null) {
                this.sb = new StringBuilder();
            }
            this.sb.append(text);
            return this;
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder
        public Set<ResponseBodyEmitter.DataWithMediaType> build() {
            if (!StringUtils.hasLength(this.sb) && this.dataToSend.isEmpty()) {
                return Collections.emptySet();
            }
            append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            saveAppendedText();
            return this.dataToSend;
        }

        private void saveAppendedText() {
            if (this.sb != null) {
                this.dataToSend.add(new ResponseBodyEmitter.DataWithMediaType(this.sb.toString(), SseEmitter.TEXT_PLAIN));
                this.sb = null;
            }
        }
    }
}
