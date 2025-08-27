package org.springframework.boot.system;

import java.io.File;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/system/EmbeddedServerPortFileWriter.class */
public class EmbeddedServerPortFileWriter implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
    private static final String DEFAULT_FILE_NAME = "application.port";
    private static final String[] PROPERTY_VARIABLES = {"PORTFILE", "portfile"};
    private static final Log logger = LogFactory.getLog(EmbeddedServerPortFileWriter.class);
    private final File file;

    public EmbeddedServerPortFileWriter() {
        this(new File(DEFAULT_FILE_NAME));
    }

    public EmbeddedServerPortFileWriter(String filename) {
        this(new File(filename));
    }

    public EmbeddedServerPortFileWriter(File file) {
        Assert.notNull(file, "File must not be null");
        String override = SystemProperties.get(PROPERTY_VARIABLES);
        if (override != null) {
            this.file = new File(override);
        } else {
            this.file = file;
        }
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        File portFile = getPortFile(event.getApplicationContext());
        try {
            String port = String.valueOf(event.getEmbeddedServletContainer().getPort());
            createParentFolder(portFile);
            FileCopyUtils.copy(port.getBytes(), portFile);
            portFile.deleteOnExit();
        } catch (Exception e) {
            logger.warn(String.format("Cannot create port file %s", this.file));
        }
    }

    protected File getPortFile(EmbeddedWebApplicationContext applicationContext) {
        String name;
        String contextName = applicationContext.getNamespace();
        if (StringUtils.isEmpty(contextName)) {
            return this.file;
        }
        String name2 = this.file.getName();
        String extension = StringUtils.getFilenameExtension(this.file.getName());
        String name3 = name2.substring(0, (name2.length() - extension.length()) - 1);
        if (isUpperCase(name3)) {
            name = name3 + "-" + contextName.toUpperCase(Locale.ENGLISH);
        } else {
            name = name3 + "-" + contextName.toLowerCase(Locale.ENGLISH);
        }
        if (StringUtils.hasLength(extension)) {
            name = name + "." + extension;
        }
        return new File(this.file.getParentFile(), name);
    }

    private boolean isUpperCase(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (Character.isLetter(name.charAt(i)) && !Character.isUpperCase(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void createParentFolder(File file) {
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
    }
}
