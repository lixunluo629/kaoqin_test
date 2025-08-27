package springfox.documentation;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/PathProvider.class */
public interface PathProvider {
    String getApplicationBasePath();

    String getOperationPath(String str);

    String getResourceListingPath(String str, String str2);
}
