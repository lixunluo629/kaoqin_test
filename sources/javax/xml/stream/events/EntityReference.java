package javax.xml.stream.events;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/events/EntityReference.class */
public interface EntityReference extends XMLEvent {
    EntityDeclaration getDeclaration();

    String getName();
}
