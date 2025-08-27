package org.springframework.mail.javamail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/javamail/ConfigurableMimeFileTypeMap.class */
public class ConfigurableMimeFileTypeMap extends FileTypeMap implements InitializingBean {
    private Resource mappingLocation = new ClassPathResource("mime.types", getClass());
    private String[] mappings;
    private FileTypeMap fileTypeMap;

    public void setMappingLocation(Resource mappingLocation) {
        this.mappingLocation = mappingLocation;
    }

    public void setMappings(String... mappings) {
        this.mappings = mappings;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        getFileTypeMap();
    }

    protected final FileTypeMap getFileTypeMap() {
        if (this.fileTypeMap == null) {
            try {
                this.fileTypeMap = createFileTypeMap(this.mappingLocation, this.mappings);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not load specified MIME type mapping file: " + this.mappingLocation, ex);
            }
        }
        return this.fileTypeMap;
    }

    protected FileTypeMap createFileTypeMap(Resource mappingLocation, String[] mappings) throws IOException {
        MimetypesFileTypeMap fileTypeMap;
        if (mappingLocation != null) {
            InputStream is = mappingLocation.getInputStream();
            try {
                fileTypeMap = new MimetypesFileTypeMap(is);
                is.close();
            } catch (Throwable th) {
                is.close();
                throw th;
            }
        } else {
            fileTypeMap = new MimetypesFileTypeMap();
        }
        if (mappings != null) {
            for (String mapping : mappings) {
                fileTypeMap.addMimeTypes(mapping);
            }
        }
        return fileTypeMap;
    }

    public String getContentType(File file) {
        return getFileTypeMap().getContentType(file);
    }

    public String getContentType(String fileName) {
        return getFileTypeMap().getContentType(fileName);
    }
}
