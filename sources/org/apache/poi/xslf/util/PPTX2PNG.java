package org.apache.poi.xslf.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import org.apache.poi.openxml4j.opc.ContentTypes;
import org.apache.poi.sl.draw.DrawFactory;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/util/PPTX2PNG.class */
public class PPTX2PNG {
    static void usage(String error) {
        String msg = "Usage: PPTX2PNG [options] <ppt or pptx file>\n" + (error == null ? "" : "Error: " + error + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR) + "Options:\n    -scale <float>   scale factor\n    -slide <integer> 1-based index of a slide to render\n    -format <type>   png,gif,jpg (,null for testing)    -outdir <dir>    output directory, defaults to origin of the ppt/pptx file    -quiet           do not write to console (for normal processing)";
        System.out.println(msg);
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            usage(null);
            return;
        }
        String slidenumStr = "-1";
        float scale = 1.0f;
        File file = null;
        String format = ContentTypes.EXTENSION_PNG;
        File outdir = null;
        boolean quiet = false;
        int i = 0;
        while (i < args.length) {
            if (args[i].startsWith("-")) {
                if ("-scale".equals(args[i])) {
                    i++;
                    scale = Float.parseFloat(args[i]);
                } else if ("-slide".equals(args[i])) {
                    i++;
                    slidenumStr = args[i];
                } else if ("-format".equals(args[i])) {
                    i++;
                    format = args[i];
                } else if ("-outdir".equals(args[i])) {
                    i++;
                    outdir = new File(args[i]);
                } else if ("-quiet".equals(args[i])) {
                    quiet = true;
                }
            } else {
                file = new File(args[i]);
            }
            i++;
        }
        if (file == null || !file.exists()) {
            usage("File not specified or it doesn't exist");
            return;
        }
        if (format == null || !format.matches("^(png|gif|jpg|null)$")) {
            usage("Invalid format given");
            return;
        }
        if (outdir == null) {
            outdir = file.getParentFile();
        }
        if (!"null".equals(format) && (outdir == null || !outdir.exists() || !outdir.isDirectory())) {
            usage("Output directory doesn't exist");
            return;
        }
        if (scale < 0.0f) {
            usage("Invalid scale given");
            return;
        }
        if (!quiet) {
            System.out.println("Processing " + file);
        }
        SlideShow<?, ?> ss = SlideShowFactory.create(file, null, true);
        try {
            List<? extends Slide<S, P>> slides = ss.getSlides();
            Set<Integer> slidenum = slideIndexes(slides.size(), slidenumStr);
            if (slidenum.isEmpty()) {
                usage("slidenum must be either -1 (for all) or within range: [1.." + slides.size() + "] for " + file);
                ss.close();
                return;
            }
            Dimension pgsize = ss.getPageSize();
            int width = (int) (pgsize.width * scale);
            int height = (int) (pgsize.height * scale);
            for (Integer slideNo : slidenum) {
                Slide<?, ?> slide = (Slide) slides.get(slideNo.intValue());
                String title = slide.getTitle();
                if (!quiet) {
                    System.out.println("Rendering slide " + slideNo + (title == null ? "" : ": " + title));
                }
                BufferedImage img = new BufferedImage(width, height, 2);
                Graphics2D graphics = img.createGraphics();
                DrawFactory.getInstance(graphics).fixFonts(graphics);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                graphics.scale(scale, scale);
                slide.draw(graphics);
                if (!"null".equals(format)) {
                    String outname = file.getName().replaceFirst(".pptx?", "");
                    File outfile = new File(outdir, String.format(Locale.ROOT, "%1$s-%2$04d.%3$s", outname, slideNo, format));
                    ImageIO.write(img, format, outfile);
                }
                graphics.dispose();
                img.flush();
            }
            if (!quiet) {
                System.out.println("Done");
            }
        } finally {
            ss.close();
        }
    }

    private static Set<Integer> slideIndexes(int slideCount, String range) throws NumberFormatException {
        Set<Integer> slideIdx = new TreeSet<>();
        if ("-1".equals(range)) {
            for (int i = 0; i < slideCount; i++) {
                slideIdx.add(Integer.valueOf(i));
            }
        } else {
            String[] arr$ = range.split(",");
            for (String subrange : arr$) {
                String[] idx = subrange.split("-");
                switch (idx.length) {
                    case 1:
                        int subidx = Integer.parseInt(idx[0]);
                        if (subrange.contains("-")) {
                            int startIdx = subrange.startsWith("-") ? 0 : subidx;
                            int endIdx = subrange.endsWith("-") ? slideCount : Math.min(subidx, slideCount);
                            for (int i2 = Math.max(startIdx, 1); i2 < endIdx; i2++) {
                                slideIdx.add(Integer.valueOf(i2 - 1));
                            }
                            break;
                        } else {
                            slideIdx.add(Integer.valueOf(Math.max(subidx, 1) - 1));
                            break;
                        }
                    case 2:
                        int startIdx2 = Math.min(Integer.parseInt(idx[0]), slideCount);
                        int endIdx2 = Math.min(Integer.parseInt(idx[1]), slideCount);
                        for (int i3 = Math.max(startIdx2, 1); i3 < endIdx2; i3++) {
                            slideIdx.add(Integer.valueOf(i3 - 1));
                        }
                        break;
                }
            }
        }
        return slideIdx;
    }
}
