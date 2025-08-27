package javax.xml.stream.events;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/events/NotationDeclaration.class */
public interface NotationDeclaration extends XMLEvent {
    String getName();

    String getPublicId();

    String getSystemId();
}
