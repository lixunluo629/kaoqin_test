package com.itextpdf.io.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/util/UrlUtil.class */
public final class UrlUtil {
    private UrlUtil() {
    }

    public static URL toURL(String filename) throws MalformedURLException {
        URL url;
        try {
            url = new URL(filename);
        } catch (MalformedURLException e) {
            url = new File(filename).toURI().toURL();
        }
        return url;
    }

    public static URI toNormalizedURI(String filename) {
        return toNormalizedURI(new File(filename));
    }

    public static URI toNormalizedURI(File file) {
        return file.toURI().normalize();
    }

    public static InputStream openStream(URL url) throws IOException {
        return url.openStream();
    }

    public static URL getFinalURL(URL initialUrl) throws IOException {
        URL finalUrl = null;
        URL url = initialUrl;
        while (true) {
            URL nextUrl = url;
            if (nextUrl != null) {
                finalUrl = nextUrl;
                URLConnection connection = finalUrl.openConnection();
                String location = connection.getHeaderField("location");
                connection.getInputStream().close();
                url = location != null ? new URL(location) : null;
            } else {
                return finalUrl;
            }
        }
    }

    public static String getFileUriString(String filename) throws MalformedURLException {
        return new File(filename).toURI().toURL().toExternalForm();
    }
}
