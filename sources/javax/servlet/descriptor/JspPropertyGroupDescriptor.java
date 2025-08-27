package javax.servlet.descriptor;

import java.util.Collection;

/* loaded from: tomcat-embed-core-8.5.43.jar:javax/servlet/descriptor/JspPropertyGroupDescriptor.class */
public interface JspPropertyGroupDescriptor {
    Collection<String> getUrlPatterns();

    String getElIgnored();

    String getPageEncoding();

    String getScriptingInvalid();

    String getIsXml();

    Collection<String> getIncludePreludes();

    Collection<String> getIncludeCodas();

    String getDeferredSyntaxAllowedAsLiteral();

    String getTrimDirectiveWhitespaces();

    String getDefaultContentType();

    String getBuffer();

    String getErrorOnUndeclaredNamespace();
}
