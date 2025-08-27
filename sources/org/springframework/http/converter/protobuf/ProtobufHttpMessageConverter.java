package org.springframework.http.converter.protobuf;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.googlecode.protobuf.format.HtmlFormat;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.ProtobufFormatter;
import com.googlecode.protobuf.format.XmlFormat;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/protobuf/ProtobufHttpMessageConverter.class */
public class ProtobufHttpMessageConverter extends AbstractHttpMessageConverter<Message> {
    public static final String X_PROTOBUF_SCHEMA_HEADER = "X-Protobuf-Schema";
    public static final String X_PROTOBUF_MESSAGE_HEADER = "X-Protobuf-Message";
    private final ExtensionRegistry extensionRegistry;
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final MediaType PROTOBUF = new MediaType("application", "x-protobuf", DEFAULT_CHARSET);
    private static final ProtobufFormatter JSON_FORMAT = new JsonFormat();
    private static final ProtobufFormatter XML_FORMAT = new XmlFormat();
    private static final ProtobufFormatter HTML_FORMAT = new HtmlFormat();
    private static final ConcurrentHashMap<Class<?>, Method> methodCache = new ConcurrentHashMap<>();

    public ProtobufHttpMessageConverter() {
        this(null);
    }

    public ProtobufHttpMessageConverter(ExtensionRegistryInitializer registryInitializer) {
        super(PROTOBUF, MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML);
        this.extensionRegistry = ExtensionRegistry.newInstance();
        if (registryInitializer != null) {
            registryInitializer.initializeExtensionRegistry(this.extensionRegistry);
        }
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean supports(Class<?> clazz) {
        return Message.class.isAssignableFrom(clazz);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public MediaType getDefaultContentType(Message message) {
        return PROTOBUF;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public Message readInternal(Class<? extends Message> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        if (contentType == null) {
            contentType = PROTOBUF;
        }
        Charset charset = contentType.getCharset();
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        try {
            Message.Builder builder = getMessageBuilder(clazz);
            if (MediaType.TEXT_PLAIN.isCompatibleWith(contentType)) {
                InputStreamReader reader = new InputStreamReader(inputMessage.getBody(), charset);
                TextFormat.merge(reader, this.extensionRegistry, builder);
            } else if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                JSON_FORMAT.merge(inputMessage.getBody(), charset, this.extensionRegistry, builder);
            } else if (MediaType.APPLICATION_XML.isCompatibleWith(contentType)) {
                XML_FORMAT.merge(inputMessage.getBody(), charset, this.extensionRegistry, builder);
            } else {
                builder.mergeFrom(inputMessage.getBody(), this.extensionRegistry);
            }
            return builder.build();
        } catch (Exception ex) {
            throw new HttpMessageNotReadableException("Could not read Protobuf message: " + ex.getMessage(), ex);
        }
    }

    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    protected boolean canWrite(MediaType mediaType) {
        return super.canWrite(mediaType) || MediaType.TEXT_HTML.isCompatibleWith(mediaType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.http.converter.AbstractHttpMessageConverter
    public void writeInternal(Message message, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        MediaType contentType = outputMessage.getHeaders().getContentType();
        if (contentType == null) {
            contentType = getDefaultContentType(message);
        }
        Charset charset = contentType.getCharset();
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        if (MediaType.TEXT_PLAIN.isCompatibleWith(contentType)) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputMessage.getBody(), charset);
            TextFormat.print(message, outputStreamWriter);
            outputStreamWriter.flush();
        } else {
            if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                JSON_FORMAT.print(message, outputMessage.getBody(), charset);
                return;
            }
            if (MediaType.APPLICATION_XML.isCompatibleWith(contentType)) {
                XML_FORMAT.print(message, outputMessage.getBody(), charset);
                return;
            }
            if (MediaType.TEXT_HTML.isCompatibleWith(contentType)) {
                HTML_FORMAT.print(message, outputMessage.getBody(), charset);
            } else if (PROTOBUF.isCompatibleWith(contentType)) {
                setProtoHeader(outputMessage, message);
                FileCopyUtils.copy(message.toByteArray(), outputMessage.getBody());
            }
        }
    }

    private void setProtoHeader(HttpOutputMessage response, Message message) {
        response.getHeaders().set(X_PROTOBUF_SCHEMA_HEADER, message.getDescriptorForType().getFile().getName());
        response.getHeaders().set(X_PROTOBUF_MESSAGE_HEADER, message.getDescriptorForType().getFullName());
    }

    private static Message.Builder getMessageBuilder(Class<? extends Message> clazz) throws Exception {
        Method method = methodCache.get(clazz);
        if (method == null) {
            method = clazz.getMethod("newBuilder", new Class[0]);
            methodCache.put(clazz, method);
        }
        return (Message.Builder) method.invoke(clazz, new Object[0]);
    }
}
