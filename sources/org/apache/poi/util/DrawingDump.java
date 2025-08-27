package org.apache.poi.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;

/* loaded from: poi-3.17.jar:org/apache/poi/util/DrawingDump.class */
public class DrawingDump {
    public static void main(String[] args) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(System.out, Charset.defaultCharset());
        PrintWriter pw = new PrintWriter(osw);
        NPOIFSFileSystem fs = new NPOIFSFileSystem(new File(args[0]));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        try {
            pw.println("Drawing group:");
            wb.dumpDrawingGroupRecords(true);
            Iterator i$ = wb.iterator();
            while (i$.hasNext()) {
                Sheet sheet = i$.next();
                pw.println("Sheet 1(" + sheet.getSheetName() + "):");
                ((HSSFSheet) sheet).dumpDrawingRecords(true, pw);
            }
        } finally {
            wb.close();
            fs.close();
        }
    }
}
