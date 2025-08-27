package org.springframework.boot.info;

import io.netty.handler.codec.rtsp.RtspHeaders;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/info/BuildProperties.class */
public class BuildProperties extends InfoProperties {
    public BuildProperties(Properties entries) {
        super(processEntries(entries));
    }

    public String getGroup() {
        return get("group");
    }

    public String getArtifact() {
        return get("artifact");
    }

    public String getName() {
        return get("name");
    }

    public String getVersion() {
        return get("version");
    }

    public Date getTime() {
        return getDate(RtspHeaders.Values.TIME);
    }

    private static Properties processEntries(Properties properties) {
        coerceDate(properties, RtspHeaders.Values.TIME);
        return properties;
    }

    private static void coerceDate(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            try {
                String updatedValue = String.valueOf(format.parse(value).getTime());
                properties.setProperty(key, updatedValue);
            } catch (ParseException e) {
            }
        }
    }
}
