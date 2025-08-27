package org.apache.poi.xdgf.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XDGFShape;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.apache.poi.xdgf.usermodel.shape.ShapeVisitor;
import org.springframework.web.util.TagUtils;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/util/HierarchyPrinter.class */
public class HierarchyPrinter {
    public static void printHierarchy(XDGFPage page, File outDir) throws UnsupportedEncodingException, FileNotFoundException {
        File pageFile = new File(outDir, TagUtils.SCOPE_PAGE + page.getPageNumber() + "-" + Util.sanitizeFilename(page.getName()) + ".txt");
        OutputStream os = new FileOutputStream(pageFile);
        PrintStream pos = new PrintStream(os, false, "utf-8");
        printHierarchy(page, pos);
        pos.close();
    }

    public static void printHierarchy(XDGFPage page, final PrintStream os) {
        page.getContent().visitShapes(new ShapeVisitor() { // from class: org.apache.poi.xdgf.util.HierarchyPrinter.1
            @Override // org.apache.poi.xdgf.usermodel.shape.ShapeVisitor
            public void visit(XDGFShape shape, AffineTransform globalTransform, int level) {
                for (int i = 0; i < level; i++) {
                    os.append((CharSequence) "  ");
                }
                os.println(shape + " [" + shape.getShapeType() + ", " + shape.getSymbolName() + "] " + shape.getMasterShape() + SymbolConstants.SPACE_SYMBOL + shape.getTextAsString().trim());
            }
        });
    }

    public static void printHierarchy(XmlVisioDocument document, String outDirname) throws UnsupportedEncodingException, FileNotFoundException {
        File outDir = new File(outDirname);
        for (XDGFPage page : document.getPages()) {
            printHierarchy(page, outDir);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: in.vsdx outdir");
            System.exit(1);
        }
        String inFilename = args[0];
        String outDir = args[1];
        XmlVisioDocument doc = new XmlVisioDocument(new FileInputStream(inFilename));
        printHierarchy(doc, outDir);
    }
}
