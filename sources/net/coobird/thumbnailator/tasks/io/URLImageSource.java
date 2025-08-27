package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

/* loaded from: thumbnailator-0.4.8.jar:net/coobird/thumbnailator/tasks/io/URLImageSource.class */
public class URLImageSource extends AbstractImageSource<URL> {
    private final URL url;
    private final Proxy proxy;

    public URLImageSource(URL url) {
        if (url == null) {
            throw new NullPointerException("URL cannot be null.");
        }
        this.url = url;
        this.proxy = null;
    }

    public URLImageSource(String str) throws MalformedURLException {
        if (str == null) {
            throw new NullPointerException("URL cannot be null.");
        }
        this.url = new URL(str);
        this.proxy = null;
    }

    public URLImageSource(URL url, Proxy proxy) {
        if (url == null) {
            throw new NullPointerException("URL cannot be null.");
        }
        if (proxy == null) {
            throw new NullPointerException("Proxy cannot be null.");
        }
        this.url = url;
        this.proxy = proxy;
    }

    public URLImageSource(String str, Proxy proxy) throws MalformedURLException {
        if (str == null) {
            throw new NullPointerException("URL cannot be null.");
        }
        if (proxy == null) {
            throw new NullPointerException("Proxy cannot be null.");
        }
        this.url = new URL(str);
        this.proxy = proxy;
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSource
    public BufferedImage read() throws IOException {
        InputStreamImageSource inputStreamImageSource;
        try {
            if (this.proxy != null) {
                inputStreamImageSource = new InputStreamImageSource(this.url.openConnection(this.proxy).getInputStream());
            } else {
                inputStreamImageSource = new InputStreamImageSource(this.url.openStream());
            }
            inputStreamImageSource.setThumbnailParameter(this.param);
            try {
                BufferedImage bufferedImage = inputStreamImageSource.read();
                this.inputFormatName = inputStreamImageSource.getInputFormatName();
                return (BufferedImage) finishedReading(bufferedImage);
            } catch (Exception e) {
                throw new IOException("Could not obtain image from URL: " + this.url);
            }
        } catch (IOException e2) {
            throw new IOException("Could not open connection to URL: " + this.url);
        }
    }

    @Override // net.coobird.thumbnailator.tasks.io.ImageSource
    public URL getSource() {
        return this.url;
    }

    public Proxy getProxy() {
        return this.proxy;
    }
}
