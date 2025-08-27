package org.springframework.web.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.InputStreamSource;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/MultipartFile.class */
public interface MultipartFile extends InputStreamSource {
    String getName();

    String getOriginalFilename();

    String getContentType();

    boolean isEmpty();

    long getSize();

    byte[] getBytes() throws IOException;

    @Override // org.springframework.core.io.InputStreamSource
    InputStream getInputStream() throws IOException;

    void transferTo(File file) throws IllegalStateException, IOException;
}
