package org.aspectj.bridge.context;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.ISourceLocation;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/context/PinpointingMessageHandler.class */
public class PinpointingMessageHandler implements IMessageHandler {
    private IMessageHandler delegate;

    public PinpointingMessageHandler(IMessageHandler delegate) {
        this.delegate = delegate;
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean handleMessage(IMessage message) throws AbortException {
        if (!isIgnoring(message.getKind())) {
            MessageIssued ex = new MessageIssued();
            ex.fillInStackTrace();
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            StringBuffer sb = new StringBuffer();
            sb.append(CompilationAndWeavingContext.getCurrentContext());
            sb.append(sw.toString());
            IMessage pinpointedMessage = new PinpointedMessage(message, sb.toString());
            return this.delegate.handleMessage(pinpointedMessage);
        }
        return this.delegate.handleMessage(message);
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public boolean isIgnoring(IMessage.Kind kind) {
        return this.delegate.isIgnoring(kind);
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void dontIgnore(IMessage.Kind kind) {
        this.delegate.dontIgnore(kind);
    }

    @Override // org.aspectj.bridge.IMessageHandler
    public void ignore(IMessage.Kind kind) {
        this.delegate.ignore(kind);
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/context/PinpointingMessageHandler$PinpointedMessage.class */
    private static class PinpointedMessage implements IMessage {
        private IMessage delegate;
        private String message;

        public PinpointedMessage(IMessage delegate, String pinpoint) {
            this.delegate = delegate;
            this.message = delegate.getMessage() + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + pinpoint;
        }

        @Override // org.aspectj.bridge.IMessage
        public String getMessage() {
            return this.message;
        }

        @Override // org.aspectj.bridge.IMessage
        public IMessage.Kind getKind() {
            return this.delegate.getKind();
        }

        @Override // org.aspectj.bridge.IMessage
        public boolean isError() {
            return this.delegate.isError();
        }

        @Override // org.aspectj.bridge.IMessage
        public boolean isWarning() {
            return this.delegate.isWarning();
        }

        @Override // org.aspectj.bridge.IMessage
        public boolean isDebug() {
            return this.delegate.isDebug();
        }

        @Override // org.aspectj.bridge.IMessage
        public boolean isInfo() {
            return this.delegate.isInfo();
        }

        @Override // org.aspectj.bridge.IMessage
        public boolean isAbort() {
            return this.delegate.isAbort();
        }

        @Override // org.aspectj.bridge.IMessage
        public boolean isTaskTag() {
            return this.delegate.isTaskTag();
        }

        @Override // org.aspectj.bridge.IMessage
        public boolean isFailed() {
            return this.delegate.isFailed();
        }

        @Override // org.aspectj.bridge.IMessage
        public boolean getDeclared() {
            return this.delegate.getDeclared();
        }

        @Override // org.aspectj.bridge.IMessage
        public int getID() {
            return this.delegate.getID();
        }

        @Override // org.aspectj.bridge.IMessage
        public int getSourceStart() {
            return this.delegate.getSourceStart();
        }

        @Override // org.aspectj.bridge.IMessage
        public int getSourceEnd() {
            return this.delegate.getSourceEnd();
        }

        @Override // org.aspectj.bridge.IMessage
        public Throwable getThrown() {
            return this.delegate.getThrown();
        }

        @Override // org.aspectj.bridge.IMessage
        public ISourceLocation getSourceLocation() {
            return this.delegate.getSourceLocation();
        }

        @Override // org.aspectj.bridge.IMessage
        public String getDetails() {
            return this.delegate.getDetails();
        }

        @Override // org.aspectj.bridge.IMessage
        public List<ISourceLocation> getExtraSourceLocations() {
            return this.delegate.getExtraSourceLocations();
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/bridge/context/PinpointingMessageHandler$MessageIssued.class */
    private static class MessageIssued extends RuntimeException {
        private static final long serialVersionUID = 1;

        private MessageIssued() {
        }

        @Override // java.lang.Throwable
        public String getMessage() {
            return "message issued...";
        }
    }
}
