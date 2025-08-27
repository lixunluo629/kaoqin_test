package com.drew.tools;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/tools/ProcessUrlUtility.class */
public class ProcessUrlUtility {
    public static void main(String[] args) throws IOException, JpegProcessingException {
        if (args.length == 0) {
            System.err.println("Expects one or more URLs as arguments.");
            System.exit(1);
        }
        for (String url : args) {
            processUrl(new URL(url));
        }
        System.out.println("Completed.");
    }

    private static void processUrl(URL url) throws IOException {
        URLConnection con = url.openConnection();
        InputStream in = con.getInputStream();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(in);
            if (metadata.hasErrors()) {
                System.err.println(url);
                for (Directory directory : metadata.getDirectories()) {
                    if (directory.hasErrors()) {
                        for (String error : directory.getErrors()) {
                            System.err.printf("\t[%s] %s%n", directory.getName(), error);
                        }
                    }
                }
            }
            for (Directory directory2 : metadata.getDirectories()) {
                for (Tag tag : directory2.getTags()) {
                    String tagName = tag.getTagName();
                    String directoryName = directory2.getName();
                    String description = tag.getDescription();
                    if (description != null && description.length() > 1024) {
                        description = description.substring(0, 1024) + "...";
                    }
                    System.out.printf("[%s] %s = %s%n", directoryName, tagName, description);
                }
            }
        } catch (ImageProcessingException e) {
            System.err.printf("%s: %s [Error Extracting Metadata]%n\t%s%n", e.getClass().getName(), url, e.getMessage());
        } catch (Throwable t) {
            System.err.printf("%s: %s [Error Extracting Metadata]%n", t.getClass().getName(), url);
            t.printStackTrace(System.err);
        }
    }
}
