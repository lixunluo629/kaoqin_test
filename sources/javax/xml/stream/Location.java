package javax.xml.stream;

/* loaded from: stax-api-1.0.1.jar:javax/xml/stream/Location.class */
public interface Location {
    int getLineNumber();

    int getColumnNumber();

    int getCharacterOffset();

    String getPublicId();

    String getSystemId();
}
