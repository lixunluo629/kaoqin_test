package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitter.class */
public class ResponseBodyEmitter {
    private final Long timeout;
    private final Set<DataWithMediaType> earlySendAttempts;
    private Handler handler;
    private boolean complete;
    private Throwable failure;
    private final DefaultCallback timeoutCallback;
    private final DefaultCallback completionCallback;

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitter$Handler.class */
    interface Handler {
        void send(Object obj, MediaType mediaType) throws IOException;

        void complete();

        void completeWithError(Throwable th);

        void onTimeout(Runnable runnable);

        void onCompletion(Runnable runnable);
    }

    public ResponseBodyEmitter() {
        this.earlySendAttempts = new LinkedHashSet(8);
        this.timeoutCallback = new DefaultCallback();
        this.completionCallback = new DefaultCallback();
        this.timeout = null;
    }

    public ResponseBodyEmitter(Long timeout) {
        this.earlySendAttempts = new LinkedHashSet(8);
        this.timeoutCallback = new DefaultCallback();
        this.completionCallback = new DefaultCallback();
        this.timeout = timeout;
    }

    public Long getTimeout() {
        return this.timeout;
    }

    synchronized void initialize(Handler handler) throws IOException {
        this.handler = handler;
        for (DataWithMediaType sendAttempt : this.earlySendAttempts) {
            sendInternal(sendAttempt.getData(), sendAttempt.getMediaType());
        }
        this.earlySendAttempts.clear();
        if (this.complete) {
            if (this.failure != null) {
                this.handler.completeWithError(this.failure);
                return;
            } else {
                this.handler.complete();
                return;
            }
        }
        this.handler.onTimeout(this.timeoutCallback);
        this.handler.onCompletion(this.completionCallback);
    }

    protected void extendResponse(ServerHttpResponse outputMessage) {
    }

    public void send(Object object) throws IOException {
        send(object, null);
    }

    public synchronized void send(Object object, MediaType mediaType) throws IOException {
        Assert.state(!this.complete, "ResponseBodyEmitter is already set complete");
        sendInternal(object, mediaType);
    }

    private void sendInternal(Object object, MediaType mediaType) throws IOException {
        if (object != null) {
            if (this.handler != null) {
                try {
                    this.handler.send(object, mediaType);
                    return;
                } catch (IOException ex) {
                    throw ex;
                } catch (Throwable ex2) {
                    throw new IllegalStateException("Failed to send " + object, ex2);
                }
            }
            this.earlySendAttempts.add(new DataWithMediaType(object, mediaType));
        }
    }

    public synchronized void complete() {
        this.complete = true;
        if (this.handler != null) {
            this.handler.complete();
        }
    }

    public synchronized void completeWithError(Throwable ex) {
        this.complete = true;
        this.failure = ex;
        if (this.handler != null) {
            this.handler.completeWithError(ex);
        }
    }

    public synchronized void onTimeout(Runnable callback) {
        this.timeoutCallback.setDelegate(callback);
    }

    public synchronized void onCompletion(Runnable callback) {
        this.completionCallback.setDelegate(callback);
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitter$DataWithMediaType.class */
    public static class DataWithMediaType {
        private final Object data;
        private final MediaType mediaType;

        public DataWithMediaType(Object data, MediaType mediaType) {
            this.data = data;
            this.mediaType = mediaType;
        }

        public Object getData() {
            return this.data;
        }

        public MediaType getMediaType() {
            return this.mediaType;
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ResponseBodyEmitter$DefaultCallback.class */
    private class DefaultCallback implements Runnable {
        private Runnable delegate;

        private DefaultCallback() {
        }

        public void setDelegate(Runnable delegate) {
            this.delegate = delegate;
        }

        @Override // java.lang.Runnable
        public void run() {
            ResponseBodyEmitter.this.complete = true;
            if (this.delegate != null) {
                this.delegate.run();
            }
        }
    }
}
