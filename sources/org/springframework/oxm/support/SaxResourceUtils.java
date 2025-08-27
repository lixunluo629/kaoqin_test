package org.springframework.oxm.support;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

/* loaded from: spring-oxm-4.3.25.RELEASE.jar:org/springframework/oxm/support/SaxResourceUtils.class */
public abstract class SaxResourceUtils {
    public static InputSource createInputSource(Resource resource) throws IOException {
        InputSource inputSource = new InputSource(resource.getInputStream());
        inputSource.setSystemId(getSystemId(resource));
        return inputSource;
    }

    private static String getSystemId(Resource resource) {
        try {
            return resource.getURI().toString();
        } catch (IOException e) {
            return null;
        }
    }
}
