package org.apache.poi.xssf.dev;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import java.io.FileOutputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/dev/XSSFSave.class */
public final class XSSFSave {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            OPCPackage pkg = OPCPackage.open(args[i]);
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            int sep = args[i].lastIndexOf(46);
            String outfile = args[i].substring(0, sep) + "-save.xls" + (wb.isMacroEnabled() ? ANSIConstants.ESC_END : "x");
            FileOutputStream out = new FileOutputStream(outfile);
            wb.write(out);
            out.close();
            pkg.close();
        }
    }
}
