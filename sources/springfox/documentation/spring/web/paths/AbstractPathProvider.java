package springfox.documentation.spring.web.paths;

import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.PathProvider;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/paths/AbstractPathProvider.class */
public abstract class AbstractPathProvider implements PathProvider {
    protected abstract String applicationPath();

    protected abstract String getDocumentationPath();

    @Override // springfox.documentation.PathProvider
    public String getApplicationBasePath() {
        return applicationPath();
    }

    @Override // springfox.documentation.PathProvider
    public String getOperationPath(String operationPath) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath("/");
        return Paths.removeAdjacentForwardSlashes(uriComponentsBuilder.path(operationPath).build().toString());
    }

    @Override // springfox.documentation.PathProvider
    public String getResourceListingPath(String groupName, String apiDeclaration) {
        String candidate = agnosticUriComponentBuilder(getDocumentationPath()).pathSegment(groupName, apiDeclaration).build().toString();
        return Paths.removeAdjacentForwardSlashes(candidate);
    }

    private UriComponentsBuilder agnosticUriComponentBuilder(String url) {
        UriComponentsBuilder uriComponentsBuilder;
        if (url.startsWith("http")) {
            uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        } else {
            uriComponentsBuilder = UriComponentsBuilder.fromPath(url);
        }
        return uriComponentsBuilder;
    }
}
