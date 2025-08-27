package javax.xml.stream.events;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/events/Characters.class */
public interface Characters extends XMLEvent {
    String getData();

    boolean isWhiteSpace();

    boolean isCData();

    boolean isIgnorableWhiteSpace();
}
