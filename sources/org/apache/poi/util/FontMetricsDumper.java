package org.apache.poi.util;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/* loaded from: poi-3.17.jar:org/apache/poi/util/FontMetricsDumper.class */
public class FontMetricsDumper {
    @SuppressForbidden("command line tool")
    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (Font allFont : allFonts) {
            String fontName = allFont.getFontName();
            Font font = new Font(fontName, 1, 10);
            FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
            int fontHeight = fontMetrics.getHeight();
            props.setProperty("font." + fontName + ".height", fontHeight + "");
            StringBuilder characters = new StringBuilder();
            char c = 'a';
            while (true) {
                char c2 = c;
                if (c2 > 'z') {
                    break;
                }
                characters.append(c2).append(", ");
                c = (char) (c2 + 1);
            }
            char c3 = 'A';
            while (true) {
                char c4 = c3;
                if (c4 > 'Z') {
                    break;
                }
                characters.append(c4).append(", ");
                c3 = (char) (c4 + 1);
            }
            char c5 = '0';
            while (true) {
                char c6 = c5;
                if (c6 > '9') {
                    break;
                }
                characters.append(c6).append(", ");
                c5 = (char) (c6 + 1);
            }
            StringBuilder widths = new StringBuilder();
            char c7 = 'a';
            while (true) {
                char c8 = c7;
                if (c8 > 'z') {
                    break;
                }
                widths.append(fontMetrics.getWidths()[c8]).append(", ");
                c7 = (char) (c8 + 1);
            }
            char c9 = 'A';
            while (true) {
                char c10 = c9;
                if (c10 > 'Z') {
                    break;
                }
                widths.append(fontMetrics.getWidths()[c10]).append(", ");
                c9 = (char) (c10 + 1);
            }
            char c11 = '0';
            while (true) {
                char c12 = c11;
                if (c12 <= '9') {
                    widths.append(fontMetrics.getWidths()[c12]).append(", ");
                    c11 = (char) (c12 + 1);
                }
            }
            props.setProperty("font." + fontName + ".characters", characters.toString());
            props.setProperty("font." + fontName + ".widths", widths.toString());
        }
        OutputStream fileOut = new FileOutputStream("font_metrics.properties");
        try {
            props.store(fileOut, "Font Metrics");
            fileOut.close();
        } catch (Throwable th) {
            fileOut.close();
            throw th;
        }
    }
}
