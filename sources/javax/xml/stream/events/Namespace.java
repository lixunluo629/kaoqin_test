package javax.xml.stream.events;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/events/Namespace.class */
public interface Namespace extends Attribute {
    String getPrefix();

    String getNamespaceURI();

    boolean isDefaultNamespaceDeclaration();
}
