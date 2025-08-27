package springfox.documentation.spi;

import org.aspectj.weaver.Constants;
import org.springframework.http.MediaType;
import org.springframework.plugin.metadata.SimplePluginMetadata;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/DocumentationType.class */
public class DocumentationType extends SimplePluginMetadata {
    public static final DocumentationType SWAGGER_12 = new DocumentationType("swagger", Constants.RUNTIME_LEVEL_12);
    public static final DocumentationType SWAGGER_2 = new DocumentationType("swagger", "2.0");
    public static final DocumentationType SPRING_WEB = new DocumentationType("spring-web", "1.0");
    private final MediaType mediaType;

    public DocumentationType(String name, String version, MediaType mediaType) {
        super(name, version);
        this.mediaType = mediaType;
    }

    public DocumentationType(String name, String version) {
        this(name, version, MediaType.APPLICATION_JSON);
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    @Override // org.springframework.plugin.metadata.SimplePluginMetadata
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentationType) || !super.equals(o)) {
            return false;
        }
        DocumentationType that = (DocumentationType) o;
        return super.equals(that) && this.mediaType.equals(that.mediaType);
    }

    @Override // org.springframework.plugin.metadata.SimplePluginMetadata
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + this.mediaType.hashCode();
    }
}
