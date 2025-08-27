package org.apache.catalina.util;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import org.apache.tomcat.util.ExceptionUtils;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/util/CharsetMapper.class */
public class CharsetMapper {
    public static final String DEFAULT_RESOURCE = "/org/apache/catalina/util/CharsetMapperDefault.properties";
    private Properties map;

    public CharsetMapper() {
        this(DEFAULT_RESOURCE);
    }

    public CharsetMapper(String name) {
        this.map = new Properties();
        try {
            InputStream stream = getClass().getResourceAsStream(name);
            Throwable th = null;
            try {
                try {
                    this.map.load(stream);
                    if (stream != null) {
                        if (0 != 0) {
                            try {
                                stream.close();
                            } catch (Throwable x2) {
                                th.addSuppressed(x2);
                            }
                        } else {
                            stream.close();
                        }
                    }
                } finally {
                }
            } finally {
            }
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            throw new IllegalArgumentException(t.toString());
        }
    }

    public String getCharset(Locale locale) {
        String charset = this.map.getProperty(locale.toString());
        if (charset == null) {
            charset = this.map.getProperty(locale.getLanguage() + "_" + locale.getCountry());
            if (charset == null) {
                charset = this.map.getProperty(locale.getLanguage());
            }
        }
        return charset;
    }

    public void addCharsetMappingFromDeploymentDescriptor(String locale, String charset) {
        this.map.put(locale, charset);
    }
}
