package org.springframework.http.converter;

import java.io.IOException;
import java.io.InputStream;
import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/ActivationMediaTypeFactory.class */
class ActivationMediaTypeFactory {
    private static final FileTypeMap fileTypeMap = loadFileTypeMapFromContextSupportModule();

    ActivationMediaTypeFactory() {
    }

    private static FileTypeMap loadFileTypeMapFromContextSupportModule() throws IOException {
        Resource mappingLocation = new ClassPathResource("org/springframework/mail/javamail/mime.types");
        if (mappingLocation.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = mappingLocation.getInputStream();
                MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap(inputStream);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
                return mimetypesFileTypeMap;
            } catch (IOException e2) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        }
        return FileTypeMap.getDefaultFileTypeMap();
    }

    public static MediaType getMediaType(Resource resource) {
        String filename = resource.getFilename();
        if (filename != null) {
            String mediaType = fileTypeMap.getContentType(filename);
            if (StringUtils.hasText(mediaType)) {
                return MediaType.parseMediaType(mediaType);
            }
            return null;
        }
        return null;
    }
}
